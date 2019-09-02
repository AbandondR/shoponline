package com.shop.online.vo;

import com.shop.online.model.PropName;
import lombok.Data;

import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/5/10 0010
 */
@Data
public class PropVo {
    private List<PropName> salePropList;
    private List<PropName> otherPropList;
}
