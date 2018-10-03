package rpc;

import rpc.client.Client;
import rpc.impl.HelloRPCImpl;
import rpc.publicInterface.HelloRPC;

public class Main {

	public static void main(String[] args) {
		HelloRPC helloRPC = new HelloRPCImpl();
		helloRPC = Client.create(helloRPC);
		System.out.println(helloRPC.hello("jinx"));
	}
}
