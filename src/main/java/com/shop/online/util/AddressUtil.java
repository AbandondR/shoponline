package com.shop.online.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/9 0009
 */
public class AddressUtil {

    public static final String SEPARATOR = "#";
    public static String combineAddress(String province, String city, String area, String street,String detailAddress) {
        StringBuilder sb = new StringBuilder();
        sb.append(province).append(SEPARATOR);
        sb.append(city).append(SEPARATOR);
        sb.append(area).append(SEPARATOR);
        sb.append(street).append(SEPARATOR);
        sb.append(detailAddress);
        return sb.toString();
    }

    public static Map<String, String> parseAddress(String combineAddress) {
        String[] arrs = combineAddress.split(SEPARATOR);
        Map<String, String> map = new HashMap<>(8);
        map.put("province", arrs[0]);
        map.put("city", arrs[1]);
        map.put("area", arrs[2]);
        map.put("street", arrs[3]);
        map.put("addressDetail", arrs[4]);
        return map;
    }

    public static String combineAddress(Map<String, Object> map) {
        String province = (String) map.get("province");
        String city = (String)map.get("city");
        String area = (String)map.get("area");
        String street = (String)map.get("street");
        String detailAddress = (String)map.get("addressDetail");
        StringBuilder sb = new StringBuilder();
        sb.append(province).append(SEPARATOR);
        sb.append(city).append(SEPARATOR);
        sb.append(area).append(SEPARATOR);
        sb.append(street).append(SEPARATOR);
        sb.append(detailAddress);
        return sb.toString();
    }
}
