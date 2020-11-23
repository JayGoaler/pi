package com.jaygoaler.pitools.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @program: backend
 * @description: aop拦截器
 * @author: JayGoal
 * @create: 2020-09-29 10:48
 **/
@Order(0)
@Aspect
@Component
@Slf4j
public class AopInterceptor {

    /**
     * 定义拦截器规则
     */
    private final String POINTCUT = "execution(* com.tfswx.diamplatform.backend.controller.*.*(..))";

    /**
     * 拦截器具体实现
     */
    @Before(POINTCUT)
    public void before() {
//        try {
//            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//            String operation = request.getRequestURI();
//            String ip = CommonUtils.getIpAddr(request);
//            log.info("=>" + ip + "--->" + operation);
//        } catch (Exception e) {
//            log.error("error", e);
//        }
    }
}
