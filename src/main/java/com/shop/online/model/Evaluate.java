package com.shop.online.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 订单评价表
 * @author yjh
 * @version V1.0 创建时间：2019-03-01
 *          Copyright 2018 by Myself
 *
 */
@Data
@Table(name = "evaluate")
@Entity
@ApiModel(value="Evaluate对象", description="订单评价表")
public class Evaluate implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "id", columnDefinition = "varchar(50)" )
    @Id
    private String id;

    @Column(name = "order_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "订单id", name="orderId", example = "", required = false)
    private String orderId;

    @Column(name = "customer_id", columnDefinition = "varchar(50)" )
    private String customerId;

    @Column(name = "product_id", columnDefinition = "varchar(50)" )
    private String productId;

    @Column(name = "content", columnDefinition = "varchar(500)" )
    @ApiModelProperty(value = "评论内容", name="content", example = "", required = false)
    private String content;

    @Column(name = "nick_name", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "评价人姓名", name="nickName", example = "", required = false)
    private String nickName;

    @Column(name = "create_time", columnDefinition = "datetime" )
    @ApiModelProperty(value = "评论时间", name="createTime", example = "", required = false)
    private Date createTime;

    @Column(name = "has_picture", columnDefinition = "varchar(255)" )
    private String hasPicture;

    @Column(name = "picture_location", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "图片地址（多张图片地址使用@分隔符）", name="pictureLocation", example = "", required = false)
    private String pictureLocation;

    @Column(name = "evaluate_level", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "对商品的评价等级", name="evaluateLevel", example = "", required = false)
    private String evaluateLevel;

    @Column(name = "alter_time", columnDefinition = "datetime" )
    @ApiModelProperty(value = "评论修改日期", name="alterTime", example = "", required = false)
    private Date alterTime;

    @Column(name="is_deleted", columnDefinition = "varchar(2)")
    @ApiModelProperty(value = "逻辑删除", name="isDeleted", example = "", required = false)
    private String isDeleted;



}