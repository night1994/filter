package com.night.filter.filterConfig;

import com.alibaba.fastjson.JSONObject;
import com.night.filter.Constants;
import com.night.filter.util.AESUntil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.util.StringUtils;

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
    * 实现功能描述： 获取请求信息 解密
    * @Author: zhengjingyun
    * @Date: 2021/4/16
    * @param
    * @return
    */
    public FilterWrapperedRequest(HttpServletRequest request) throws Exception {
        super(request);
        String data = getBodyString(request);
        log.info("request data origin --- >{}",data);
        if(!StringUtils.isEmpty(data)){
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
//                log.info("request read");
                return in.read();
            }
            @Override
            public boolean isFinished() {
//                log.info("request isFinished");

                return false;
            }
            @Override
            public boolean isReady() {
//                log.info("request isReady");

                return false;
            }
            @Override
            public void setReadListener(ReadListener readListener) {
//                log.info("request setReadListener");

            }
        };
    }


    /**
     * 从流中获取请求Body
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
