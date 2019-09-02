package com.shop.online.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/27 0027
 */
@Table(name = "country_info")
@Entity
@Data
public class CountryInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "country_info_id")
    @GenericGenerator(name="country_info_id", strategy = "com.shop.online.util.MyCustomIdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name="prefix", value="cart"),
                    @org.hibernate.annotations.Parameter(name="batchNum", value="1")})
    private String id;

    @Column(name = "code", columnDefinition = "varchar(11)" )
    private String code;

    @Column(name = "parent_id", columnDefinition = "varchar(11)" )
    private String parentId;

    @Column(name = "name", columnDefinition = "varchar(50)" )
    private String name;

    @Column(name = "level", columnDefinition = "tinyint(1)" )
    private int level;

}
