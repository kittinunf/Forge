package com.github.kttinunf.forge.util

/**
 * Created by Kittinun Vantasin on 8/23/15.
 */

public fun <A, B, X> Function2<A, B, X>.curry(): (A) -> (B) -> X {
    return { a ->
        { b -> invoke(a, b) }
    }
}

public fun <A, B, C, X> Function3<A, B, C, X>.curry(): (A) -> (B) -> (C) -> X {
    return { a ->
        { b ->
            { c -> invoke(a, b, c) }
        }
    }
}

public fun <A, B, C, D, X> Function4<A, B, C, D, X>.curry(): (A) -> (B) -> (C) -> (D) -> X {
    return { a ->
        { b ->
            { c ->
                { d -> invoke(a, b, c, d) }
            }
        }
    }
}

public fun <A, B, C, D, E, X> Function5<A, B, C, D, E, X>.curry(): (A) -> (B) -> (C) -> (D) -> (E) -> X {
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

public fun <A, B, C, D, E, F, X> Function6<A, B, C, D, E, F, X>.curry(): (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> X {
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

public fun <A, B, C, D, E, F, G, X> Function7<A, B, C, D, E, F, G, X>.curry(): (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> X {
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

public fun <A, B, C, D, E, F, G, H, X> Function8<A, B, C, D, E, F, G, H, X>.curry(): (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> (H) -> X {
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

public fun <A, B, C, D, E, F, G, H, I, X> Function9<A, B, C, D, E, F, G, H, I, X>.curry(): (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> (H) -> (I) -> X {
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

public fun <A, B, C, D, E, F, G, H, I, J, X> Function10<A, B, C, D, E, F, G, H, I, J, X>.curry(): (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> (H) -> (I) -> (J) -> X {
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




 
