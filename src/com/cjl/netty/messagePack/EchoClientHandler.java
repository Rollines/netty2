package com.cjl.netty.messagePack;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author chenjunlin  junlin.chen@msn.cn
 * @Date 2019-06-12 08:40
 * @Description:
 */
public class EchoClientHandler extends ChannelHandlerAdapter {
    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       /* User user = getUser();
        ctx.writeAndFlush(user);*/
        User[] users = getUsers();
        for(User u : users){
            ctx.write(u);
        }
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("this is client receive msg【  "+ ++count +"  】times:【"+msg+"】");
        if(count<5){ //控制运行次数，因为不加这个控制直接调用下面代码的话，客户端和服务端会形成闭环循环，一直运行
            ctx.write(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
    private User[] getUsers(){
        User[] users = new User[5];
        for(int i=0;i<5;i++){
            User user = new User();
            user.setId(String.valueOf(i));
            user.setAge(18+i);
            user.setName("张元"+i);
            user.setSex("男"+String.valueOf(i*2));
            users[i]=user;
        }
        return users;
    }

    private User getUser(){
        User user = new User();
        user.setId("11");
        user.setAge(18);
        user.setName("张元");
        user.setSex("男");
        return user;
    }
}
