package com.shop.online.vo;

import com.shop.online.model.ProductSku;
import com.shop.online.model.PropName;
import lombok.Data;

import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/25 0025
 */
@Data
public class ProdSkuGroupVo {

    List<PropName> propNameList;
    List<ProductSku> productSkuList;
}
