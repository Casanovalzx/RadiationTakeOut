package org.example.radiation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.radiation.entity.SetmealDish;
import org.example.radiation.mapper.SetmealDishMapper;
import org.example.radiation.service.SetmealDishService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
