package me.waffle.api;

import java.sql.ResultSet;

import org.bukkit.scheduler.BukkitRunnable;

import me.waffle.api.mysql.QueryType;
import me.waffle.api.mysql.MySQL;

public class Convert {
	public static void run() {
		System.out.println("[API] Converting from old databases to new database...");
		new BukkitRunnable() {
			public void run() {
				try {
					MySQL.get().query(QueryType.POST, "delete from user;");
					ResultSet rs = (ResultSet) MySQL.get().query(QueryType.RS, "select * from uuids;");
					while(rs.next()) {
						String uuid = rs.getString("uuid");
						String name = rs.getString("name");
						String rank = (String) MySQL.get().query(QueryType.GET, "select rank from ranks where uuid = '" + uuid + "';");
						if(rank == null) { rank = "NULL"; }
						String color = (String) MySQL.get().query(QueryType.GET, "select color from namecolor where uuid = '" + uuid + "';");
						if(color == null) { color = "NULL"; }
						String prefix = (String) MySQL.get().query(QueryType.GET, "select pre from prefix2 where uuid = '" + uuid + "';");
						if(prefix == null) { prefix = "NULL"; }
						String rating = (String) MySQL.get().query(QueryType.GET, "select rating from score where uuid = '" + uuid + "';");
						if(rating == null) { rating = "1000"; }
						String gold = (String) MySQL.get().query(QueryType.GET, "select gold from gold where uuid = '" + uuid + "';");
						if(gold == null) { gold = "0"; }
						String xp = (String) MySQL.get().query(QueryType.GET, "select xp from levels where uuid = '" + uuid + "';");
						if(xp == null) { xp = "0"; }
						String level = (String) MySQL.get().query(QueryType.GET, "select level from levels where uuid = '" + uuid + "';");
						if(level == null) { level = "0"; }
						
						MySQL.get().query(QueryType.POST, ""
								+ "replace into user "
								+ "(uuid, name, rank, color, prefix, rating, gold, xp, level)"
								+ " values "
								+ "("
									+ "'" + uuid + "','" + name + "','" + rank + "','" + color + "','" + prefix + "'," + rating + "," + gold + "," + xp + "," + level + ""
								+ ");");
					}
					System.out.println("[API] Conversion complete.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(API.get());
	}
}
