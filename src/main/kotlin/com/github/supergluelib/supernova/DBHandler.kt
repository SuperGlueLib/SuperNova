package com.github.supergluelib.supernova

import com.github.supergluelib.foundation.database.SQLiteDatabase
import com.github.supergluelib.foundation.extensions.executeQuery
import com.github.supergluelib.foundation.extensions.executeUpdate
import com.github.supergluelib.foundation.extensions.toUUID
import com.github.supergluelib.supernova.SuperNova.PlayerKey
import org.bukkit.NamespacedKey
import java.io.File
import java.util.*
import kotlin.jvm.java

// TODO: Make a uuid: Int and String: Int map that loads the DB mapping ID of the values on relevant load.

internal class DBHandler(val plugin: SuperNovaMain) {
    private val db = SQLiteDatabase(File(plugin.dataFolder, "data.sqlite"))
    private val queries = Queries(db)

    private val pluginEntries: HashMap<NamespacedKey, String> = hashMapOf()
    private val playerEntries: HashMap<PlayerKey, String> = hashMapOf()

    private val registeredPlugins = hashSetOf<String>()

    internal fun loadEntries(uuid: UUID) {
        queries.INSERT_UUID_MAPPING.executeUpdate(uuid to UUID::class.java)
        val rs = queries.GET_ENTRIES_BY_UUID.executeQuery(uuid to UUID::class.java)
        while (rs.next()) {
            val key = PlayerKey(rs.getString("namespace"), rs.getString("uuid").toUUID()!!, rs.getString("key"))
            playerEntries[key] = rs.getString("value")
        }
    }

    internal fun unloadEntries(uuid: UUID) {
        // TODO: If writes are replicated immediately, no need to save here -- double check
        playerEntries.filter { it.key.uuid == uuid }.forEach { playerEntries.remove(it.key) }
    }

    init {
        queries.createTables()
        val rs = db.prepareStatement("""
            SELECT (namespace_mapping.namespace, key, value) 
            FROM plugin_data
            JOIN namespace_mapping ON namespace_mapping.id = plugin_data.namespace
        """.trimIndent()).executeQuery()

        while (rs.next()) {
            val key = NamespacedKey(rs.getString("namespace"), rs.getString("key"))
            pluginEntries[key] = rs.getString("value")
        }

        registeredPlugins.addAll(pluginEntries.map { it.key.namespace })
    }

    /**
     * Write a plugin data entry to the db. Will overwrite existing entries with this key.
     */
    fun writePluginEntry(key: NamespacedKey, value: String) { // Can be new or existing, will overwrite.
        pluginEntries[key] = value
        if (key.namespace !in registeredPlugins) {
            registeredPlugins.add(key.namespace)
            queries.INSERT_NAMESPACE_MAPPING.executeUpdate(key.namespace to String::class.java)
        }
        // TODO: Async?
        queries.INSERT_PLUGIN_ENTRY.executeUpdate(key.namespace to String::class.java, key.key to String::class.java, value to String::class.java)
    }

    /**
     * Write a player data entry to the db. Will overwrite existing values.
     */
    fun writePlayerEntry(key: PlayerKey, value: String) {
        playerEntries[key] = value
        // TODO: Async?
        queries.INSERT_PLAYER_ENTRY.executeUpdate(key.key to String::class.java, value to String::class.java, key.pluginName to String::class.java, key.uuid to UUID::class.java)
    }

    fun readPluginEntry(key: NamespacedKey): String? = pluginEntries[key]
    fun readPlayerEntry(key: PlayerKey): String? = playerEntries[key]

}