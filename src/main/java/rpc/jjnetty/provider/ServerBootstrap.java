package rpc.jjnetty.provider;

import rpc.jjnetty.netty.NettyServer;

/**
 * @author heyball
 */
public class ServerBootstrap {

    public static void main(String[] args) {
        NettyServer.start("localhost", 8088);
    }
}
