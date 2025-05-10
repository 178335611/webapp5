package com.itheima.ecommerceweb.Controller;

import ch.qos.logback.core.model.Model;
import com.itheima.ecommerceweb.Entities.User;
import com.itheima.ecommerceweb.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import com.itheima.ecommerceweb.Entities.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class WelcomeController {
    private final ManagerService managerService;
    private final SellerService sellerService;
    private final LogService logService;
    private final RecommendService recommendService;
    private final AnalyticsService analyticsService;
    private final UserService userService;

    @Autowired
    public WelcomeController(ManagerService ManagerService, AnalyticsService analyticsService,
                             LogService logService, SellerService sellerService, RecommendService recommendService, UserService userService) {
        this.managerService = ManagerService;
        this.sellerService = sellerService;
        this.logService = logService;
        this.analyticsService = analyticsService;
        this.recommendService = recommendService;
        this.userService = userService;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome"; // 对应于 html/welcome.html
    }
    @GetMapping("/")
    public String welcomeForm() {
        return "welcome"; // 对应于 html/welcome.html
    }
    @GetMapping("/seller/sales")
    public String sales(Model model) {
        return "seller/sales";
    }
    //大数据部分的函数也在这里
    @GetMapping("/api/sales/trend/{goodId}")
    @ResponseBody
    public Map<LocalDate, Integer> getSalesTrend(@PathVariable Long goodId) {
        return analyticsService.getSalesTrend(goodId);
    }

    @GetMapping("/api/sales/anomalies/{goodId}")
    @ResponseBody
    public List<Order> detectSalesAnomalies(@PathVariable Long goodId) {
        return analyticsService.detectSalesAnomalies(goodId);
    }

    @GetMapping("/api/users/{userId}/profile")
    @ResponseBody
    public User getUserProfile(@PathVariable Long userId) {
        System.out.println(userService.getUser(userId));
        return analyticsService.updateUserProfile(userId);
    }

    @GetMapping("/api/users/{userId}/purchase-trend")
    @ResponseBody
    public Map<String, Object> getUserPurchaseTrend(@PathVariable Long userId) {
        List<Order> recommendedProducts = recommendService.recommendProducts(userId);
        return Map.of("recommendedProducts", recommendedProducts.stream().map(Order::getGood_name).collect(Collectors.toList()));
    }
    @GetMapping("manager/dashboard")
    public String dashboard() {
        return "manager/dashboard";
    }
}
