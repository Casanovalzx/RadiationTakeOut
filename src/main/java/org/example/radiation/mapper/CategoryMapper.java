package org.example.radiation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.radiation.entity.Category;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
