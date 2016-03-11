package decoders;

import http.MyHttpRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.channels.Channels;
import java.util.List;


public class StringToHttpDecoder extends MessageToMessageDecoder<String> {


//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("read string to http!!");
//        MyHttpRequest request = new MyHttpRequest((String)msg);
//        ctx.channel().write(request);
//
//    }

    @Override
    protected void decode(ChannelHandlerContext ctx, String s, List<Object> out) throws Exception {
        MyHttpRequest request = new MyHttpRequest(s);
        ctx.channel().write(request);
    }
}
