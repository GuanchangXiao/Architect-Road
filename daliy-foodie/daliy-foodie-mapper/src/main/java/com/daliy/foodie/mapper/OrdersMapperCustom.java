package com.daliy.foodie.mapper;

import com.daliy.foodie.pojo.OrderStatus;
import com.daliy.foodie.pojo.vo.MyOrdersVO;
import com.daliy.foodie.pojo.vo.MySubOrderItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by perl on 2019-12-08.
 */
public interface OrdersMapperCustom {

    List<MyOrdersVO> queryMyOrders(@Param("paramsMap") Map<String, Object> map);

    int getMyOrderStatusCounts(@Param("paramsMap") Map<String, Object> map);

    List<OrderStatus> getMyOrderTrend(@Param("paramsMap") Map<String, Object> map);

    List<MySubOrderItemVO> getSubItems(@Param("paramsMap") Map<String, Object> map);

    List<MyOrdersVO> queryMyOrdersDoNotUse(@Param("paramsMap") Map<String, Object> map);
}
