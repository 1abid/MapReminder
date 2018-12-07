package com.bus.green.mapreminder.di

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bus.green.mapreminder.model.CurrentLocation
import com.bus.green.mapreminder.model.CurrentLocationLiveData
import com.bus.green.mapreminder.ui.CurrentLocationViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass


@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {


    @Binds
    abstract fun bindLivaData(currentLocationLiveData: CurrentLocationLiveData) : LiveData<CurrentLocation>

    @Binds
    @IntoMap
    @ViewModelKey(CurrentLocationViewModel::class)
    abstract fun bindViewModel(currentLocationViewModel: CurrentLocationViewModel) : ViewModel


    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}