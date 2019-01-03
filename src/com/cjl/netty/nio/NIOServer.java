package com.cjl.netty.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author chenjunlin
 * @date 2018-12-28
 */
public class NIOServer {
    private int port = 8080;
    private Charset charset = Charset.forName("UTF-8");
    //记录在线人数和名称
    private static HashSet<String> users = new HashSet<String>();

    private static String USER_EXIST = "系统提示:该昵称已经粗在";

    private static String USER_CONTENT_SPILIT = "#@#";

    private Selector selector = null;

    public NIOServer(int port) throws IOException{
        this.port = port;
        ServerSocketChannel server = ServerSocketChannel.open();

        server.bind(new InetSocketAddress(this.port));
        server.configureBlocking(false);

        selector = Selector.open();

        server.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("服务已启动,监听端口是"+this.port);
    }

    public void listener() throws IOException{
        while (true){
            int wait = selector.select();
            if (wait == 0) {
                continue;
            }

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = (SelectionKey) iterator.next();
                iterator.remove();
                process(key);
            }
        }
    }

    public void process(SelectionKey key) throws IOException {
        //判断客户端确定已经进入服务大厅并且已经可以实现交互了
        if(key.isAcceptable()){
            ServerSocketChannel server = (ServerSocketChannel)key.channel();
            SocketChannel client = server.accept();
            //非阻塞模式
            client.configureBlocking(false);
            //注册选择器，并设置为读取模式，收到一个连接请求，然后起一个SocketChannel，并注册到selector上，之后这个连接的数据，就由这个SocketChannel处理
            client.register(selector, SelectionKey.OP_READ);

            //将此对应的channel设置为准备接受其他客户端请求
            key.interestOps(SelectionKey.OP_ACCEPT);
//            System.out.println("有客户端连接，IP地址为 :" + sc.getRemoteAddress());
            client.write(charset.encode("请输入你的昵称"));
        }
        //处理来自客户端的数据读取请求
        if(key.isReadable()){
            //返回该SelectionKey对应的 Channel，其中有数据需要读取
            SocketChannel client = (SocketChannel)key.channel();

            //往缓冲区读数据
            ByteBuffer buff = ByteBuffer.allocate(1024);
            StringBuilder content = new StringBuilder();
            try{
                while(client.read(buff) > 0)
                {
                    buff.flip();
                    content.append(charset.decode(buff));

                }
//                System.out.println("从IP地址为：" + sc.getRemoteAddress() + "的获取到消息: " + content);
                //将此对应的channel设置为准备下一次接受数据
                key.interestOps(SelectionKey.OP_READ);
            }catch (IOException io){
                key.cancel();
                if(key.channel() != null)
                {
                    key.channel().close();
                }
            }
            if(content.length() > 0) {
                String[] arrayContent = content.toString().split(USER_CONTENT_SPILIT);
                //注册用户
                if(arrayContent != null && arrayContent.length == 1) {
                    String nickName = arrayContent[0];
                    if(users.contains(nickName)) {
                        client.write(charset.encode(USER_EXIST));
                    } else {
                        users.add(nickName);
                        int onlineCount = onlineCount();
                        String message = "欢迎 " + nickName + " 进入聊天室! 当前在线人数:" + onlineCount;
                        broadCast(null, message);
                    }
                }
                //注册完了，发送消息
                else if(arrayContent != null && arrayContent.length > 1) {
                    String nickName = arrayContent[0];
                    String message = content.substring(nickName.length() + USER_CONTENT_SPILIT.length());
                    message = nickName + "说 : " + message;
                    if(users.contains(nickName)) {
                        //不回发给发送此内容的客户端
                        broadCast(client, message);
                    }
                }
            }

        }
    }

    public int onlineCount() {
        int res = 0;
        for(SelectionKey key : selector.keys()){
            Channel target = key.channel();

            if(target instanceof SocketChannel){
                res++;
            }
        }
        return res;
    }


    public void broadCast(SocketChannel client, String content) throws IOException {
        //广播数据到所有的SocketChannel中
        for(SelectionKey key : selector.keys()) {
            Channel targetchannel = key.channel();
            //如果client不为空，不回发给发送此内容的客户端
            if(targetchannel instanceof SocketChannel && targetchannel != client) {
                SocketChannel target = (SocketChannel)targetchannel;
                target.write(charset.encode(content));
            }
        }
    }


    public static void main(String[] args) throws IOException {
        new NIOServer(8080).listener();
    }

}
