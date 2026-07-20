package com.booktrade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.booktrade.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT id FROM `user`")
    java.util.List<Long> findAllIds();
}