package com.itheima.ecommerceweb.Service;

import com.itheima.ecommerceweb.Entities.Manager;
import com.itheima.ecommerceweb.Entities.Order;
import com.itheima.ecommerceweb.Entities.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;
    @Autowired
    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public boolean registerSeller(Seller seller) {
        Optional<Seller> existingSeller = sellerRepository.findByName(seller.getName());
        if (existingSeller.isPresent()) {
            return false; // 用户名已存在
        }
        System.out.println("Seller " + seller.getName() + " not exists");
        sellerRepository.save(seller);
        return true;
    }

    public Optional<Seller> loginSeller(String sellername, String password) {
        return sellerRepository.findByName(sellername)
                .filter(seller -> seller.getPassword().equals(password));
    }

    public boolean deleteSeller(String sellername) {
        Optional<Seller> seller = sellerRepository.findByName(sellername);
        if (seller.isPresent()) {
            sellerRepository.delete(seller.get());
            return true;
        }
        return false;
    }
    public void addSeller(Seller seller) {
        sellerRepository.save(seller);
    }

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    public void deleteSeller(Long id) {
        sellerRepository.deleteById(id);
    }
    public void resetPassword(Long sellerId,String password) {
        if (sellerRepository.findById(sellerId).isPresent()) {
            Seller seller = sellerRepository.findById(sellerId).get();
            seller.setPassword(password);
            sellerRepository.save(seller);
        }
    }
    public Seller getSellerById(Long id) {
        return sellerRepository.findById(id).orElse(null);
    }
    public List<Long> getSellerSids() {
        return sellerRepository.getSellerSids();
    }
}