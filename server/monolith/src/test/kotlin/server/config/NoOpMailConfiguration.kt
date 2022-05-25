package server.config

import org.mockito.Mockito
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import server.domain.User
import server.service.MailService

@Configuration
class NoOpMailConfiguration {
    private val mockMailService = Mockito.mock(MailService::class.java)

    @Bean
    fun mailService(): MailService {
        return mockMailService
    }

    init {
        Mockito.doNothing().`when`(mockMailService).sendActivationEmail(User())
    }
}
