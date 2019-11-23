package com.daliy.foodie.mapper;

import com.daliy.foodie.pojo.vo.CategoryVO;
import com.daliy.foodie.pojo.vo.NewItemsVO;

import java.util.List;

/**
 * Created by perl on 11/23/19.
 */
public interface CategoryCustomMapper {

    List<CategoryVO> selectSubCatList(Integer rootCatId);

    List<NewItemsVO> selectSixNewItemsLazy(Integer rootCatId);
}
