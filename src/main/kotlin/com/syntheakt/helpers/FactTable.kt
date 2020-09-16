package com.syntheakt.helpers

import java.io.IOException
import java.io.Writer
import java.util.concurrent.atomic.AtomicInteger

class FactTable {
    var id = AtomicInteger(1)
    val keys = mutableMapOf<String, Int>()
    val facts = mutableMapOf<Int, String>()

    var header: String? = null


    companion object LOCK {
        val LOCK = Object()
    }

    private val NEWLINE = System.lineSeparator()

    fun setNextId(id: Int) {
        synchronized(LOCK.LOCK) { this.id = AtomicInteger(id) }
    }

    fun getFactId(key: String): Int? = synchronized(LOCK.LOCK) { return keys[key] }

    fun getFactByKey(key: String): String? = synchronized(LOCK.LOCK) { return facts[keys[id]] }

    fun getFactById(id: Int): String? = synchronized(LOCK.LOCK) { return facts[id] }

    fun addFact(key: String, fact: String): Int? {
        synchronized(LOCK.LOCK) {
            if (keys.containsKey(key)) {
                return keys[key]
            }
            val next = id.getAndIncrement()
            keys[key] = next
            facts[next] = fact
            return next
        }
    }

    @Throws(IOException::class)
    fun write(writer: Writer) {
        synchronized(LOCK.LOCK) {
            writer.write(header)
            writer.write(NEWLINE)
            facts.keys.forEach { key ->
                writer.write(key.toString())
                writer.write(",")
                writer.write(facts[key] ?: "")
                writer.write(NEWLINE)
            }
            writer.flush()
        }
    }

}