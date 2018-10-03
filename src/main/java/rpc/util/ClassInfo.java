package rpc.util;

import java.io.Serializable;

public class ClassInfo implements Serializable {

	private static final long serialVersionUID = 5156453794166586837L;

	private String className; //类名
	private String methodName; //方法名称
	private Class<?>[] types; //参数类型
	private Object[] objects; //参数列表
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Class<?>[] getTypes() {
		return types;
	}
	public void setTypes(Class<?>[] types) {
		this.types = types;
	}
	public Object[] getObjects() {
		return objects;
	}
	public void setObjects(Object[] objects) {
		this.objects = objects;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
