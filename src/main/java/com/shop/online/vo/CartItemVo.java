package com.shop.online.vo;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/4 0004
 */
@NoArgsConstructor
@Getter
@Setter
public class CartItemVo {

    public CartItemVo(String cartItemId, String price, String skuGroupStr, String skuItemCount, String skuId, String productId,
                      String cartId, String imageLocation, String stock, String productName, String description) {
        this.cartItemId = cartItemId;
        this.price = price;
        this.skuGroupStr = skuGroupStr;
        this.skuItemCount = skuItemCount;
        this.skuId = skuId;
        this.productId = productId;
        this.cartId = cartId;
        this.imageLocation = imageLocation;
        this.stock = stock;
        this.productName = productName;
        this.description = description;
    }

    private String cartItemId;
    private String price;
    private String skuGroupStr;
    private String skuItemCount;
    private String skuId;
    private String productId;

    private String cartId;

    private String imageLocation;
    private String stock;

    private String productName;
    private String description;

    private String totalPrice;
}
