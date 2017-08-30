package me.waffle.api.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class Listeners {
	public Listeners(Plugin instance, Listener... listeners) {
		for(Listener ll : listeners) {
			Bukkit.getServer().getPluginManager().registerEvents(ll, instance);
		}
	}
}
