package com.sarabyeet.portalworlds

import android.app.Application
import android.content.Context

class PortalWorldsApplication: Application() {
    companion object {
        lateinit var context: Context
    }


    override fun onCreate() {
        super.onCreate()
        context = this
    }
}