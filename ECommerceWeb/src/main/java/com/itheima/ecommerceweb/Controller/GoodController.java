package com.itheima.ecommerceweb.Controller;

import com.itheima.ecommerceweb.Entities.Good;
import com.itheima.ecommerceweb.Service.GoodService;
import com.itheima.ecommerceweb.Service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class GoodController {

    private final GoodService goodService;
    private final LogService logService;

    // 构造函数依赖注入
    @Autowired
    public GoodController(GoodService goodService, LogService logService) {
        this.goodService = goodService;
        this.logService = logService;
    }
    private static final Logger logger = LoggerFactory.getLogger(GoodController.class);

    // 获取所有商品信息
    @GetMapping("/seller/warehouse/goods/{sid}")
    public ResponseEntity<List<Good>> getSellerGoods(@PathVariable Long sid) {
        List<Good> goods = goodService.getGoodBySid(sid);
        return ResponseEntity.ok(goods);
    }

    @GetMapping("/seller/warehouse")
    public String showWarehouseForm() {
        return "seller/warehouse";
    }
    // 删除指定 ID 的商品
    @PostMapping("/seller/warehouse/{id}")
    public String deleteGood(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        goodService.deleteGood(id);
        System.out.println(id);
        return "seller/warehouse";
    }
    @GetMapping("/seller/warehouse/goodsadd")//在这里写上/seller/warehouse会和上面叠加
    public String showAddGoodForm(HttpSession session, Model model) {
        session.setAttribute("good_add_stime", new Date());
        session.setAttribute("goodid",model.getAttribute("goodid"));
        return "seller/goodsadd";
    }

    @PostMapping("/seller/warehouse/goodsadd")
    public ResponseEntity<Good> addGood(@RequestBody Good good,HttpSession session) {
        logService.logOperation((Long)session.getAttribute("id"), "商家添加商品");
        Good savedGood = goodService.saveGood(good);
        return ResponseEntity.ok(savedGood);
    }
    // 通过 ID 查询商品信息
    @GetMapping("/seller/warehouse/goodsedit/{id}")
    public String showEditGoodForm(@PathVariable Long id, Model model) {

        Good good = goodService.getGoodById(id).orElseThrow(() -> new RuntimeException("商品未找到"));
        // 将商品信息传递到前端
        model.addAttribute("good", good);
        //可以通过thymeleaf模板语言来渲染
        return "seller/goodsedit"; // 返回对应的编辑页面

    }
    @PutMapping("/seller/warehouse/goodsedit/{id}")
    public ResponseEntity<Good> editGood(@PathVariable Long id, @RequestBody Good updatedGood, HttpSession session) {
        try {
            // 检查商品是否存在
            Optional<Good> existingGoodOptional = goodService.getGoodById(id);
            if (!existingGoodOptional.isPresent()) {
                return ResponseEntity.notFound().build(); // 如果商品不存在，返回 404
            }
            logger.info("Editing good with id: " + id);
            logService.logOperation((Long)session.getAttribute("id"), "商家编辑商品");
            // 获取现有商品对象
            Good existingGood = existingGoodOptional.get();

            // 更新现有商品的字段
            existingGood.setName(updatedGood.getName());
            existingGood.setPrice(updatedGood.getPrice());
            existingGood.setSale(updatedGood.getSale());
            existingGood.setDescription(updatedGood.getDescription());
            existingGood.setSid(updatedGood.getSid());
            existingGood.setGoodtype(updatedGood.getGoodtype());

            // 保存更新后的商品
            System.out.println("Existing Good: " + existingGood);
            System.out.println("Updated Good: " + updatedGood);
            Good savedGood = goodService.saveGood(existingGood);

            // 返回更新后的商品
            return ResponseEntity.ok(savedGood);
        } catch (Exception e) {
            logger.error("Failed to edit good with id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/user/shoppingMall/goods")
    public ResponseEntity<List<Good>> getAllGoods() {
        List<Good> goods = goodService.getAllGoods();
        return ResponseEntity.ok(goods);
    }
    @GetMapping("/user/shoppingMall")
    public String showShoppingMallForm() {
        return "user/shoppingMall";
    }
    @GetMapping("/user/order_add/{id}")
    public String showOrderAddForm(@PathVariable Long id, Model model, HttpSession session) {
        // 通过 ID 查询商品信息
        Good good = goodService.getGoodById(id).orElseThrow(() -> new RuntimeException("商品未找到"));
        // 将商品信息传递到前端
        model.addAttribute("good", good);
        //点开商品详情页的时候，记录下用户的浏览时间
        session.setAttribute("order_add_stime",new Date());
        //可以通过thymeleaf模板语言来渲染
        return "user/order_add"; // 返回对应的编辑页面

    }
    @GetMapping("/api/goods/search")
    @ResponseBody
    public ResponseEntity<List<Good>> searchGoods(@RequestParam(required = false) String keyword) {
        List<Good> goodsList = goodService.searchGoods(keyword);
        return ResponseEntity.ok(goodsList);
    }
}
