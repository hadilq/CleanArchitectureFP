package com.github.hadilq.presentationcommon

import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseActivity : DaggerAppCompatActivity() {

    private val disposables = CompositeDisposable()
    private var snackbar: Snackbar? = null

    protected fun Disposable.track() = disposables.add(this)

    protected fun <T> Flowable<T>.observe(o: (T) -> Unit) {
        RxLifecycleHandler(this@BaseActivity, this, o)
    }

    inline fun <reified T : ViewModel> viewModel(factory: ViewModelProvider.Factory, body: T.() -> Unit): T {
        val vm = ViewModelProviders.of(this, factory)[T::class.java]
        vm.body()
        return vm
    }

    protected fun showFailure(view: View, errorMessage: Int, retry: () -> Unit) {
        if (snackbar?.isShown == true) {
            snackbar!!.dismiss()
        }
        snackbar = Snackbar.make(view, errorMessage, Snackbar.LENGTH_INDEFINITE)
            .setActionTextColor(ContextCompat.getColor(this, R.color.white))
            .setAction(R.string.retry) {
                snackbar = null
                retry()
            }
        snackbar!!.show()
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}