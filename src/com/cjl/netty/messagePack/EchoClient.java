package com.cjl.netty.messagePack;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


/**
 * @author chenjunlin  junlin.chen@msn.cn
 * @Date 2019-06-11 13:25
 * @Description: messagePack
 */
public class EchoClient {
    public void connection(int port,String host) throws InterruptedException {
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,3000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("msgpack decoder",new MsgPackDecoder());
                            ch.pipeline().addLast("msgpack encoder",new MsgPackEncoder());
                            ch.pipeline().addLast(new EchoClientHandler());
//
                        }
                    });
//            发起异步连接操作
            ChannelFuture f = b.connect(host,port).sync();
//                          等待客户端链路关闭
            f.channel().closeFuture().sync();
        } finally {
            workGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        if(args.length>0&&args!=null){
            System.out.println(args[0]);
            port = Integer.parseInt(args[0]);
        }
        new EchoClient().connection(port,"127.0.0.1");
    }
}