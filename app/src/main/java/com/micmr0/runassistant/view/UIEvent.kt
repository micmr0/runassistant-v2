package com.micmr0.runassistant.view

class UIEvent(pEventType: EventType) {
    val mEventType = pEventType


    enum class EventType {
        START_LOGGING, PAUSE_LOGGING, RESUME_LOGGING, STOP_LOGGING
    }
}

