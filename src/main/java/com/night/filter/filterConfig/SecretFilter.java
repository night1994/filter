package com.night.filter.filterConfig;

import com.alibaba.fastjson.JSONObject;
import com.night.filter.Constants;
import com.night.filter.util.AESUntil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
@WebFilter(urlPatterns = { "^static/*" }, filterName = "DataFilter")
@Slf4j
public class SecretFilter  implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {

        try {
            FilterWrapperedRequest req = new FilterWrapperedRequest((HttpServletRequest) request);
            FilterWrapperedResponse resp = new FilterWrapperedResponse((HttpServletResponse) response);
            log.info("request url ---->{}" ,req.getRequestURI());
            chain.doFilter(req,resp);

            writerWrapper(resp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    /**
    * 实现功能描述：拦截返回值 加密
    * @Author: 郑净云
    * @Date: 2020/11/12
    * @param
    * @return
    */
    private void writerWrapper(FilterWrapperedResponse resp) throws Exception {

        String res = new String(resp.getResponseData());
        if(StringUtils.isEmpty(res)){
            return;
        }
        JSONObject jsonObject = JSONObject.parseObject(res);
        String data = jsonObject.getString("data");
        data = AESUntil.encrypt2Java(Constants.aesPassword,data);
        jsonObject.put("data",data);
        PrintWriter out = resp.getWriter();
        out.print(jsonObject.toJSONString());
        out.flush();
        out.close();
    }
}
