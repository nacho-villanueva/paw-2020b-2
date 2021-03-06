package ar.edu.itba.paw.webapp.auth;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CorsFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse)  servletResponse;
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        // Setting CORS headers
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "*");
        res.setHeader("Access-Control-Allow-Headers", "*");
        res.setHeader("Access-Control-Expose-Headers", "Authorization, Link, Location");
        res.setHeader("Access-Control-Max-Age", "3600");

        // If the request is OPTIONS, we need to give it an OK for preflight response
        if (req.getMethod().toUpperCase().trim().equals("OPTIONS")) {
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().flush();
            res.getWriter().close();
            return;
        }

        filterChain.doFilter(req, res);
    }
}
