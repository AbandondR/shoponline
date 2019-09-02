package com.shop.online.service.impl;

import com.shop.online.dao.CountryInfoDao;
import com.shop.online.model.CountryInfo;
import com.shop.online.service.CountryInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/27 0027
 */
@Service
public class CountryInfoServiceImpl implements CountryInfoService {

    @Resource
    private CountryInfoDao countryInfoDao;

    /**
     * 根据区查询街道
     * @param areaCode
     * @return
     */
    @Override
    public List<CountryInfo> getStreet(String areaCode) {
        String hql = "from CountryInfo c where c.parentId = :areaCode";
        List<CountryInfo> streets = countryInfoDao.findStreet(hql, areaCode);
        return streets;
    }
}
