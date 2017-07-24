package me.waffle.api.entity;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.waffle.api.API;

public class Firework {
	protected org.bukkit.entity.Firework firework;
	protected FireworkMeta fireworkMeta;
	
	public Firework(Location loc, Type type, boolean flicker, boolean trail, boolean instant, Color... colors) {
		this.firework = (org.bukkit.entity.Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		this.fireworkMeta = this.firework.getFireworkMeta();
		
		this.fireworkMeta.addEffect(FireworkEffect.builder()
			.with(type)
			.flicker(flicker)
			.trail(trail)
			.withColor(colors)
		.build());
		this.fireworkMeta.setPower(0);
		
		this.firework.setFireworkMeta(this.fireworkMeta);
		
		if(instant == true) {
			new BukkitRunnable() {
				public void run() {
					firework.detonate();
				}
			}.runTaskLater(API.get(), 2);
		}
	}
}

