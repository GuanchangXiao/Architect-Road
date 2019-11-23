package com.daliy.foodie.service.impl;

import com.daliy.foodie.mapper.CategoryMapper;
import com.daliy.foodie.mapper.CategoryCustomMapper;
import com.daliy.foodie.pojo.Category;
import com.daliy.foodie.service.CategoryService;
import com.daliy.foodie.pojo.vo.CategoryVO;
import com.daliy.foodie.pojo.vo.NewItemsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by perl on 11/23/19.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryCustomMapper categoryMapperCustom;

    public final static Integer ROOT_ID = 0;

    @Override
    public List<Category> queryAllRootLevelCat() {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("fatherId", ROOT_ID);
        return categoryMapper.selectByExample(example);
    }

    @Override
    public List<CategoryVO> getSubCatList(Integer rootCatId) {
        return categoryMapperCustom.selectSubCatList(rootCatId);
    }

    @Override
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId) {
        return categoryMapperCustom.selectSixNewItemsLazy(rootCatId);
    }
}
