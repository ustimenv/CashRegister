package shop.cashregister.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    @Bean
    public CashierDetailsService getCashierDetailsService() {        // allow cashiers to be used by Spring Security
        return new CashierDetailsService();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception{
        return super.authenticationManagerBean();
    }


    protected void configure(HttpSecurity httpSec) throws Exception {
        httpSec.authorizeRequests()
                .antMatchers("/**").permitAll().anyRequest().authenticated()
                .and()
                .httpBasic();

        httpSec.csrf().disable();      // TODO add support for csrf client-side
    }
}
