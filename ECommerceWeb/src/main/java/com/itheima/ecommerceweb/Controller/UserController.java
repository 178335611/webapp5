package com.itheima.ecommerceweb.Controller;

import com.itheima.ecommerceweb.Entities.Good;
import com.itheima.ecommerceweb.Entities.User;
import com.itheima.ecommerceweb.Entities.Log;
import com.itheima.ecommerceweb.Service.LogService;
import com.itheima.ecommerceweb.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    private final UserService userService;
    private final LogService logService;
    @Autowired
    public UserController(UserService userService, LogService logService) {
        this.userService = userService;
        this.logService = logService;
    }

    @GetMapping("/user/register")
    public String showRegistrationForm() {
        return "user/register";
    }

    @PostMapping("/user/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        // 检查用户对象是否为空
        if (user == null) {
            model.addAttribute("message", "Invalid request. User object is missing!");
            return "user/register";
        }

        // 检查用户名是否为空
        if (user.getName() == null || user.getName().isEmpty()) {
            model.addAttribute("message", "Username is required!");
            return "user/register";
        }

        // 检查密码是否为空
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            model.addAttribute("message", "Password is required!");
            return "user/register";
        }

        // 检查其他必要字段是否为空（例如邮箱）
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            model.addAttribute("message", "Email is required!");
            return "user/register";
        }

        // 调用userService的registerUser方法进行注册
        if (userService.registerUser(user)) {
            model.addAttribute("message", "Registration successful!");
        } else {
            model.addAttribute("message", "Username already exists!");
        }

        return "user/register";
    }


    @GetMapping("/user/login")
    public String showLoginForm(Model model) {
        return "user/login";
//        这段代码的主要功能是处理用户登录页面的显示请求。当用户通过浏览器访问/login路径时，服务器会返回一个登录表单的视图，
//        同样的可以实现跳转
    }
    @GetMapping("/user/logout")
    public String showLogoutForm(Model model,HttpSession session,HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        logService.logLogin((Long) session.getAttribute("id"), "用户退出",ipAddress);
        // session可以正常用.
        return "user/login";
//        这段代码的主要功能是处理用户登录页面的显示请求。当用户通过浏览器访问/login路径时，服务器会返回一个登录表单的视图，
//        同样的可以实现跳转
    }

    @PostMapping("/user/login")
    public String loginUser(@RequestParam String username, @RequestParam String password,
                             HttpSession session, Model model, HttpServletRequest request) {
        // 调用userService的loginUser方法进行登录判断
        Optional<User> user = userService.loginUser(username, password);
        // 获取用户 IP 地址
        String ipAddress = request.getRemoteAddr();
        if (user.isPresent()) {
            logService.logLogin(user.get().getId(), "用户登录",ipAddress);

            model.addAttribute("message", "Login successful!");
            model.addAttribute("id", user.get().getId()); // 添加用户 ID 到模型
            model.addAttribute("type", "user"); // 添加用户类型商家到模型
            model.addAttribute("name", user.get().getName()); // 添加用户名到模型
            session.setAttribute("type","user");
            session.setAttribute("id",user.get().getId());
            session.setAttribute("stime",new Date().getTime());

            return "user/shoppingMall";
        } else {
            model.addAttribute("message", "Invalid username or password!");
        }
        return "/user/login";
    }

    @GetMapping("/user/delete")
    public String showDeleteForm() {
        return "user/delete";
    }//此处就写视图名称即可

    @PostMapping("/user/delete")
    public String deleteUser(@RequestParam String username, @RequestParam String password, Model model) {
        Optional<User> user = userService.loginUser(username, password);
        if (user.isPresent()) {
            if (userService.deleteUser(username)) {
                model.addAttribute("message", "用户删除成功！");
            } else {
                model.addAttribute("message", "用户未找到！");
            }
        } else {
            model.addAttribute("message", "用户名或密码无效！");
        }
        return "user/delete"; // 返回删除结果页面
    }
    @GetMapping("/user/no_login")
    public String showNoLoginForm(Model model) {
        return "user/no_login";

    }

}