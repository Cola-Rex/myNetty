package rpc.server;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import rpc.entity.ClassInfo;

/**
 * 服务端操作，
 * 由服务端我们看到具体的数据传输操作是进行序列化的，
 * 具体的操作还是比较简单的，就是获取发送过来的信息，
 * 这样就可以通过反射获得类名，
 * 根据函数名和参数值，执行具体的操作，
 * 将执行结果发送给客户端。
 */
public class InvokerHandler extends ChannelInboundHandlerAdapter {

	public static ConcurrentHashMap<String, Object> classMap = new ConcurrentHashMap<>();
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ClassInfo classInfo = (ClassInfo) msg;
		Object clazz = null;
		
		if (classMap.containsKey(classInfo.getClassName())) {
			try {
				clazz = Class.forName(classInfo.getClassName()).newInstance();
				classMap.put(classInfo.getClassName(), clazz);
			} catch ( InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			clazz = classMap.get(classInfo.getClassName());
		}
		
		Method method = clazz.getClass().getMethod(classInfo.getMethodName(), classInfo.getTypes());
		Object result = method.invoke(clazz, classInfo.getObjects());
		
		ctx.write(result);
		ctx.flush();
		ctx.close();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
