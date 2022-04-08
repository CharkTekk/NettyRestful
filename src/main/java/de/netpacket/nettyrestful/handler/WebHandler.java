package de.netpacket.nettyrestful.handler;

import de.netpacket.nettyrestful.interfaces.Route;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.net.URI;

public class WebHandler extends ChannelInboundHandlerAdapter {

    private final Route route;

    public WebHandler(Route route) {
        this.route = route;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) throws Exception {
        if(object instanceof HttpRequest request) {

            if(!request.getProtocolVersion().isKeepAliveDefault()) {
                ctx.channel().disconnect();
                return;
            }

            FullHttpResponse response = new DefaultFullHttpResponse(
                    request.getProtocolVersion(),
                    HttpResponseStatus.ACCEPTED,
                    Unpooled.wrappedBuffer(route.handle(new URI(request.getUri()).getRawPath(),
                            ctx.channel()).getBytes()));

            ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception { }
}
