package com.micmr0.runassistant.util

import android.content.Context
import android.util.Log

class Logger {
    companion object {
        fun d(pAny: Any, pLogInfo: String) {
            Log.d(pAny.javaClass.simpleName, pLogInfo)
        }

        fun i(pAny: Any, pLogInfo: String) {
            Log.d(pAny.javaClass.simpleName, pLogInfo)
        }

        fun e(pAny: Any, pLogInfo: String) {
            Log.d(pAny.javaClass.simpleName, pLogInfo)
        }

        fun v(pAny: Any, pLogInfo: String) {
            Log.d(pAny.javaClass.simpleName, pLogInfo)
        }
    }
}