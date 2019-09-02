package com.shop.online.dao.impl;

import com.shop.online.dao.PropNameDao;
import com.shop.online.model.PropName;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/22 0022
 */
@Repository
public class PropNameDaoImpl extends BaseDaoImpl<PropName> implements PropNameDao {

    @Override
    public List<PropName> findSearchPropByCata(String hql, String cataId) {
        Query q = this.getCurrentSession().createQuery(hql);
        q.setParameter("cataId", cataId);
        return q.list();
    }

    @Override
    public List<PropName> findPropNameByIds(String hql, List<String> propNameIds) throws Exception {
        Query q = this.getCurrentSession().createQuery(hql);
        q.setParameterList("ids", propNameIds);
        return q.list();
    }


}
