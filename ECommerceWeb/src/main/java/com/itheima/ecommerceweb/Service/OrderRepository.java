package com.itheima.ecommerceweb.Service;

import com.itheima.ecommerceweb.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findById(long id);
    List<Order> findByUid(long uid);
    List<Order> findBySid(long sid);
    // 使用 @Query 注解显式定义查询
    //由于无法正确读取下划线会报错，所以@Query
    @Query("SELECT o FROM Order o WHERE o.good_type = ?1")
    List<Order> findByGood_type(String good_type);
    List<Order> findAllByUid(Long userId);
    List<Order> findAllByGid(Long gid);
}
