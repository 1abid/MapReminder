package com.bus.green.mapreminder.location

import android.content.Context
import android.content.pm.PackageManager
import com.bus.green.mapreminder.model.CurrentLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString

class LocationProviderTest {

    private val context: Context = mock()
    private val fusedLocationProviderClient: FusedLocationProviderClient = mock()
    private val locationRequestCallBack1: ((currentLocation: CurrentLocation) -> Unit) = mock()
    private val locationRequestCallBack2: ((currentLocation: CurrentLocation) -> Unit) = mock()
    private val subscriberList = mock<MutableList<(currentLocation: CurrentLocation) -> Unit>>()
    private val locationProvider: LocationProvider = FusedLocationProvider(context, fusedLocationProviderClient)

    @BeforeEach
    fun setUp() {
        whenever(context.checkSelfPermission(anyString())).thenReturn(PackageManager.PERMISSION_GRANTED)
    }


    @Nested
    @DisplayName("permission not given")
    inner class WithoutPermission {

        @BeforeEach
        fun setUp() {
            whenever(context.checkSelfPermission(anyString())).thenReturn(PackageManager.PERMISSION_DENIED)
        }

        @Test
        @DisplayName("if we don't grant location permission fusedLocationProvider will not be invoked")
        fun doNotRequestLocationUpdate() {

            locationProvider.requestUpdate(locationRequestCallBack1)

            verify(fusedLocationProviderClient, never()).requestLocationUpdates(any(), any(), anyOrNull())

        }
    }

    @Nested
    @DisplayName("permission given")
    inner class WithPermission {

        @Test
        @DisplayName("if we grant location permission fusedLocationProvider will be invoked once for two requestCallback")
        fun doNotRequestLocationUpdate() {

            locationProvider.requestUpdate(locationRequestCallBack1)
            locationProvider.requestUpdate(locationRequestCallBack2)

            verify(fusedLocationProviderClient, times(1)).requestLocationUpdates(any(), any(), anyOrNull())

        }
    }

    @Nested
    @DisplayName("single subscriber invoke fusedLocationProvider once and after removing it subscriber list become empty")
    inner class SingleSubscriberTest {

        @BeforeEach
        fun setUp() {
            locationProvider.requestUpdate(locationRequestCallBack1)
        }

        @Test
        @DisplayName("fusedLocationProvider accept location update request once")
        fun giveLocationUpdateOnce() {
            verify(fusedLocationProviderClient, times(1)).requestLocationUpdates(any(), any(), anyOrNull())
        }

        @Test
        @DisplayName("removing one and only subscriber makes subscriber list empty")
        fun subscriberListIsEmpty() {

            locationProvider.cancelRequest(locationRequestCallBack1)
            subscriberList.add(locationRequestCallBack1)
            subscriberList.remove(locationRequestCallBack1)

            verify(subscriberList, atLeast(1)).add(locationRequestCallBack1)
            assertEquals(0, subscriberList.size)

        }
    }


    @Nested
    @DisplayName("multiple subscriber test")
    inner class MultipleSubscriber {

        @BeforeEach
        fun setUp() {
            locationProvider.requestUpdate(locationRequestCallBack1)
            locationProvider.requestUpdate(locationRequestCallBack2)
        }

        @Test
        @DisplayName("if unsubscribe one subscriber we will not invoke fusedLocationProviderClient.removeLocationUpdates")
        fun removeOneSubscriber() {
            locationProvider.cancelRequest(locationRequestCallBack1)

            verify(fusedLocationProviderClient, never()).removeLocationUpdates(any<LocationCallback>())
        }

        @Test
        @DisplayName("if we remove two subscriber then we invoke fusedLocationProviderClient.removeLocationUpdates once")
        fun removeBothSubscriber() {
            locationProvider.cancelRequest(locationRequestCallBack1)
            locationProvider.cancelRequest(locationRequestCallBack2)

            verify(fusedLocationProviderClient, times(1)).removeLocationUpdates(any<LocationCallback>())
        }


        @Test
        @DisplayName("if we remove same subscriber twice, fusedLocationProviderClient.removeLocationUpdates still won't be called for the remaining subscriber")
        fun doNotRemoveLocationUpdate() {
            locationProvider.cancelRequest(locationRequestCallBack1)
            locationProvider.cancelRequest(locationRequestCallBack1)

            verify(fusedLocationProviderClient, never()).removeLocationUpdates(any<LocationCallback>())
        }

    }


}