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

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable

class RxLifecycleHandler<T>(
    owner: LifecycleOwner,
    private val flowable: Flowable<T>,
    private val observer: (T) -> Unit
) : LifecycleObserver {
    private val lifecycle = owner.lifecycle
    private var disposable: Disposable? = null

    init {
        if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
            owner.lifecycle.addObserver(this)
            observeIfPossible()
        }
    }

    private fun observeIfPossible() {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            disposable ?: let {
                disposable = flowable.subscribe { data -> observer(data) }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        observeIfPossible()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        disposable?.dispose()
        disposable = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        onStop()
        lifecycle.removeObserver(this)
    }
}