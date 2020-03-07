package com.foodie.item.mapper;

import com.foodie.item.pojo.ItemsComments;
import com.foodie.item.pojo.vo.MyCommentVO;
import com.foodie.my.mapper.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsMapperCustom extends MyMapper<ItemsComments> {

    void saveComments(Map<String, Object> map);

    List<MyCommentVO> queryMyComments(@Param("paramsMap") Map<String, Object> map);

}