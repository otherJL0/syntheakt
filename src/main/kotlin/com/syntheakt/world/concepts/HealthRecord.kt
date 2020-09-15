package com.syntheakt.world.concepts

import com.syntheakt.world.agents.Person
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import java.math.BigDecimal

/**
 * HealthRecord contains all the coded entries in a person's health record.
 * This class represents a logical health record. Exporters will convert this
 * health record into various standarized formats.
 */
@Serializable
class HealthRecord(person: Person) {
    private val person: Person = person
    val ENCOUNTERS = "encounters"
    val PROCEDURES = "procedures"
    val MEDICATIONS = "medications"
    val IMMUNIZATIONS = "immunizations"

    /**
     * HealthRecord.Code represents a system, code, and display value
     */
    @Serializable
    data class Code(
        val system: String,
        val code: String,
        val display: String,
        var valueSet: String = ""
    ): Comparable<Code> {

        constructor(definition: JsonObject) {
            val system = definition["system"].toString()
            val code = definition["code"].toString()
            val display = definition["display"].toString()
        }
        fun equals(other: Code) = this.system == other.system && this.code == other.code

        override fun toString() = "system=$system, code=$code, display=$display, valueSet=$valueSet"

        fun fromJson(jsonCodes: JsonArray): List<Code> = jsonCodes.map{ Code(it as JsonObject) }

        override fun compareTo(other: Code): Int {
            val compare = this.system.compareTo(other.system)
            return if (compare == 0) this.code.compareTo(other.code) else compare
        }

    }
    
   open class Entry (start: Long, type: String) {
        val record: HealthRecord = this@HealthRecord
        val codes = mutableListOf<Code>()
        var cost: Double? = null
        fun determineCost(): Double {
            Costs.determineCostOfEntry(this@Entry, record.person)
        }
        fun getCost(): Double = cost ?: determineCost()
    }

    class Observation: Entry() {
    }

    class Procedure: Entry() {

    }
    class Medication: Entry() {

    }
    class Encounter: Entry() {

    }
    class Immunization: Entry() {

    }
}
