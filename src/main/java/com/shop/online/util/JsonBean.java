package com.shop.online.util;

import lombok.Data;

import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/1 0001
 */
@Data
public class JsonBean {

    private boolean multi;
    private boolean must;
    private String name;
    private long pid;
    private boolean is_color_prop;
    private boolean is_key_prop;
    private boolean is_sale_prop;
}
@Data
class Values{
    private String name;
    private long vid;
    private long pid;
    private String prop_name;
}
@Data
class Wrapper {
    List<Values> prop_value;
}
