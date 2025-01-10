package ru.mikhail.lab4_backend.authenticate

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.mikhail.lab4_backend.dbobjects.User
import java.time.LocalDateTime

class UserDetailsImpl(
    private val id: Long,
    private val username: String,
    private val password: String,
) : UserDetails {

    companion object {
        fun build(user: User): UserDetailsImpl {
            return UserDetailsImpl(
                user.id,
                user.username,
                user.password,
            )
        }
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    fun getId(): Long{
        return id
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
