package com.itheima.ecommerceweb.Service;

import com.itheima.ecommerceweb.Entities.Order;
import com.itheima.ecommerceweb.Service.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendService {
    @Autowired
    private OrderRepository orderRepository;

    public List<Order> recommendProducts(Long userId) {
        List<Order> orders = orderRepository.findAllByUid(userId);

        // 按购买日期分组
        Map<LocalDate, List<Order>> ordersByDate = orders.stream()
                .collect(Collectors.groupingBy(order -> order.getOrder_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));

        // 计算购买频率
        Map<LocalDate, Long> purchaseFrequency = ordersByDate.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> (long) entry.getValue().size()));

        // 预测未来购买日期
        LocalDate lastPurchaseDate = orders.stream()
                .map(order -> order.getOrder_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .max(LocalDate::compareTo)
                .orElse(LocalDate.now());

        LocalDate nextPurchaseDate = lastPurchaseDate.plusDays(7); // 假设用户每7天购买一次

        // 统计购买频率最高的商品类别
        Map<String, Long> categoryFrequency = orders.stream()
                .collect(Collectors.groupingBy(Order::getGood_type, Collectors.counting()));

        // 获取购买频率最高的商品类别
        String mostFrequentCategory = categoryFrequency.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("未知类别");

        // 推荐该类别下的商品
        List<Order> recommendedOrders = orders.stream()
                .filter(order -> order.getGood_type().equals(mostFrequentCategory))
                .collect(Collectors.toList());

        return recommendedOrders;
    }
}