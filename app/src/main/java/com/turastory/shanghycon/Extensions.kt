package com.turastory.shanghycon

import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by tura on 2018-09-15.
 */

fun ViewGroup.inflate(layoutId: Int) =
    LayoutInflater.from(this.context).inflate(layoutId, this, false)
