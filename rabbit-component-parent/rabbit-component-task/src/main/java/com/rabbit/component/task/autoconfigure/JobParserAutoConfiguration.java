package com.rabbit.component.task.autoconfigure;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.rabbit.component.task.parser.ElasticJobConifurationParser;
import com.rabbit.component.task.properties.JobZookeeperProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by perl on 2020-02-22.
 */
@Configuration
@ConditionalOnProperty(prefix = "elastic.job.zk", name = {"namespace", "serverLists"})
@EnableConfigurationProperties(JobZookeeperProperties.class)
@Slf4j
public class JobParserAutoConfiguration {

    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter zookeeperRegistryCenter(JobZookeeperProperties jobZookeeperProperties) {

        String serverLists = jobZookeeperProperties.getServerLists();
        String namespace = jobZookeeperProperties.getNamespace();

        // create zkConfig
        ZookeeperConfiguration zkConfig = new ZookeeperConfiguration(serverLists, namespace);

        // set configuration properties
        zkConfig.setBaseSleepTimeMilliseconds(jobZookeeperProperties.getBaseSleepTimeMilliseconds());
        zkConfig.setMaxSleepTimeMilliseconds(jobZookeeperProperties.getMaxSleepTimeMilliseconds());
        zkConfig.setConnectionTimeoutMilliseconds(jobZookeeperProperties.getConnectionTimeoutMilliseconds());
        zkConfig.setSessionTimeoutMilliseconds(jobZookeeperProperties.getSessionTimeoutMilliseconds());
        zkConfig.setMaxRetries(jobZookeeperProperties.getMaxRetries());
        zkConfig.setDigest(jobZookeeperProperties.getDigest());
        log.info("---- Initialization Elastic Job Registry Center Configuration Success ---- >> zk serverLists : {}, namespace : {}", serverLists, namespace);

        return new ZookeeperRegistryCenter(zkConfig);
    }

    @Bean
    public ElasticJobConifurationParser elasticJobConifurationParser(JobZookeeperProperties jobZookeeperProperties, ZookeeperRegistryCenter zookeeperRegistryCenter) {
        return new ElasticJobConifurationParser(jobZookeeperProperties, zookeeperRegistryCenter);
    }
}
