package com.shop.online.util;

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/1 0001
 */
public class JsonToSQL {

    public static void main(String[] args)throws Exception {
        File jsonFile = new File("C:\\Users\\Administrator\\Desktop\\prop_value.json");
        /*try {

            List<String> a = new ArrayList<>();
            List<String> b = new ArrayList<>();
            List<String> c = new ArrayList<>();
            a.add("A");
            a.add("B");
            a.add("C");
            a.add("D");
            b.add("1");
            b.add("2");
            c.add("@");
            c.add("#");
            c.add("$");

            Map<String, List<String>> data = new HashMap<>();
            data.put("001", a);
            data.put("003", b);
            data.put("002", c);
            Set<String> keys = data.keySet();
            String[] strs = keys.toArray(new String[0]);

            List<String> result = dicard(data, strs);
            System.out.println(result);*/
            InputStream stream = new FileInputStream(jsonFile);
             byte[] b = new byte[stream.available() + 1];
            stream.read(b);
            String jsonStr = new String(b, "utf-8");
            List<JsonBean> beans = JSON.parseArray(jsonStr, JsonBean.class);
            List<Values> valueBeans = JSON.parseArray(jsonStr, Values.class);
            HandlePropValueBean(valueBeans, "prop_value.sql");
            getValues(beans);
            HandlePropNameBean(beans, "prop_json.sql");
           // commonProp("common_prop.sql");
        }

