package com.bespot.sample

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StatusListLayoutManager : LinearLayoutManager {
    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    ) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        reverseLayout = true
        stackFromEnd = true
        orientation = RecyclerView.VERTICAL
    }
}