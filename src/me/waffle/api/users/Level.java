package me.waffle.api.users;

import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Player;

import me.waffle.api.mysql.QueryType;
import me.waffle.api.mysql.exceptions.NotConnectedException;
import me.waffle.api.entity.Firework;
import me.waffle.api.mysql.MySQL;

public class Level {
	protected User user;
	
	public Level(User user) {
		this.user = user;
	}
	
	public int getLevel() {
		try {
			String rating_string = (String) MySQL.get().query(QueryType.GET, "select level from user where uuid = '" + this.user.getUUID() + "';");
			if(rating_string != null) {
				return Integer.parseInt(rating_string);
			} else {
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} catch (NotConnectedException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public int getXP() {
		try {
			String rating_string = (String) MySQL.get().query(QueryType.GET, "select xp from user where uuid = '" + this.user.getUUID() + "';");
			if(rating_string != null) {
				return Integer.parseInt(rating_string);
			} else {
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} catch (NotConnectedException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public int getMaxXP() {
		int level = this.getLevel();
		return ((level*(level*10))+1000);
	}
	
	public int addXP(int amount) {
		try {
			int level = this.getLevel();
			if(level >= 100) {
				return 0;
			}
			
			int xp = this.getXP();
			xp = xp+amount;
			MySQL.get().query(QueryType.POST, "update user set xp = '" + xp + "' where uuid = '" + this.user.getUUID() + "';");
			
			if(xp >= this.getMaxXP()) {
	    		this.levelup();
			} return xp;
		} catch (SQLException e) {
			e.printStackTrace();
			return amount;
		} catch (NotConnectedException e) {
			e.printStackTrace();
			return amount;
		}
	}
	
	public String getColor(int level) {
		if(level >= 0 && level <= 19) {
			return "§7";
		} else if(level >= 20 && level <= 39) {
			return "§9";
		} else if(level >= 40 && level <= 59) {
			return "§6";
		} else if(level >= 60 && level <= 79) {
			return "§c";
		} else if(level >= 80 && level <= 100) {
			return "§4";
		} else {
			return "§7";
		}
	}
	
	private ArrayList<User> leveling = new ArrayList<User>();
	public void levelup() {
		Player player = Bukkit.getPlayer(this.user.getName());
		if(leveling.contains(this.user) || player == null || !player.isOnline()) {
			return;
		} leveling.add(this.user);
		
		int level = this.getLevel();
		int xp = this.getXP();
		
		if(xp == 0 || level >= 100) {
			leveling.remove(this.user);
			return;
		}
		
		try {
			MySQL.get().query(QueryType.POST, "update user set level = " + (level+1) + " where uuid = '" + this.user.getUUID() + "';");
			MySQL.get().query(QueryType.POST, "update user set xp = 0 where uuid = '" + this.user.getUUID() + "';");
			Bukkit.broadcastMessage(this.user.getColor() + this.user.getName() + " §aleveled up from level " + this.getColor(level) + level + " §ato " + this.getColor(level+1) + (level+1) + "§a!");
			leveling.remove(this.user);
			
			Color color = Color.GRAY;
			Type type = Type.BURST;
			String lColor = this.getColor(level+1);
			if(lColor.equals("§9")) {
				color = Color.BLUE;
			} else if(lColor.equals("§6")) {
				color = Color.ORANGE;
				type = Type.BALL;
			} else if(lColor.equals("§c")) {
				color = Color.RED;
				type = Type.BALL_LARGE;
			} else if(lColor.equals("§4")) {
				color = Color.MAROON;
				type = Type.STAR;
			} new Firework(player.getLocation(), type, true, false, true, color);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
	}
}

