package me.waffle.api.server;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Ping {
	public static int get(Player player) {
		return ((CraftPlayer) player).getHandle().ping;
	}
}