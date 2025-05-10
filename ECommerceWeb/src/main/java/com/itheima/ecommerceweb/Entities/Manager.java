package com.itheima.ecommerceweb.Entities;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "managers")
public class Manager {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( nullable = false,name="id")
    private Long id;

    @Column(nullable = false,name = "name")
    private String name;
    @Column(nullable = false,name = "password")
    private String password;

}
