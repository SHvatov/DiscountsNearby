package com.discounts.nearby.config

import com.discounts.nearby.model.User
import com.discounts.nearby.repository.UserRepository
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.HttpStatusEntryPoint


/**
 * @author shvatov
 */
@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests {
            it.apply {
                antMatchers(*ALLOWED_ENDPOINTS).permitAll()
                        .anyRequest().authenticated()
            }
        }.exceptionHandling {
            it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        }.logout {
            it.logoutSuccessUrl("/").permitAll()
        }.csrf().disable()
                .oauth2Login()
    }

    @Bean
    fun principalExtractor(usersRepository: UserRepository) = PrincipalExtractor {
        val id = it["sub"] as String
        val user = usersRepository.findByIdOrNull(id)
            ?: User().apply {
                this.id = id
                this.email = it["email"] as? String
                this.name = it["name"] as? String
            }
        usersRepository.save(user)
    }

    private companion object {
        val ALLOWED_ENDPOINTS = arrayOf("/", "/error", "/js/**", "/css/**", "/supermarkets/**")
    }
}