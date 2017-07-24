package me.waffle.api.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.mojang.authlib.GameProfile;

import io.netty.util.internal.ThreadLocalRandom;
import me.waffle.api.API;
import me.waffle.api.wrapper.WrapperPlayServerEntityMetadata;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;

public class FakePlayer {
	protected Player onlyFor;
	protected String name;
	protected Location loc;
	protected EntityPlayer entity;
	protected boolean invis;
	
	public FakePlayer(Player player, String name, Location loc, boolean invisible) {
		this.onlyFor = player;
		this.name = name;
		this.loc = loc;
		this.invis = invisible;
	}
	
	@SuppressWarnings("unused")
	public void spawn() {
		MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer nmsWorld = ((CraftWorld) this.onlyFor.getWorld()).getHandle();
		
		this.entity = new EntityPlayer(nmsServer, nmsWorld, new GameProfile(UUID.randomUUID(), this.name), new PlayerInteractManager(nmsWorld));
		this.entity.setLocation(this.loc.getX(), this.loc.getY(), this.loc.getZ(), 0F, 0F);
		
		PlayerConnection connection = ((CraftPlayer) this.onlyFor).getHandle().playerConnection;
		connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, this.entity));
		connection.sendPacket(new PacketPlayOutNamedEntitySpawn(this.entity));
		connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, this.entity));
		
		if(this.invis) {
			this.entity.ping = ThreadLocalRandom.current().nextInt(50);
			connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.UPDATE_LATENCY, this.entity));
			
			try {
				WrappedDataWatcher watcher = new WrappedDataWatcher();
				watcher.setObject(0, false ? (byte)0 : 0x20);
				WrapperPlayServerEntityMetadata update = new WrapperPlayServerEntityMetadata();
				update.setEntityId(this.entity.getId());
				update.setEntityMetadata(watcher.getWatchableObjects());
				API.protocols().sendServerPacket(this.onlyFor, update.getHandle());
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
			//connection.sendPacket(new PacketPlayOutEntityEffect(this.entity.getId(), new MobEffect(PotionEffectType.INVISIBILITY.getId(), 240000, 1, true, false)));
		}
	}
	
	public int getEntityID() {
		return this.entity.getId();
	}
	
	public void despawn() {
		PlayerConnection connection = ((CraftPlayer) this.onlyFor).getHandle().playerConnection;
		connection.sendPacket(new PacketPlayOutEntityDestroy(this.entity.getId()));
	}
	
	public void teleport(Location loc) {
		PlayerConnection connection = ((CraftPlayer) this.onlyFor).getHandle().playerConnection;
		this.entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
		connection.sendPacket(new PacketPlayOutEntityTeleport(this.entity));
	}
}