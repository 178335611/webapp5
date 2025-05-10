package com.itheima.ecommerceweb.Service;


import com.itheima.ecommerceweb.Entities.User;
import com.itheima.ecommerceweb.Entities.Log;
import com.itheima.ecommerceweb.Entities.Order;
import com.itheima.ecommerceweb.Service.LogRepository;
import com.itheima.ecommerceweb.Service.OrderRepository;
import com.itheima.ecommerceweb.Service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final LogRepository logRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    @Autowired
    public AnalyticsService(LogRepository logRepository,
                            OrderRepository orderRepository, UserRepository userRepository) {
        this.logRepository = logRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public User updateUserProfile(Long userId) {
        List<Log> logs = logRepository.findAllByUid(userId);
        List<Order> orders = orderRepository.findAllByUid(userId);

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // 地域信息
        Set<String> locations = logs.stream()
                .map(log -> getRegionFromIpAddress(log.getIpAddress())) // 使用 IP 地址解析地域
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        user.setLocations(String.join(",", locations));

        // 购买力
        double totalSpent = 0.0;
        for (Order order : orders) {
            System.out.println(order.getTotalprice());
            totalSpent += order.getTotalprice();
        }
        user.setTotalSpent(totalSpent);

        // 主要购买物品类别
        Map<String, Long> categoryCounts = orders.stream()
                .collect(Collectors.groupingBy(Order::getGood_type, Collectors.counting()));
        user.setCategoryCounts(categoryCounts.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining(",")));

        userRepository.save(user);

        return user;
    }

    // 模拟 IP 地址解析地域的方法
    private String getRegionFromIpAddress(String ipAddress) {
        // 这里可以使用第三方服务或本地数据库来解析 IP 地址
        // 例如：使用 IP2Location 或其他 IP 地理位置服务
        // 以下是一个简单的模拟实现
        if (ipAddress.equals("192.168.1.1")) {
            return "Beijing";
        } else if (ipAddress.equals("192.168.1.2")) {
            return "Shanghai";
        }
        return ipAddress;
    }


    public Map<LocalDate, Integer> getSalesTrend(Long goodId) {
        List<Order> orders = orderRepository.findAllByGid(goodId);

        // 按日期分组并计算每天的销售数量
        Map<LocalDate, Integer> salesTrend = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrder_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        Collectors.summingInt(order -> 1) // 每个订单计数为1
                ));

        // 填充缺失的日期
        LocalDate startDate = orders.stream()
                .map(order -> order.getOrder_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .min(LocalDate::compareTo)
                .orElse(LocalDate.now());

        LocalDate endDate = orders.stream()
                .map(order -> order.getOrder_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .max(LocalDate::compareTo)
                .orElse(LocalDate.now());

        Map<LocalDate, Integer> completeSalesTrend = new HashMap<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            completeSalesTrend.put(date, salesTrend.getOrDefault(date, 0));
        }

        return completeSalesTrend;
    }
    public List<Order> detectSalesAnomalies(Long goodId) {
        List<Order> orders = orderRepository.findAllByGid(goodId);

        // 按日期分组并计算每天的销售数量
        Map<LocalDate, Integer> salesTrend = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrder_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        Collectors.summingInt(order -> 1) // 每个订单计数为1
                ));

        // 计算平均销售量和标准差
        double averageSales = salesTrend.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        double stdDev = Math.sqrt(salesTrend.values().stream()
                .mapToDouble(value -> Math.pow(value - averageSales, 2))
                .average()
                .orElse(0.0));

        // 识别异常销售数据
        List<Order> anomalies = orders.stream()
                .filter(order -> {
                    int quantity = order.getSum();
                    return quantity > averageSales + 2 * stdDev || quantity < averageSales - 2 * stdDev;
                })
                .collect(Collectors.toList());

        return anomalies;
    }

}