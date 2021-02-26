package com.example.gateway.inbound;

import com.example.gateway.filter.HeaderHttpRequestFilter;
import com.example.gateway.filter.HttpRequestFilter;
import com.example.gateway.outbound.HttpOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;

import java.util.List;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {
    private List<String> proxyServer;
    private HttpOutboundHandler handler;
    HttpRequestFilter filter = new HeaderHttpRequestFilter();
    public HttpInboundHandler(List<String> proxyServer) {
        this.proxyServer = proxyServer;
        this.handler = new HttpOutboundHandler(proxyServer);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);

        FullHttpRequest fullRequest = (FullHttpRequest) msg;
        handler.handle(fullRequest,ctx,filter);
    }
}
