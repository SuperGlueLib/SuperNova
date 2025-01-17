package com.github.supergluelib.supernova

import org.bukkit.NamespacedKey
import java.util.*

internal class DBHandler(val plugin: SuperNovaMain) {
    internal data class PlayerKey(val key: NamespacedKey, val uuid: UUID)


    // TODO: Loading on Join/Load, etc.
    internal val pluginEntries: HashMap<NamespacedKey, String> = hashMapOf()
    internal val playerEntries: HashMap<PlayerKey, String> = hashMapOf()

}