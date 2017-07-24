package me.waffle.api.server;

import net.minecraft.server.v1_8_R3.MinecraftServer;

public class TPS {
	public static double get() {
		return MinecraftServer.getServer().recentTps[0];
	}
}
