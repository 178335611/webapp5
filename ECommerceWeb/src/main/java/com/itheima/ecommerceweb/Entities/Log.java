package com.itheima.ecommerceweb.Entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "logs")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( nullable = false,name="id")
    private Long id;

    @Column(nullable = false,name = "uid")
    private Long uid;
    @Column(nullable = false,name = "action")
    private String action;
    @Column(name = "ipaddress")
    private String ipAddress;

    @Column(nullable = false,name = "actiontime")
    private Date actionTime;
    @Column(name = "goodid")
    private Long goodId;
    @Column(name = "goodcategory")
    private String goodCategory;
    @Column(name = "stayseconds")
    private int staySeconds;

    public Log() {
        this.goodId = -1L; // 默认值
        this.goodCategory = ""; // 默认值
        this.staySeconds = -1; // 默认值
        this.actionTime = new Date(); // 默认值
        this.ipAddress = "";
        this.uid = -1L;
        this.action = "";
    }

    public String toString(){
        return "Log{" +
                "id=" + id +
                ", uid=" + uid +
                ", action='" + action + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", actionTime=" + actionTime +
                ", goodId=" + goodId +
                ", goodCategory='" + goodCategory + '\'' +
                ", staySeconds=" + staySeconds +
                '}';
    }

}
