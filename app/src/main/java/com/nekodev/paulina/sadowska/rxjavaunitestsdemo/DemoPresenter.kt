package com.nekodev.paulina.sadowska.rxjavaunitestsdemo

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by Paulina Sadowska on 16.05.2018.
 */
class DemoPresenter(private val schedulerProvider: BaseSchedulerProvider,
                    private val view: DemoView,
                    private val service: DemoService) {

    fun getSomeDataBad() {
        service.getSomeRemoteData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { view.showData(it) },
                        onError = { view.showError() }
                )
    }

    fun getSomeData() {
        service.getSomeRemoteData()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeBy(
                        onSuccess = { view.showData(it) },
                        onError = { view.showError() }
                )
    }

    fun getSomeDataWithDelay(delayInMillis: Long) {
        service.getSomeRemoteData()
                .delay(delayInMillis, TimeUnit.MILLISECONDS, schedulerProvider.io())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeBy(
                        onSuccess = { view.showData(it) },
                        onError = { view.showError() }
                )
    }

    fun getSomeDataWithTimeout() {
        service.getSomeRemoteData()
                .subscribeOn(schedulerProvider.io())
                .timeout(1, TimeUnit.SECONDS, schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeBy(
                        onSuccess = { view.showData(it) },
                        onError = { view.showError() }
                )
    }

}