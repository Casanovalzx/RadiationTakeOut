package org.example.radiation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.radiation.commom.CustomException;
import org.example.radiation.dto.SetmealDto;
import org.example.radiation.entity.Setmeal;
import org.example.radiation.entity.SetmealDish;
import org.example.radiation.mapper.SetmealMapper;
import org.example.radiation.service.SetmealDishService;
import org.example.radiation.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService{

    @Autowired
    private SetmealDishService setmealDIshService;

    /**
     * 新增套餐，同时保存套餐和菜品的映射关系
     * @param setmealDto
     */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存套餐的基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联信息，操作setmeal_dish，执行insert操作
        setmealDIshService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐，同时删除套餐与菜品的映射关系
     * @param ids
     */
    @Transactional
    public void removeWithDish(List<Long> ids){
        LambdaQueryWrapper<Setmeal> QueryWrapper = new LambdaQueryWrapper<>();

        // 查询套餐状态，确定是否可以删除
        QueryWrapper.in(Setmeal::getId, ids);
        QueryWrapper.eq(Setmeal::getStatus, 1);

        int count = this.count(QueryWrapper);
        if (count > 0) {
            // 如果不能删除，抛出一个业务异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        // 如果可以删除，先删除套餐表数据
        this.removeByIds(ids);

        // 删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);

        setmealDIshService.remove(lambdaQueryWrapper);
    }

    /**
     * 根据id查询套餐信息和对应的菜品信息
     * @param id
     */
    public SetmealDto getByIdWithDish(Long id){
        log.info("id为{}",id);
        // 根据套餐id获取套餐信息，访问setmeal表
        Setmeal setmeal = this.getById(id);

        // 根据套餐id查询对应菜品信息，访问setmeal_dish表
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal != null, SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishes = setmealDIshService.list(queryWrapper);

        // 新建SetmealDto对象
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        setmealDto.setSetmealDishes(setmealDishes);

        return setmealDto;
    }

    /**
     * 更新套餐信息，同时更新套餐和菜品的映射关系
     * @param setmealDto
     */
    public void updateWithDish(SetmealDto setmealDto){
        // 更新setmeal表基本信息
        this.updateById(setmealDto);

        // 清理当前套餐与菜品的关联信息，setmeal_dish表的delete操作
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());

        setmealDIshService.remove(queryWrapper);

        // 添加当前提交过来的套餐与菜品的关联信息，setmeal_dish表的insert操作
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDIshService.saveBatch(setmealDishes);
    }
}
