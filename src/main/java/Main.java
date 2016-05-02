import channel.MyChannelInitializer;
import http.MyHttpResponse;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Main {

    private static final int PORT = 80;
    private static int threadsNumber = 4;
    private static final int BACKLOG_OPTION = 512;
    private static final int BUF_OPTION = 32000;

    public static void main(String[] args) {
        if (args.length == 1) {
            threadsNumber = Integer.parseInt(args[0]);
        }

        if (args.length == 2) {
            threadsNumber = Integer.parseInt(args[0]);
            MyHttpResponse.setRootDir(args[1]);
        }
        System.out.println("Server started!");
        System.out.println("Threads number : " + threadsNumber);
        System.out.println("=======================================");
        System.out.println("This is resource server based on netty.");
        System.out.println("First  parameter : count of threads");
        System.out.println("Second parameter : source dir");
        System.out.println("Parameters are optional");
        System.out.println("Made by Semenchenko Ivan and Pohodnya Ivan");
        System.out.println("IU5-3kurs, April 2016");
        System.out.println("Supported multithreading and multiprocessing");

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(threadsNumber);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MyChannelInitializer()).option(ChannelOption.SO_BACKLOG, BACKLOG_OPTION)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_SNDBUF, BUF_OPTION)
                    .childOption(ChannelOption.SO_RCVBUF, BUF_OPTION)
                    .childOption(ChannelOption.SO_REUSEADDR, true);

            ChannelFuture f = bootstrap.bind(PORT).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}