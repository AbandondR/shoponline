package com.shop.online.service.impl;

import com.shop.online.common.PageBean;
import com.shop.online.constant.LogicDeleted;
import com.shop.online.constant.TradeEnum;
import com.shop.online.dao.EvaluateDao;
import com.shop.online.dao.OrderDao;
import com.shop.online.model.Evaluate;
import com.shop.online.model.User;
import com.shop.online.service.EvaluateService;
import com.shop.online.vo.EvaluateVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/25 0025
 */
@Service
public class EvaluateServiceImpl implements EvaluateService {

    public final int pageSize = 20;
    public static final String PATH = "D:\\idea_data\\shoponline\\img\\comment\\";

    @Resource
    private EvaluateDao evaluateDao;

    @Resource
    private OrderDao orderDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageBean findEvaluatesByProd(String productId, int page) throws Exception {
        String evaluateHql = "select new com.shop.online.vo.EvaluateVo(e.id, e.content, e.productId, e.createTime, e.evaluateLevel, e.nickName, e.hasPicture, e.pictureLocation, o.skuGroup)" +
                " from Evaluate e left join Order o on e.orderId=o.id and e.productId=:productId and e.isDeleted=:isDeleted";
        Map<String, Object> params = new HashMap<>();
        params.put("productId", productId);
        params.put("isDeleted", LogicDeleted.NOT_DELETED);

        PageBean pageBean = new PageBean();
        pageBean.setCurrentPage(page);
        pageBean.setPageSize(pageSize);
        List<EvaluateVo> evaluateVoList = evaluateDao.findEvaluatesByProd(evaluateHql, params, pageBean);
        pageBean.setResultList(evaluateVoList);
        Integer totalCount = evaluateDao.count(evaluateHql, params);
        pageBean.setTotalCount(totalCount !=null ? totalCount.intValue() : 0);
        return pageBean;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveComment(Evaluate evaluate, User user, MultipartFile[] pictures) throws Exception {
        evaluate.setCustomerId(user.getId());
        evaluate.setNickName(user.getNickName());
        evaluate.setIsDeleted(LogicDeleted.NOT_DELETED);
        evaluate.setHasPicture("0");

        StringBuilder builder = new StringBuilder();
        if(pictures!=null) {
            for (MultipartFile multipartFile : pictures) {

                String originalName = multipartFile.getOriginalFilename();
                String type = originalName.substring(originalName.lastIndexOf("."));
                String newFileName = originalName.substring(0, originalName.lastIndexOf(".")) + "_" + UUID.randomUUID().toString()+ type;
                builder.append(newFileName);
                builder.append("@");
                File targetFile = new File(PATH, newFileName);
                if (!targetFile.exists()) {
                    targetFile.mkdirs();
                }
                multipartFile.transferTo(targetFile);
            }
            evaluate.setHasPicture("1");
        }
        String filePaths = builder.toString();
        filePaths = filePaths.substring(0, filePaths.length()-1);
        evaluate.setPictureLocation(filePaths);
        evaluate.setCreateTime(new Date());
        evaluate.setId(UUID.randomUUID().toString());
        evaluateDao.save(evaluate);
        String hql_1 = "update Order o set o.orderStatus=:orderStatus where o.orderNum=:orderNum and o.customerId=:userId";
        Map<String, String> params = new HashMap<>();
        params.put("orderStatus", TradeEnum.FINISHED.getCode());
        params.put("orderNum", evaluate.getOrderId());
        params.put("userId", user.getId());
        orderDao.updateOrderStatusById(hql_1, params);
    }
}
