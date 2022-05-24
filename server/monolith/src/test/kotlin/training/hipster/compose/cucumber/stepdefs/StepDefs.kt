package training.hipster.compose.cucumber.stepdefs

import org.springframework.test.web.reactive.server.WebTestClient

abstract class StepDefs {
    protected var actions: WebTestClient.ResponseSpec? = null
}
