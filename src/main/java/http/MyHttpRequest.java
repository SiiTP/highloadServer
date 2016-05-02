package http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.HashMap;

public class MyHttpRequest {
    public static final String QUERY_TYPE_GET = "GET";
    public static final String QUERY_TYPE_HEAD = "HEAD";
    public static final String QUERY_TYPE_POST = "POST";
    public static final String DEFAULT_CONNECTION_TYPE = "keep-alive";


    private HashMap<String, String> request;

    private String queryType;
    private String path;
    private String version;
    private String header;
    public static final String test = "GET /dir1/dir2 HTTP/1.1\r\nHost: localhost\r\nConnection: keep-alive\r\nCache-Control: max-age=0\r\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\r\nUpgrade-Insecure-Requests: 1\r\n\r\n";

    public  MyHttpRequest(String strRequest) throws ParseException {
        request = new HashMap<>();
        header = "";
        parseRequest(strRequest);
    }

    public String getQueryType() {
        return queryType;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public String getHeaderLine() {
        if (header.isEmpty()) {
            //noinspection StringBufferReplaceableByString
            StringBuilder builder = new StringBuilder();
            builder.append(queryType).append(' ')
                    .append(path).append(' ')
                    .append("HTTP/").append(version).append("\r\n");
            header = builder.toString();
        }
        return header;
    }

    public void parseRequest(String strRequest) throws ParseException {
        parseQueryTypeAndPath(strRequest);

        int endLineIndex = strRequest.indexOf("\r\n");
        do {
            int newLineIndex = endLineIndex + 2;
            strRequest = strRequest.substring(newLineIndex);

            endLineIndex = strRequest.indexOf("\r\n");
            if (endLineIndex > 0) {
                String line = strRequest.substring(0, endLineIndex);
                parseLine(line);
            }
        } while (!strRequest.isEmpty());
    }

    private void parseQueryTypeAndPath(String baseStrRequest) throws ParseException{
        String strRequest;

        //escape % symbols
        int i = baseStrRequest.indexOf('%');
        if (i >= 0) {
            try {
                strRequest = URLDecoder.decode(baseStrRequest, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                strRequest = baseStrRequest;
            }
        } else {
            strRequest = baseStrRequest;
        }

        //escape /../
        i = strRequest.indexOf("/../");
        if (i >= 0) {
            strRequest = strRequest.replace("/../", "/azazazaza/");
        }

        i = strRequest.indexOf(' ');
        if (i > 0) {
            String type = strRequest.substring(0, i);

            if (type.equals(QUERY_TYPE_GET) || type.equals(QUERY_TYPE_HEAD) || type.equals(QUERY_TYPE_POST)) {
                this.queryType = type;
            } else {
                throw new ParseException("incorrect query type : " + type, 0);
            }
        } else {
            throw new ParseException("no query type", 0);
        }

        strRequest = strRequest.substring(i + 1);

        //parsing path
        i = strRequest.indexOf("HTTP/");
        this.path = strRequest.substring(0, i - 1);

        //parsing version
        strRequest = strRequest.substring(i);
        if (i >= 0) {
            this.version = strRequest.substring(5, strRequest.indexOf("\r\n"));
        } else {
            throw new ParseException("not http query", 0);
        }
    }

    private void parseLine(String line) {
        String[] map = line.split(": ");
        if (map.length == 2) {
            request.put(map[0], map[1]);
        }
    }

    public String getHeader(String key) {
        if (request.containsKey(key)) {
            return request.get(key);
        } else {
            return DEFAULT_CONNECTION_TYPE;
        }
    }
}
