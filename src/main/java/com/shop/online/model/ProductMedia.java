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
 * 商品媒体展示
 * @author yjh
 * @version V1.0 创建时间：2019-03-01
 *          Copyright 2018 by Myself
 *
 */
@Data
@Table(name = "product_media")
@Entity
@ApiModel(value="ProductMedia对象", description="商品媒体展示")
public class ProductMedia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "id", columnDefinition = "varchar(50)" )
    @Id
    private String id;

    @Column(name = "product_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "商品ID", name="productId", example = "", required = false)
    private String productId;

    @Column(name = "media_location", columnDefinition = "varchar(255)" )
    private String mediaLocation;

    @Column(name = "media_type", columnDefinition = "varchar(2)" )
    private String mediaType;

}