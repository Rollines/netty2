package com.cjl.netty.sofarpc;

import com.alipay.sofa.rpc.config.ConsumerConfig;

/**
 * @author : junlinchen
 * @date: 2019/6/28 8:39
 * @version: 1.0
 * @Des :  拿到服务端接口
 *     一般服务端会通过jar的形式将接口类提供给客户端。而在本例中，由于服务端和客户端在一个工程所以跳过。
 */
public class QuickStartClient {
    public static void main(String[] args) {
        ConsumerConfig<HelloService> consumerConfig = new ConsumerConfig<HelloService>()
                .setInterfaceId(HelloService.class.getName()) // 指定接口
                .setProtocol("bolt") // 指定协议
                .setDirectUrl("bolt://127.0.0.1:12200"); // 指定直连地址
        // 生成代理类
        HelloService helloService = consumerConfig.refer();
        while (true) {
            System.out.println(helloService.sayHello("world"));
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
            }
        }
    }
}
