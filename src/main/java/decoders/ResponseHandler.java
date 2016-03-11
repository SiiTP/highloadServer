package decoders;

import http.MyHttpRequest;
import http.MyHttpResponse;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.*;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import util.ByteUtils;

import java.nio.CharBuffer;
import java.util.List;


public class ResponseHandler extends MessageToMessageEncoder<MyHttpRequest> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MyHttpRequest req, List<Object> out) throws Exception {
        MyHttpResponse resp = new MyHttpResponse();

        switch (req.getQueryType()) {
            case MyHttpRequest.QUERY_TYPE_GET:
                byte[] bytes = resp.getBytesFromFilePath(req.getPath());
                if (bytes != null) { //200
                    resp.setHeaders(req, MyHttpResponse.CODE_OK);
                    byte[] result = ByteUtils.concateBytes(resp.getByteAllHeaders(), bytes);
                    out.add(result);
                } else { //404
                    if (!resp.isIndexDirectory()) {
                        resp.setHeaders(req, MyHttpResponse.CODE_NOT_FOUND);
                    } else {
                        resp.setHeaders(req, MyHttpResponse.CODE_FORBIDDEN);
                    }
                    byte[] result = ByteUtils.concateBytes(resp.getByteAllHeaders(), MyHttpResponse.getByteNotFound());
                    out.add(result);
                }
                break;
            case MyHttpRequest.QUERY_TYPE_HEAD:
                if (resp.getFileIsExistAndLength(req.getPath())) { //200
                    resp.setHeaders(req, MyHttpResponse.CODE_OK);
                    out.add(resp.getByteAllHeaders());
                } else { //404
                    resp.setHeaders(req, MyHttpResponse.CODE_NOT_FOUND);
                    byte[] result = ByteUtils.concateBytes(resp.getByteAllHeaders(), MyHttpResponse.getByteNotFound());
                    out.add(result);
                }
                break;
            case MyHttpRequest.QUERY_TYPE_POST:
                resp.setHeaders(req, MyHttpResponse.CODE_INCORRECT_QTYPE);
                byte[] result = ByteUtils.concateBytes(resp.getByteAllHeaders(), MyHttpResponse.getByteIncorrectQueryType());
                out.add(result);
                break;
        }
    }
}
