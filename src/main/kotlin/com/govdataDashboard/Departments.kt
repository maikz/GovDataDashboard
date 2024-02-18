package com.govdataDashboard

import com.google.gson.Gson
import java.io.File


/**
 * Contains federal ministries and their subordinated agencies.
 * Matches the provided json.
 */
data class MinistryList(
    val ministries: List<Ministry>,
) {
    companion object Factory {
        fun create(path: String = "src/main/resources/departments.json"): MinistryList {
            val json = File(path).readText()
            return Gson().fromJson(json, MinistryList::class.java)
        }
    }

    /**
     * A flat list of the ministry names and the names of their subordinates.
     */
    fun nameList(): List<String> {
        return this.ministries
            .flatMap { ministry ->
                val subordinateNames = ministry.subordinates?.map { it.name } ?: emptyList()
                listOf(ministry.name) + subordinateNames
            }
    }
}

/**
 * A federal ministry
 */
data class Ministry(
    val name: String,
    val subordinates: List<Subordinate>?,
)

/**
 * A subordinate of a federal ministry
 */
data class Subordinate(
    val name: String,
)

