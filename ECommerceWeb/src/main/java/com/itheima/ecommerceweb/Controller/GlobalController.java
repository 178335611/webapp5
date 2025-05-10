package com.itheima.ecommerceweb.Controller;

import com.itheima.ecommerceweb.Entities.Global;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalController {

    @Autowired
    private HttpSession session;

    @GetMapping("/global")
    public Global getGlobal() {
        Long id = (Long) session.getAttribute("id");
        String type = (String) session.getAttribute("type");
        return new Global(id, type);
    }
}