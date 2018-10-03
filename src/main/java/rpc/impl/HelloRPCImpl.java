package rpc.impl;

import rpc.publicInterface.HelloRPC;

public class HelloRPCImpl implements HelloRPC{

	@Override
	public String hello(String name) {
		return "hello" + name;
	}
}
