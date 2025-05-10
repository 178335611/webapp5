package com.itheima.ecommerceweb.Controller;

import com.itheima.ecommerceweb.Entities.Good;
import com.itheima.ecommerceweb.Entities.Log;
import com.itheima.ecommerceweb.Service.GoodService;
import com.itheima.ecommerceweb.Service.LogService;
import com.itheima.ecommerceweb.Service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.itheima.ecommerceweb.Entities.Order;

import java.util.*;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final GoodService goodService;
    private final LogService logService;
    public OrderController(OrderService orderService, LogService logService,GoodService goodService) {
        this.orderService = orderService;
        this.goodService=goodService;
        this.logService=logService;

    }
    @PostMapping("/user/order_add")
    public ResponseEntity<?> addOrder(@RequestBody Order order) {
        long gid = order.getGid();
        Optional<Good> good=goodService.getGoodById(gid);
        good.ifPresent(value -> order.setGood_name(value.getName()));
        good.ifPresent(value -> order.setGood_type(value.getGoodtype()));

        System.out.println(order.toString()); // 打印订单信息，便于调试
        try {
            orderService.addOrder(order);
            return ResponseEntity.ok().body("{\"message\":\"添加订单成功\"}");
        } catch (Exception e) {
            e.printStackTrace(); // 打印堆栈信息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"添加订单失败: " + e.getMessage() + "\"}");       }
    }
    @GetMapping("/user/order_list")
    public String orderList() {
        return "user/order_list";
    }
    @GetMapping("/api/order_list/{uid}")
    @ResponseBody
    public List<Order> orderList(@PathVariable Long uid) {
        System.out.println("uid: " + uid);
        return orderService.getOrdersByUid(uid);
    }
    @GetMapping("/api/order_list_seller/{uid}")
    @ResponseBody
    public List<Order> orderList_seller(@PathVariable Long uid) {
        System.out.println("sid: " + uid);
        return orderService.getOrderBySid(uid);
    }
    @PostMapping("/api/order_status/{id}")
    @ResponseBody
    public ResponseEntity<String> orderStatus(@PathVariable Long id) {
        System.out.println("orderid: " + id);
        Order order=orderService.getOrderByOid(id);
        String status=order.getStatus();
        if(status.equals("待付款"))
            order.setStatus("已付款");
        if(status.equals("已发货"))
            order.setStatus("已收货");
        orderService.saveOrder(order);//更新数据库
        System.out.println(order.toString());
        return ResponseEntity.ok("订单状态已更新");
    }

    @PostMapping("/api/order_status_seller/{id}")
    @ResponseBody
    public ResponseEntity<String> orderStatus_seller(@PathVariable Long id) {
        System.out.println("orderid: " + id);
        Order order=orderService.getOrderByOid(id);
        String status=order.getStatus();
        if(status.equals("已付款"))
            order.setStatus("已发货");
        orderService.saveOrder(order);//更新数据库
        System.out.println(order.toString());
        return ResponseEntity.ok("订单状态已更新");
    }

    @PostMapping("/api/cancel_order/{orderId}")
    @ResponseBody
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        System.out.println("orderid: " + orderId);
        Order order=orderService.getOrderByOid(orderId);
        String status=order.getStatus();
        if (!status.contains("-商家已取消")) {
            if(!status.contains("-用户已取消"))
                status = status + "-用户已取消";
            order.setStatus(status);
            orderService.saveOrder(order);//更新数据库
            System.out.println(order.toString());
            return ResponseEntity.ok("订单已取消");
        }
        else {
            return ResponseEntity.ok("无法进行此操作");
        }
    }

    @PostMapping("/api/cancel_order_seller/{orderId}")
    @ResponseBody
    public ResponseEntity<String> cancelOrder_seller(@PathVariable Long orderId) {
        System.out.println("orderid: " + orderId);
        Order order=orderService.getOrderByOid(orderId);
        String status=order.getStatus();
        if (!status.contains("用户已取消")) {
            if(!status.contains("-商家已取消"))
                status = status + "-商家已取消";
            order.setStatus(status);
            orderService.saveOrder(order);//更新数据库
            System.out.println(order.toString());
            return ResponseEntity.ok("订单已取消");
        }
        else
            return ResponseEntity.ok("无法进行此操作");

    }


    @GetMapping("api/by-good-type")
    public List<Order> getOrdersByGoodType(@RequestParam String good_type) {
        return orderService.getOrdersByGood_type(good_type);
        //未使用
    }

    @PostMapping("/user/order_add/logout")//在这里写上/seller/warehouse会和上面叠加
    @ResponseBody
    public ResponseEntity<?> showAddGoodOutForm(HttpSession session, HttpServletRequest request,
                                     @RequestBody Log log) {
        Object startTimeObj = session.getAttribute("good_add_stime");
        int stayTime = 0;
        if (startTimeObj instanceof Date) {
            Date startTime = (Date) startTimeObj;
            Date endTime = new Date();
            // 计算时间差毫秒
            long timeDiffInMillis = endTime.getTime() - startTime.getTime();
            // 将时间差转换为秒
            stayTime = (int) (timeDiffInMillis / 1000);
            System.out.println("Stay time: " + stayTime + " seconds");
        } else {
            // 如果 session 中没有开始时间，打印错误信息或采取其他措施
            System.out.println("Start time not found in session");
        }
        String ipaddress=request.getRemoteAddr();
        logService.logBuyGood((Long)session.getAttribute("id"),"访问商品结束",ipaddress
                ,(Long)log.getUid(),log.getGoodCategory(), stayTime);
        // 返回一个 JSON 对象
        Map<String, String> response = new HashMap<>();
        response.put("message", "Log action recorded successfully");
        response.put("redirectUrl", "/user/shoppingMall");

        return ResponseEntity.ok(response);
    }


}
