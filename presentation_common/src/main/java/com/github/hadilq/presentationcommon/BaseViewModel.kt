package com.github.hadilq.presentationcommon

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {
    private val disposables = CompositeDisposable()

    private fun Disposable.track() = disposables.add(this)

    protected fun <T> Flowable<T>.observe() {
        this
            .onErrorResumeNext { e: Throwable ->
                Log.e("BaseViewModel", "An error happened in ${this@BaseViewModel.javaClass.simpleName}", e)
                Flowable.empty<T>()
            }
            .subscribe()
            .track()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}