package com.itheima.ecommerceweb.Service;

import com.itheima.ecommerceweb.Entities.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;
    @Autowired
    public ManagerService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public boolean registerManager(Manager manager) {
        Optional<Manager> existingManager = managerRepository.findByName(manager.getName());
        if (existingManager.isPresent()) {
            return false; // 用户名已存在
        }
        System.out.println("Manager " + manager.getName() + " not exists");
        managerRepository.save(manager);
        return true;
    }

    public Optional<Manager> loginManager(String managername, String password) {
        return managerRepository.findByName(managername)
                .filter(manager -> manager.getPassword().equals(password));
    }

    public boolean deleteManager(String managername) {
        Optional<Manager> manager = managerRepository.findByName(managername);
        if (manager.isPresent()) {
            managerRepository.delete(manager.get());
            return true;
        }
        return false;
    }

}