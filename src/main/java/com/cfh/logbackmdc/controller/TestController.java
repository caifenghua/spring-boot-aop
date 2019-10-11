package com.cfh.logbackmdc.controller;

import com.cfh.logbackmdc.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Description:
 * @Author: bughua
 * @CreateDate: 2019/9/25 11:41
 */
@RestController
@Slf4j
public class TestController {

    @RequestMapping("/test")
    public void test(User user){
        log.debug("记录debug日志");
        log.info("访问了index方法");
        log.error("记录了error错误日志");
    }
}
