package com.sarabyeet.portalworlds.network

import com.sarabyeet.portalworlds.domain.models.Character

object PortalWorldsCache {
    val characterMap = mutableMapOf<Int, Character>()
}