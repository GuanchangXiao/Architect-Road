package com.foodie.item.service.impl;

import com.foodie.enums.YesOrNo;
import com.foodie.item.mapper.ItemsCommentsMapperCustom;
import com.foodie.item.pojo.vo.MyCommentVO;
import com.foodie.item.service.ItemCommentsService;
import com.foodie.pojo.PagedGridResult;
import com.foodie.service.BaseService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by perl on 2020-02-29.
 */
@RestController
@Slf4j
public class ItemCommentsServiceImpl extends BaseService implements ItemCommentsService {

    @Autowired
    private ItemsCommentsMapperCustom itemsCommentsMapperCustom;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComments(Map<String, Object> params) {
        itemsCommentsMapperCustom.saveComments(params);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyComments(String userId,
                                           Integer page,
                                           Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        PageHelper.startPage(page, pageSize);
        List<MyCommentVO> list = itemsCommentsMapperCustom.queryMyComments(map);

        return setterPagedGrid(list, page);
    }
}
