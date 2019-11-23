package com.daliy.foodie.service.impl;

import com.daliy.foodie.mapper.CarouselMapper;
import com.daliy.foodie.pojo.Carousel;
import com.daliy.foodie.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by perl on 11/23/19.
 */
@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    public List<Carousel> queryAll(int status) {
        Example example = new Example(Carousel.class);
        example.orderBy("sort").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isShow",status);
        return carouselMapper.selectByExample(example);
    }


}
