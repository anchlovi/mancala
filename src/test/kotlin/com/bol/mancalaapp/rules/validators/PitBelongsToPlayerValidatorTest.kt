package com.bol.mancalaapp.rules.validators

import com.bol.mancalaapp.domain.Player
import com.bol.mancalaapp.helpers.GamesHelper
import com.bol.mancalaapp.rules.GameContext
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PitBelongsToPlayerValidatorTest {

    private lateinit var gameContext: GameContext

    @BeforeEach
    fun setUp() {
        gameContext = GamesHelper.newGameContext()
        gameContext = gameContext.copy(game = gameContext.game.copy(currentPlayer = Player.PLAYER1))
    }

    @Test
    fun `should not throw exception when pit belongs to player`() {
        val pitBelongsToPlayerIndex = 2

        assertDoesNotThrow { PitBelongsToPlayerValidator.validate(gameContext.copy(pitIdx = pitBelongsToPlayerIndex)) }
    }

    @Test
    fun `should throw exception when pit does not belong to player`() {
        val pitDoesNotBelongToPlayerIndex = 8

        assertThrows<PitDoesNotBelongToPlayerException> {
            PitBelongsToPlayerValidator.validate(gameContext.copy(pitIdx = pitDoesNotBelongToPlayerIndex))
        }
    }
}