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
 * 交易表
 * @author yjh
 * @version V1.0 创建时间：2019-03-01
 *          Copyright 2018 by Myself
 *
 */
@Data
@Table(name = "trade")
@Entity
@ApiModel(value="Trade对象", description="交易表")
public class Trade implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "id", columnDefinition = "varchar(50)" )
    @Id
    private String id;

    @Column(name = "trade_num", columnDefinition = "varchar(30)" )
    @ApiModelProperty(value = "交易编号", name="tradeNum", example = "", required = false)
    private String tradeNum;

    @Column(name = "trade_create_time", columnDefinition = "datetime" )
    @ApiModelProperty(value = "交易时间", name="tradeCreateTime", example = "", required = false)
    private Date tradeCreateTime;

    @Column(name = "paid_type", columnDefinition = "varchar(20)" )
    @ApiModelProperty(value = "支付方式", name="paidType", example = "", required = false)
    private String paidType;

    @Column(name = "pay_finish_time", columnDefinition = "datetime" )
    @ApiModelProperty(value = "支付时间", name="payFinishTime", example = "", required = false)
    private Date payFinishTime;

    @Column(name = "pay_num", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "支付流水号", name="payNum", example = "", required = false)
    private String payNum;

    @Column(name = "has_express_fee", columnDefinition = "varchar(2)" )
    @ApiModelProperty(value = "是否有邮费", name="hasExpressFee", example = "", required = false)
    private String hasExpressFee;

    @Column(name = "express_type", columnDefinition = "varchar(20)" )
    @ApiModelProperty(value = "物流类型", name="expressType", example = "", required = false)
    private String expressType;

    /*@Column(name = "express_num", columnDefinition = "varchar(36)" )
    @ApiModelProperty(value = "运单编号", name="expressNum", example = "", required = false)
    private String expressNum;*/

    @Column(name = "express_fee", columnDefinition = "varchar(48)" )
    @ApiModelProperty(value = "邮费", name="expressFee", example = "", required = false)
    private String expressFee;

    @Column(name = "customer_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "买家ID", name="customerId", example = "", required = false)
    private String customerId;

    @Column(name = "trade_status", columnDefinition = "varchar(20)" )
    @ApiModelProperty(value = "交易状态", name="orderStatus", example = "", required = false)
    private String tradeStatus;

    @Column(name = "trade_type", columnDefinition = "varchar(20)" )
    @ApiModelProperty(value = "交易类型", name="orderType", example = "", required = false)
    private String tradeType;

    @Column(name = "amount", columnDefinition = "varchar(48)" )
    @ApiModelProperty(value = "总金额", name="amount", example = "", required = false)
    private String amount;

    @Column(name = "actual_paid", columnDefinition = "varchar(48)" )
    @ApiModelProperty(value = "实付款", name="actualPaid", example = "", required = false)
    private String actualPaid;

    /*@Column(name = "sku_prop_value_group", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "商品销售属性值组合（id1;id2;id3）冗余字段", name="skuPropValueGroup", example = "", required = false)
    private String skuPropValueGroup;*/

    @Column(name = "address_group", columnDefinition = "varchar(500)" )
    @ApiModelProperty(value = "地址组合（省/市/区/街道）", name="addressGroup", example = "", required = false)
    private String addressGroup;

    @Column(name = "deleted", columnDefinition = "varchar(2)" )
    @ApiModelProperty(value = "是否被删除(逻辑删除)", name="deleted", example = "", required = false)
    private String deleted;

    @Column(name = "customer_name", columnDefinition = "varchar(20)" )
    @ApiModelProperty(value = "收货人姓名", name="customerName", example = "", required = false)
    private String customerName;

    @Column(name = "customer_tel", columnDefinition = "varchar(15)" )
    @ApiModelProperty(value = "收货人联系电话", name="customerTel", example = "", required = false)
    private String customerTel;

    @Column(name = "generate_type", columnDefinition = "varchar(2)" )
    @ApiModelProperty(value = "交易单生成类型（购物车结算、直接购买）", name="generateType", example = "", required = false)
    private String generateType;

    @Column(name = "trade_token", columnDefinition = "varchar(500)" )
    private String tradeToken;

    @Column(name = "has_parent", columnDefinition = "varchar(2)" )
    private String hasParent;

    @Column(name = "parent_num", columnDefinition = "varchar(50)" )
    private String parentNum;

    @Column(name="is_valid", columnDefinition = "varchar(10)")
    private String isValid;

}