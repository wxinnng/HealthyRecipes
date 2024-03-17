package com.healthyrecipes.mapper;

import com.healthyrecipes.pojo.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author:86198
 * @DATE:2024/2/3 10:52
 * @DESCRIPTION:
 * @VERSION:1.0
 */
@Mapper
public interface AdminMapper {

    @Select("select * from admin where username=#{username} and password=#{password}")
    Admin getAdmin(Admin admin);
}
