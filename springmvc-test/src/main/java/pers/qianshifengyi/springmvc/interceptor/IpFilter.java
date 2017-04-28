package pers.qianshifengyi.springmvc.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import pers.qianshifengyi.springmvc.service.IpStrategyService;
import pers.qianshifengyi.springmvc.util.IpUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 在preHandle中，可以进行编码、安全控制等处理；
 在postHandle中，有机会修改ModelAndView；
 在afterCompletion中，可以根据ex是否为null判断是否发生了异常，进行日志记录
 * Created by zhangshan193 on 17/4/20.
 */
public class IpFilter extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(IpFilter.class);

    @Autowired
    private IpStrategyService ipStrategyService;

    public IpFilter() {
        super();
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = IpUtil.getRealIp(request);
        boolean flag = StringUtils.isBlank(ip) || (!ip.equalsIgnoreCase("localhost") && !ip.equals("0:0:0:0:0:0:0:1")
                && !ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"));
        logger.info("ip:{}    flag:{}",ip,flag);
        if(flag == true){
            return false;
        }
        List<String> ipList = ipStrategyService.getAllDisabledIps();
        logger.info("ipList:{}",ipList);
        logger.info("ipList.contains(ip):{}",ipList.contains(ip));

        if(ipList.contains(ip)){
            response.sendRedirect("/visitForbidden.jsp");
            return false;
        }

        return super.preHandle(request, response, handler);
    }

}
