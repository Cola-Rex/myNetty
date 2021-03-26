package rpc.jjnetty.consumer;

import rpc.jjnetty.netty.NettyClient;
import rpc.jjnetty.service.HelloService;

/**
 * @author heyball
 */
public class ClientBootstrap {

    public static final String PROVIDER_NAME = "hello";

    public static void main(String[] args) throws InterruptedException {
        NettyClient client = new NettyClient();
        //创建一个代理对象
        HelloService service = (HelloService) client.createProxy(HelloService.class, PROVIDER_NAME);

        while (true) {
            Thread.sleep(1000);
            String result = service.hello("孙亚龙");
            System.out.println(result);
        }
    }
}