    /*public static void main(String[] args) {
        try {
            combineProp("pro_sku.sql");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    public static void HandlePropValueBean(List<Values> valuesList, String sqlFilePath) throws Exception {
        File file = new File(sqlFilePath);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        OutputStream stream = new FileOutputStream(file);
        String sql = "INSERT INTO prop_value(id, prop_nameId, prop_value) values ";
        //过滤品牌
        List<Values> newList = valuesList.stream().filter(e -> e.getPid() != 20000).collect(Collectors.toList());
        for (Values values : newList) {
            StringBuilder builder = new StringBuilder();
            builder.append("(");
            builder.append("\"" + values.getVid() + "\",");
            builder.append("\"" + values.getPid() + "\",");
            builder.append("\"" + values.getName() + "\"");
            builder.append("),");
            sql += builder.toString();
        }
        stream.write(sql.getBytes());
        stream.flush();
        stream.close();
    }

    public static void HandlePropNameBean(List<JsonBean> jsonBeanList, String sqlFilePath) throws Exception {

        File file = new File(sqlFilePath);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        OutputStream stream = new FileOutputStream(file);
        String sql = "INSERT INTO prop_name (id,prop_name,is_color_prop,is_multi_choice,is_sale_prop,is_primitive_prop,is_must) VALUES";
        for (JsonBean jsonBean : jsonBeanList) {
            StringBuilder builder = new StringBuilder();
            int c = jsonBean.is_color_prop() ? 1 : 0;
            int must = jsonBean.isMust() ? 1 : 0;
            int mutli = jsonBean.isMulti() ? 1 : 0;
            int sale = jsonBean.is_sale_prop() ? 1 : 0;
            int key = jsonBean.is_key_prop() ? 1 : 0;
            builder.append("(");
            builder.append("'" + jsonBean.getPid() + "',");
            builder.append("'" + jsonBean.getName() + "',");
            builder.append("'" + String.valueOf(c) + "',");
            builder.append("'" + String.valueOf(mutli) + "',");
            builder.append("'" + String.valueOf(sale) + "',");
            builder.append("'" + String.valueOf(key) + "',");
            builder.append("'" + String.valueOf(must) + "'");
            builder.append(")");
            builder.append(",");
            sql += builder.toString();
        }
        stream.write(sql.getBytes());
        stream.flush();
        stream.close();
    }

    public static void getValues(List<JsonBean> jsonBeanList) {
        List<Long> ids = jsonBeanList.stream().map(e -> e.getPid()).collect(Collectors.toList());
        String idsStr = ids.stream().map(e -> String.valueOf(e)).collect(Collectors.joining(";"));
        System.out.println(idsStr);
    }

    public static void commonProp(String sqlFilePath) throws Exception {
        String str = "20000:3236884;20518:28314,20518:28316,20518:9876263,20518:13051253,20518:3217383,20518:9446190,20518:3271761,20518:3271773,20518:10504551,20518:12597254,20518:222044050,20518:3217389,20518:3217388,20518:222318595;1627207:28321,1627207:20412615,1627207:30155,1627207:28337,1627207:28866,1627207:3594022,1627207:130166,1627207:3232480,1627207:3743025,1627207:5167321,1627207:6588790,1627207:132476,1627207:28324,1627207:60092,1627207:3224419;42722636:248572013;122216515:4068154;122216586:4043538;122276111:24484806;20021:28352;6209522:121783509;20551:20592878;20603:3346764;20677:65908348;42860829:20307554;122216345:29458;122216507:3226292;122216588:3267902;122216589:102510;122216608:42007;122276380:248582450;122276347:192580268;122276315:29519436;42718685:57159575;36228475:174934690;8560225:112030;1627863:28355;";
        String[] strings = str.split(";|,");
        String sql = "insert into product_common_prop( id,prop_name_id, prop_value_id,is_sku) values ";
        int index = 1;
        String[] commponet = {"20518", "1627207"};
        List<String> stringList = Arrays.asList(commponet);
        for (String string : strings) {

            String[] s = string.split(":");
            StringBuilder builder = new StringBuilder();
            builder.append("(");
            builder.append("'" + index + "',");
            builder.append("'" + s[0] + "',");
            builder.append("'" + s[1] + "',");
            if (stringList.contains(s[0])) {
                builder.append("'" + 1 + "'");
            } else {
                builder.append("'" + 0 + "'");
            }
            builder.append("),");
            sql += builder.toString();
            index++;
        }
        sql = sql.substring(0, sql.length() - 1);
        sql += ";";
        File file = new File(sqlFilePath);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        OutputStream stream = new FileOutputStream(file);
        stream.write(sql.getBytes());
        stream.flush();
        stream.close();
    }

    /**
     * sale_prop组合
     */
    public static void combineProp(String sqlFilePath) throws Exception{
        String data = "20000:3236884;20518:28314,20518:28316,20518:9876263,20518:13051253,20518:3217383,20518:9446190,20518:3271761,20518:3271773,20518:10504551,20518:12597254,20518:222044050,20518:3217389,20518:3217388,20518:222318595;1627207:28321,1627207:20412615,1627207:30155,1627207:28337,1627207:28866,1627207:3594022,1627207:130166,1627207:3232480,1627207:3743025,1627207:5167321,1627207:6588790,1627207:132476,1627207:28324,1627207:60092,1627207:3224419;42722636:248572013;122216515:4068154;122216586:4043538;122276111:24484806;20021:28352;6209522:121783509;20551:20592878;20603:3346764;20677:65908348;42860829:20307554;122216345:29458;122216507:3226292;122216588:3267902;122216589:102510;122216608:42007;122276380:248582450;122276347:192580268;122276315:29519436;42718685:57159575;36228475:174934690;8560225:112030;1627863:28355;";
        List<String> dataList = Arrays.asList(data.split(";|,"));
        //销售属性
        List<String> propValue = dataList.stream().filter(e -> e.split(":")[0].contains("20518") || e.split(":")[0].contains("1627207")).collect(Collectors.toList());
        //属性与属性值得归类
        Map<String, List<String>> propGroup = propValue.stream().collect(Collectors.groupingBy(e ->
                        e.split(":")[0]
                , Collectors.mapping(e -> e.split(":")[1], Collectors.toList())));
        String[] propNames = propGroup.keySet().toArray(new String[0]);
        List<String> results = dicard(propGroup, propNames);
        int index = 0;
        Random random = new Random();
        String sql = "insert into product_sku(id,sku_group,price, weight) values ";
        for(String str : results) {
            ++index;
            StringBuilder builder = new StringBuilder();
            builder.append("(");
            builder.append("'" +index +"',");
            builder.append("'"+str+"',");
            builder.append("'" + (random.nextInt(5) + 70) + "',");
            builder.append("'" + (random.nextFloat()+1)+"kg'");
            builder.append("),");
            sql += builder.toString();
        }
        sql = sql.substring(0, sql.length() - 1);
        sql += ";";
        File file = new File(sqlFilePath);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        OutputStream stream = new FileOutputStream(file);
        stream.write(sql.getBytes());
        stream.flush();
        stream.close();

    }

    public static List<String> dicard(Map<String, List<String>> propGroup, String[] propNames) {
        String[] array = Arrays.copyOf(propNames, propNames.length);
        List<String> result = new ArrayList<>();
        if (propNames.length >= 2) {
            String name1 = array[0];
            String name2 = array[1];
            List<String> propValue1 = propGroup.get(name1);
            List<String> propValue2 = propGroup.get(name2);
            for (int i = 0; i < propValue1.size(); i++) {
                for (int j = 0; j < propValue2.size(); j++) {
                    String temp;
                    if(propValue1.get(i).contains(":")) {
                        temp = propValue1.get(i) + "," + name2 + ":" + propValue2.get(j);
                    }
                    else {
                        temp = name1 + ":" + propValue1.get(i) + "," + name2 + ":" + propValue2.get(j);
                    }
                    result.add(temp);
                }
            }
            propGroup.put(name2, result);
            String[] arr = Arrays.copyOfRange(array, 1, array.length);
            return dicard(propGroup, arr);
        } else {
            return propGroup.get(propNames[propNames.length-1]);
        }
    }
}
