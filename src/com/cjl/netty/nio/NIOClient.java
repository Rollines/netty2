package com.cjl.netty.nio;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @author chenjunlin
 * @date 2018-12-28
 */
public class NIOClient {
    private final InetSocketAddress serverAdrress = new InetSocketAddress("localhost", 8080);
    private Selector selector = null;
    private SocketChannel client = null;

    private String nickName = "";
    private Charset charset = Charset.forName("UTF-8");
    private static String USER_EXIST = "系统提示：该昵称已经存在，请换一个昵称";
    private static String USER_CONTENT_SPILIT = "#@#";

    public NIOClient() throws IOException{
        client = SocketChannel.open(serverAdrress);
        client.configureBlocking(false);

        selector = Selector.open();
        client.register(selector, SelectionKey.OP_READ);
    }
    public void session(){
        new Reader().start();
        new Writer().start();
    }

    private class Writer extends Thread{
        @Override
        public void run(){
            try {
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNextLine()){
                    String line = scanner.nextLine();
                    if ("".equals(line)) {
                        continue;
                    }
                    if ("".equals(nickName)){
                        nickName = line;
                        line = nickName+USER_CONTENT_SPILIT;

                    }else {
                        line = nickName + USER_CONTENT_SPILIT+line;
                    }
                    client.write(charset.encode(line));
                }
                scanner.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class Reader extends Thread{
        @Override
        public void run(){
            try {
                while (true){
                    int readyChannels = selector.select();
                    if (readyChannels ==0){
                        continue;
                    }
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                    while (keyIterator.hasNext()){
                        SelectionKey key = (SelectionKey) keyIterator.next();
                        keyIterator.remove();
                        process(key);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        private void process(SelectionKey key)throws IOException{
            if (key.isReadable()){
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                String content="";
                while (sc.read(buffer)>0)
                {
                    buffer.flip();
                    content += charset.decode(buffer);
                }
                if (USER_EXIST.equals(content)){
                    nickName ="";
                }
                System.out.println(content);
                key.interestOps(SelectionKey.OP_READ);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new NIOClient().session();
    }


}

