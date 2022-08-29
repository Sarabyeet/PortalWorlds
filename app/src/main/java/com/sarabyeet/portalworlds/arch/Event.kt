package com.sarabyeet.portalworlds.arch

import java.lang.Exception

sealed class Event {

    sealed class LocalException(
        val title: String,
        val description: String = ""
    ): Exception() {
        object EmptySearch : LocalException(
            title = "Start typing to search!"
        )
        object NoResults : LocalException(
            title = "Whoops!",
            description = "Looks like your search returned no results"
        )
    }

}
