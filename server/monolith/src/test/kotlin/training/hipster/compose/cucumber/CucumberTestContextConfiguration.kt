package training.hipster.compose.cucumber

import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import training.hipster.compose.IntegrationTest

@CucumberContextConfiguration
@IntegrationTest
@WebAppConfiguration
class CucumberTestContextConfiguration
