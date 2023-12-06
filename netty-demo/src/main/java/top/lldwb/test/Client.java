package top.lldwb.test;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
    public static void main(String[] args) throws Exception {
        // 设置服务器地址和端口
        String host = "127.0.0.1";
        int port = 8080;
        // 创建EventLoopGroup，用于处理网络操作
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建Bootstrap实例，可以轻松引导Channel以供客户端使用
            Bootstrap bootstrap = new Bootstrap();
            // 设置EventLoopGroup
            bootstrap.group(workerGroup);
            // 设置Channel类型
            bootstrap.channel(NioSocketChannel.class);
            // 设置SO_KEEPALIVE选项
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            // 设置ChannelHandler
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new ObjectDecoder(String.class), new ClientHandler());
                }
            });

            // 连接服务器
            ChannelFuture future = bootstrap.connect(host, port).sync();

            // 关闭连接
            future.channel().closeFuture().sync();
        } finally {
            // 优雅关闭EventLoopGroup
            workerGroup.shutdownGracefully();
        }
    }
}