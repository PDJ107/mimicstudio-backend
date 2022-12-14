package soma.gstbackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.ForwardedHeaderFilter;
import soma.gstbackend.config.entrypoint.CustomAuthenticationEntryPoint;
import soma.gstbackend.filter.JwtTokenFilter;
import soma.gstbackend.service.OAuthService;
import soma.gstbackend.util.JwtUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final OAuthService oAuthService;
    private final JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                    .authorizeRequests()
                    .antMatchers("/**").permitAll()
                    //.antMatchers("/auth/**", "/members/login", "/members/signup", "/3d-items/coin", "/3d-items/check-item", "/3d-items/queue").permitAll()
                    //.anyRequest().authenticated()
                .and()
                    .logout().logoutSuccessUrl("/")
                .and()
                    .oauth2Login()
                        .defaultSuccessUrl("/auth/login")
                        .userInfoEndpoint().userService(oAuthService);
        http
                .addFilterBefore(new JwtTokenFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint);
        http
                .headers()
                .xssProtection()
                .and().contentSecurityPolicy("script-src 'self'");

        return http.build();
    }

//    @Bean
//    FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
//
//        final FilterRegistrationBean<ForwardedHeaderFilter> filterRegistrationBean = new FilterRegistrationBean<ForwardedHeaderFilter>();
//
//        filterRegistrationBean.setFilter(new ForwardedHeaderFilter());
//        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//
//        return filterRegistrationBean;
//    }
}
