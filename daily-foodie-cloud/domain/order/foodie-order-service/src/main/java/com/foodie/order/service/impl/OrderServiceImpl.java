package com.foodie.order.service.impl;

import com.foodie.enums.OrderStatusEnum;
import com.foodie.enums.YesOrNo;
import com.foodie.item.pojo.Items;
import com.foodie.item.pojo.ItemsSpec;
import com.foodie.order.mapper.OrderItemsMapper;
import com.foodie.order.mapper.OrderStatusMapper;
import com.foodie.order.mapper.OrdersMapper;
import com.foodie.order.pojo.OrderItems;
import com.foodie.order.pojo.OrderStatus;
import com.foodie.order.pojo.Orders;
import com.foodie.order.pojo.bo.PlaceOrderBO;
import com.foodie.order.pojo.bo.SubmitOrderBO;
import com.foodie.order.pojo.vo.MerchantOrdersVO;
import com.foodie.order.pojo.vo.OrderVO;
import com.foodie.order.service.OrderService;
import com.foodie.shopcart.pojo.bo.ShopcartBO;
import com.foodie.user.pojo.UserAddress;
import com.foodie.utils.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

//    @Autowired
//    private AddressService addressService;
//
//    @Autowired
//    private ItemService itemService;
//
//    @Autowired
//    private ShopcartService shopcartService;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public OrderVO createOrder(PlaceOrderBO placeOrderBO) {

        List<ShopcartBO> shopcartList = placeOrderBO.getShopcartList();
        SubmitOrderBO submitOrderBO = placeOrderBO.getSubmitOrderBO();

        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();
        // 包邮费用设置为0
        Integer postAmount = 0;

        String orderId = sid.nextShort();

//        UserAddress address = addressService.queryUserAddres(userId, addressId);
//        TODO Feign
        ServiceInstance addressInstance = loadBalancerClient.choose("FOODIE-USER-SERVICE");
        String queryUserAddresUrl = String.format("http://%s:%s/user-address-api/query-address?userId=%s&addressId=%s",
                addressInstance.getHost(),
                addressInstance.getPort(),
                userId,
                addressId);

        UserAddress address = restTemplate.getForObject(queryUserAddresUrl, UserAddress.class);

        // 1. 新订单数据保存
        Orders newOrder = new Orders();
        newOrder.setId(orderId);
        newOrder.setUserId(userId);

        newOrder.setReceiverName(address.getReceiver());
        newOrder.setReceiverMobile(address.getMobile());
        newOrder.setReceiverAddress(address.getProvince() + " "
                                    + address.getCity() + " "
                                    + address.getDistrict() + " "
                                    + address.getDetail());

