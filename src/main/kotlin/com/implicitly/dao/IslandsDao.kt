package com.implicitly.dao

import com.implicitly.models.Country
import com.implicitly.models.Island

class IslandsDao {

    companion object {
        private val MOCK_ISLANDS by lazy {
            listOf(
                    Island("Kotlin", Country("Russia", "RU")),
                    Island("Stewart Island", Country("New Zealand", "NZ")),
                    Island("Cockatoo Island", Country("Australia", "AU")),
                    Island("Tasmania", Country("Australia", "AU"))
            )
        }
    }

    fun fetchIslands() = MOCK_ISLANDS

    fun fetchCountries(code: String? = null) =
            MOCK_ISLANDS.map { it.country }
                    .distinct()
                    .filter { code == null || it.code.equals(code, true) }
                    .sortedBy { it.code }

}