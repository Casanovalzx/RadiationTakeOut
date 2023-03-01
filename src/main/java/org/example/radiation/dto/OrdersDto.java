package org.example.radiation.dto;

import org.example.radiation.entity.OrderDetail;
import org.example.radiation.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {
    private List<OrderDetail> orderDetails;
	
}
