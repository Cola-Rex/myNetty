package rpc.jjnetty.provider;

import rpc.jjnetty.service.HelloService;

/**
 * @author heyball
 */
public class HelloServiceImpl implements HelloService {

    /**
     * 你好召唤师
     *
     * @param msg 发信号
     * @return over
     */
    @Override
    public String hello(String msg) {
        return msg != null ? msg + "aahph" : "起飞";
    }
}
