package org.healthafrica.shared.config;

import org.healthafrica.auth.security.JwtAuthenticationFilter;
import org.healthafrica.shared.security.PlatformAccessFilter;
import org.healthafrica.shared.tenant.TenantFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Prevents security filters from being registered twice in the servlet container.
 */
@Configuration
public class FilterConfig {

    @Bean
    FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration(
            JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registration =
                new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    FilterRegistrationBean<PlatformAccessFilter> platformAccessFilterRegistration(
            PlatformAccessFilter filter) {
        FilterRegistrationBean<PlatformAccessFilter> registration =
                new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    FilterRegistrationBean<TenantFilter> tenantFilterRegistration(
            TenantFilter filter) {
        FilterRegistrationBean<TenantFilter> registration =
                new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }
}
