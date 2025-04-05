package engine

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AppUserAdapter(private val user: AppUser) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority("ROLE_USER"))

    override fun getPassword(): String {
        val password = requireNotNull(user.password)
        require(password.length >= 5)
        return password
    }
//    fun getEmail(): String {
//        val email = requireNotNull(user.email)
//        require(email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\\.(.+)$")))
//        return email
//    }
    override fun getUsername(): String = requireNotNull(user.email)
//    override fun getUsername(): String {
//        val email = requireNotNull(user.email)
//        require(email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\\.(.+)$")))
//        return email
//    }

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true


}