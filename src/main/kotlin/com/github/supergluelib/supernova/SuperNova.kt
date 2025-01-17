package com.github.supergluelib.supernova

import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

object SuperNova {

    private fun JavaPlugin.key(key: String) = NamespacedKey(this, key)

    fun writePluginData(plugin: JavaPlugin, key: String, value: String): Unit = TODO()
    fun writePlayerData(plugin: JavaPlugin, uuid: UUID, key: String, value: String): Unit = TODO()

    fun readPluginData(plugin: JavaPlugin, key: String): String = TODO()
    fun readPlayerData(plugin: JavaPlugin, uuid: UUID, key: String): String = TODO()
}