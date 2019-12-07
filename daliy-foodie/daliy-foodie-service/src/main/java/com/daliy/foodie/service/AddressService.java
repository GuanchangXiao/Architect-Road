package com.daliy.foodie.service;

import com.daliy.foodie.pojo.UserAddress;
import com.daliy.foodie.pojo.bo.AddressBO;

import java.util.List;

/**
 * Created by perl on 2019-12-07.
 */
public interface AddressService {
    /**
     * 根据用户id查询地址
     * @param userId
     * @return
     */
    List<UserAddress> queryAll(String userId);

    /**
     * 新增收货地址
     * @param addressBO
     */
    void addNewUserAddress(AddressBO addressBO);

    /**
     * 修改收货地址
     * @param addressBO
     */
    void updateUserAddress(AddressBO addressBO);

    /**
     * 删除收货地址
     * @param userId 用户id
     * @param addressId 收货地址id
     */
    void deleteUserAddress(String userId, String addressId);

    /**
     * 设置收货地址为默认收货地址
     * @param userId 用户id
     * @param addressId 收货地址id
     */
    void updateUserAddressToBeDefault(String userId, String addressId);

    /**
     * 根据用户id和收货地址id查询收货地址
     * @param userId 用户id
     * @param addressId 收货地址id
     * @return
     */
    UserAddress queryUserAddres(String userId, String addressId);
}
