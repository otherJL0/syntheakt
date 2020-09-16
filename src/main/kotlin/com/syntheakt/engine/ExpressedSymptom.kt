package com.syntheakt.engine

import kotlinx.serialization.Serializable

@Serializable
class ExpressedSymptom private constructor(
    var sources: MutableMap<String, SymptomSource>,
    var name: String,
) : Cloneable {
    private val serialVersionUID = 4322116644425686800L

    @Serializable
    class SymptomInfo private constructor(
        val serialVersionUID: Long,
        var cause: String,
        var value: Int,
        var time: Long,
    ) : Cloneable {
        init {
            val serialVersionUID = 4322116644425686801L
        }

        constructor(cause: String, value: Int, time: Long) {
            this.cause = cause
            this.value = value
            this.time = time
        }

        override fun clone() = SymptomInfo(cause = this.cause, value = this.value, time = this.time)
    }

    @Serializable
    class SymptomSource private constructor(
        var source: String,
        var resolved: Boolean,
        var lastUpdateTime: Long?,
        var timeInfos: MutableMap<Long, SymptomInfo>,
    ) : Cloneable {
        constructor(source: String) {
            this.source = source
            this.timeInfos = mutableMapOf()
            this.resolved = false
            this.lastUpdateTime = null
        }

        override fun clone(): SymptomSource {
            val data = SymptomSource(this.source)
            data.resolved = this.resolved
            data.lastUpdateTime = this.lastUpdateTime
            data.timeInfos.putAll(this.timeInfos)
            return data
        }

        fun isResolved() = this.resolved
        fun resolve() {
            this.resolved = true
        }

        fun activate() {
            this.resolved = false
        }

        fun addInfo(cause: String, time: Long, value: Int, addressed: Boolean) {
            val info = SymptomInfo(cause, value, time)
            timeInfos[time] = info
            lastUpdateTime = time
            resolved = addressed
        }

        fun getCurrentValue() = timeInfos[lastUpdateTime]?.value
    }

    constructor(name: String) {
        this.name = name
        sources = mutableMapOf()
    }

    override fun clone(): ExpressedSymptom {
        val data = ExpressedSymptom(this.name)
        data.sources.putAll(this.sources)
        return data
    }

    fun onset(module: String, cause: String, time: Long, value: Int, addressed: Boolean) {
        if (!sources.containsKey(module)) sources[module] = SymptomSource(module) else sources[module]?.addInfo(cause,
            time,
            value,
            addressed)
    }

    fun getSymptom(): Int = sources.values.filter { !it.isResolved() }.maxOf { it.getCurrentValue() ?: 0 }
    fun getSourceWithHighestValue(): String =
        sources.filter { it.value.getCurrentValue() == getSymptom() }.keys.elementAt(0)

    fun getValueFromSource(source: String): Int? = sources[source]?.getCurrentValue()
    fun addressSource(source: String) {
        sources[source]?.resolve()
    }

    fun getSymptomLastUpdatedTime(module: String): Long? = sources[module]?.lastUpdateTime

}