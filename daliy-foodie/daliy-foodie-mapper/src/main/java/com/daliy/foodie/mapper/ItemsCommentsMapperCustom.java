package com.daliy.foodie.mapper;

import com.daliy.foodie.my.BaseMapper;
import com.daliy.foodie.pojo.ItemsComments;
import com.daliy.foodie.pojo.vo.MyCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by perl on 2019-12-08.
 */
public interface ItemsCommentsMapperCustom extends BaseMapper<ItemsComments> {

    public void saveComments(Map<String, Object> map);

    public List<MyCommentVO> queryMyComments(@Param("paramsMap") Map<String, Object> map);

}
