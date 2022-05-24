package training.hipster.compose.config.dbmigrations

import io.mongock.api.annotations.ChangeUnit
import io.mongock.api.annotations.Execution
import io.mongock.api.annotations.RollbackExecution
import org.springframework.data.mongodb.core.MongoTemplate
import training.hipster.compose.config.SYSTEM_ACCOUNT
import training.hipster.compose.domain.Authority
import training.hipster.compose.domain.User
import training.hipster.compose.security.ADMIN
import training.hipster.compose.security.USER
import java.time.Instant

/**
 * Creates the initial database setup.
 */
@ChangeUnit(id = "users-initialization", order = "001")
class InitialSetupMigration(private val template: MongoTemplate) {

    @Execution
    fun changeSet() {
        var userAuthority = createUserAuthority()
        userAuthority = template.save(userAuthority)
        var adminAuthority = createAdminAuthority()
        adminAuthority = template.save(adminAuthority)
        addUsers(userAuthority, adminAuthority)
    }

    @RollbackExecution
    fun rollback() { }

    fun createAuthority(authority: String): Authority {
        var adminAuthority = Authority()
        adminAuthority.name = authority
        return adminAuthority
    }

    private fun createAdminAuthority() = createAuthority(ADMIN)

    private fun createUserAuthority() = createAuthority(USER)

    private fun addUsers(userAuthority: Authority, adminAuthority: Authority) {
        val user = createUser(userAuthority)
        template.save(user)
        val admin = createAdmin(adminAuthority, userAuthority)
        template.save(admin)
    }

    private fun createUser(userAuthority: Authority): User {
        val userUser = User(
            id = "user-2",
            login = "user",
            password = "\$2a\$10\$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K",
            firstName = "",
            lastName = "User",
            email = "user@localhost",
            activated = true,
            langKey = "fr",
            createdBy = SYSTEM_ACCOUNT,
            createdDate = Instant.now(),
            authorities = mutableSetOf(userAuthority)
        )
        return userUser
    }

    private fun createAdmin(adminAuthority: Authority, userAuthority: Authority): User {
        val adminUser = User(
            id = "user-1",
            login = "admin",
            password = "\$2a\$10\$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC",
            firstName = "admin",
            lastName = "Administrator",
            email = "admin@localhost",
            activated = true,
            langKey = "fr",
            createdBy = SYSTEM_ACCOUNT,
            createdDate = Instant.now(),
            authorities = mutableSetOf(adminAuthority, userAuthority)
        )
        return adminUser
    }
}
