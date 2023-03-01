package org.example.radiation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.radiation.dto.DishDto;
import org.example.radiation.entity.Dish;

import java.util.List;


public interface DishService extends IService<Dish> {

    // 新增菜品，同时插入对应的口味数据，需要操作两张表：dish、dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    // 根据id查询菜品信息和对应口味信息
    public DishDto getByIdWithFlavor(Long id);

    // 更新菜品信息同时更新对应口味信息
    public void updateWithFlavor(DishDto dishDto);

    // 删除菜品信息同时删除对应口味信息
    public void deleteWithFlavor(List<Long> ids);
}
