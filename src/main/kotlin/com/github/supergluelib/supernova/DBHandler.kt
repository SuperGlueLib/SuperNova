package com.github.supergluelib.supernova

import com.github.supergluelib.foundation.database.SQLiteDatabase
import com.github.supergluelib.supernova.SuperNova.PlayerKey
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*

internal class DBHandler(val plugin: SuperNovaMain) {
    private val db = SQLiteDatabase(File(plugin.dataFolder, "data.sqlite"))

    // TODO: Loading on Join/Load, etc.
    internal val pluginEntries: HashMap<NamespacedKey, String> = hashMapOf()
    internal val playerEntries: HashMap<PlayerKey, String> = hashMapOf()

    internal fun loadEntries(plugin: JavaPlugin): Unit = TODO()
    internal fun loadEntries(uuid: UUID): Unit = TODO()


    fun writePluginEntry(key: NamespacedKey, value: String) { // Can be new or existing, will overwrite.
        pluginEntries[key] = value
        // TODO: SQL
    }

    fun writePlayerEntry(key: PlayerKey, value: String) {
        playerEntries[key] = value
        // TODO: SQL
    }

    fun readPluginEntry(key: NamespacedKey): String? = pluginEntries[key]
    fun readPlayerEntry(key: PlayerKey): String? = playerEntries[key]

    // EXPLAIN QUERY PLAN SELECT * FROM main_data WHERE id = 1;
    // INSERT OR IGNORE
    // UNIQUE improves performance

}