package com.shop.online.service;

import com.shop.online.common.PageBean;
import com.shop.online.model.Evaluate;
import com.shop.online.model.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/25 0025
 */
public interface EvaluateService {
    PageBean findEvaluatesByProd(String productId, int page) throws Exception;

    void saveComment(Evaluate evaluate, User user, MultipartFile[] pictures) throws Exception;
}
