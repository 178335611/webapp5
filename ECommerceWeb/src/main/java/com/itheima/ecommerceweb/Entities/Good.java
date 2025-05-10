package com.itheima.ecommerceweb.Entities;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "goods")
public class Good {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( nullable = false,name="id")
    private Long id;

    @Column(nullable = false,name = "name")
    private String name;
    @Column(nullable = false,name = "price")
    private double price;
    @Column(nullable = false,name = "description")
    private String description;
    @Column(nullable = false,name = "sale")
    private double sale;
    @Column(nullable = false,name="sid")
    private Long sid;
    @Column(nullable = true,name="goodtype")
    private String goodtype;//明明是可null的却顺手加上了not null

}
