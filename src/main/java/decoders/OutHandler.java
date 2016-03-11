package decoders;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;

/**
 * Created by root on 08.03.16.
 */
public class OutHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        ChannelFuture future = ctx.writeAndFlush(Unpooled.copiedBuffer((byte[])msg));
        future.addListener(ChannelFutureListener.CLOSE);
    }
}