//        newOrder.setTotalAmount();
//        newOrder.setRealPayAmount();
        newOrder.setPostAmount(postAmount);

        newOrder.setPayMethod(payMethod);
        newOrder.setLeftMsg(leftMsg);

        newOrder.setIsComment(YesOrNo.NO.type);
        newOrder.setIsDelete(YesOrNo.NO.type);
        newOrder.setCreatedTime(new Date());
        newOrder.setUpdatedTime(new Date());


        // 2. 循环根据itemSpecIds保存订单商品信息表
        String itemSpecIdArr[] = itemSpecIds.split(",");
        Integer totalAmount = 0;    // 商品原价累计
        Integer realPayAmount = 0;  // 优惠后的实际支付价格累计
        List<ShopcartBO> toBeRemovedShopcatdList = Lists.newArrayList();
        for (String itemSpecId : itemSpecIdArr) {
//            ShopcartBO cartItem = shopcartService.getBuyCountsFromShopcart(shopcartList, itemSpecId);

            // TODO Feign
            Map<String, Object> map = new HashMap<>();
            map.put("shopcartList", shopcartList);
            map.put("specId", itemSpecId);

            ServiceInstance shopcartInstance = loadBalancerClient.choose("FOODIE-SHOPCART-SERVICE");
            String getBuyCountsFromShopcartUrl = String.format("http://%s:%s/shopcart-api/counts",
                    addressInstance.getHost(),
                    addressInstance.getPort());

            ShopcartBO cartItem = restTemplate.postForObject(getBuyCountsFromShopcartUrl, map, ShopcartBO.class);


            // 整合redis后，商品购买的数量重新从redis的购物车中获取
            int buyCounts = cartItem.getBuyCounts();
            toBeRemovedShopcatdList.add(cartItem);

            // 2.1 根据规格id，查询规格的具体信息，主要获取价格
//            ItemsSpec itemSpec = itemService.queryItemSpecById(itemSpecId);
//            TODO Feign
            ServiceInstance itemInstance = loadBalancerClient.choose("FOODIE-ITEM-SERVICE");
            String queryItemSpecByIdUrl = String.format("http://%s:%s/item-api/item-spec?specId=%s",
                    addressInstance.getHost(),
                    addressInstance.getPort(),
                    itemSpecId);

            ItemsSpec itemSpec = restTemplate.getForObject(queryItemSpecByIdUrl, ItemsSpec.class);

            totalAmount += itemSpec.getPriceNormal() * buyCounts;
            realPayAmount += itemSpec.getPriceDiscount() * buyCounts;

            // 2.2 根据商品id，获得商品信息以及商品图片
            String itemId = itemSpec.getItemId();


//            Items item = itemService.queryItemById(itemId);
//            TODO Feign
            String queryItemByIdUrl = String.format("http://%s:%s/item-api/item?itemId=%s",
                    addressInstance.getHost(),
                    addressInstance.getPort(),
                    itemId);

            Items item = restTemplate.getForObject(queryItemByIdUrl, Items.class);

//            String imgUrl = itemService.queryItemMainImgById(itemId);
            String queryItemMainImgByIdUrl = String.format("http://%s:%s/item-api/item-main-image?itemId=%s",
                    itemInstance.getHost(),
                    itemInstance.getPort(),
                    itemId);
            String imgUrl = restTemplate.getForObject(queryItemMainImgByIdUrl, String.class);

            // 2.3 循环保存子订单数据到数据库
            String subOrderId = sid.nextShort();
            OrderItems subOrderItem = new OrderItems();
            subOrderItem.setId(subOrderId);
            subOrderItem.setOrderId(orderId);
            subOrderItem.setItemId(itemId);
            subOrderItem.setItemName(item.getItemName());
            subOrderItem.setItemImg(imgUrl);
            subOrderItem.setBuyCounts(buyCounts);
            subOrderItem.setItemSpecId(itemSpecId);
            subOrderItem.setItemSpecName(itemSpec.getName());
            subOrderItem.setPrice(itemSpec.getPriceDiscount());
            orderItemsMapper.insert(subOrderItem);

            // 2.4 在用户提交订单以后，规格表中需要扣除库存
//            itemService.decreaseItemSpecStock(itemSpecId, buyCounts);
            Map<String, Object> param = Maps.newHashMap();
            param.put("specId", itemSpecId);
            param.put("buyCounts", buyCounts);
            String decreaseItemSpecStockUrl = String.format("http://%s:%s/item-api/decrease-item-spec",
                    itemInstance.getHost(),
                    itemInstance.getPort());
            restTemplate.postForLocation(decreaseItemSpecStockUrl, param);
        }

        newOrder.setTotalAmount(totalAmount);
        newOrder.setRealPayAmount(realPayAmount);
        ordersMapper.insert(newOrder);

        // 3. 保存订单状态表
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitPayOrderStatus);

        // 4. 构建商户订单，用于传给支付中心
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setAmount(realPayAmount + postAmount);
        merchantOrdersVO.setPayMethod(payMethod);

        // 5. 构建自定义订单vo
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);
        orderVO.setToBeRemovedShopcatdList(toBeRemovedShopcatdList);

        return orderVO;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateOrderStatus(String orderId, Integer orderStatus) {

        OrderStatus paidStatus = new OrderStatus();
        paidStatus.setOrderId(orderId);
        paidStatus.setOrderStatus(orderStatus);
        paidStatus.setPayTime(new Date());

        orderStatusMapper.updateByPrimaryKeySelective(paidStatus);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatus queryOrderStatusInfo(String orderId) {
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void closeOrder() {

        // 查询所有未付款订单，判断时间是否超时（1天），超时则关闭交易
        OrderStatus queryOrder = new OrderStatus();
        queryOrder.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> list = orderStatusMapper.select(queryOrder);
        for (OrderStatus os : list) {
            // 获得订单创建时间
            Date createdTime = os.getCreatedTime();
            // 和当前时间进行对比
            int days = DateUtil.daysBetween(createdTime, new Date());
            if (days >= 1) {
                // 超过1天，关闭订单
                doCloseOrder(os.getOrderId());
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    void doCloseOrder(String orderId) {
        OrderStatus close = new OrderStatus();
        close.setOrderId(orderId);
        close.setOrderStatus(OrderStatusEnum.CLOSE.type);
        close.setCloseTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(close);
    }
}
