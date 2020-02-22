package com.rabbit.component.core.producer.mapper;

import com.rabbit.component.core.producer.entity.BrokerMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by perl on 2020-02-21.
 */
@Mapper
public interface BrokerMessageMapper {

    int deleteByPrimaryKey(String messageId);

    int insert(BrokerMessage record);

    int insertSelective(BrokerMessage record);

    BrokerMessage selectByPrimaryKey(String messageId);

    int updateByPrimaryKeySelective(BrokerMessage record);

    int updateByPrimaryKeyWithBLOBs(BrokerMessage record);

    int updateByPrimaryKey(BrokerMessage record);

    void changeBrokerMessageStatus(@Param("brokerMessageId")String brokerMessageId, @Param("brokerMessageStatus")String brokerMessageStatus, @Param("updateTime") Date updateTime);

    List<BrokerMessage> queryBrokerMessageStatus4Timeout(@Param("brokerMessageStatus")String brokerMessageStatus);

    List<BrokerMessage> queryBrokerMessageStatus(@Param("brokerMessageStatus")String brokerMessageStatus);

    int update4TryCount(@Param("brokerMessageId")String brokerMessageId, @Param("updateTime")Date updateTime);
}
