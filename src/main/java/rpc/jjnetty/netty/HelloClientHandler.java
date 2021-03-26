package rpc.jjnetty.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * @author heyball
 */
public class HelloClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;

    private String result;

    private String param;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) {
        //收到数据，唤醒等待线程
        result = msg.toString();
        notify();
    }

    @Override
    public synchronized Object call() throws Exception {
        //写入 ，进入等待状态
        context.writeAndFlush(param);
        wait();
        return result;
    }

    void setParam(String param) {
        this.param = param;
    }

}
