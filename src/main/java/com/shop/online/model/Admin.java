package com.shop.online.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 
 * 管理员表
 * @author yjh
 * @version V1.0 创建时间：2019-03-01
 *          Copyright 2018 by Myself
 *
 */
@Data
@Table(name = "admin")
@Entity
@ApiModel(value="Admin对象", description="管理员表")
public class Admin implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "id", columnDefinition = "varchar(50)" )
    @Id
    private String id;

    @Column(name = "login_name", columnDefinition = "varchar(50)" )
    private String loginName;

    @Column(name = "password", columnDefinition = "varchar(255)" )
    private String password;

}