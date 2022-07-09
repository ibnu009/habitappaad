package com.dicoding.habitapp.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.dicoding.habitapp.R
import com.dicoding.habitapp.ui.random.RandomHabitAdapter

fun String.priorityToColour(context: Context): Drawable?{
    return when {
        this == "High" -> ContextCompat.getDrawable(context, R.drawable.ic_priority_high)
        this == "Medium" -> ContextCompat.getDrawable(context, R.drawable.ic_priority_medium)
        else -> ContextCompat.getDrawable(context, R.drawable.ic_priority_low)
    }
}

fun RandomHabitAdapter.PageType.pageTypeToColour(context: Context): Drawable? {
    return when {
        this == RandomHabitAdapter.PageType.HIGH -> ContextCompat.getDrawable(context, R.drawable.ic_priority_high)
        this == RandomHabitAdapter.PageType.MEDIUM -> ContextCompat.getDrawable(context, R.drawable.ic_priority_medium)
        else -> ContextCompat.getDrawable(context, R.drawable.ic_priority_low)
    }
}