package com.cjl.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @author chenjunlin  junlin.chen@msn.cn
 * @Date 2019-06-06 13:10
 * @Description:
 */
public class TimeServer {
    private static int DEFAULT_PORT = 8989;
    public static void main(String[] args) {
        int port = 8989;
        if (args!= null && args.length>0){
            try{
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer,"nio-1").start();
    }

}
class MultiplexerTimeServer implements Runnable{
    private Selector selector;
    private ServerSocketChannel servChannel;
    private volatile boolean stop;

    public MultiplexerTimeServer(int port) {
        try{
            selector = Selector.open();
            servChannel = ServerSocketChannel.open();
            servChannel.configureBlocking(false);
            servChannel.socket().bind(new InetSocketAddress(port),1024);
            servChannel.register(selector, SelectionKey.OP_CONNECT);
            this.stop = false;
            System.out.println("The Time Server is start in port : " + port);
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    public void stop() {
        this.stop = true;
    }

    public void run() {
        while(!stop) {
            try {
                //wait until 1000 millisecond
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while(it.hasNext()) {
                    key = it.next();
                    try{
                        handleInput(key);
                    }catch(Exception e) {

                    }
                    if(key != null) {
                        key.cancel();
                        if(key.channel() != null) {
                            key.channel().close();
                        }
                    }
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        //多路复用器关闭以后，所有注册在上面的Channel和Pipe等资源都会自动去注册并关闭，所以不需要重复释放资源
        if(selector != null) {
            try{
                selector.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    //处理事件响应
    private void handleInput(SelectionKey key) throws Exception {
        if(key.isValid()) {
            if(key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                sc.register(selector, SelectionKey.OP_READ);
            }
            if(key.isReadable()) {
                SocketChannel sc = (SocketChannel)key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if(readBytes > 0) {
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("The time Server receive order:" + body);
                    String response = "success get message at:" + new Date(System.currentTimeMillis()).toString();
                    doWrite(sc, response);
                } else {
                    key.cancel();
                    sc.close();
                }
            }
        }
    }

    //在成功接受客户端的请求消息以后，同时给客户端返回一条成功的信息
    private void doWrite(SocketChannel sc, String response) throws Exception {
        if(response != null && response.length() > 0) {
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer  = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            sc.write(writeBuffer);
        }
    }
}
