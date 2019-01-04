package com.bus.green.mapreminder.model


import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.bus.green.mapreminder.InstantTaskExecutorExtension
import com.bus.green.mapreminder.location.LocationProvider
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(InstantTaskExecutorExtension::class)
class CurrentLocationLiveDataTest {

    private val lifeCycleOwner: LifecycleOwner = mock()
    private val lifecycleRegistry = LifecycleRegistry(lifeCycleOwner)
    private val observer: Observer<CurrentLocation> = mock()

    private val testLocationProvider =  TestLocationProvider()

    private val currentLocationLiveData = CurrentLocationLiveData(testLocationProvider)


    @BeforeEach
    fun setUp(){
        whenever(lifeCycleOwner.lifecycle).thenReturn(lifecycleRegistry)
        currentLocationLiveData.observe(lifeCycleOwner, observer)
    }


}


private class TestLocationProvider: LocationProvider {

    private var location: ((currentLocation: CurrentLocation)-> Unit)? = null

    override fun requestUpdate(callback: (currentLocation: CurrentLocation) -> Unit) {
        location = callback
    }

    override fun cancelRequest(callback: (currentLocation: CurrentLocation) -> Unit) {
        location = null
    }

    fun trigger(){
        location?.invoke(CurrentLocation(1.0, 1.0))
    }

}