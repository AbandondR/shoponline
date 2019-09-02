package com.shop.online.web.rest;

import com.shop.online.model.Evaluate;
import com.shop.online.model.User;
import com.shop.online.service.EvaluateService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/4/15 0015
 */
@Controller
@RequestMapping("/comment")
public class EvaluateController {

    public static final String PATH = "D:\\\\idea_data\\shoponline\\img\\comment\\";

    @Resource
    private EvaluateService evaluateService;

    @PostMapping("/saveComment")
    public String addEvaluate(Evaluate evaluate, MultipartFile[] pictures, HttpSession session) throws IOException {
        User user = (User)session.getAttribute("user");
        try {
            evaluateService.saveComment(evaluate, user, pictures);
            return "redirect:/user/orders/1";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

}
