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
 * 品牌表
 * @author yjh
 * @version V1.0 创建时间：2019-03-01
 *          Copyright 2018 by Myself
 *
 */
@Data
@Table(name = "brand")
@Entity
@ApiModel(value="Brand对象", description="品牌表")
public class Brand implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "ID", columnDefinition = "varchar(50)" )
    @Id
    private String id;

    @Column(name = "cata_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "分类ID", name="cataId", example = "", required = false)
    private String cataId;

    @Column(name = "chinese_name", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "品牌前端显示名称", name="chineseName", example = "", required = false)
    private String chineseName;

    @Column(name = "eng_name", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "品牌后端处理名称", name="engName", example = "", required = false)
    private String engName;

    @Column(name = "description", columnDefinition = "varchar(1000)" )
    @ApiModelProperty(value = "品牌描述", name="description", example = "", required = false)
    private String description;

    @Column(name = "logo", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "品牌logo(地址)", name="logo", example = "", required = false)
    private String logo;

    @Column(name = "offical_address", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "品牌官方地址", name="officalAddress", example = "", required = false)
    private String officalAddress;

    @Column(name = "story", columnDefinition = "varchar(5000)" )
    @ApiModelProperty(value = "品牌故事", name="story", example = "", required = false)
    private String story;

    @Column(name = "create_time", columnDefinition = "datetime" )
    @ApiModelProperty(value = "品牌创建时间", name="createTime", example = "", required = false)
    private Date createTime;

}