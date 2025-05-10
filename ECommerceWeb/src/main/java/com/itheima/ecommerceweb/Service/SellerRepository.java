package com.itheima.ecommerceweb.Service;

import com.itheima.ecommerceweb.Entities.Seller;
import com.itheima.ecommerceweb.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByName(String name);//在此处定义该方法JPA会自动生成方法
    @Query("SELECT DISTINCT o.sid FROM Order o")
    List<Long> getSellerSids();
}