package com.shop.online.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/23 0023
 */
@Data
@Table(name = "product_prop_image")
@Entity
@ApiModel(value="ProductPropImage", description="商品属性图片")
public class ProductPropImage {

    private static final long serialVersionUID = 1L;
    @Column(name = "id", columnDefinition = "varchar(50)" )
    @Id
    private String id;

    @Column(name = "prop_group", columnDefinition = "varchar(100)" )
    @ApiModelProperty(value = "属性组合", name="productId", example = "颜色：红色(不用分解，只有一组属性)", required = false)
    private String propGroup;

    @Column(name = "product_id", columnDefinition = "varchar(50)" )
    private String productId;

    @Column(name = "prop_value_id", columnDefinition = "varchar(50)" )
    private String propValueId;

    @Column(name = "img_loaction", columnDefinition = "varchar(255)" )
    private String imgLocation;

    @Column(name = "is_sku_prop", columnDefinition = "varchar(2)" )
    private String isSkuProp;

    //图片描述
    @Column(name = "description", columnDefinition = "varchar(15)" )
    private String description;

}
