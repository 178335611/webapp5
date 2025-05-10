package com.itheima.ecommerceweb.Service;

import com.itheima.ecommerceweb.Entities.Manager;
import com.itheima.ecommerceweb.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Optional<Manager> findByName(String name);//在此处定义该方法JPA会自动生成方法
}