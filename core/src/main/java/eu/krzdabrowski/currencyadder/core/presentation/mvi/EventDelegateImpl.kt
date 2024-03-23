package eu.krzdabrowski.currencyadder.core.presentation.mvi

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class EventDelegateImpl<EVENT> : EventDelegate<EVENT> {

    private val eventChannel = Channel<EVENT>(Channel.BUFFERED)
    val event = eventChannel.receiveAsFlow()

    override suspend fun publishEvent(event: EVENT) {
        eventChannel.send(event)
    }
}
