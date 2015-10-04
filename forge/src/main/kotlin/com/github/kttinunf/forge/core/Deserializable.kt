package com.github.kttinunf.forge.core

/**
 * Created by Kittinun Vantasin on 8/20/15.
 */

public interface Deserializable<out T: Any> {

    val deserializer: (JSON) -> Result<T>

}
