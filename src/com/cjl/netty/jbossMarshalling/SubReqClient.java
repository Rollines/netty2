package com.cjl.netty.jbossMarshalling;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author chenjunlin  junlin.chen@msn.cn
 * @Date 2019-06-12 13:18
 * @Description:
 */
public class SubReqClient {
    public void connect(int port,String host) throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                            ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                            ch.pipeline().addLast(new SubReqClientHandler());

                        }
                    });
            //发起异步连接操作
            ChannelFuture future = bootstrap.connect(host,port).sync();

            //等待客户端链路关闭
            future.channel().closeFuture().sync();

        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port = 4545;
        if (args!=null && args.length>0){
            try {
                port = Integer.valueOf(args[10]);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
    }
}
