package com.shop.online.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * 商品属性名
 * @author yjh
 * @version V1.0 创建时间：2019-03-01
 *          Copyright 2018 by Myself
 *
 */
@Data
@Table(name = "prop_name")
@Entity
@ApiModel(value="PropName对象", description="商品属性名")
public class PropName implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "id", columnDefinition = "varchar(50)" )
    @Id
    private String id;


    @OneToMany(mappedBy = "propName", fetch=FetchType.EAGER)
    private List<PropValue> propValueList;

    @Column(name = "cata_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "所属分类", name="cataId", example = "", required = false)
    private String cataId;

    @Column(name = "prop_name_num", columnDefinition = "varchar(36)" )
    @ApiModelProperty(value = "属性名唯一标志", name="propNameNum", example = "", required = false)
    private String propNameNum;

    @Column(name = "prop_name", columnDefinition = "varchar(100)" )
    @ApiModelProperty(value = "属性名", name="propName", example = "", required = false)
    private String propName;

    @Column(name = "is_color_prop", columnDefinition = "varchar(2)" )
    @ApiModelProperty(value = "是否是颜色属性（1：是， 0：不是）", name="isColorProp", example = "", required = false)
    private String isColorProp;

    @Column(name = "is_multi_choice", columnDefinition = "varchar(2)" )
    @ApiModelProperty(value = "是否多选", name="isMultiChoice", example = "", required = false)
    private String isMultiChoice;

    @Column(name = "is_sale_prop", columnDefinition = "varchar(2)" )
    @ApiModelProperty(value = "是否销售属性", name="isSaleProp", example = "", required = false)
    private String isSaleProp;

    @Column(name = "is_primitive_prop", columnDefinition = "varchar(2)" )
    @ApiModelProperty(value = "是否关键属性", name="isPrimitiveProp", example = "", required = false)
    private String isPrimitiveProp;

    @Column(name = "is_must", columnDefinition = "varchar(2)" )
    @ApiModelProperty(value = "是否必须", name="isMust", example = "", required = false)
    private String isMust;

    @Column(name = "is_input_prop", columnDefinition = "varchar(2)" )
    @ApiModelProperty(value = "是否输入属性", name="isInputProp", example = "", required = false)
    private String isInputProp;

    @Column(name = "is_search_prop", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "是否是搜索属性", name="isSearchProp", example = "", required = false)
    private String isSearchProp;

    @Column(name = "is_need_image", columnDefinition = "varchar(2)" )
    @ApiModelProperty(value = "该属性是否需要上传图片", name="isNeedImage", example = "", required = false)
    private String isNeedImage;

    @Column(name = "create_time", columnDefinition = "datetime" )
    @ApiModelProperty(value = "创建时间", name="createTime", example = "", required = false)
    private Date createTime;

    @Column(name = "order", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "排序号", name="order", example = "", required = false)
    private String order;

    @Column(name = "status", columnDefinition = "varchar(2)" )
    private String status;



}