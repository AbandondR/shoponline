package com.shop.online.vo;

import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/23 0023
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateVo {

    private String id;
    private String content;
    private String productId;
    private Date createTime;
    private String evaluateLevel;
    private String nickName;
    private String hasPicture;
    private String pictureLocations;

    private String skuGroup;

}
