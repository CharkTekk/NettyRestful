package de.netpacket.nettyrestful;

import de.netpacket.nettyrestful.handler.ChannelInitializerHandler;
import de.netpacket.nettyrestful.interfaces.Route;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

class NettyServer implements AutoCloseable {

    private final String address;
    private final int port;
    private final Route route;
    private EventLoopGroup workerGroup, bossGroup;
    private Class<? extends ServerSocketChannel> channelClazz;

    public NettyServer(String address, int port, Route route) {
        this.address = address;
        this.port = port;
        this.route = route;
        this.workerGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        this.bossGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        this.channelClazz = Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
    }

    public void connect(){
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .childOption(ChannelOption.IP_TOS, 24)
                .childOption(ChannelOption.AUTO_READ, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_REUSEADDR, true)
                .channel(channelClazz);

        serverBootstrap.childHandler(new ChannelInitializerHandler(route));

        try {
            serverBootstrap.bind(address, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        this.workerGroup.shutdownGracefully();
        this.bossGroup.shutdownGracefully();
    }
}
