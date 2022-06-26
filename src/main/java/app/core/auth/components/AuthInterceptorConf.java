package app.core.auth.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class AuthInterceptorConf implements WebMvcConfigurer {

    @Value("${configs.auth.security.enabled}")
    private boolean securityEnabled;

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
        if (securityEnabled) {
            registry.addInterceptor(authInterceptor);
        }
    }
}
