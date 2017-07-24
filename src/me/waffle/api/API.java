package me.waffle.api;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import me.waffle.api.mysql.QueryType;
import me.waffle.api.mysql.exceptions.NotConnectedException;
import me.waffle.api.bungee.Bungeecord;
import me.waffle.api.listeners.Listeners;
import me.waffle.api.mysql.MySQL;
import me.waffle.api.users.User;

public class API extends JavaPlugin {
	private static API instance;
	private static ProtocolManager protocols;
	
	public static API get() {
		return instance;
	}
	
	public static ProtocolManager protocols() {
		return protocols;
	}
	
	public void onEnable() {
		instance = this;
		protocols = ProtocolLibrary.getProtocolManager();
		
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new Bungeecord());
		new Listeners(this,
			new JoinEvent()
		);

		Bungeecord.updater();
		MySQL.connect();
		
		//Convert.run();
	}
	
	public void onDisable() {
		try {
			Bukkit.getScheduler().cancelAllTasks();
			MySQL.get().disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
	}

	public static class JoinEvent implements Listener {
		@EventHandler
		public void onJoin(PlayerJoinEvent e) throws Exception {
			Player player = e.getPlayer();
			String ip = (player.getAddress()+"").replace("/", "");
			String[] ip_s = ip.split(":");
			ip = ip_s[0];
			
			PacketContainer packet = new PacketContainer(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
			packet.getChatComponents()
				.write(0, WrappedChatComponent.fromText("§fWelcome to the §r§e§lWaffle NETWORK§r"))
				.write(1, WrappedChatComponent.fromText("§fWebsite: §r§bWaffle.org§r    §fTwitter:§r §b@TwittWaffle§r\n§r§fDiscord: §r§bWaffle.org/discord    §r§fIP: §r§bplay.Waffle.org§r"));
			protocols().sendServerPacket(player, packet);
			
			if(MySQL.get().query(QueryType.GET, "select uuid from user where uuid = '" + player.getUniqueId().toString() + "';") == null) {
				if(Bukkit.getServerName().contains("Hub")) {
					ResultSet rs = (ResultSet) MySQL.get().query(QueryType.RS, "select * from user;");
					int number = 1;
					while(rs.next()) {
						number += 1;
					} Bukkit.broadcastMessage("§d" + player.getName() + " has joined for the first time ever! (#" + number + ")");
				}
				
				String uuid = (String) MySQL.get().query(QueryType.GET, "select uuid from user where uuid = '" + player.getUniqueId().toString() + "';");
				if(uuid == null) {
					MySQL.get().query(QueryType.POST, "replace into user (name, uuid) values ('" + player.getName() + "','" + player.getUniqueId().toString() + "');");
				} else {
					MySQL.get().query(QueryType.POST, "update user set name = '" + player.getName() + "' where uuid = '" + player.getUniqueId().toString() + "';");
				}
			} else {
				String storedName = (String) MySQL.get().query(QueryType.GET, "select name from user where uuid = '" + player.getUniqueId().toString() + "';");
				if(!storedName.equals(player.getName())) {
					MySQL.get().query(QueryType.POST, "update user set name = '" + player.getName() + "' where uuid = '" + player.getUniqueId().toString() + "';");
				}
			}
			
			String uuid = (String) MySQL.get().query(QueryType.GET, "select uuid from user where uuid = '" + player.getUniqueId().toString() + "';");
			String check2 = (String) MySQL.get().query(QueryType.GET, "select ip from ips where ip = '" + ip + "' and uuid = '" + uuid + "';");
			if(check2 == null) {
				MySQL.get().query(QueryType.POST, "replace into ips (uuid, ip) values ('" + uuid + "','" + ip + "');");
			}
			MySQL.get().query(QueryType.POST, "delete from ips where ip = 'null';");
			MySQL.get().query(QueryType.POST, "delete from ips where uuid = 'null';");
			
			User user = new User(player.getName());
			if(user.level().getXP() >= user.level().getMaxXP()) {
				user.level().levelup();
			}
		}
	}
}