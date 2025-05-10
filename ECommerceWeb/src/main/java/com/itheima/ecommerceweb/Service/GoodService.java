package com.itheima.ecommerceweb.Service;

import com.itheima.ecommerceweb.Entities.Good;
import com.itheima.ecommerceweb.Service.GoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GoodService {

    private final GoodRepository goodRepository;
    @Autowired
    public GoodService(GoodRepository goodRepository) {
        this.goodRepository = goodRepository;
    }

    public List<Good> getAllGoods() {
        return goodRepository.findAll();
    }
    public List<Good> getGoodBySid(long sid) {
        return goodRepository.findBySid(sid);
//        似乎这个要在接口声明，但是findbyid不用。
    }

    public Optional<Good> getGoodById(Long id) {
        return goodRepository.findById(id);
    }


    public void deleteGood(Long id) {
        goodRepository.deleteById(id);
    }
    public Good saveGood(Good good) {
        return goodRepository.save(good);
    }
    public List<Good> searchGoods(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return goodRepository.findAll(); // 如果没有关键字，返回所有商品
        }
        return goodRepository.searchByKeyword(keyword.trim());
    }
}
