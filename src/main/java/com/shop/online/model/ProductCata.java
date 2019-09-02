package com.shop.online.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 
 * 产品分类
 * @author yjh
 * @version V1.0 创建时间：2019-03-01
 *          Copyright 2018 by Myself
 *
 */
@Data
@Table(name = "product_cata")
@Entity
@ApiModel(value="ProductCata对象", description="产品分类")
public class ProductCata implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "id", columnDefinition = "varchar(50)" )
    @Id
    private String id;

    @Column(name = "cata_name", columnDefinition = "varchar(500)" )
    @ApiModelProperty(value = "分类名称", name="cataName", example = "", required = false)
    private String cataName;

    @Column(name = "isParent", columnDefinition = "bit(2)" )
    @ApiModelProperty(value = "是否为父分类 0-否，1-是", name="isParent", example = "", required = false)
    
    private Boolean isParent;

    @Column(name = "parent_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "父类别", name="parentNum", example = "", required = false)
    private String parentId;

    @Column(name = "status", columnDefinition = "varchar(1)")
    private String status;

    @Transient
    private List<ProductCata> childrens;

}