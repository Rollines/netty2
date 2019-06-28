package com.cjl.netty.sofarpc;

import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.config.ServerConfig;

/**
 * @author : junlinchen
 * @date: 2019/6/28 8:34
 * @version: 1.0
 * @Des : sofa 服务端代码
 */
public class QuickStartServer {
    public static void main(String[] args) {
        ServerConfig serverConfig = new ServerConfig()
                .setProtocol("bolt") //设置一个协议
                .setPort(12200).setDaemon(false);
        ProviderConfig<HelloService> providerConfig = new ProviderConfig<HelloService>()
                .setInterfaceId(HelloService.class.getName()) // 指定接口
                .setRef(new HelloServiceImpl()) // 指定实现
                .setServer(serverConfig); // 指定服务端
        providerConfig.export();
    }
}
