package de.netpacket.nettyrestful.interfaces;

import io.netty.channel.Channel;

public interface Route {

    String handle(final String url, final Channel channel);
}
