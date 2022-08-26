package com.example.weatherforecast.view.currentforecast

import android.util.Log
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import com.example.weatherforecast.MainViewModel
import com.example.weatherforecast.contract.MainContract
import com.example.weatherforecast.contract.MainContract.View
import com.example.weatherforecast.model.Model

class CurrentForecastPresenter(_view: View?) : MainContract.Presenter {

    @Nullable
    private var view = _view
    private val model = Model()

//    private val viewModel = MainViewModel()
//
//    override fun getCurrentForecast(lifecycleOwner: LifecycleOwner) {
//        viewModel.currentDayForecastLiveData.observe(lifecycleOwner) {
//            Log.d("testLogs", "getCurrentForecast: $it")
//            view?.showCurrentForecast(it)
//        }
//    }

    override fun onPermissionListenerCallback(callbackResult: Boolean) {
        if (callbackResult) {
            view?.showPermissionGranted()
        } else {
            view?.showPermissionDenied()
        }
    }

    override fun startPermissionLauncher() {
        if (view?.isPermissionGranted() == true) {
            //write code with granted permission
        } else {
            view?.showPermission()
        }
    }

    override fun onDetachView() {
        view = null
    }

}