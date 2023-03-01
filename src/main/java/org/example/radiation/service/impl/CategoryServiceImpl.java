package org.example.radiation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.radiation.commom.CustomException;
import org.example.radiation.entity.Category;
import org.example.radiation.entity.Dish;
import org.example.radiation.entity.Setmeal;
import org.example.radiation.mapper.CategoryMapper;
import org.example.radiation.service.CategoryService;
import org.example.radiation.service.DishService;
import org.example.radiation.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除分类， 删除之前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 查询当前分类是否关联了菜品，如果已经关联，抛出业务异常
        // 添加查询条件，根据分类id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if(count1 > 0) {
            // 已经关联了菜品，抛出业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        // 查询当前分类是否关联了套餐，如果已经关联，抛出业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if(count2 > 0) {
            // 已经关联了套餐，抛出业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        // 正常删除
        super.removeById(id);

    }
}
