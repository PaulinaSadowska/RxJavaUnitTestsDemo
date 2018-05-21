package com.nekodev.paulina.sadowska.rxjavaunitestsdemo

import io.reactivex.Single

/**
 * Created by Paulina Sadowska on 16.05.2018.
 */
interface DemoService {

    fun getSomeRemoteData(): Single<Int>
}