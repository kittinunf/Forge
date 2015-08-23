package com.github.kttinunf.forge.core

import com.sun.org.apache.xpath.internal.operations.Bool

/**
 * Created by Kittinun Vantasin on 8/20/15.
 */

public interface Deserializable<T> {

    public fun deserialize(json: JSON): T?

}
