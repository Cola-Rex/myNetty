package rpc.jjnetty.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import rpc.jjnetty.consumer.ClientBootstrap;
import rpc.jjnetty.provider.HelloServiceImpl;

/**
 * @author heyball
 */
public class HelloServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg.toString().startsWith(ClientBootstrap.PROVIDER_NAME)) {
            String result = new HelloServiceImpl().hello(msg.toString());
            ctx.writeAndFlush(result);
        }
    }
}
