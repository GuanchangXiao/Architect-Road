package com.rabbit.component.task.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by perl on 2020-02-22.
 * Elastic Job 配置注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticJobConfiguration {

    String jobName();

    String cron() default "";

    int shardingTotalCount() default 1;

    String shardingItemParameters() default "";

    String jobParameter() default "";

    boolean failover() default false;

    boolean misfire() default true;

    String description() default "";

    boolean overwrite() default false;

    boolean streamingProcess() default false;

    String scriptCommandLine() default "";

    boolean monitorExecution() default false;

    public int monitorPort() default -1;

    public int maxTimeDiffSeconds() default -1;

    public String jobShardingStrategyClass() default "";

    public int reconcileIntervalMinutes() default 10;

    public String eventTraceRdbDataSource() default "";

    public String listener() default "";

    public boolean disabled() default false;

    public String distributedListener() default "";

    public long startedTimeoutMilliseconds() default Long.MAX_VALUE;

    public long completedTimeoutMilliseconds() default Long.MAX_VALUE;

    public String jobExceptionHandler() default "com.dangdang.ddframe.job.executor.handler.impl.DefaultJobExceptionHandler";

    public String executorServiceHandler() default "com.dangdang.ddframe.job.executor.handler.impl.DefaultExecutorServiceHandler";
}
