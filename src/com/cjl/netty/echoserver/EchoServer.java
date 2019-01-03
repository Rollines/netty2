package com.cjl.netty.echoserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.EventExecutorGroup;

import java.net.InetSocketAddress;

/**
 * @author chenjunlin
 * @date 2018-12-26
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port){
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length !=1){
            //设置端口值
            System.out.println("Usage: "+EchoServer.class.getSimpleName()+
                    "<port>");
        }
        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }

    //调用服务器的start方法
    private void start() throws Exception{
        final EchoServerHandler serverHandler = new EchoServerHandler();
        //创建EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)  //指定所使用的Nio传输的channel
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //EchoServerHandler被标注为@shareable，所以我们可以总是使用同样的实例
                            ch.pipeline().addLast((EventExecutorGroup) serverHandler);

                        }
                    });
            //异步地绑定服务器；调用sync方法阻塞等待知道绑定完成
            ChannelFuture future = bootstrap.bind().sync();
            //获取channel的closefutrue,并且阻塞当前线程知道完成
            future.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }

    }
}
