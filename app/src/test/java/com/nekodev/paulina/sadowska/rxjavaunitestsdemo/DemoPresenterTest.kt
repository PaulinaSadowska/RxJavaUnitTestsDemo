package com.nekodev.paulina.sadowska.rxjavaunitestsdemo

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import org.mockito.BDDMockito.*
import org.mockito.Mockito.mock
import java.util.concurrent.TimeUnit


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DemoPresenterTest {

    private var view = mock(DemoView::class.java)
    private var service = mock(DemoService::class.java)
    private var schedulerProvider = TrampolineSchedulerProvider()
    private val testScheduler = TestScheduler()
    private var testSchedulerProvider = TestSchedulerProvider(testScheduler)

    @Test
    fun simpleTestExample_wrong() {
        //given
        val presenter = DemoPresenter(schedulerProvider, view, service)
        given(service.getSomeRemoteData()).willReturn(Single.just(5))

        //when
        presenter.getSomeDataBad()

        //then
        then(view).should().showData(5)
    }

    @Test
    fun simpleTestExample() {
        //given
        val presenter = DemoPresenter(schedulerProvider, view, service)
        given(service.getSomeRemoteData()).willReturn(Single.just(5))

        //when
        presenter.getSomeData()

        //then
        then(view).should().showData(5)
    }

    @Test
    fun delayTestExample() {
        //given
        val presenter = DemoPresenter(testSchedulerProvider, view, service)
        given(service.getSomeRemoteData()).willReturn(Single.just(5))
        val delayInMillis = 1000L

        //when
        presenter.getSomeDataWithDelay(delayInMillis)

        //then
        then(view).should(never()).showData(anyInt())
        testScheduler.advanceTimeBy(delayInMillis, TimeUnit.MILLISECONDS)
        then(view).should().showData(5)
    }

    @Test
    fun timeoutTestExample() {
        //given
        val presenter = DemoPresenter(testSchedulerProvider, view, service)
        given(service.getSomeRemoteData()).willReturn(Single.just(5).delay(3, TimeUnit.SECONDS, testScheduler))

        //when
        presenter.getSomeDataWithTimeout()

        //then
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        then(view).should().showError()
    }

    @Test
    fun testSubscriberTestExample() {
        //given
        val observable = Observable.just(6).delay(1, TimeUnit.SECONDS, testScheduler)

        val testObserver = TestObserver<Int>()

        //when
        observable.subscribe(testObserver)

        //then
        testScheduler.advanceTimeBy(950, TimeUnit.MILLISECONDS)
        testObserver.assertNotTerminated()
        testScheduler.advanceTimeBy(60, TimeUnit.MILLISECONDS)
        testObserver.assertValue(6)
        testObserver.assertComplete()
    }
}
