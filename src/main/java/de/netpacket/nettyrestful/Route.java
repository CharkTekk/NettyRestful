package de.netpacket.nettyrestful;

import io.netty.channel.Channel;

public interface Route {

    String handleURI(final String url, final Channel channel);
}
