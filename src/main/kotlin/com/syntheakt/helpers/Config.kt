package com.syntheakt.helpers

import java.io.FileInputStream
import java.util.Properties

abstract class Config {
    companion object {
    private val properties: Properties = Properties()
    private val propertiesFile = System.getProperty("/synthea.properties")
    private val inputStream = FileInputStream(propertiesFile)
    val empty = properties.load(inputStream)

        fun get(key: String, defaultValue: String = ""): String {
            return if (defaultValue == "") properties.getProperty(key) else {
                properties.getProperty(key, defaultValue)
            }
        }
    }


}