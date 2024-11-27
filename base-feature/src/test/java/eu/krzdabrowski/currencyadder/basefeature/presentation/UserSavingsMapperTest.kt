package eu.krzdabrowski.currencyadder.basefeature.presentation

import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.mapper.toDomainModel
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.mapper.toPresentationModel
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.model.UserSavingDisplayable
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class UserSavingsMapperTest {

    @Test
    fun `should map domain to presentation model with formatted rounded amount`() {
        // Given
        val domainModel = UserSaving(
            id = 1,
            position = 0,
            place = "home",
            amount = 123.456789,
            currency = "EUR",
        )

        // When
        val presentationModel = domainModel.toPresentationModel()

        // Then
        assertEquals(
            expected = UserSavingDisplayable(
                id = 1,
                position = 0,
                place = "home",
                amount = "123.46",
                currency = "EUR",
                currencyPossibilities = emptyList(),
            ),
            actual = presentationModel,
        )
    }

    @Test
    fun `should map domain to presentation model with no fractional digits in amount if zeroes`() {
        // Given
        val domainModel = UserSaving(
            id = 2,
            position = 1,
            place = "bank",
            amount = 100.0,
            currency = "GBP",
        )

        // When
        val presentationModel = domainModel.toPresentationModel()

        // Then
        assertEquals(
            expected = UserSavingDisplayable(
                id = 2,
                position = 1,
                place = "bank",
                amount = "100",
                currency = "GBP",
                currencyPossibilities = emptyList(),
            ),
            actual = presentationModel,
        )
    }

    @Test
    fun `should map domain to presentation model with empty amount if zero`() {
        // Given
        val domainModel = UserSaving(
            id = 3,
            position = 2,
            place = "nowhere",
            amount = 0.0,
            currency = "USD",
        )

        // When
        val presentationModel = domainModel.toPresentationModel()

        // Then
        assertEquals(
            expected = UserSavingDisplayable(
                id = 3,
                position = 2,
                place = "nowhere",
                amount = "",
                currency = "USD",
                currencyPossibilities = emptyList(),
            ),
            actual = presentationModel,
        )
    }

    @Test
    fun `should map presentation to domain model with correct amount`() {
        // Given
        val presentationModel = UserSavingDisplayable(
            id = 4,
            position = 3,
            place = "mattress",
            amount = "567.89",
            currency = "CHF",
            currencyPossibilities = emptyList(),
        )

        // When
        val domainModel = presentationModel.toDomainModel()

        // Then
        assertEquals(
            expected = UserSaving(
                id = 4,
                position = 3,
                place = "mattress",
                amount = 567.89,
                currency = "CHF",
            ),
            actual = domainModel,
        )
    }

    @Test
    fun `should map presentation to domain model with zero amount if incorrect format`() {
        // Given
        val presentationModel = UserSavingDisplayable(
            id = 5,
            position = 4,
            place = "family",
            amount = "987,65",
            currency = "HUF",
            currencyPossibilities = emptyList(),
        )

        // When
        val domainModel = presentationModel.toDomainModel()

        // Then
        assertEquals(
            expected = UserSaving(
                id = 5,
                position = 4,
                place = "family",
                amount = 0.0,
                currency = "HUF",
            ),
            actual = domainModel,
        )
    }
}
