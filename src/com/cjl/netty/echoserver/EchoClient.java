package com.cjl.netty.echoserver;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author chenjunlin
 * @date 2018-12-26
 */
public class EchoClient {
    private final String host;
    private final int port;
    public EchoClient(String host,int port){
        this.host = host;
        this.port = port;
    }
    public void start() throws Exception{
        //设置服务器的inetetsocketackl
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host,port))
                    .handler(new ChannelInitializer<SocketChannel>() { //在创建channel时，向channelpipeline中添加一个echoclinetHandler实例

                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(
                                 new EchoServerHandler()
                            );
                        }
                    });
            //连接到远程节点，阻塞等待知道连接完成
            ChannelFuture future = b.connect().sync();
            //阻塞，直到channel关闭
            future.channel().closeFuture().sync();

        }finally {
            //关闭线程池并且释放所有的资源
            group.shutdownGracefully().sync();
        }

    }

    public static void main(String[] args) throws Exception {
        if (args.length!=2){
            System.out.println("Usage:" + EchoClient.class.getSimpleName() + "<host><port>");
            return;
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        new EchoClient(host,port).start();
    }
}
