package com.booktrade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.booktrade.entity.TradeOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<TradeOrder> {
}
