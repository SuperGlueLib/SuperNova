package com.github.supergluelib.supernova

import com.github.supergluelib.foundation.database.SQLiteDatabase

internal class Queries(val db: SQLiteDatabase) {
    val INSERT_UUID_MAPPING = db.prepareStatement("INSERT OR IGNORE INTO uuid_mapping(uuid) VALUES(?);")
    val INSERT_NAMESPACE_MAPPING = db.prepareStatement("INSERT OR IGNORE INTO namespace_mapping(namespace) VALUES(?);")

    /** Namespace (text), key (text), value (text) */
    val INSERT_PLUGIN_ENTRY = db.prepareStatement("INSERT OR REPLACE INTO plugin_data SELECT (SELECT id FROM namespace_mapping WHERE namespace = ?), ?, ?;")

    /** key(text), value(text), namespace(text), uuid(UUID), */
    val INSERT_PLAYER_ENTRY = db.prepareStatement("""
        INSERT OR REPLACE INTO player_data 
            SELECT nm.id, um.id, ?, ?
            FROM namespace_mapping nm
            JOIN uuid_mapping um
            WHERE nm.namespace = ? AND um.uuid = ?
        """.trimIndent())

    val GET_ENTRIES_BY_UUID = db.prepareStatement("""
        SELECT (namespace_mapping.namespace, uuid_mapping.uuid, key, value)
        FROM player_data
        JOIN namespace_mapping ON player_data.namespace = namespace_mapping.id
        JOIN uuid_mapping ON player_data.uuid = uuid_mapping.id
        WHERE uuid_mapping.uuid = ?;"
    """.trimIndent())



    fun createTables() {
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
    // EXPLAIN QUERY PLAN SELECT * FROM main_data WHERE id = 1;
}