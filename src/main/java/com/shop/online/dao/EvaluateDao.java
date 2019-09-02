package com.shop.online.dao;

import com.shop.online.common.PageBean;
import com.shop.online.model.Evaluate;
import com.shop.online.vo.EvaluateVo;

import java.util.List;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/23 0023
 */
public interface EvaluateDao extends BaseDao<Evaluate>{
    List<EvaluateVo> findByproId(String evaluHql, String id) throws Exception;

    List<EvaluateVo> findEvaluatesByProd(String evaluateHql, Map<String,Object> params, PageBean pageBean) throws Exception;
}
