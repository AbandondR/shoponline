package com.shop.online.vo;

import com.shop.online.model.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/22 0022
 */
@Getter
@Setter
@NoArgsConstructor
public class ProductDetailsVo {

    @ApiModelProperty(value = "产品编号", name="id", example = "", required = false)
    private String id;

    @ApiModelProperty(value = "产品名称", name="productName", example = "", required = false)
    private String productName;

    @ApiModelProperty(value = "分类ID", name="cataId", example = "", required = false)
    private String cataId;

    @ApiModelProperty(value = "该商品总销量(所有sku销量之和)", name="saleTotalNum", example = "", required = false)
    private String saleTotalNum;

    private String totalStock;

    @ApiModelProperty(value = "商品编号", name="productNum", example = "", required = false)
    private String productNum;

    @ApiModelProperty(value = "价格列表", name="priceListId", example = "", required = false)
    private String priceListId;

    @ApiModelProperty(value = "商品描述", name="priceListId", example = "", required = false)
    private String description;

    public ProductDetailsVo(String id, String productName, String cataId, String saleTotalNum, String totalStock, String productNum, String priceListId, String description) {
        this.id = id;
        this.productName = productName;
        this.cataId = cataId;
        this.saleTotalNum = saleTotalNum;
        this.productNum = productNum;
        this.priceListId = priceListId;
        this.description = description;
    }
    //商品详情
    private List<PropName> propNameList;
    //商品评论
    private List<EvaluateVo> evaluateVoList;

    //商品图片展示
    private List<ProductMedia> productMediaList;

    //private List<ProductPropImage> productPropImageList;
}
