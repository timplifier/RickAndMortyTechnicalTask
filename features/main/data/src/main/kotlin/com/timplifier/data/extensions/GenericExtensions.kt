package com.timplifier.data.extensions

import com.apollographql.apollo3.api.Optional

fun <T : Any> T?.toOptional(): Optional<T> =
    if (this == null) Optional.absent() else Optional.present(this)