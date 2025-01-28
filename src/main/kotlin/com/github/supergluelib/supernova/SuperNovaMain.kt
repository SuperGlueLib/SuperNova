package com.github.supergluelib.supernova

import com.github.supergluelib.foundation.extensions.registerListeners
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

class SuperNovaMain: JavaPlugin(), Listener {

    companion object {
        lateinit var instance: SuperNovaMain private set
    }

    override fun onEnable() {
        instance = this

        registerListeners(this)

        SuperNova // Load
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun loadPlayerOnJoin(event: PlayerJoinEvent) {
        SuperNova.loadPlayer(event.player)
    }

    @EventHandler
    fun unloadPlayer(event: PlayerQuitEvent) {
        SuperNova.unloadPlayer(event.player)
    }

}