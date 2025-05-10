package com.itheima.ecommerceweb.Controller;

import com.itheima.ecommerceweb.Entities.Order;
import com.itheima.ecommerceweb.Entities.Seller;
import com.itheima.ecommerceweb.Service.LogService;
import com.itheima.ecommerceweb.Service.SellerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class SellerController {
    private final SellerService sellerService;
    private final LogService logService;
    @Autowired
    public SellerController(SellerService sellerService, LogService logService) {

        this.sellerService = sellerService;
        this.logService = logService;
    }

    @GetMapping("/seller/register")
    public String showRegistrationForm() {
        return "seller/register";
    }

    @PostMapping("/seller/register")
    public String registerSeller(@ModelAttribute Seller seller, Model model) {
        // 检查用户对象是否为空
        if (seller == null) {
            model.addAttribute("message", "Invalid request. Seller object is missing!");
            return "seller/register";
        }

        // 检查用户名是否为空
        if (seller.getName() == null || seller.getName().isEmpty()) {
            model.addAttribute("message", "Sellername is required!");
            return "seller/register";
        }

        // 检查密码是否为空
        if (seller.getPassword() == null || seller.getPassword().isEmpty()) {
            model.addAttribute("message", "Password is required!");
            return "seller/register";
        }


        // 调用sellerService的registerSeller方法进行注册
        if (sellerService.registerSeller(seller)) {
            model.addAttribute("message", "Registration successful!");
        } else {
            model.addAttribute("message", "Sellername already exists!");
        }

        return "seller/register";
    }


    @GetMapping("/seller/login")
    public String showLoginForm(Model model) {
        return "seller/login";
//        这段代码的主要功能是处理用户登录页面的显示请求。当用户通过浏览器访问/login路径时，服务器会返回一个登录表单的视图，
//        同样的可以实现跳转
    }
    //商家退出
    @GetMapping("/seller/logout")
    public String showLogoutForm(Model model, HttpSession session, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        logService.logLogin((Long) session.getAttribute("id"),"商家退出",ipAddress);
        return "seller/login";
    }

    @PostMapping("/seller/login")
    public String loginSeller(@RequestParam String name, @RequestParam String password,
                              Model model, HttpSession session, HttpServletRequest request) {
        Optional<Seller> seller = sellerService.loginSeller(name, password);

        if (seller.isPresent()) {
            model.addAttribute("message", "Login successful!");
            model.addAttribute("id", seller.get().getId()); // 添加用户 ID 到模型
            model.addAttribute("type", "seller"); // 添加用户类型商家到模型
            session.setAttribute("type","seller");
            session.setAttribute("id",seller.get().getId());
            String ipAddress = request.getRemoteAddr();
            logService.logLogin(seller.get().getId(),"商家登录",ipAddress);

            return "seller/warehouse";
        } else {
            model.addAttribute("message", "Invalid sellername or password!");
        }
        return "seller/login";
    }

    @GetMapping("/seller/delete")
    public String showDeleteForm() {
        return "seller/delete";
    }//此处就写视图名称即可

    @PostMapping("/seller/delete")
    public String deleteSeller(@RequestParam String name, @RequestParam String password, Model model) {
        Optional<Seller> seller = sellerService.loginSeller(name, password);
        if (seller.isPresent()) {
            if (sellerService.deleteSeller(name)) {
                model.addAttribute("message", "用户删除成功！");
            } else {
                model.addAttribute("message", "用户未找到！");
            }
        } else {
            model.addAttribute("message", "用户名或密码无效！");
        }
        return "seller/delete"; // 返回删除结果页面
    }
    @GetMapping("/api/seller_sids")
    @ResponseBody
    public List<Long> getSellerSids() {
        return sellerService.getSellerSids();
    }
    @GetMapping("/seller/saleroom")
    public String sellerSaleroom(HttpSession session) {
        logService.logOperation((Long) session.getAttribute("id"),"商家查看销售信息");
        return "seller/saleroom";
    }
    @GetMapping("/seller/order_list")
    public String sellerOrderList(HttpSession session) {
        logService.logOperation((Long) session.getAttribute("id"),"商家查看订单信息");
        return "seller/order_list";
    }

}