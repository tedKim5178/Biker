package com.mk.bikey

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class FragmentArgumentDelegate<T : Any> : ReadWriteProperty<Fragment, T> {

    var value: T? = null

    override operator fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        if (value == null) {
            val args = thisRef.arguments
                ?: throw IllegalStateException("Cannot read property ${property.name} if no arguments have been set")
            @Suppress("UNCHECKED_CAST")
            value = args.get(property.name) as T
        }
        return value ?: throw IllegalStateException("Property ${property.name} could not be read")
    }

    override operator fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        if (null == thisRef.arguments) {
            thisRef.arguments = Bundle()
        }

        thisRef.arguments?.putAll(bundleOf(property.name to value))
    }
}
