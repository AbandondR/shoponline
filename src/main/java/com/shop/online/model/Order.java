package com.shop.online.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * 订单详情表
 * @author yjh
 * @version V1.0 创建时间：2019-03-01
 *          Copyright 2018 by Myself
 *
 */
@Data
@Table(name = "orders")
@Entity
@ApiModel(value="Order对象", description="订单详情表")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "id", columnDefinition = "varchar(50)" )
    @Id
    private String id;

    @Column(name = "trade_id", columnDefinition = "varchar(50)" )
    private String tradeId;

    @Column(name = "sku_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "商品sku", name="skuId", example = "", required = false)
    private String skuId;

    @Column(name = "product_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "商品id", name="productId", example = "", required = false)
    private String productId;

    @Column(name = "order_num", columnDefinition = "varchar(30)" )
    @ApiModelProperty(value = "子订单号", name="orderNum", example = "", required = false)
    private String orderNum;

    @Column(name = "product_name", columnDefinition = "varchar(100)" )
    private String productName;

    @Column(name = "item_count", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "sku商品数量", name="itemCount", example = "", required = false)
    private String itemCount;

    @Column(name = "price", columnDefinition = "varchar(20)" )
    @ApiModelProperty(value = "单价", name="price", example = "", required = false)
    private String price;

    @Column(name = "description", columnDefinition = "varchar(500)" )
    @ApiModelProperty(value = "商品sku说明", name="description", example = "", required = false)
    private String description;

    @Column(name = "amount", columnDefinition = "varchar(48)" )
    @ApiModelProperty(value = "总金额", name="amount", example = "", required = false)
    private String amount;

    @Column(name = "actual_paid", columnDefinition = "varchar(20)" )
    @ApiModelProperty(value = "实付款", name="actualPaid", example = "", required = false)
    private String actualPaid;

    @Column(name = "sku_group", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "销售属性组合(需要分解，存在多组属性)", name="skuGroup", example = "", required = false)
    private String skuGroup;

    @Column(name = "order_status", columnDefinition = "varchar(20)" )
    @ApiModelProperty(value = "订单状态", name="orderStatus", example = "", required = false)
    private String orderStatus;

    @Column(name = "express_num", columnDefinition = "varchar(36)" )
    @ApiModelProperty(value = "运单编号", name="expressNum", example = "", required = false)
    private String expressNum;

    @Column(name = "image_location", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "商品sku图片（一个sku只有一张图片）", name="imageLocation", example = "", required = false)
    private String imageLocation;

    @Column(name="create_time", columnDefinition = "datetime")
    private Date createTime;

    @Column(name="customer_id", columnDefinition = "varchar(255)")
    @ApiModelProperty(value = "订单所有者", name="customerId", example = "", required = false)
    private String customerId;

    @Column(name="receiver", columnDefinition = "varchar(255)")
    @ApiModelProperty(value = "收货人", name="receiver", example = "", required = false)
    private String receiver;

    @Column(name="is_exclusive_trade", columnDefinition = "varchar(2)")
    private String isExclusiveTrade;

    @Transient
    private Trade trade;
}