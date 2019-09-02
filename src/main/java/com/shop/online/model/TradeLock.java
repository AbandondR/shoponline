package com.shop.online.model;

import lombok.Data;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/12 0012
 */
@Data
@Table(name = "trade_lock")
@Entity
public class TradeLock implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @GenericGenerator(name = "persistenceGenerator", strategy = "increment")
    private Integer id;

    @Column(name="trade_token", columnDefinition = "varchar(255)", unique = true)
    private String tradeToken;
}
