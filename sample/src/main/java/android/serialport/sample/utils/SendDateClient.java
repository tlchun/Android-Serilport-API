package android.serialport.sample.utils;

import android.serialport.sample.net.SendPacket;

import com.google.gson.Gson;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * Created by lc on 2018/11/4.
 */

public class SendDateClient {

    public void connect(){
        //worker负责读写数据
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            //辅助启动类
            Bootstrap bootstrap = new Bootstrap();

            //设置线程池
            bootstrap.group(worker);

            //设置socket工厂
            bootstrap.channel(NioSocketChannel.class);

            //设置管道
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    //获取管道
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    ByteBuf delimiter = Unpooled.copiedBuffer("#".getBytes());
                    pipeline.addLast(new DelimiterBasedFrameDecoder(1024,delimiter));
                    //字符串解码器
                    pipeline.addLast(new StringDecoder());
                    //字符串编码器
                    pipeline.addLast(new StringEncoder());
                    //处理类
                    pipeline.addLast(new ClientHandler4());
                }
            });

            //发起异步连接操作
            ChannelFuture futrue = bootstrap.connect(new InetSocketAddress("10.0.0.38",8020)).sync();

            //等待客户端链路关闭
            futrue.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅的退出，释放NIO线程组
            worker.shutdownGracefully();
        }
    }

    static class ClientHandler4 extends ChannelHandlerAdapter{

//        //接受服务端发来的消息
//        @Override
//        protected void channelRead(ChannelHandlerContext ctx, String msg) throws Exception {
//            System.out.println("server response ： "+msg);
//        }

        //与服务器建立连接
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            //给服务器发消息

            SendPacket sendPacket1 = new SendPacket("1234567890AB",1,10,System.currentTimeMillis());

            SendPacket sendPacket2 = new SendPacket("1234567890AB",2,10,System.currentTimeMillis());

            SendPacket sendPacket3 = new SendPacket("1234567890AB",3,10,System.currentTimeMillis());

            String json = new Gson().toJson(sendPacket1);

            ctx.channel().writeAndFlush(json+"#");

            json = new Gson().toJson(sendPacket2);
            ctx.channel().writeAndFlush(json+"#");

            json = new Gson().toJson(sendPacket3);
            ctx.channel().writeAndFlush(json+"#");

            System.out.println("channelActive");
        }

        //与服务器断开连接
        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelInactive");
        }

        //异常
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            //关闭管道
            ctx.channel().close();
            //打印异常信息
            cause.printStackTrace();
        }

    }
}
