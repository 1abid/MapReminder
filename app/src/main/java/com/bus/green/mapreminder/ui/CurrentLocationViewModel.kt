package com.bus.green.mapreminder.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bus.green.mapreminder.model.CurrentLocation
import javax.inject.Inject

class CurrentLocationViewModel @Inject constructor(val currentLocation: LiveData<CurrentLocation>) : ViewModel()