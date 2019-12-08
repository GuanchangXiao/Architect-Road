package com.daliy.foodie.pojo.bo;

import lombok.Data;

/**
 * Created by perl on 2019-12-07.
 */
@Data
public class SubmitOrderBO {
    private String userId;
    private String itemSpecIds;
    private String addressId;
    private Integer payMethod;
    private String leftMsg;
}
