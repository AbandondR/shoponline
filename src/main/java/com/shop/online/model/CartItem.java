package com.shop.online.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name = "cart_product")
@Entity
@ApiModel(value="CartProduct对象", description="")
public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "id", columnDefinition = "varchar(50)" )
    @Id
    private String id;

    @Column(name = "cart_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "购物车id", name="cartId", example = "", required = false)
    private String cartId;

    @Column(name = "sku_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "商品skuID", name="skuId", example = "", required = false)
    private String skuId;

    @Column(name = "product_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "商品ID", name="productId", example = "", required = false)
    private String productId;

    @Column(name = "sku_item_count", columnDefinition = "varchar(10)" )
    @ApiModelProperty(value = "商品sku数量", name="skuItemCount", example = "", required = false)
    private String skuItemCount;

    @Column(name = "sku_group_str", columnDefinition = "varchar(1000)" )
    @ApiModelProperty(value = "商品sku组合str", name="skuName", example = "", required = false)
    private String skuGroupStr;

    @Column(name = "sku_prop_group", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "销售属性组合（prop_name:prop_value;...）", name="skuPropGroup", example = "", required = false)
    private String skuPropGroup;

    @Column(name = "price", columnDefinition = "varchar(20)" )
    @ApiModelProperty(value = "商品sku价格", name="price", example = "", required = false)
    private String price;

    @Column(name = "status", columnDefinition = "varchar(5)" )
    @ApiModelProperty(value = "添加到购物车后的商品状态（如：待下单，待结算。。）", name="status", example = "", required = false)
    private String status;

    @Column(name = "orders", columnDefinition = "varchar(20)" )
    private String order;

}