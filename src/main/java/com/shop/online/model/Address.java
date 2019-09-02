package com.shop.online.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 
 * 地址表
 * @author yjh
 * @version V1.0 创建时间：2019-03-01
 *          Copyright 2018 by Myself
 *
 */
@Data
@Table(name = "address")
@Entity
@ApiModel(value="Address对象", description="地址表")
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "id", columnDefinition = "varchar(50)" )
    @Id
    @GeneratedValue(generator = "customerId")
    @GenericGenerator(name="customerId", strategy = "com.shop.online.util.MyCustomIdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name="prefix", value="addr"),
                    @org.hibernate.annotations.Parameter(name="batchNum", value="1")})
    private String id;

    @Column(name = "customer_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "用户", name="customerId", example = "", required = false)
    private String customerId;

    @Column(name = "user_name", columnDefinition = "varchar(20)" )
    @ApiModelProperty(value = "取货用户的姓名", name="customerId", example = "", required = false)
    private String userName;

    @Column(name = "province", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "省", name="provice", example = "", required = false)
    private String province;

    @Column(name = "city", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "市", name="city", example = "", required = false)
    private String city;

    @Column(name = "area", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "区", name="area", example = "", required = false)
    private String area;

    @Column(name = "street", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "街道", name="street", example = "", required = false)
    private String street;

    @Column(name = "address_detail", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "详细地址", name="addressDetail", example = "", required = false)
    private String addressDetail;

    @Column(name = "postal_code", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "邮政编码", name="postalCode", example = "", required = false)
    private String postalCode;

    @Column(name = "is_default", columnDefinition = "varchar(2)" )
    @ApiModelProperty(value = "是否为默认地址", name="isDefault", example = "", required = false)
    private String isDefault;

    @Column(name = "telephone", columnDefinition = "varchar(15)" )
    @ApiModelProperty(value = "联系电话", name="telephone", example = "", required = false)
    private String telephone;

}