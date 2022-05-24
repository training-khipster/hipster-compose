package training.hipster.compose.config

import io.mongock.runner.springboot.EnableMongock
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.core.convert.converter.Converter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import tech.jhipster.config.JHipsterConstants
import tech.jhipster.domain.util.JSR310DateConverters.DateToZonedDateTimeConverter
import tech.jhipster.domain.util.JSR310DateConverters.ZonedDateTimeToDateConverter

@Configuration
@EnableMongock
@EnableReactiveMongoRepositories("training.hipster.compose.repository")
@Profile("!" + JHipsterConstants.SPRING_PROFILE_CLOUD)
@Import(value = [MongoAutoConfiguration::class, MongoReactiveAutoConfiguration::class])
class DatabaseConfiguration {

    @Bean
    fun validatingMongoEventListener() = ValidatingMongoEventListener(validator())

    @Bean
    fun validator() = LocalValidatorFactoryBean()

    @Bean
    fun customConversions() =
        MongoCustomConversions(
            mutableListOf<Converter<*, *>>(
                DateToZonedDateTimeConverter.INSTANCE,
                ZonedDateTimeToDateConverter.INSTANCE
            )
        )
}
