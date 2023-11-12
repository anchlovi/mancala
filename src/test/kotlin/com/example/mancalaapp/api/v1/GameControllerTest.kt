package com.example.mancalaapp.api.v1

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(GameController::class)
@AutoConfigureWebClient
@ActiveProfiles("test")
class GameControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `create should return 201 when a new game is created`() {
        mockMvc.post("/games")
            .andExpect {
                status { isCreated() }
            }
    }
}