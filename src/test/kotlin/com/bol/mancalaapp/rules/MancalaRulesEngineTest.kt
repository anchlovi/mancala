package com.bol.mancalaapp.rules

import com.bol.mancalaapp.rules.validators.PlayerMoveValidator
import com.bol.mancalaapp.rules.validators.ValidationException
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.mock
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever

class MancalaRulesEngineTest {

    private lateinit var rule1: GameRule
    private lateinit var rule2: GameRule
    private lateinit var validator1: PlayerMoveValidator
    private lateinit var validator2: PlayerMoveValidator
    private lateinit var ctx1: GameContext
    private lateinit var ctx2: GameContext
    private lateinit var ctx3: GameContext

    private lateinit var rulesEngine: MancalaRulesEngine

    @BeforeEach
    fun setUp() {
        rule1 = mock<GameRule>()
        rule2 = mock<GameRule>()

        validator1 = mock<PlayerMoveValidator>()
        validator2 = mock<PlayerMoveValidator>()

        ctx1 = mock<GameContext>()
        ctx2 = mock<GameContext>()
        ctx3 = mock<GameContext>()

        whenever(rule1.apply(ctx1)).thenReturn(ctx2)
        whenever(rule2.apply(ctx2)).thenReturn(ctx3)

        rulesEngine = MancalaRulesEngine(listOf(rule1, rule2), listOf(validator1, validator2))
    }

    @Test
    fun `should apply rules and validators in correct order`() {
        val res = rulesEngine.apply(ctx1)

        inOrder(validator1, validator2, rule1, rule2) {
            verify(validator1).validate(ctx1)
            verify(validator2).validate(ctx1)
            verify(rule1).apply(ctx1)
            verify(rule2).apply(ctx2)
        }

        assertSame(ctx3, res)
    }

    @Test
    fun `should fail if one of the validators fails`() {
        whenever(validator2.validate(ctx1)).thenThrow(ValidationException("test"))

        assertThrows<ValidationException> { rulesEngine.apply(ctx1) }

        verifyNoInteractions(rule1, rule2)
    }
}