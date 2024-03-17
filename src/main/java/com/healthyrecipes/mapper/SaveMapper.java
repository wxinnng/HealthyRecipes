package com.healthyrecipes.mapper;

import com.healthyrecipes.pojo.entity.Save;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author:86198
 * @DATE:2024/3/1 10:49
 * @DESCRIPTION:
 * @VERSION:1.0
 */
@Mapper
public interface SaveMapper {

    @Select("select * from save where userid = #{userid} ")
    List<Save> getSaveListByUserId(Integer userid);


    @Insert("insert into save (userid,food_id,type) values (#{userid},#{foodId},#{type})")
    void addSave(Save save);
}
