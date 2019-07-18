/***
 * Copyright 2019 Hadi Lashkari Ghouchani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * */
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