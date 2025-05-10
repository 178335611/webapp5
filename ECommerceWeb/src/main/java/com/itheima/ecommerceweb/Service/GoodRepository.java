package com.itheima.ecommerceweb.Service;

import com.itheima.ecommerceweb.Entities.Good;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GoodRepository extends JpaRepository<Good, Long> {
    List<Good> findBySid(Long sid);
    @Query("SELECT g FROM Good g WHERE g.name LIKE %:keyword% OR g.goodtype LIKE %:keyword%")
    List<Good> searchByKeyword(@Param("keyword") String keyword);

}
