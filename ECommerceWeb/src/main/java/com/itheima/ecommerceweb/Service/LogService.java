package com.itheima.ecommerceweb.Service;

import com.itheima.ecommerceweb.Entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import com.itheima.ecommerceweb.Entities.Log;

@Service
public class LogService {
    private final LogRepository logRepository;
    @Autowired
    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }
    public void logLogin(Long userId, String action, String ipAddress) {
        Log log = new Log();
        log.setAction(action);
        log.setUid(userId);
        log.setIpAddress(ipAddress);
        log.setActionTime(new Date());
        System.out.println(log);
        logRepository.save(log); // 保存到数据库
    }
    public void logBuyGood(Long userId, String action, String ipAddress,
                           Long goodid, String goodCategory,int stayTime) {
        Log log = new Log();
        log.setAction(action);
        log.setUid(userId);
        log.setIpAddress(ipAddress);
        log.setActionTime(new Date());
        log.setGoodCategory(goodCategory);
        log.setGoodId(goodid);
        log.setStaySeconds(stayTime);

        System.out.println(log);
        logRepository.save(log); // 保存到数据库
    }
    public void logOperation(Long userId, String action) {
        Log log = new Log();
        log.setAction(action);
        log.setUid(userId);
        log.setActionTime(new Date());
        logRepository.save(log);
        logRepository.save(log);
    }

}
