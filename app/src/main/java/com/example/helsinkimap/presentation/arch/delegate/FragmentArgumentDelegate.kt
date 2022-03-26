package com.example.helsinkimap.presentation.arch.delegate

import android.os.Binder
import android.os.Bundle
import androidx.core.app.BundleCompat
import androidx.fragment.app.Fragment
import kotlin.reflect.KProperty

class FragmentArgumentDelegate(val argKey: String? = null) {

    var value: Any? = null

    // fixme use reified type parameter to avoid unchecked cast after
    // https://youtrack.jetbrains.com/issue/KT-45032 will be fixed
    @Suppress("UNCHECKED_CAST")
    operator fun <T> getValue(thisRef: Fragment, property: KProperty<*>): T {
        val result = value ?: thisRef.arguments?.get(argKey ?: property.name)?.also { value = it }
        return when {
            result != null -> result as T
            // null is T -> null as T
            else -> null as T
        }
    }

    operator fun <T> setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        this.value = value
        val args = thisRef.arguments ?: Bundle().also { thisRef.arguments = it }
        val key = argKey ?: property.name

        when (value) {
            null -> args.remove(key)
            is String -> args.putString(key, value)
            is Int -> args.putInt(key, value)
            is Short -> args.putShort(key, value)
            is Long -> args.putLong(key, value)
            is Byte -> args.putByte(key, value)
            is ByteArray -> args.putByteArray(key, value)
            is Char -> args.putChar(key, value)
            is CharArray -> args.putCharArray(key, value)
            is CharSequence -> args.putCharSequence(key, value)
            is Float -> args.putFloat(key, value)
            is Bundle -> args.putBundle(key, value)
            is Binder -> BundleCompat.putBinder(args, key, value)
            is android.os.Parcelable -> args.putParcelable(key, value)
            is java.io.Serializable -> args.putSerializable(key, value)
            else -> throw IllegalStateException("Type of property $key is not supported")
        }
    }
}
