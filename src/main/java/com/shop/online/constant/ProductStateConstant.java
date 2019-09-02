package com.shop.online.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/18 0018
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ProductStateConstant {

    NORMAL("A", "正常状态"),
    DELETED("D", "下架状态"),
    LACK_TEMP("E", "暂时缺货"),
    NEW_PRODUCT("G", "新商品");

    String code;
    String desc;
}
