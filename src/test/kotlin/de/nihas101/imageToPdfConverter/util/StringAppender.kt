package de.nihas101.imageToPdfConverter.util

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.UnsynchronizedAppenderBase

class StringAppender : UnsynchronizedAppenderBase<ILoggingEvent>() {
    var lastMessage = ""

    override fun append(eventObject: ILoggingEvent?) {
        lastMessage = eventObject!!.formattedMessage
    }
}