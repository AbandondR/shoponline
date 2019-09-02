package com.shop.online.dao;

import com.shop.online.model.PropName;

import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/22 0022
 */
public interface PropNameDao extends BaseDao<PropName>{

    public List<PropName> findSearchPropByCata(String hql, String cataId) throws Exception;

    List<PropName> findPropNameByIds(String hql, List<String> propNameIds) throws Exception;
}
