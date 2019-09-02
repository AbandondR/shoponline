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
 * 商品表
 * @author yjh
 * @version V1.0 创建时间：2019-03-01
 *          Copyright 2018 by Myself
 *
 */
@Data
@Table(name = "product")
@Entity
@ApiModel(value="Product对象", description="商品表")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "ID", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "产品编号", name="id", example = "", required = false)
    @Id
    private String id;

    @Column(name = "product_name", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "产品名称", name="productName", example = "", required = false)
    private String productName;

    @Column(name = "brand_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "品牌ID", name="brandId", example = "", required = false)
    private String brandId;

    @Column(name = "cata_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "分类ID", name="cataId", example = "", required = false)
    private String cataId;

    @Column(name="sale_totalNum", columnDefinition = "varchar(20)")
    @ApiModelProperty(value = "该商品总销量(所有sku销量之和)", name="saleTotalNum", example = "", required = false)
    private String saleTotalNum;

    @Column(name="total_stock", columnDefinition = "varchar(20)")
    @ApiModelProperty(value = "该商品总库存(所有sku库存之和)", name="totalStock", example = "", required = false)
    private String totalStock;

    @Column(name = "create_time", columnDefinition = "datetime" )
    @ApiModelProperty(value = "创建时间", name="createTime", example = "", required = false)
    private Date createTime;

    @Column(name = "product_num", columnDefinition = "varchar(36)" )
    @ApiModelProperty(value = "商品编号", name="productNum", example = "", required = false)
    private String productNum;

    @Column(name = "status", columnDefinition = "varchar(2)" )
    @ApiModelProperty(value = "状态（是否上架？）", name="status", example = "", required = false)
    private String status;

    @Column(name = "price_list_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "价格列表", name="priceListId", example = "", required = false)
    private String priceListId;

    @Column(name="description", columnDefinition = "varchar(500)")
    @ApiModelProperty(value = "商品描述", name="priceListId", example = "", required = false)
    private String description;

    @Column(name = "picture_snapshot", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "图片快照地址（浏览商品时的显示图片）", name="pictureSnapshot", example = "", required = false)
    private String pictureSnapshot;

}