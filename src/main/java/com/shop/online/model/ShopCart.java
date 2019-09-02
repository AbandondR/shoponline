package com.shop.online.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 
 * 
 * @author yjh
 * @version V1.0 创建时间：2019-03-01
 *          Copyright 2018 by Myself
 *
 */
@Data
@Table(name = "cart")
@Entity
@ApiModel(value="Cart对象", description="购物车")
public class ShopCart implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "id", columnDefinition = "varchar(50)" )
    @Id
    @GeneratedValue(generator = "cartId")
    @GenericGenerator(name="cartId", strategy = "com.shop.online.util.MyCustomIdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name="prefix", value="cart"),
                          @org.hibernate.annotations.Parameter(name="batchNum", value="1")})
    private String id;

    @Column(name = "customer_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "用户", name="customerId", example = "", required = false)
    private String customerId;

    @Column(name = "capacity", columnDefinition = "varchar(5)" )
    @ApiModelProperty(value = "购物车容量", name="capacity", example = "", required = false)
    private String capacity;

}