package engine

import engine.AppUserAdapter
import engine.AppUserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AppUserDetailsImpl(private val repository: AppUserRepository) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {
        val user = repository.findAppUserByEmail(email)
            ?: throw UsernameNotFoundException("Not found")

        return AppUserAdapter(user)
    }
}