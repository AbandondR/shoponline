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
 * 产品sku：销售产品的最小单元
 * @author yjh
 * @version V1.0 创建时间：2019-03-01
 *          Copyright 2018 by Myself
 *
 */
@Data
@Table(name = "product_sku")
@Entity
@ApiModel(value="ProductSku对象", description="产品sku：销售产品的最小单元")
public class ProductSku implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "id", columnDefinition = "varchar(50)" )
    @Id
    private String id;

    @Column(name = "product_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "商品ID", name="productId", example = "", required = false)
    private String productId;

    @Column(name = "brand_code", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "品牌编码", name="brandCode", example = "", required = false)
    private String brandCode;

    @Column(name = "sku_product_code", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "sku商品码（自定义码）", name="skuProductCode", example = "", required = false)
    private String skuProductCode;

    @Column(name = "product_code", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "商品码", name="productCode", example = "", required = false)
    private String productCode;

    @Column(name = "sku_group", columnDefinition = "varchar(1000)" )
    @ApiModelProperty(value = "sku组合", name="skuName", example = "（propNameId:propValueId;propNameId2:propValueID2）", required = false)
    private String skuGorup;

    @Column(name = "price", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "价格", name="price", example = "", required = false)
    private String price;

    @Column(name = "stock", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "库存", name="stock", example = "", required = false)
    private String stock;

    @Column(name = "weight", columnDefinition = "varchar(20)" )
    @ApiModelProperty(value = "重量", name="weight", example = "", required = false)
    private String weight;

    @Column(name = "image_location", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "商品sku图片（一个sku只有一张图片）", name="imageLocation", example = "", required = false)
    private String imageLocation;

    @Column(name = "status", columnDefinition = "varchar(2)" )
    private String status;

    @Column(name = "orders", columnDefinition = "varchar(50)" )
    private String order;

    @Column(name = "description", columnDefinition = "varchar(500)" )
    @ApiModelProperty(value = "商品sku描述", name="description", example = "", required = false)
    private String description;

    @Column(name = "sale_num", columnDefinition = "varchar(20)")
    @ApiModelProperty(value = "商品销量", name="description", example = "", required = false)
    private String saleNum;


}