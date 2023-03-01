package org.example.radiation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.radiation.dto.SetmealDto;
import org.example.radiation.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时保存套餐和菜品的映射关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐及套餐与菜品的关联数据
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

    /**
     * 根据id查询套餐信息和对应的菜品信息
     * @param id
     */
    public SetmealDto getByIdWithDish(Long id);

    /**
     * 更新套餐信息，同时更新套餐和菜品的映射关系
     * @param setmealDto
     */
    public void updateWithDish(SetmealDto setmealDto);
}
