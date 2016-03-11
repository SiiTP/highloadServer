package decoders;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.string.StringDecoder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by root on 27.02.16.
 */
public class ByteToStringReader extends StringDecoder {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        List<Object> out = new ArrayList<Object>();
        decode(ctx, (ByteBuf) msg, out);
        String query =(String)out.get(0);
        super.channelRead(ctx, query);
    }
}
