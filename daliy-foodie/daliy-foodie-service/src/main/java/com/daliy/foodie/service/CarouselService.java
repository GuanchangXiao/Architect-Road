package com.daliy.foodie.service;

import com.daliy.foodie.pojo.Carousel;

import java.util.List;

/**
 * Created by perl on 11/23/19.
 */
public interface CarouselService {

    List<Carousel> queryAll(int status);
}
