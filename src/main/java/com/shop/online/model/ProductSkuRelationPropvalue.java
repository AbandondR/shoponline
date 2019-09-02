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
 * 商品sku与sku属性值关联表
 * @author yjh
 * @version V1.0 创建时间：2019-03-01
 *          Copyright 2018 by Myself
 *
 */
@Data
@Table(name = "product_sku_relation_propvalue")
@Entity
@ApiModel(value="ProductSkuRelationPropvalue对象", description="商品sku与sku属性值关联表")
public class ProductSkuRelationPropvalue implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "id", columnDefinition = "varchar(50)" )
    @Id
    private String id;

    @Column(name = "prod_sku_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "sku产品id", name="prodSkuId", example = "", required = false)
    private String prodSkuId;

    @Column(name = "prop_name", columnDefinition = "varchar(36)" )
    @ApiModelProperty(value = "属性名", name="propName", example = "", required = false)
    private String propName;

    @Column(name = "prop_value", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "属性值", name="propValue", example = "", required = false)
    private String propValue;

    @Column(name = "order", columnDefinition = "varchar(255)" )
    private String order;

    @Column(name = "status", columnDefinition = "varchar(255)" )
    private String status;

}