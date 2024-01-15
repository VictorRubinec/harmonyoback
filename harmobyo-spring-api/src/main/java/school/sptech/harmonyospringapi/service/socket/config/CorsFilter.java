package school.sptech.harmonyospringapi.service.socket.config;

import jakarta.servlet.*;

import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@WebFilter("/*")
public class CorsFilter implements Filter {

    private String url = ""; //  54.209.110.127:80 -> IP do front na AWS

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //httpServletResponse.setHeader("Access-Control-Allow-Origin", url);
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        chain.doFilter(request, response);
    }
}