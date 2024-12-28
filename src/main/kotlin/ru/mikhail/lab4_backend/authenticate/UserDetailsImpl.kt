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
    private var refreshTokenHash: String?,
    private var refreshTokenExpireTime: LocalDateTime?,
) : UserDetails {

    companion object {
        fun build(user: User): UserDetailsImpl {
            return UserDetailsImpl(
                user.id,
                user.username,
                user.password,
                user.refreshTokenHash,
                user.refreshTokenExpireTime
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

    fun getRefreshTokenHash(): String? {
        return refreshTokenHash
    }

    fun getRefreshTokenExpireTime(): LocalDateTime? {
        return refreshTokenExpireTime
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
