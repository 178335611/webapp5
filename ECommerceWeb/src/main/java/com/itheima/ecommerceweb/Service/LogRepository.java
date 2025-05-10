package com.itheima.ecommerceweb.Service;

import org.springframework.data.jpa.repository.JpaRepository;
import com.itheima.ecommerceweb.Entities.Log;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {
    Log findById(long id);
    List<Log> findAllByUid(Long userId);
}
