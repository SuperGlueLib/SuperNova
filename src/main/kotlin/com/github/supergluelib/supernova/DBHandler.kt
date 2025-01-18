package com.github.supergluelib.supernova

import com.github.supergluelib.foundation.database.SQLiteDatabase
import com.github.supergluelib.foundation.extensions.executeQuery
import com.github.supergluelib.foundation.extensions.toUUID
import com.github.supergluelib.supernova.SuperNova.PlayerKey
import org.bukkit.NamespacedKey
import java.io.File
import java.util.*

internal class DBHandler(val plugin: SuperNovaMain) {
    private val db = SQLiteDatabase(File(plugin.dataFolder, "data.sqlite"))

    private val pluginEntries: HashMap<NamespacedKey, String> = hashMapOf()
    private val playerEntries: HashMap<PlayerKey, String> = hashMapOf()

    internal fun loadEntries(uuid: UUID) {
        val rs = GET_BY_UUID_QUERY.executeQuery(uuid to UUID::class.java)
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
        createTables()
        // TODO: Load Plugins
    }

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

    private val GET_BY_UUID_QUERY = db.prepareStatement("""
        SELECT (namespace_mapping.namespace, uuid_mapping.uuid, key, value) 
        FROM player_data
        JOIN namespace_mapping ON player_data.namespace = namespace_mapping.id
        JOIN uuid_mapping ON player_data.uuid = uuid_mapping.id
        WHERE uuid_mapping.uuid = ?;"
    """.trimIndent())

    private val INSERT_UUID_QUERY = db.prepareStatement("INSERT OR IGNORE INTO uuid_mapping (uuid) VALUES (?);")

    private fun createTables() {
        db.prepareStatement("""
            CREATE TABLE IF NOT EXISTS namespace_mapping(
              id INT PRIMARY KEY AUTOINCREMENT,
              namespace TEXT NOT NULL UNIQUE
            );
        """.trimIndent()).execute()

        db.prepareStatement("""
            CREATE TABLE IF NOT EXISTS uuid_mapping(
              id INT PRIMARY KEY AUTOINCREMENT,
              uuid CHAR(36) NOT NULL UNIQUE
            );
        """.trimIndent()).execute()

        db.prepareStatement("""
          CREATE TABLE IF NOT EXISTS plugin_data(
            namespace INT NOT NULL,
            key TEXT NOT NULL,
            value TEXT NOT NULL,
            PRIMARY KEY (namespace, key),
            FOREIGN KEY (namespace) REFERENCES namespace_mapping (id) 
          );
    """.trimIndent()).execute()

        db.prepareStatement("""
            CREATE TABLE IF NOT EXISTS player_data(
              namespace INT NOT NULL,
              uuid INT NOT NULL,
              key TEXT NOT NULL,
              value TEXT NOT NULL,
              PRIMARY KEY (namespace, uuid, key),
              FOREIGN KEY (namespace) REFERENCES namespace_mapping (id),
              FOREIGN KEY (uuid) REFERENCES uuid_mapping (id)
            )
        """.trimIndent()).execute()
    }

}