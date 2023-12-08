package top.lldwb.test;

import io.netty.bootstrap.ServerBootstrap;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Discards any incoming data.
 */
public class Server {

    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        // 创建两个EventLoopGroup，一个用于接受客户端连接，另一个用于处理网络操作
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建ServerBootstrap实例，用于启动和绑定服务器
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            // 为每一个客户端连接创建一个ServerHandler实例
                            ch.pipeline().addLast(new ObjectEncoder(),new ObjectDecoder(String.class),new ServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 绑定端口，启动服务器
            ChannelFuture f = b.bind(port).sync();

            // 等待服务器关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅关闭EventLoopGroup，释放所有资源
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        // 如果用户提供了命令行参数，则使用该参数作为端口号
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        // 创建并启动服务器
        new Server(port).run();
    }
}