package org.example.radiation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.radiation.entity.DishFlavor;
import org.example.radiation.mapper.DishFlavorMapper;
import org.example.radiation.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
