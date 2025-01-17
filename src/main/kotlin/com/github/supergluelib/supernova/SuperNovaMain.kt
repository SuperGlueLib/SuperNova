package com.github.supergluelib.supernova

import org.bukkit.plugin.java.JavaPlugin

class SuperNovaMain: JavaPlugin() {

    companion object {
        lateinit var instance: SuperNovaMain private set
    }

    override fun onEnable() {
        instance = this

        SuperNova // Load
    }

}