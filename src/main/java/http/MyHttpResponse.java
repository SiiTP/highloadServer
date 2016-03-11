
//todo Files readAllBytes
package http;


import io.netty.util.CharsetUtil;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;
import util.ContentTypeUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class MyHttpResponse {
    public static final String HELLO_FILE = "index.html";
    public static final String SERVER_NAME = "netty server";

    public static final int CODE_OK = 200;
    public static final int CODE_NOT_FOUND = 404;
    public static final int CODE_FORBIDDEN = 403;
    public static final int CODE_INCORRECT_QTYPE = 405;
    public static final String STATUS_OK = "OK";
    public static final String STATUS_NOT_FOUND = "Not found";
    public static final String STATUS_FORBIDDEN = "Forbidden";
    public static final String STATUS_INCORRECT_QTYPE = "Unsupported query type";
    public static final String STATUS_UNSUPPORTED_ERROR = "Unsupported error";

    private static String rootDir = System.getProperty("user.dir");
    private String header;
    private byte[] byteFile;
    private long byteFileSize;
    private boolean isIndexDirectory = false;

    public static byte[] getByteNotFound() {
        String strMessage = Integer.toString(CODE_NOT_FOUND) + ' ' + STATUS_NOT_FOUND;
        return strMessage.getBytes(CharsetUtil.UTF_8);
    }

    public static byte[] getByteIncorrectQueryType() {
        String strMessage = Integer.toString(CODE_INCORRECT_QTYPE) + ' ' + STATUS_INCORRECT_QTYPE;
        return strMessage.getBytes(CharsetUtil.UTF_8);
    }

    public static void setRootDir(String newRootDir) {
        MyHttpResponse.rootDir = newRootDir;
    }

    @Nullable
    public byte[] getBytesFromFilePath(String path) {
        path = rootDir + path; //absolute path
        String endSymbol = path.substring(path.length() - 1);
        if (endSymbol.equals("/")) {
            isIndexDirectory = true;
            path += MyHttpResponse.HELLO_FILE;
        }
        int getParamsBegin = path.indexOf('?');
        if (getParamsBegin > 0) {
            path = path.substring(0, getParamsBegin);
        }
        File file = new File(path);

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byteFile = IOUtils.toByteArray(fileInputStream);
            byteFileSize = byteFile.length;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return byteFile;
    }

    public boolean getFileIsExistAndLength(String path) {
        path = rootDir + path; //absolute path
        String endSymbol = path.substring(path.length()-1);
        if (endSymbol.equals("/")) {
            path += MyHttpResponse.HELLO_FILE;
        }
        File file = new File(path);
        byteFileSize = file.length();
        return file.exists() && !file.isDirectory();
    }

    public String getHeader() {
        return header;
    }

    public void setHeaders(MyHttpRequest req, int status) {
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/").append(req.getVersion()).append(' ').append(status).append(' ');

        switch (status) {
            case CODE_OK:
                builder.append(STATUS_OK);
                break;
            case CODE_NOT_FOUND:
                builder.append(STATUS_NOT_FOUND);
                break;
            case CODE_FORBIDDEN:
                builder.append(STATUS_FORBIDDEN);
                break;
            default:
                builder.append(STATUS_UNSUPPORTED_ERROR);
                break;
        }
        builder.append("\r\n");

        addServerHeader(builder);
        addDateHeader(builder);
        addConnectionHeader(builder, req.getHeader("Connection"));
        if (req.getQueryType().equals(MyHttpRequest.QUERY_TYPE_GET) || req.getQueryType().equals(MyHttpRequest.QUERY_TYPE_HEAD)) {
            addContentLengthHeader(builder);
            addContentTypeHeader(builder, req.getPath());
        }
        builder.append("\r\n");

        header = builder.toString();
    }

    public void addServerHeader(StringBuilder builder) {
        builder.append("Server: ").append(SERVER_NAME).append("\r\n");
    }

    public void addDateHeader(StringBuilder builder) {
        builder.append("Date: ").append(getServerTime()).append("\r\n");
    }

    public void addContentLengthHeader(StringBuilder builder) {
        builder.append("Content-Length: ").append(byteFileSize).append("\r\n");
    }

    public void addConnectionHeader(StringBuilder builder, String conncetionType) {
        builder.append("Connection: ").append(conncetionType).append("\r\n");
    }

    public void addContentTypeHeader (StringBuilder builder, String path) {
        String fileName = path.substring(path.lastIndexOf('/') + 1);

        //clean get params
        int getParamIndex = fileName.indexOf('?');
        if (getParamIndex > 0) {
            fileName = fileName.substring(0, getParamIndex);
        }

        int beginExtention = fileName.lastIndexOf('.') + 1;
        if (beginExtention > 0) {
            String fileExtention = fileName.substring(beginExtention);
            String contentType = ContentTypeUtils.getContentTypeByExtension(fileExtention);
            builder.append("Content-type: ").append(contentType).append("\r\n");
        }

    }

    String getServerTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }

    public byte[] getByteAllHeaders() {
        return header.getBytes(CharsetUtil.UTF_8);
    }

    public boolean isIndexDirectory() {
        return isIndexDirectory;
    }
}
