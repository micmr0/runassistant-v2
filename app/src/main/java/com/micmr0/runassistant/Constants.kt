package com.micmr0.runassistant

class Constants {
    class IntentExtras {
        companion object {
            const val TRAINING_ID = "com.micmr0.runassistant.Constants.training_id"
            const val PLAN_ID = "com.micmr0.runassistant.Constants.plan_id"
            const val DAY = "com.micmr0.runassistant.Constants.day"
            const val LOCATION = "com.micmr0.runassistant.Constants.location"
            const val LAST_LOCATION_NAME = "com.micmr0.runassistant.Constants.last_location_name"
            const val WEATHER_LOCATION_NAME = "com.micmr0.runassistant.Constants.weather_location_name"

        }
    }

    class RequestCodes {
        companion object {
            const val DEFAULT = 100
            const val DELETE_PLAN = 101
            const val TRAINING_SET = 102
        }
    }

    class ResultCodes {
        companion object {
            const val DELETE_PLAN = 100
            const val DELETE_TRAINING = 101
            const val TRAINING_SET = 102
        }
    }

    class Uri {
        companion object {
            const val INITIALIZE_TRAINING = "initialize_training"
            const val DELETE_TRAINING = "delete_training"
        }
    }
}