package com.shop.online.vo;

import com.shop.online.model.Address;
import com.shop.online.model.CartItem;
import lombok.Data;

import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/6 0006
 */
@Data
public class OrderConfirmVo {
    private List<Address> addressList;

    private List<CartItemVo> cartItemVos;

    private String totalPrice;

    private String totalNum;
}
