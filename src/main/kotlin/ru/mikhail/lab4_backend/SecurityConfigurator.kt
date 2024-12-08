package ru.mikhail.lab4_backend

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import ru.mikhail.lab4_backend.service.UserService


@Configuration
@EnableWebSecurity
class SecurityConfigurator( private var tokenFilter: TokenFilter,

                            private var userService: UserService) {



    @Autowired
    public fun setTokenFilter(tokenFilter: TokenFilter) {
        this.tokenFilter = tokenFilter
    }

    @Bean
    public fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    public fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager

    }


    @Bean
    @Primary
    public fun configureAuthenticationManagerBuilder(authenticationManagerBuilder: AuthenticationManagerBuilder): AuthenticationManagerBuilder {
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder())
        return authenticationManagerBuilder

    }


    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { cors ->
                cors.configurationSource { request ->
                    CorsConfiguration().applyPermitDefaultValues()
                }
            }
            .exceptionHandling { exceptions ->
                exceptions.authenticationEntryPoint(
                    HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
                )
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/secured/user").fullyAuthenticated()
                    .anyRequest().permitAll()
            }.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }


}
