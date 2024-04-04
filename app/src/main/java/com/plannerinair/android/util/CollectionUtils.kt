package com.plannerinair.android.util

fun <T> setOfNotNull(vararg values: T): Set<T> = HashSet<T>().apply {
    values.filterNotTo(this) { it != null }
}

fun String.nullIfBlank() = if (isBlank()) null else this
