package com.foodie.item.mapper;

import com.foodie.item.pojo.vo.ItemCommentVO;
import com.foodie.item.pojo.vo.ShopcartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom {

    List<ItemCommentVO> queryItemComments(@Param("paramsMap") Map<String, Object> map);

//    public List<SearchItemsVO> searchItems(@Param("paramsMap") Map<String, Object> map);

//    public List<SearchItemsVO> searchItemsByThirdCat(@Param("paramsMap") Map<String, Object> map);

    List<ShopcartVO> queryItemsBySpecIds(@Param("paramsList") List specIdsList);

    int decreaseItemSpecStock(@Param("specId") String specId,
                              @Param("pendingCounts") int pendingCounts);
}