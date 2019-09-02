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
@Table(name = "table_1")
@Entity
@ApiModel(value="Table1对象", description="")
public class Table1 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "id", columnDefinition = "varchar(50)" )
    @Id
    private String id;

    @Column(name = "product_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "商品id", name="productId", example = "", required = false)
    private String productId;

    @Column(name = "product_sku_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "商品skuid", name="productSkuId", example = "", required = false)
    private String productSkuId;

    @Column(name = "price", columnDefinition = "varchar(15)" )
    @ApiModelProperty(value = "sku价格", name="price", example = "", required = false)
    private String price;

}