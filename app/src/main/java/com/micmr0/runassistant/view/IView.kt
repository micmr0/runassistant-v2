package com.micmr0.runassistant.view

interface IView<T> {
    fun onGetData(pData : T)
}