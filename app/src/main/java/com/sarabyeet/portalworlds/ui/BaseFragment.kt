package com.sarabyeet.portalworlds.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.crashlytics.FirebaseCrashlytics

abstract class BaseFragment:Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseCrashlytics.getInstance().log(
            this.javaClass.simpleName
        )
    }
}