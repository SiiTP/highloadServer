package channel;

import decoders.ByteToStringReader;
import decoders.OutHandler;
import decoders.ResponseHandler;
import decoders.StringToHttpDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


/**
 * Created by root on 28.02.16.
 */
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new ByteToStringReader());
        pipeline.addLast(new StringToHttpDecoder());
        pipeline.addLast(new OutHandler());
        pipeline.addLast(new ResponseHandler());
    }
}
