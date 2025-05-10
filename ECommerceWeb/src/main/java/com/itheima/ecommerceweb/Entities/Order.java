package com.itheima.ecommerceweb.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( nullable = false,name="id")
    private Long id;

    @Column( nullable = false,name="sid")
    private Long sid;
    @Column( nullable = false,name="gid")
    private Long gid;
    @Column( nullable = false,name="uid")
    private Long uid;

    @Column(nullable = false,name = "sum")
    private int sum;
    @Column(nullable = false,name = "totalprice")
    private double totalprice;
    @Column(nullable = false,name = "status")
    private String status;
    @Column(nullable = false,name = "order_date")
    private Date order_date;
    @Column(name = "good_name")
    private String good_name;
    @Column(name = "good_type")
    private String good_type;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", uid=" + uid +
                ", sid=" + sid +
                ", gid=" + gid +
                ", good_name='" + good_name + '\'' +
                ", good_type='" + good_type + '\'' +
                ", sum=" + sum +
                ", totalprice=" + totalprice +
                ", orderDate='" + order_date + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

}
