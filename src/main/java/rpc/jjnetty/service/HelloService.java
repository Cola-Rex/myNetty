package rpc.jjnetty.service;

/**
 * @author heyball
 */
public interface HelloService {

    /**
     * 你好召唤师
     *
     * @param msg 发信号
     * @return over
     */
    String hello(String msg);
}
