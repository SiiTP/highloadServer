package decoders;

import http.MyHttpRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.channels.Channels;
import java.util.List;


public class StringToHttpDecoder extends MessageToMessageDecoder<String> {

    @Override
    protected void decode(ChannelHandlerContext ctx, String s, List<Object> out) throws Exception {
        MyHttpRequest request = new MyHttpRequest(s);
        ctx.channel().write(request);
    }
}
