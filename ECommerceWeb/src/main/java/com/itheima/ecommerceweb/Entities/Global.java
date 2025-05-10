package com.itheima.ecommerceweb.Entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Global {
    private Long id;
    private String type;

    public Global(Long id, String type) {
        this.id=id;
        this.type=type;
    }


}
