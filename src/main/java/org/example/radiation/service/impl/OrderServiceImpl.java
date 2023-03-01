package org.example.radiation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.radiation.commom.BaseContext;
import org.example.radiation.commom.CustomException;
import org.example.radiation.entity.*;
import org.example.radiation.mapper.OrderMapper;
import org.example.radiation.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     * @param orders
     */
    @Transactional
    public void submit(Orders orders) {
        // 获得当前用户id
        Long userId = BaseContext.getCurrentId();

        // 查询当前用户购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);

        if(shoppingCartList == null || shoppingCartList.size() == 0){
            throw new CustomException("购物车为空，无法下单");
        }

        // 查询用户数据
        User user = userService.getById(userId);

        // 查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if(addressBook == null){
            throw new CustomException("用户地址信息有误，无法下单");
        }

        // 原子整数类，保证线程安全
        AtomicInteger amount = new AtomicInteger(0);
        long orderId = IdWorker.getId();

        // 遍历购物车数据,计算总金额并准备订单明细
        List<OrderDetail> orderDetails = shoppingCartList.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        // 向订单表插入（一条）数据
        orders.setId(orderId); // 订单号
        orders.setNumber(String.valueOf(orderId)); // 订单号
        orders.setOrderTime(LocalDateTime.now()); // 下单时间
        orders.setCheckoutTime(LocalDateTime.now()); // 结账时间
        orders.setStatus(2); // 1：待付款 2：待派送
        orders.setAmount(new BigDecimal(amount.get())); // 总金额
        orders.setUserId(userId); // 用户id
        orders.setUserName(user.getName()); // 用户名称
        orders.setConsignee(addressBook.getConsignee()); // 收货人
        orders.setPhone(addressBook.getPhone()); // 联系电话
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName()) +
                (addressBook.getCityName() == null ? "" : addressBook.getCityName()) +
                (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName()) +
                (addressBook.getDetail() == null ? "" : addressBook.getDetail())
        ); // 收货地址
        this.save(orders);

        // 向订单明细表插入（n条）数据
        orderDetailService.saveBatch(orderDetails);

        // 清理当前用户的购物车数据
        shoppingCartService.remove(queryWrapper);
    }
}
