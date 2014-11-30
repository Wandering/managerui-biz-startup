package cn.thinkjoy.mubs.controller.bussiness;

import cn.thinkjoy.common.protocol.RequestT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 示例http api
 */
@Controller
@RequestMapping(value="/user")
public class HttpApiController {
    private static final Logger LOGGER= LoggerFactory.getLogger(HttpApiController.class);

    /**
     * 示例服务
     * @param request
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String service1(@RequestBody RequestT<String> request) {
        System.out.println("run here");
        return "return str";
    }
}

