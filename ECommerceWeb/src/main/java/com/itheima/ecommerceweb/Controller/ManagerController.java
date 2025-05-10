package com.itheima.ecommerceweb.Controller;

import com.itheima.ecommerceweb.Entities.Global;
import com.itheima.ecommerceweb.Entities.Seller;
import com.itheima.ecommerceweb.Entities.User;
import com.itheima.ecommerceweb.Service.LogService;
import com.itheima.ecommerceweb.Service.ManagerService;
import com.itheima.ecommerceweb.Service.SellerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.itheima.ecommerceweb.Entities.Manager;

import java.util.List;
import java.util.Optional;

@Controller
public class ManagerController {

    private final ManagerService managerService;
    private final SellerService sellerService;
    private final LogService logService;
    @Autowired
    public ManagerController(ManagerService ManagerService,
                             LogService logService,SellerService sellerService) {
        this.managerService = ManagerService;
        this.sellerService = sellerService;
        this.logService = logService;

    }
    @Autowired
    private HttpSession session;

    @GetMapping("/manager/login")
    public String login() {
        return "manager/login";
    }
    @GetMapping("/manager/logout")
    public String logout(HttpSession session,HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        logService.logLogin((Long) session.getAttribute("id"),"管理员退出",ipAddress);
        return "manager/login";
    }
    @PostMapping("/manager/login")
    public String login(@RequestParam String name, @RequestParam String password,
                        HttpSession session, Model model, HttpServletRequest request) {

        Optional<Manager> manager = managerService.loginManager(name, password);
        if (manager.isPresent()) {
            model.addAttribute("message", "Login successful!");
            model.addAttribute("id", manager.get().getId()); // 添加用户 ID 到模型
            model.addAttribute("type", "manager"); // 添加用户类型到模型
            model.addAttribute("name", manager.get().getName()); // 添加用户名到模型
            session.setAttribute("type","manager");
            session.setAttribute("id",manager.get().getId());

            String ipAddress = request.getRemoteAddr();
            logService.logLogin((Long) session.getAttribute("id"),"管理员登录",ipAddress);
            return "manager/manager_center";
        } else {
            model.addAttribute("message", "Invalid username or password!");
            return "manager/login";
        }

    }
    @GetMapping("/manager/delete")
    public String showDeleteForm() {
        return "manager/delete";
    }//此处就写视图名称即可

    @PostMapping("/manager/delete")
    public String deleteManager(@RequestParam String name, @RequestParam String password, Model model) {
        Optional<Manager> manager = managerService.loginManager(name, password);
        if (manager.isPresent()) {
            if (managerService.deleteManager(name)) {
                model.addAttribute("message", "用户删除成功！");
            } else {
                model.addAttribute("message", "用户未找到！");
            }
        } else {
            model.addAttribute("message", "用户名或密码无效！");
        }
        return "manager/delete"; // 返回删除结果页面
    }
    @GetMapping("/manager/register")
    public String showRegistrationForm() {
        return "manager/register";
    }

    @PostMapping("/manager/register")
    public String registerManager(@ModelAttribute Manager manager, Model model) {
        // 检查用户对象是否为空
        if (manager == null) {
            model.addAttribute("message", "Invalid request. User object is missing!");
            return "manager/register";
        }

        // 检查用户名是否为空
        if (manager.getName() == null || manager.getName().isEmpty()) {
            model.addAttribute("message", "Username is required!");
            return "manager/register";
        }

        // 检查密码是否为空
        if (manager.getPassword() == null || manager.getPassword().isEmpty()) {
            model.addAttribute("message", "Password is required!");
            return "manager/register";
        }


        if (managerService.registerManager(manager)) {
            model.addAttribute("message", "Registration successful!");
        } else {
            model.addAttribute("message", "Username already exists!");
        }

        return "manager/register";
    }
    @PostMapping("/manager/sellers")
    @ResponseBody
    public ResponseEntity<?> addSeller(@RequestBody Seller seller,HttpSession session) {

        sellerService.addSeller(seller);
        logService.logOperation((Long) session.getAttribute("id"),"管理员添加sid-"+seller.getId());
        return ResponseEntity.ok("销售人员添加成功");

    }

    @GetMapping("/manager/sellers")
    @ResponseBody
    public ResponseEntity<List<Seller>> getAllSellers() {
        return ResponseEntity.ok(sellerService.getAllSellers());
    }

    @DeleteMapping("/manager/sellers/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteSeller(@PathVariable Long id) {
        //未使用
        sellerService.deleteSeller(id);
        return ResponseEntity.ok("销售人员删除成功");
    }
    @GetMapping("/manager/manager_center")
    public String managerCenter() {
        return "manager/manager_center";
    }
    @GetMapping("/manager/manager_reset")
    public String managerReset() {
        return "manager/manager_reset";
    }
    @PostMapping("/manager/manager_reset")
    public String managerResetManager(@RequestParam String newPassword,HttpSession session,
                                      @RequestParam Long sellerId, Model model) {
        if (sellerService.getSellerById(sellerId)!= null) {
            model.addAttribute("message", "密码重置成功！");
            logService.logOperation((Long) session.getAttribute("id"),
                    "管理员修改密码sid-"+sellerId);

            sellerService.resetPassword(sellerId, newPassword);
        }
        else {
            model.addAttribute("message", "密码重置失败！");
        }
        return "manager/manager_reset";
    }
    @GetMapping("/manager/sales-performance")
    public String sellerSalesPerformance(HttpSession session) {
        logService.logOperation((Long) session.getAttribute("id"),"管理员查看销售业绩");

        return "manager/sales-performance";

    }
    @GetMapping("/manager/reports")
    public String showReports(HttpSession session) {
        logService.logOperation((Long) session.getAttribute("id"),"管理员查看销售报表");
        return "manager/reports";
    }



}
