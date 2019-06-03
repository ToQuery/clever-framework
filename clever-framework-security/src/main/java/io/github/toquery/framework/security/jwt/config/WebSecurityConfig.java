package io.github.toquery.framework.security.jwt.config;

import io.github.toquery.framework.security.jwt.JwtAuthenticationEntryPoint;
import io.github.toquery.framework.security.jwt.JwtAuthorizationTokenFilter;
import io.github.toquery.framework.security.jwt.properties.AppJwtProperties;
import io.github.toquery.framework.security.jwt.service.JwtUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Resource
    private JwtUserDetailsService jwtUserDetailsService;

    // Custom JWT based security filter
    @Resource
    private JwtAuthorizationTokenFilter authenticationTokenFilter;

    @Resource
    private AppJwtProperties appJwtProperties;

    @Resource
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService)
                .passwordEncoder(passwordEncoderBean());
    }

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        //BCryptPasswordEncoder
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return "admin";
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return true;
            }
        };
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        AppJwtProperties.AppJwtPathProperties pathProperties = appJwtProperties.getPath();

        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()

                // 只有用于web管理页面的url才进行权限效验 todo 临时取消取消权限
                //.antMatchers("/conf/**", "/import/**").authenticated()
                // jwt
                .antMatchers(pathProperties.getRegister(), pathProperties.getToken(),"/sys/**").permitAll()
                .anyRequest().permitAll()
        ;

        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity
                .headers()
                .frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
                .cacheControl();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        AppJwtProperties.AppJwtPathProperties pathProperties = appJwtProperties.getPath();
        // AuthenticationTokenFilter will ignore the below paths
//        web.ignoring().antMatchers(HttpMethod.POST, pathProperties.getRegister(), pathProperties.getToken(),"/**");

        web.ignoring().antMatchers(pathProperties.getRegister(), pathProperties.getToken(),"/sys/**");
    }
}