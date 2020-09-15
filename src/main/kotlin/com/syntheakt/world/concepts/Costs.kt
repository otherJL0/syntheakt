package com.syntheakt.world.concepts

import com.syntheakt.helpers.Config
import com.syntheakt.world.agents.Person
import kotlin.math.sqrt
import kotlin.random.Random

class Costs {

    private val PROCEDURE_COSTS = parseCsvToMap("costs/procedures.csv")
    private val MEDICATION_COSTS = parseCsvToMap("costs/medications.csv")
    private val ENCOUNTER_COSTS = parseCsvToMap("costs/encounters.csv")
    private val IMMUNIZATION_COSTS = parseCsvToMap("costs/immunizations.csv")

    fun genKey(key: String) = "generate.costs.default_${key}_cost"
    private val DEFAULT_PROCEDURE_COSTS: Double = Config.get(genKey("procedure")).toDouble()
    private val DEFAULT_MEDICATION_COSTS: Double = Config.get(genKey("medication")).toDouble()
    private val DEFAULT_ENCOUNTER_COSTS: Double = Config.get(genKey("encounter")).toDouble()
    private val DEFAULT_IMMUNIZATION_COSTS: Double = Config.get(genKey("immunization")).toDouble()

    data class CostData(
    val min: Double,
    val mode: Double,
    val max: Double
    ) {
        fun chooseCost(rand: Random) = triangularDistribution(min, max, mode, rand.nextDouble())
        fun triangularDistribution(min: Double, max: Double, mode: Double, rand: Double): Double {
            val f = (mode - min) / (max - min)
           return if (rand < f) {
               min + sqrt(rand * (max - min) * (mode - min))
           } else {
               max - sqrt((1-rand) * (max-min) * (max - mode))
           }
        }

    }

    fun determineCostOfEntry(entry: HealthRecord.Entry, person: Person): Double {
        var costs: Map<String, CostData>?
        var defaultCost = 0.0

        when (entry) {
            is HealthRecord.Procedure -> {
                costs = PROCEDURE_COSTS
                defaultCost = DEFAULT_PROCEDURE_COSTS
            }
            is HealthRecord.Medication -> {
                costs = MEDICATION_COSTS
                defaultCost = DEFAULT_MEDICATION_COSTS
            }
            is HealthRecord.Encounter -> {
                costs = ENCOUNTER_COSTS
                defaultCost = DEFAULT_ENCOUNTER_COSTS
            }
            is HealthRecord.Immunization -> {
                costs = IMMUNIZATION_COSTS
                defaultCost = DEFAULT_IMMUNIZATION_COSTS
            }
            else -> return 0.0
        }

        val code: String = entry.codes[0].code
        val baseCost: Double = costs?.get(code)?.chooseCost(Random) ?: defaultCost

        val locationAdjustment = 1.0

        return baseCost * locationAdjustment
    }

    private fun parseCsvToMap(filename: String ): Map<String, CostData>? {
        // TODO
       return null
    }
}