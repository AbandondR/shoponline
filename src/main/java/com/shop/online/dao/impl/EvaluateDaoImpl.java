package com.shop.online.dao.impl;

import com.shop.online.common.PageBean;
import com.shop.online.dao.EvaluateDao;
import com.shop.online.model.Evaluate;
import com.shop.online.vo.EvaluateVo;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/23 0023
 */
@Repository
public class EvaluateDaoImpl extends BaseDaoImpl<Evaluate> implements EvaluateDao {
    @Override
    public List<EvaluateVo> findByproId(String evaluHql, String id) throws Exception {
        Query q = this.getCurrentSession().createQuery(evaluHql);
        q.setParameter("productId", id);
        return q.list();
    }

    @Override
    public List<EvaluateVo> findEvaluatesByProd(String evaluateHql, Map<String, Object> params, PageBean pageBean) throws Exception {

        Query q = this.getCurrentSession().createQuery(evaluateHql);
        if(params!=null && !params.isEmpty()) {
            for(String key : params.keySet()) {
                q.setParameter(key, params.get(key));
            }
        }
        return q.list();
    }
}
