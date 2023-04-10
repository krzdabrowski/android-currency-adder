package eu.krzdabrowski.currencyadder.basefeature.data

import eu.krzdabrowski.currencyadder.basefeature.domain.model.ExchangeRate
import eu.krzdabrowski.currencyadder.basefeature.presentation.model.UserSavingDisplayable
import java.time.LocalDate

internal fun generateTestRocketsFromPresentation() = listOf(
    UserSavingDisplayable(
        id = "1",
        currencyCode = "test rocket",
        currencyRate = 10,
        firstFlightDate = "2022-09-25",
        heightInMeters = 20,
        weightInTonnes = 30,
        wikiUrl = "https://testrocket.com",
        imageUrl = "",
    ),
    UserSavingDisplayable(
        id = "2",
        currencyCode = "test rocket 2",
        currencyRate = 20,
        firstFlightDate = "2022-09-25",
        heightInMeters = 40,
        weightInTonnes = 50,
        wikiUrl = "https://testrocket.com",
        imageUrl = "",
    ),
    UserSavingDisplayable(
        id = "3",
        currencyCode = "test rocket 3",
        currencyRate = 30,
        firstFlightDate = "2022-09-25",
        heightInMeters = 60,
        weightInTonnes = 70,
        wikiUrl = "https://testrocket.com",
        imageUrl = "",
    ),
)

internal fun generateTestRocketsFromDomain() = listOf(
    ExchangeRate(
        id = "1",
        currencyCode = "test rocket",
        currencyRate = 10_000_000,
        firstFlight = LocalDate.parse("2022-09-25"),
        height = 20,
        weight = 30_000,
        wikiUrl = "https://testrocket.com",
        imageUrl = "https://testrocket.com/1.jpg",
    ),
    ExchangeRate(
        id = "2",
        currencyCode = "test rocket 2",
        currencyRate = 20_000_000,
        firstFlight = LocalDate.parse("2022-09-25"),
        height = 40,
        weight = 50_000,
        wikiUrl = "https://testrocket.com",
        imageUrl = "https://testrocket.com/2.jpg",
    ),
    ExchangeRate(
        id = "3",
        currencyCode = "test rocket 3",
        currencyRate = 30_000_000,
        firstFlight = LocalDate.parse("2022-09-25"),
        height = 60,
        weight = 70_000,
        wikiUrl = "https://testrocket.com",
        imageUrl = "https://testrocket.com/3.jpg",
    ),
)
