package com.example.pogo_framework.ui

import android.content.Context
import android.view.MotionEvent
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.GoogleMapOptions

import android.util.AttributeSet

class CustomMapView : MapView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, options: GoogleMapOptions?) : super(context, options)


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(ev)
    }
}