package com.yandexgallery.yandexgallery

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet

class GridElementLayout : ConstraintLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, i: Int) : super(context, attrs, i)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}