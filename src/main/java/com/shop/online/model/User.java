package com.shop.online.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 用户信息表
 * @author yjh
 * @version V1.0 创建时间：2019-03-01
 *          Copyright 2018 by Myself
 *
 */
@Data
@Table(name = "customer")
@Entity
@ApiModel(value="Customer对象", description="用户信息表")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "id", columnDefinition = "varchar(50)" )
    @Id
    @GeneratedValue(generator = "customerId")
    @GenericGenerator(name="customerId", strategy = "com.shop.online.util.MyCustomIdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name="prefix", value="customer"),
                    @org.hibernate.annotations.Parameter(name="batchNum", value="1")})
    private String id;

    @Column(name = "nick_name", columnDefinition = "varchar(48)" )
    @ApiModelProperty(value = "昵称", name="nickName", example = "", required = false)
    private String nickName;

    @Column(name = "gender", columnDefinition = "varchar(1)" )
    @ApiModelProperty(value = "性别（0-女，1-男）", name="nickName", example = "", required = false)
    private String gender;

    @Column(name = "birthday", columnDefinition = "date" )
    @ApiModelProperty(value = "出生日期", name="birthday", example = "", required = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @Column(name = "tele_phone", columnDefinition = "varchar(20)" )
    @ApiModelProperty(value = "注册电话", name="birthday", example = "", required = false)
    private String telePhone;

    @Column(name = "e_mail", columnDefinition = "varchar(20)" )
    @ApiModelProperty(value = "邮箱", name="eMail", example = "", required = false)
    private String eMail;

    @Column(name = "is_active_email", columnDefinition = "varchar(20)" )
    @ApiModelProperty(value = "邮箱激活", name="isActiveEmail", example = "", required = false)
    private String isActiveEmail;

    @Column(name = "active_code", columnDefinition = "varchar(20)" )
    @ApiModelProperty(value = "邮箱激活码", name="activeCode", example = "", required = false)
    private String activeCode;

    @Column(name = "real_name", columnDefinition = "varchar(20)" )
    @ApiModelProperty(value = "真实姓名", name="realName", example = "", required = false)
    private String realName;

    @Column(name = "family_address", columnDefinition = "varchar(500)" )
    @ApiModelProperty(value = "居住地址", name="familyAddress", example = "", required = false)
    private String familyAddress;

    @Column(name = "head_picture", columnDefinition = "varchar(255)" )
    @ApiModelProperty(value = "头像", name="headPicture", example = "", required = false)
    private String headPicture;

    @Column(name = "password", columnDefinition = "varchar(255)" )
    private String password;

    @Column(name = "registry_time", columnDefinition = "datetime" )
    @ApiModelProperty(value = "注册时间", name="registryTime", example = "", required = false)
    private Date registryTime;

    @Column(name = "last_modify", columnDefinition = "datetime" )
    @ApiModelProperty(value = "最后一次修改时间", name="lastModify", example = "", required = false)
    private Date lastModify;

    @Column(name="status", columnDefinition = "varchar(2)")
    @ApiModelProperty(value = "状态（0-禁用，1-启用)", name="status", example = "", required = false)
    private String status;

}