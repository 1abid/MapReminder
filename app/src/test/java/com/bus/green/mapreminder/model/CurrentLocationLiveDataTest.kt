package com.bus.green.mapreminder.model


import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.bus.green.mapreminder.InstantTaskExecutorExtension
import com.bus.green.mapreminder.location.LocationProvider
import com.nhaarman.mockito_kotlin.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
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

    @Nested
    @DisplayName("Given a CurrentLocationLiveData which is not active")
    inner class InActive{

        @BeforeEach
        fun setUp(){
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        }

        @Nested
        @DisplayName("When the location is updated")
        inner class NeverActive{

            @BeforeEach
            fun  setUp(){
                testLocationProvider.trigger()
            }

            @Test
            @DisplayName("We never receive a location callback")
            fun noCallBack(){
                verify(observer, never()).onChanged(any())
            }
        }
    }

    @Nested
    @DisplayName("Given a CurrentLocationLiveData which is active")
    inner class Active{

        @BeforeEach
        fun setUp(){
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        }


        @Nested
        @DisplayName("When the location is updated")
        inner class Activated{
            @BeforeEach
            fun setUp(){
                testLocationProvider.trigger()
            }

            @Test
            @DisplayName("then we receive a location update")
            fun receiveACallBack(){
                verify(observer, times(1)).onChanged(any())
            }
        }
    }


    @Nested
    @DisplayName("Given a CurrentLocationLiveData which was active")
    inner class WasActive{

        @BeforeEach
        fun setUp(){
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        }


        @Nested
        @DisplayName("When the location is updated")
        inner class Activated{
            @BeforeEach
            fun setUp(){
                testLocationProvider.trigger()
            }

            @Test
            @DisplayName("then we receive no location update")
            fun noCallBack(){
                verify(observer, never()).onChanged(any())
            }
        }


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