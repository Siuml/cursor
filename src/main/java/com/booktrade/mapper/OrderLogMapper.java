package com.booktrade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.booktrade.entity.OrderLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderLogMapper extends BaseMapper<OrderLog> {
}