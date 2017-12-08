package com.mytest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Main {

  public static void main(String[] args) {
    RealObject real = new RealObject();
    ProxyInterface proxy = (ProxyInterface) Proxy.newProxyInstance(ProxyInterface.class.getClassLoader(),
        new Class[] {ProxyInterface.class}, new ProxyObject(real));
    
    proxy.say();
  }
}

interface ProxyInterface {
  void say();
}

// 被代理类
class RealObject implements ProxyInterface {
  public void say() {
    System.out.println("i'm talking");
  }
}

// 代理类，实现InvocationHandler 接口
class ProxyObject implements InvocationHandler {
  private Object proxied = null;

  public ProxyObject() {
  }

  public ProxyObject(Object proxied) {
    this.proxied = proxied;
  }

  public Object invoke(Object arg0, Method arg1, Object[] arg2) throws Throwable {
    System.out.println("hello");
    return arg1.invoke(proxied, arg2);
  };
}
