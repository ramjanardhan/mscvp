package com.mss.ediscv.general;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import com.mss.ediscv.util.LoggerUtility;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionFilter implements Filter {

    private ArrayList<String> urlList;
    private static Logger logger = LogManager.getLogger(SessionFilter.class.getName());

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        try {
            String url = request.getServletPath();
            url = request.getRequestURI();
            HttpSession session = request.getSession(false);
            if (session == null && !url.trim().equals("/ediscv/")) {
                response.sendRedirect("/ediscv");
            }
            if (session != null) {
                String user = (String) session.getAttribute("userName");
            }
            chain.doFilter(req, res);
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in doFilter method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
    }

    public void init(FilterConfig config) throws ServletException {
        try {
            String urls = config.getInitParameter("avoid-urls");
            StringTokenizer token = new StringTokenizer(urls, ",");
            urlList = new ArrayList<String>();
            while (token.hasMoreTokens()) {
                urlList.add(token.nextToken());
            }
            urlList.size();
        } catch (Exception exception) {
            LoggerUtility.log(logger, "Exception occurred in init method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        }
    }
}
