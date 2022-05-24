package training.hipster.compose.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import tech.jhipster.config.JHipsterConstants
import training.hipster.compose.aop.logging.LoggingAspect

@Configuration
@EnableAspectJAutoProxy
class LoggingAspectConfiguration {

    @Bean
    @Profile(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)
    fun loggingAspect(env: Environment) = LoggingAspect(env)
}
