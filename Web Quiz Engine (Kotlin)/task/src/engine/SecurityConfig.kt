package engine

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .httpBasic(Customizer.withDefaults()) // Default Basic auth config
            .csrf { it.disable() }                // for POST requests via Postman
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(HttpMethod.POST, "/api/register/**").permitAll()
                    .requestMatchers("/error").permitAll()
                    .requestMatchers("/actuator/shutdown").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/quizzes/**").hasRole("USER")
                    .requestMatchers(HttpMethod.GET, "/api/**").hasRole("USER")
                    .requestMatchers(HttpMethod.DELETE, "/api/quizzes/**").hasRole("USER")


            }

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}