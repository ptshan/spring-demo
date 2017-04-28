package pers.qianshifengyi.springmvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhangshan193 on 17/4/11.
 */
@Controller
@RequestMapping("/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello(){
        logger.info("---- va haha "+System.currentTimeMillis());
        logger.error("---- va error -----");
        System.out.println("---- va sout");

        return "{\"msg\":\"ok\"}";
    }

    @RequestMapping("/hello2")
    public String sayHello2(){
        logger.info("-- hello2 lalala");
        return "hello2";
    }

}
