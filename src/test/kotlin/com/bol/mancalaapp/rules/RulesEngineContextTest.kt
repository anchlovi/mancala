package com.bol.mancalaapp.rules

import com.bol.mancalaapp.configuration.DefaultTestConfiguration
import com.bol.mancalaapp.rules.validators.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration


@SpringBootTest
@ContextConfiguration(classes = [DefaultTestConfiguration::class])
class RulesEngineContextTest {
    @Autowired
    private lateinit var rules: List<GameRule>

    @Autowired
    private lateinit var validators: List<PlayerMoveValidator>

    @Test
    fun `tests rules should be in order` () {
        val expectedRulesOrder = listOf(
            DistributeStonesGameRule::class.java,
            CaptureStonesGameRule::class.java,
            GameOverGameRule::class.java,
            DetermineNextPlayerGameRule::class.java
        )

        assertEquals(expectedRulesOrder, rules.map { it::class.java })
    }

    @Test
    fun `all validators should be injected` () {
        val expectedValidators = listOf(
            EmptyPitValidator::class.java,
            PitIsMancalaValidator::class.java,
            PitBelongsToPlayerValidator::class.java,
            PitIsValidValidator::class.java,
            GameOverValidator::class.java,
        ).sortedBy { it.simpleName }

        val actualValidators = validators.map { it::class.java }.sortedBy { it.simpleName }

        assertIterableEquals(expectedValidators, actualValidators)
    }
}