package com.night.filter.filterConfig;

import com.alibaba.fastjson.JSONObject;
import com.night.filter.Constants;
import com.night.filter.util.AESUntil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

@Slf4j
public class FilterWrapperedRequest extends HttpServletRequestWrapper {

    private String requestBody;

    private     HttpServletRequest req = null;

    private String encoding = "utf-8";
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public FilterWrapperedRequest(HttpServletRequest request) throws Exception {
        super(request);
        String data = getBodyString(request);
        log.info("request data origin --- >{}",data);
        if(null != data){
            data = AESUntil.decrypt(Constants.aesPassword, JSONObject.parseObject(data).getString("data"));
            log.info("request data decry --- >{}",data);
            this.requestBody = data;
        }
        this.req = request;
    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {
            private InputStream in = new ByteArrayInputStream(
                    requestBody.getBytes(req.getCharacterEncoding()));
            @Override
            public int read() throws IOException {
                return in.read();
            }
            @Override
            public boolean isFinished() {
                // TODO Auto-generated method stub
                return false;
            }
            @Override
            public boolean isReady() {
                // TODO Auto-generated method stub
                return false;
            }
            @Override
            public void setReadListener(ReadListener readListener) {
                // TODO Auto-generated method stub

            }
        };
    }


    /**
     * 获取请求Body
     *
     * @param request
     * @return
     */
    public String getBodyString(final ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, encoding));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
