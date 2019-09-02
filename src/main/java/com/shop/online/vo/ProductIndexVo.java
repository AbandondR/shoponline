package com.shop.online.vo;

import com.shop.online.model.Product;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Date;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/18 0018
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductIndexVo {

    private String id;


    private String productName;

    @Column(name = "brand_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "品牌ID", name="brandId", example = "", required = false)
    private String brandId;

    @Column(name = "create_time", columnDefinition = "datetime" )
    @ApiModelProperty(value = "创建时间", name="createTime", example = "", required = false)
    private Date createTime;

    @Column(name = "product_num", columnDefinition = "varchar(36)" )
    @ApiModelProperty(value = "商品编号", name="productNum", example = "", required = false)
    private String productNum;

    @Column(name = "status", columnDefinition = "varchar(2)" )
    @ApiModelProperty(value = "状态（是否上架？）", name="status", example = "", required = false)
    private String status;

    @Column(name = "price_list_id", columnDefinition = "varchar(50)" )
    @ApiModelProperty(value = "价格列表", name="priceListId", example = "", required = false)
    private String priceListId;


    @Column(name="description", columnDefinition = "varchar(500)")
    @ApiModelProperty(value = "商品描述", name="priceListId", example = "", required = false)
    private String desc;
    private String pictureSnapshot;
    private String saleTotalNum;
}
