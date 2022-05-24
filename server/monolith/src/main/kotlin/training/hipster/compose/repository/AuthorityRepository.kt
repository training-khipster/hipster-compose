package training.hipster.compose.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import training.hipster.compose.domain.Authority

/**
 * Spring Data MongoDB repository for the [Authority] entity.
 */

interface AuthorityRepository : ReactiveMongoRepository<Authority, String>
