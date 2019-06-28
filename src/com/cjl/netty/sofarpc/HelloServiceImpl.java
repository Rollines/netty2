package com.cjl.netty.sofarpc;

/**
 * @author : junlinchen
 * @date: 2019/6/28 8:33
 * @version: 1.0
 * @Des : 实现类
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String string) {
        System.out.println("Server receive: " + string);
        return "hello " + string + " ！";
    }
}
