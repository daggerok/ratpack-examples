package daggerok.web.infrastructure.interceptor;

import daggerok.web.infrastructure.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.util.Optional;

@Component
public class ContextPathInterceptor implements WebRequestInterceptor {

    @Autowired
    ServletContext servletContext;

    String ctx;

    @PostConstruct
    public void init() {
        ctx = Util.ctx(servletContext);
    }


    @Override
    public void preHandle(WebRequest request) throws Exception { /* empty */ }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {

        Optional.ofNullable(model)
                .ifPresent(map -> map.addAttribute("ctx", ctx));
    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception { /* empty */ }
}
