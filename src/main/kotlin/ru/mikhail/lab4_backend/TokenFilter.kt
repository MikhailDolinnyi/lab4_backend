package ru.mikhail.lab4_backend

import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class TokenFilter (private var jwtCore: JwtCore,
                   private var userDetailsService: UserDetailsService): OncePerRequestFilter() {



    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        var jwt: String? = null
        var username: String? = null
        var userDetails : UserDetails? = null
        var auth: UsernamePasswordAuthenticationToken? = null

        try{
            var headerAuth = request.getHeader("Authorization")
            if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
                jwt = headerAuth.substring(7)
            }

            if (jwt != null) {
                try {
                    username = jwtCore.getNameFromJwt(jwt)
                }catch (e: ExpiredJwtException ){
                    // TODO
                }
                if (username != null && SecurityContextHolder.getContext().authentication == null) {
                    userDetails = userDetailsService.loadUserByUsername(username)
                    auth = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    SecurityContextHolder.getContext().authentication = auth
                }



            }

        }catch(e:Exception){
            //TODO
        }
        filterChain.doFilter(request, response)




    }
}