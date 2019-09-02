package com.shop.online.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 
 * 属性名对应属性可选值
 * @author yjh
 * @version V1.0 创建时间：2019-03-01
 *          Copyright 2018 by Myself
 *
 */
@Table(name = "prop_value")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ApiModel(value="PropValue对象", description="属性名对应属性可选值")
public class PropValue implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", columnDefinition = "varchar(50)" )
    private String id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="prop_nameId")
    private PropName propName;

    /*@Column(name = "prop_nameId", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "属性名ID", name="propNameNum", example = "", required = false)
    private String propNameId;*/

    @Column(name = "prop_value", columnDefinition = "varchar(100)" )
    @ApiModelProperty(value = "属性值", name="propValue", example = "", required = false)
    private String propValue;

    @Column(name = "cata_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "所属分类", name="cataId", example = "", required = false)
    private String cataId;

    @Column(name = "status", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "状态", name="status", example = "", required = false)
    private String status;

    @Column(name = "order", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "排序号", name="order", example = "", required = false)
    private String order;

    @Transient
    private String imgLocation;


}