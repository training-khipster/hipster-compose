package training.hipster.compose.repository

import org.springframework.data.domain.*
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import training.hipster.compose.domain.User
import java.time.Instant

/**
 * Spring Data MongoDB repository for the {@link User} entity.
 */
@Repository
interface UserRepository : ReactiveMongoRepository<User, String> {

    fun findOneByActivationKey(activationKey: String): Mono<User>

    fun findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(dateTime: Instant): Flux<User>

    fun findOneByResetKey(resetKey: String): Mono<User>

    fun findOneByEmailIgnoreCase(email: String?): Mono<User>

    fun findOneByLogin(login: String): Mono<User>

    fun findAllByIdNotNull(pageable: Pageable): Flux<User>

    fun findAllByIdNotNullAndActivatedIsTrue(pageable: Pageable): Flux<User>

    override fun count(): Mono<Long>
}
