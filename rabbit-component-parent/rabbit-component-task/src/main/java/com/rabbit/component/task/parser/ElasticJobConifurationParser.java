package com.rabbit.component.task.parser;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.executor.handler.JobProperties;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.rabbit.component.task.annotation.ElasticJobConfiguration;
import com.rabbit.component.task.enums.ElasticJobType;
import com.rabbit.component.task.properties.JobZookeeperProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by perl on 2020-02-22.
 */
@AllArgsConstructor
@Slf4j
public class ElasticJobConifurationParser implements ApplicationListener<ApplicationReadyEvent> {

    private JobZookeeperProperties jobZookeeperProperties;
    private ZookeeperRegistryCenter zookeeperRegistryCenter;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            ApplicationContext ctx = event.getApplicationContext();
            Map<String, Object> beanMap = ctx.getBeansWithAnnotation(ElasticJobConfiguration.class);

            beanMap.forEach((key,configBean) -> {
                Class<?> clazz = configBean.getClass();
                if(clazz.getName().indexOf("$") > 0) {
                    String className = clazz.getName();
                    try {
                        clazz = Class.forName(className.substring(0, className.indexOf("$")));
                    } catch (ClassNotFoundException e) {
                        log.error("Not Found Class : ElasticJobConfiguration.class");
                    }
                }
                // 	获取接口类型 用于判断是什么类型的任务
                String jobTypeName = clazz.getInterfaces()[0].getSimpleName();
                //	获取配置注解对象 ElasticJobConfiguration
                ElasticJobConfiguration conf = clazz.getAnnotation(ElasticJobConfiguration.class);

                String jobClass = clazz.getName();
                String jobName = this.jobZookeeperProperties.getNamespace() + "." + conf.jobName();
                String cron = conf.cron();
                String shardingItemParameters = conf.shardingItemParameters();
                String description = conf.description();
                String jobParameter = conf.jobParameter();
                String jobExceptionHandler = conf.jobExceptionHandler();
                String executorServiceHandler = conf.executorServiceHandler();

                String jobShardingStrategyClass = conf.jobShardingStrategyClass();
                String eventTraceRdbDataSource = conf.eventTraceRdbDataSource();
                String scriptCommandLine = conf.scriptCommandLine();

                boolean failover = conf.failover();
                boolean misfire = conf.misfire();
                boolean overwrite = conf.overwrite();
                boolean disabled = conf.disabled();
                boolean monitorExecution = conf.monitorExecution();
                boolean streamingProcess = conf.streamingProcess();

                int shardingTotalCount = conf.shardingTotalCount();
                int monitorPort = conf.monitorPort();
                int maxTimeDiffSeconds = conf.maxTimeDiffSeconds();
                int reconcileIntervalMinutes = conf.reconcileIntervalMinutes();

                JobCoreConfiguration coreConfiguration = JobCoreConfiguration
                        .newBuilder(jobName, cron, shardingTotalCount)
                        .shardingItemParameters(shardingItemParameters)
                        .failover(failover)
                        .misfire(misfire)
                        .description(description)
                        .jobParameter(jobParameter)
                        .jobProperties(JobProperties.JobPropertiesEnum.JOB_EXCEPTION_HANDLER.getKey(), jobExceptionHandler)
                        .jobProperties(JobProperties.JobPropertiesEnum.EXECUTOR_SERVICE_HANDLER.getKey(), executorServiceHandler)
                        .build();

                JobTypeConfiguration typeConfiguration = null;
                if (ElasticJobType.SIMPLE.getJobType().equals(jobTypeName)) {
                    typeConfiguration = new SimpleJobConfiguration(coreConfiguration, jobClass);
                }
                if (ElasticJobType.DATAFLOW.getJobType().equals(jobTypeName)) {
                    typeConfiguration = new DataflowJobConfiguration(coreConfiguration, jobClass, streamingProcess);
                }
                if (ElasticJobType.SCRIPT.getJobType().equals(jobTypeName)) {
                    typeConfiguration = new ScriptJobConfiguration(coreConfiguration, scriptCommandLine);
                }

                LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration
                        .newBuilder(typeConfiguration)
                        .overwrite(overwrite)
                        .disabled(disabled)
                        .monitorPort(monitorPort)
                        .monitorExecution(monitorExecution)
                        .jobShardingStrategyClass(jobShardingStrategyClass)
                        .reconcileIntervalMinutes(reconcileIntervalMinutes)
                        .maxTimeDiffSeconds(maxTimeDiffSeconds)
                        .build();

                BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder
                        .rootBeanDefinition(SpringJobScheduler.class);
                definitionBuilder.setInitMethodName("init");
                definitionBuilder.setScope("prototype");

                //	1.添加bean构造参数，相当于添加自己的真实的任务实现类
                if (!ElasticJobType.SCRIPT.getJobType().equals(jobTypeName)) {
                    definitionBuilder.addConstructorArgValue(configBean);
                }
                //	2.添加注册中心
                definitionBuilder.addConstructorArgValue(zookeeperRegistryCenter);
                //	3.添加LiteJobConfiguration
                definitionBuilder.addConstructorArgValue(liteJobConfiguration);

                //	4.如果有eventTraceRdbDataSource 则也进行添加
                if (StringUtils.hasText(eventTraceRdbDataSource)) {
                    BeanDefinitionBuilder rdbFactory = BeanDefinitionBuilder.rootBeanDefinition(JobEventRdbConfiguration.class);
                    rdbFactory.addConstructorArgReference(eventTraceRdbDataSource);
                    definitionBuilder.addConstructorArgValue(rdbFactory.getBeanDefinition());
                }

                //  5.添加监听
                List<?> elasticJobListeners = getTargetElasticJobListeners(conf);
                definitionBuilder.addConstructorArgValue(elasticJobListeners);

                // 	接下来就是把SpringJobScheduler注入到Spring容器中
                DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();

                String registerBeanName = conf.jobName() + "SpringJobScheduler";
                defaultListableBeanFactory.registerBeanDefinition(registerBeanName, definitionBuilder.getBeanDefinition());
                SpringJobScheduler scheduler = (SpringJobScheduler)ctx.getBean(registerBeanName);
                scheduler.init();

                log.info("启动Elastic Job作业: " + jobName);
            });
            log.info("共计启动Elastic Job作业数量为: {} 个", beanMap.size());
        }catch (Exception e) {
            log.error("Elastic Job 启动异常, 系统强制退出", e);
            System.exit(1);
        }
    }

    private List<?> getTargetElasticJobListeners(ElasticJobConfiguration conf) {
        List<BeanDefinition> result = new ManagedList<BeanDefinition>(2);
        String listeners = conf.listener();
        if (StringUtils.hasText(listeners)) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(listeners);
            factory.setScope("prototype");
            result.add(factory.getBeanDefinition());
        }

        String distributedListeners = conf.distributedListener();
        long startedTimeoutMilliseconds = conf.startedTimeoutMilliseconds();
        long completedTimeoutMilliseconds = conf.completedTimeoutMilliseconds();

        if (StringUtils.hasText(distributedListeners)) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(distributedListeners);
            factory.setScope("prototype");
            factory.addConstructorArgValue(Long.valueOf(startedTimeoutMilliseconds));
            factory.addConstructorArgValue(Long.valueOf(completedTimeoutMilliseconds));
            result.add(factory.getBeanDefinition());
        }
        return result;
    }
}
