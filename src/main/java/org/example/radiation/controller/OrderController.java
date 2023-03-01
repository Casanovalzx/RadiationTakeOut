package org.example.radiation.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.radiation.commom.BaseContext;
import org.example.radiation.commom.R;
import org.example.radiation.dto.OrdersDto;
import org.example.radiation.entity.OrderDetail;
import org.example.radiation.entity.Orders;
import org.example.radiation.service.OrderDetailService;
import org.example.radiation.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}", orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    /**
     * 根据用户id获取订单列表
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize){
        // 构造分页构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> dtoPage = new Page<>();

        // 根据用户id查询订单，访问orders表
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByDesc(Orders::getCheckoutTime);

        orderService.page(pageInfo, queryWrapper);

        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");

        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> list = records.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);

            Long orderId = item.getId();
            LambdaQueryWrapper<OrderDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(OrderDetail::getOrderId, orderId);
            List<OrderDetail> orderDetailList = orderDetailService.list(lambdaQueryWrapper);
            ordersDto.setOrderDetails(orderDetailList);

            return ordersDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }

    /**
     * 获取所有订单列表
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String number, String beginTime, String endTime){
        // 构造分页构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);

        // 访问orders表
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        if(number != null){
            queryWrapper.eq(Orders::getNumber, number);
        }
        if(beginTime != null){
            queryWrapper.ge(Orders::getCheckoutTime, beginTime);
        }
        if(endTime != null){
            queryWrapper.le(Orders::getCheckoutTime, endTime);
        }
        queryWrapper.orderByDesc(Orders::getCheckoutTime);

        orderService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 修改订单状态
     * @return
     */
    @PutMapping
    public R<String> status(@RequestBody Orders orders){
        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<>();

        updateWrapper.eq(Orders::getId, orders.getId());
        updateWrapper.set(Orders::getStatus, orders.getStatus());

        orderService.update(updateWrapper);
        return R.success("修改订单状态成功");
    }
}
