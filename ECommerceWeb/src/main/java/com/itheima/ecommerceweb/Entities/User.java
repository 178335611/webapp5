package com.itheima.ecommerceweb.Entities;

import jakarta.persistence.*;
import lombok.*;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( nullable = false,name="id")
    private Long id;

    @Column(nullable = false,name = "name")
    private String name;
    @Column(nullable = false,name = "password")
    private String password;
    @Column(nullable = false,name = "email")
    private String email;
    @Column(name = "locations")
    private String locations; // 用逗号分隔的地域信息

    @Column(name = "totalspent")
    private Double totalSpent; // 总消费金额

    @Column(name = "categorycounts")
    private String categoryCounts; // 用逗号分隔的类别计数信息

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", locations='" + locations + '\'' +
                ", totalSpent=" + totalSpent +
                ", categoryCounts='" + categoryCounts + '\'' +
                '}';
    }
}
