package com.rabbit.component.task.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by perl on 2020-02-22.
 */
@AllArgsConstructor
@Getter
public enum ElasticJobType {

    SIMPLE("SimpleJob"),
    DATAFLOW("DataflowJob"),
    SCRIPT("ScriptJob");

    private String jobType;
}
