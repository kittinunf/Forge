package com.github.kittinunf.forge.util

fun <A, X> Function1<A, X>.curry(): (A) -> X {
    return { a -> invoke(a) }
}

val <A, X> Function1<A, X>.create: (A) -> (X)
    get() = curry()

fun <A, B, X> Function2<A, B, X>.curry(): (A) -> (B) -> X {
    return { a ->
        { b -> invoke(a, b) }
    }
}

val <A, B, X> Function2<A, B, X>.create: (A) -> (B) -> (X)
    get() = curry()

fun <A, B, C, X> Function3<A, B, C, X>.curry(): (A) -> (B) -> (C) -> X {
    return { a ->
        { b ->
            { c -> invoke(a, b, c) }
        }
    }
}

val <A, B, C, X> Function3<A, B, C, X>.create: (A) -> (B) -> (C) -> X
    get() = curry()

fun <A, B, C, D, X> Function4<A, B, C, D, X>.curry(): (A) -> (B) -> (C) -> (D) -> X {
    return { a ->
        { b ->
            { c ->
                { d -> invoke(a, b, c, d) }
            }
        }
    }
}

val <A, B, C, D, X> Function4<A, B, C, D, X>.create: (A) -> (B) -> (C) -> (D) -> X
    get() = curry()

fun <A, B, C, D, E, X> Function5<A, B, C, D, E, X>.curry(): (A) -> (B) -> (C) -> (D) -> (E) -> X {
    return { a ->
        { b ->
            { c ->
                { d ->
                    { e -> invoke(a, b, c, d, e) }
                }
            }
        }
    }
}

val <A, B, C, D, E, X> Function5<A, B, C, D, E, X>.create: (A) -> (B) -> (C) -> (D) -> (E) -> X
    get() = curry()

fun <A, B, C, D, E, F, X> Function6<A, B, C, D, E, F, X>.curry(): (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> X {
    return { a ->
        { b ->
            { c ->
                { d ->
                    { e ->
                        { f -> invoke(a, b, c, d, e, f) }
                    }
                }
            }
        }
    }
}

val <A, B, C, D, E, F, X> Function6<A, B, C, D, E, F, X>.create: (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> X
    get() = curry()

fun <A, B, C, D, E, F, G, X> Function7<A, B, C, D, E, F, G, X>.curry(): (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> X {
    return { a ->
        { b ->
            { c ->
                { d ->
                    { e ->
                        { f ->
                            { g ->
                                invoke(a, b, c, d, e, f, g)
                            }
                        }
                    }
                }
            }
        }
    }
}

val <A, B, C, D, E, F, G, X> Function7<A, B, C, D, E, F, G, X>.create: (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> X
    get() = curry()

fun <A, B, C, D, E, F, G, H, X> Function8<A, B, C, D, E, F, G, H, X>.curry(): (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> (H) -> X {
    return { a ->
        { b ->
            { c ->
                { d ->
                    { e ->
                        { f ->
                            { g ->
                                { h ->
                                    invoke(a, b, c, d, e, f, g, h)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

val <A, B, C, D, E, F, G, H, X> Function8<A, B, C, D, E, F, G, H, X>.create: (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> (H) -> X
    get() = curry()

fun <A, B, C, D, E, F, G, H, I, X> Function9<A, B, C, D, E, F, G, H, I, X>.curry(): (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> (H) -> (I) -> X {
    return { a ->
        { b ->
            { c ->
                { d ->
                    { e ->
                        { f ->
                            { g ->
                                { h ->
                                    { i ->
                                        invoke(a, b, c, d, e, f, g, h, i)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

val <A, B, C, D, E, F, G, H, I, X> Function9<A, B, C, D, E, F, G, H, I, X>.create: (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> (H) -> (I) -> X
    get() = curry()

fun <A, B, C, D, E, F, G, H, I, J, X> Function10<A, B, C, D, E, F, G, H, I, J, X>.curry(): (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> (H) -> (I) -> (J) -> X {
    return { a ->
        { b ->
            { c ->
                { d ->
                    { e ->
                        { f ->
                            { g ->
                                { h ->
                                    { i ->
                                        { j ->
                                            invoke(a, b, c, d, e, f, g, h, i, j)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

val <A, B, C, D, E, F, G, H, I, J, X> Function10<A, B, C, D, E, F, G, H, I, J, X>.create: (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> (H) -> (I) -> (J) -> X
    get() = curry()

fun <A, B, C, D, E, F, G, H, I, J, K, X> Function11<A, B, C, D, E, F, G, H, I, J, K, X>.curry(): (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> (H) -> (I) -> (J) -> (K) -> X {
    return { a ->
        { b ->
            { c ->
                { d ->
                    { e ->
                        { f ->
                            { g ->
                                { h ->
                                    { i ->
                                        { j ->
                                            { k ->
                                                invoke(a, b, c, d, e, f, g, h, i, j, k)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

val <A, B, C, D, E, F, G, H, I, J, K, X> Function11<A, B, C, D, E, F, G, H, I, J, K, X>.create: (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> (H) -> (I) -> (J) -> (K) -> X
    get() = curry()

fun <A, B, C, D, E, F, G, H, I, J, K, L, X> Function12<A, B, C, D, E, F, G, H, I, J, K, L, X>.curry(): (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> (H) -> (I) -> (J) -> (K) -> (L) -> X {
    return { a ->
        { b ->
            { c ->
                { d ->
                    { e ->
                        { f ->
                            { g ->
                                { h ->
                                    { i ->
                                        { j ->
                                            { k ->
                                                { l ->
                                                    invoke(a, b, c, d, e, f, g, h, i, j, k, l)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

val <A, B, C, D, E, F, G, H, I, J, K, L, X> Function12<A, B, C, D, E, F, G, H, I, J, K, L, X>.create: (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> (H) -> (I) -> (J) -> (K) -> (L) -> X
    get() = curry()

fun <A, B, C, D, E, F, G, H, I, J, K, L, M, X> Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, X>.curry(): (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> (H) -> (I) -> (J) -> (K) -> (L) -> (M) -> X {
    return { a ->
        { b ->
            { c ->
                { d ->
                    { e ->
                        { f ->
                            { g ->
                                { h ->
                                    { i ->
                                        { j ->
                                            { k ->
                                                { l ->
                                                    { m ->
                                                        invoke(a, b, c, d, e, f, g, h, i, j, k, l, m)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

val <A, B, C, D, E, F, G, H, I, J, K, L, M, X> Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, X>.create: (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> (H) -> (I) -> (J) -> (K) -> (L) -> (M) -> X
    get() = curry()

fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N, X> Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, X>.curry(): (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> (H) -> (I) -> (J) -> (K) -> (L) -> (M) -> (N) -> X {
    return { a ->
        { b ->
            { c ->
                { d ->
                    { e ->
                        { f ->
                            { g ->
                                { h ->
                                    { i ->
                                        { j ->
                                            { k ->
                                                { l ->
                                                    { m ->
                                                        { n ->
                                                            invoke(a, b, c, d, e, f, g, h, i, j, k, l, m, n)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

val <A, B, C, D, E, F, G, H, I, J, K, L, M, N, X> Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, X>.create: (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> (H) -> (I) -> (J) -> (K) -> (L) -> (M) -> (N) -> X
    get() = curry()
