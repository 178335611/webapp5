package com.itheima.ecommerceweb.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itheima.ecommerceweb.Entities.Order;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    public void addOrder(Order order) {
        if (order.getOrder_date() == null) {
            order.setOrder_date(new Date()); // 设置当前日期，或者根据业务逻辑设置其他日期
        }
        orderRepository.save(order);
    }
    public List<Order> getOrdersByUid(Long uid) {
        return orderRepository.findByUid(uid);
    }
    public Order getOrderByOid(Long oid) {
        return orderRepository.findById(oid).orElse(null);
    }
    public List<Order> getOrderBySid(Long sid) {
        return orderRepository.findBySid(sid);
    }
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }
    public List<Order> getOrdersByGood_type(String good_type) {
        return orderRepository.findByGood_type(good_type);
    }
}
