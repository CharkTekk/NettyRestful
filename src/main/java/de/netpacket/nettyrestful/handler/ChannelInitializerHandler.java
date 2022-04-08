package de.netpacket.nettyrestful.handler;

import de.netpacket.nettyrestful.interfaces.Route;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class ChannelInitializerHandler extends ChannelInitializer {

    private final Route route;

    public ChannelInitializerHandler(Route route) {
        this.route = route;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline().addLast(
                new ReadTimeoutHandler(5),
                new HttpServerCodec(),
                new HttpObjectAggregator(Integer.MAX_VALUE),
                new WebHandler(route)
        );
    }
}
