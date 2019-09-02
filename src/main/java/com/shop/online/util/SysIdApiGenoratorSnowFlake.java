package com.shop.online.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 使用雪花算法，在本机生成id
 * @author peter
 * @version V1.0 创建时间：18/5/28
 *          Copyright 2018 by landray & STIC
 */

@Component
public class SysIdApiGenoratorSnowFlake {

   @Resource
   private  SnowFlake snowFlake;

    /**
     * 生成id逻辑
     * @param prefix 自定义前缀
     * @return
     * @throws Exception
     */
    private String generateProcess(String prefix) throws Exception {
        if(snowFlake == null) {
            snowFlake = new SnowFlake();
        }
        if(prefix==null){
            return String.valueOf(snowFlake.nextId());
        }else{
            return prefix.trim() + snowFlake.nextId();
        }
    }

    /**
     * 生成id-前缀+雪花算法id
     * @param prefix
     * @param batchNum
     * @return
     * @throws Exception
     */
    public String generate(String prefix, Integer batchNum) throws Exception {
        //判断是否批量
        if(batchNum==null || batchNum == 1){
            return generateProcess(prefix);
        }else{
            ArrayList<String> idList  = new ArrayList<String>();
            for (int i = 0; i < batchNum; i++) {
                String id = generateProcess(prefix);
                idList.add(id);
            }
            return StringUtils.join(idList,",");
        }
    }
    /**
     * 生成id-按开发规范提供的id：前缀（组织id,时间,业务生成id,如:院所编号+yyyyMMdd+业务因子）+ 雪花算法id；
     * @param org  院所编号
     * @param buisness 哪个模块
     * @param batchNum 批量生成id
     * @return
     * @throws Exception
     */
    public String generateByOrg(String org, String buisness, Integer batchNum)
            throws Exception {

        //参数校验
        if (StringUtils.isEmpty(org)) {
            throw new Exception("院所编号不能为空");
        }
        if (StringUtils.isEmpty(buisness)) {
            throw new Exception("业务因子不能为空");
        }
        if ( batchNum == null) {
            batchNum = 1;
        }
        if ( batchNum> 999 || batchNum < 1) {
            throw new Exception("batchNum长度在1到999位之间");
        }

        //前缀拼接
        StringBuilder prefix = new StringBuilder();
        prefix.append(org);
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String date = df.format(new Date());
        prefix.append(date);
        prefix.append(buisness);

        return  generate(prefix.toString(),batchNum);
    }

}
