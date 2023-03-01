package org.example.radiation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.radiation.entity.ShoppingCart;
import org.example.radiation.mapper.ShoppingCartMapper;
import org.example.radiation.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
