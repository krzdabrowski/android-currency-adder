package eu.krzdabrowski.currencyadder.core.presentation.mvi

interface EventDelegate<EVENT> {
    suspend fun publishEvent(event: EVENT)
}
