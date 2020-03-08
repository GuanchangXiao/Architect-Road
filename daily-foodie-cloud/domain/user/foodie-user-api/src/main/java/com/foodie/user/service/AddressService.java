package com.foodie.user.service;

import com.foodie.user.pojo.UserAddress;
import com.foodie.user.pojo.bo.AddressBO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("user-address-api")
public interface AddressService {

    /**
     * 根据用户id查询用户的收货地址列表
     * @param userId
     * @return
     */
    @GetMapping("address-list")
    List<UserAddress> queryAll(@RequestParam("userId") String userId);

    /**
     * 用户新增地址
     * @param addressBO
     */
    @PostMapping("add-address")
    void addNewUserAddress(@RequestBody AddressBO addressBO);

    /**
     * 用户修改地址
     * @param addressBO
     */
    @PutMapping("update-address")
    void updateUserAddress(@RequestBody AddressBO addressBO);

    /**
     * 根据用户id和地址id，删除对应的用户地址信息
     * @param userId
     * @param addressId
     */
    @DeleteMapping("del-address")
    void deleteUserAddress(@RequestParam("userId") String userId,
                           @RequestParam("addressId") String addressId);

    /**
     * 修改默认地址
     * @param userId
     * @param addressId
     */
    @PostMapping("set-default-address")
    void updateUserAddressToBeDefault(@RequestParam("userId") String userId,
                                      @RequestParam("addressId") String addressId);

    /**
     * 根据用户id和地址id，查询具体的用户地址对象信息
     * @param userId
     * @param addressId
     * @return
     */
    @GetMapping("query-address")
    UserAddress queryUserAddres(@RequestParam("userId") String userId,
                                @RequestParam(value = "addressId", required = false) String addressId);
}
