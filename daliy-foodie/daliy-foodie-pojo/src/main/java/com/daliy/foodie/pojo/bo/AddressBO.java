package com.daliy.foodie.pojo.bo;

import lombok.Data;

/**
 * Created by perl on 2019-12-07.
 */
@Data
public class AddressBO {
    private String addressId;
    private String userId;
    private String receiver;
    private String mobile;
    private String province;
    private String city;
    private String district;
    private String detail;
}
