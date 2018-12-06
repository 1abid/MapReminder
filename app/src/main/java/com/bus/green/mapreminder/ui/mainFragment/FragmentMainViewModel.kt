package com.bus.green.mapreminder.ui.mainFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bus.green.mapreminder.model.CurrentLocation
import javax.inject.Inject

class FragmentMainViewModel @Inject constructor(val currentLocation: LiveData<CurrentLocation>) : ViewModel()