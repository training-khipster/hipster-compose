package server.web.rest

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.reactive.server.WebTestClient
import server.IntegrationTest
import server.config.SYSTEM_ACCOUNT
import server.domain.User
import server.repository.UserRepository
import server.web.rest.vm.LoginVM

/**
 * Integration tests for the [UserJWTController] REST controller.
 */
@AutoConfigureWebTestClient
@IntegrationTest
class UserJWTControllerIT {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    @Throws(Exception::class)
    fun testAuthorize() {
        val user = User(
            login = "user-jwt-controller",
            email = "user-jwt-controller@example.com",
            activated = true,
            createdBy = SYSTEM_ACCOUNT,
            password = passwordEncoder.encode("test")
        )

        userRepository.save(user).block()

        val login = LoginVM(username = "user-jwt-controller", password = "test")
        webTestClient.post().uri("/api/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(convertObjectToJsonBytes(login))
            .exchange()
            .expectStatus().isOk
            .expectHeader().valueMatches("Authorization", "Bearer .+")
            .expectBody()
            .jsonPath("\$.id_token").isNotEmpty
    }

    @Test
    @Throws(Exception::class)
    fun testAuthorizeWithRememberMe() {
        val user = User(
            login = "user-jwt-controller-remember-me",
            email = "user-jwt-controller-remember-me@example.com",
            activated = true,
            createdBy = SYSTEM_ACCOUNT,
            password = passwordEncoder.encode("test")
        )

        userRepository.save(user).block()

        val login = LoginVM(
            username = "user-jwt-controller-remember-me",
            password = "test",
            isRememberMe = true
        )
        webTestClient.post().uri("/api/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(convertObjectToJsonBytes(login))
            .exchange()
            .expectStatus().isOk
            .expectHeader().valueMatches("Authorization", "Bearer .+")
            .expectBody()
            .jsonPath("\$.id_token").isNotEmpty
    }

    @Test
    @Throws(Exception::class)
    fun testAuthorizeFails() {
        val login = LoginVM(username = "wrong-user", password = "wrong password")
        webTestClient.post().uri("/api/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(convertObjectToJsonBytes(login))
            .exchange()
            .expectStatus().isUnauthorized
            .expectHeader().doesNotExist("Authorization")
            .expectBody()
            .jsonPath("\$.id_token").doesNotExist()
    }
}
