package com.github.kttinunf.forge.transformer

/**
 * Created by Kittinun Vantasin on 8/23/15.
 */

public interface Transformer<T> {

    public fun transform(value: Any) : T

}