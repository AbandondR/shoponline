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
 * 商品基本属性表
 * @author yjh
 * @version V1.0 创建时间：2019-03-01
 *          Copyright 2018 by Myself
 *
 */
@Data
@Table(name = "product_common_prop")
@Entity
@ApiModel(value="ProductCommonProp对象", description="商品基本属性表")
public class ProductCommonProp implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "id", columnDefinition = "varchar(50)" )
    @Id
    private String id;

    @Column(name = "product_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "商品ID", name="productId", example = "", required = false)
    private String productId;

    @Column(name = "prop_name_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "属性名唯一标志", name="propNameId", example = "", required = false)
    private String propNameId;

    @Column(name = "prop_value_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "属性值", name="propValueId", example = "", required = false)
    private String propValueId;

    @Column(name = "cata_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "分类", name="cataId", example = "", required = false)
    private String cataId;

    @Column(name = "status", columnDefinition = "varchar(2)" )
    private String status;

    @Column(name = "order", columnDefinition = "varchar(50)" )
    private String order;

    @Column(name = "is_sku", columnDefinition = "varchar(2)" )
    @ApiModelProperty(value = "是否是sku属性(0-不是，1-是)", name="isSku", example = "", required = false)
    private String isSku;

    //TODO 注释字段
    @Column(name = "product_sku_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "商品skuID", name="productSkuId", example = "", required = false)
    private String productSkuId;

}