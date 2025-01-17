package com.github.supergluelib.supernova

import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

object SuperNova {

    private val db = DBHandler(SuperNovaMain.instance)

    private fun JavaPlugin.key(key: String) = NamespacedKey(this, key)
    private fun JavaPlugin.playerkey(uuid: UUID, key: String) = PlayerKey(this.name, uuid, key)

    internal fun loadPlayer(player: Player) = db.loadEntries(player.uniqueId)

    data class PlayerKey(val pluginName: String, val uuid: UUID, val key: String) {
        constructor(plugin: JavaPlugin, uuid: UUID, key: String): this(plugin.name, uuid, key)
    }

    fun writePluginData(key: NamespacedKey, value: String) = db.writePluginEntry(key, value)
    fun writePluginData(plugin: JavaPlugin, key: String, value: String): Unit = writePluginData(plugin.key(key), value)

    fun writePlayerData(key: PlayerKey, value: String) = db.writePlayerEntry(key, value)
    fun writePlayerData(plugin: JavaPlugin, uuid: UUID, key: String, value: String): Unit = writePlayerData(plugin.playerkey(uuid, key), value)


    fun readPluginData(key: NamespacedKey): String? = db.readPluginEntry(key)
    fun readPluginData(plugin: JavaPlugin, key: String): String? = readPluginData(plugin.key(key))

    fun readPlayerData(key: PlayerKey): String? = db.readPlayerEntry(key)
    fun readPlayerData(plugin: JavaPlugin, uuid: UUID, key: String): String? = readPlayerData(plugin.playerkey(uuid, key))


}