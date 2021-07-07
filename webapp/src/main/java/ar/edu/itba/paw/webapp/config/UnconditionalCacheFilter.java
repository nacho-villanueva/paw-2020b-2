package ar.edu.itba.paw.webapp.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class UnconditionalCacheFilter extends OncePerRequestFilter {
    private static final String MAX_TIME = String.valueOf(TimeUnit.DAYS.toSeconds(365));

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        httpServletResponse.setHeader("Cache-Control", "public, max-age=" + MAX_TIME + ", immutable");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
