package me.waffle.api.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import me.waffle.api.mysql.QueryType;
import me.waffle.api.mysql.exceptions.NotConnectedException;
import me.waffle.api.math.Maths;
import me.waffle.api.mysql.MySQL;
import me.waffle.api.users.Ranks.Rank;

public class User {
	protected String player;
	protected String uuid;
	
	public User(String player) throws UnknownUserException {
		try {
			if(player.contains("-")) {
				this.uuid = player;
				String name = (String) MySQL.get().query(QueryType.GET, "select name from user where uuid = '" + this.uuid + "';");
				if(name == null) {
					throw new UnknownUserException("That user has never joined before.");
				} else {
					this.player = name;
				}
			} else {
				this.uuid = (String) MySQL.get().query(QueryType.GET, "select uuid from user where name = '" + player + "';");
				this.player = (String) MySQL.get().query(QueryType.GET, "select name from user where uuid = '" + this.uuid + "';");
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(NotConnectedException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<User> getAlts() {
		ArrayList<User> users = new ArrayList<User>();
		try {
			String ip = this.getIP();
			ResultSet rs = (ResultSet) MySQL.get().query(QueryType.RS, "select uuid from ips where ip = '" + ip + "';");
			while(rs.next()) {
				users.add(new User(rs.getString("uuid")));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} return users;
	}
	
	public String getIP() {
		try {
			return (String) MySQL.get().query(QueryType.GET, "select ip from ips where uuid = '" + this.uuid + "';");
		} catch (Exception ex) {
			ex.printStackTrace();
		} return null;
	}
	
	public String getUUID() {
		try {
			if(this.uuid == null) {
				this.uuid = (String) MySQL.get().query(QueryType.GET, "select uuid from user where name = '" + this.player + "';");
			} return this.uuid;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getName() {
		return this.player;
	}
	
	public String getColor() {
		try {
			String color = (String) MySQL.get().query(QueryType.GET, "select color from user where uuid = '" + this.uuid + "';");
			if(color == null || color.equals("NULL")) { return this.getRank().getColor(); }
			return color.replace("&", "§");
		} catch (SQLException e) {
			e.printStackTrace();
			return this.getRank().getColor();
		} catch (NotConnectedException e) {
			e.printStackTrace();
			return this.getRank().getColor();
		}
	}
	
	public String getChatColor() {
		if(this.hasPermission(Permission.STAFF) && this.getRank() != Rank.OP) {
			return "§e";
		} return "§f";
	}
	
	public boolean hasPermission(Permission perm) {
		Rank rank = this.getRank();
		boolean value = false;
		for(Rank lr : perm.getRanks()) {
			if(lr == rank) {
				value = true;
			}
		} return value;
	}
	
	public String getPrefix() {
		try {
			String prefix = (String) MySQL.get().query(QueryType.GET, "select prefix from user where uuid = '" + this.uuid + "';");
			if(prefix == null || prefix.equals("NULL")) { prefix = ""; }
			if(!prefix.equals("")) {
				String[] prefix_S = prefix.split("");
				return "§" + prefix_S[1] + "[" + prefix.replace("&" + prefix_S[1], "").replace("&", "§") + "]";
			} else {
				return prefix;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		} return "";
	}
	
	public void setRank(Rank rank) {
		String value = rank.getValue();
		try {
			MySQL.get().query(QueryType.POST, "update user set rank = '" + value + "' where uuid = '" + this.uuid + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
	}
	
	public Rank getRank() {
		try {
			String rank = (String) MySQL.get().query(QueryType.GET, "select rank from user where uuid = '" + this.uuid + "';");
			if(rank == null || rank.equals("NULL")) {
				return Rank.DEFAULT;
			} else {
				Rank r = Rank.DEFAULT;
				for(Rank lr : Rank.values()) {
					if(lr.getValue().equalsIgnoreCase(rank)) {
						r = lr;
						break;
					}
				} return r;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Rank.DEFAULT;
		} catch (NotConnectedException e) {
			e.printStackTrace();
			return Rank.DEFAULT;
		}
	}
	
	public int getBanID() {
		int id = -1;
		try {
			if(this.isBanned()) {
				id = Integer.parseInt((String) MySQL.get().query(QueryType.GET, "select id from punishments where victim = '" + this.uuid + "' and active = 'true' and type = 'Ban';"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} return id;
	}
	
	public boolean isBanned() {
		try {
			String id = (String) MySQL.get().query(QueryType.GET, "select id from punishments where victim = '" + this.uuid + "' and active = 'true' and type = 'Ban';");
			if(id != null) {
				Punishment pun = new Punishment(Integer.parseInt(id));
				long diff = pun.getLength()-(System.currentTimeMillis()-pun.getTime());
				String timeLeft = Maths.secondsToString(diff);
				if(timeLeft.contains("-")) {
					return false;
				} return true;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (NotConnectedException ex) {
			ex.printStackTrace();
		} return false;
	}
	
	public int getMuteID() {
		int id = -1;
		try {
			if(this.isMuted()) {
				id = Integer.parseInt((String) MySQL.get().query(QueryType.GET, "select id from punishments where victim = '" + this.uuid + "' and active = 'true' and type = 'Mute';"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} return id;
	}
	
	public boolean isMuted() {
		try {
			String id = (String) MySQL.get().query(QueryType.GET, "select id from punishments where victim = '" + this.uuid + "' and active = 'true' and type = 'Mute';");
			if(id != null) {
				Punishment pun = new Punishment(Integer.parseInt(id));
				long diff = pun.getLength()-(System.currentTimeMillis()-pun.getTime());
				String timeLeft = Maths.secondsToString(diff);
				if(timeLeft.contains("-")) {
					return false;
				} return true;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (NotConnectedException ex) {
			ex.printStackTrace();
		} return false;
	}
	
	public Stats stats() {
		return new Stats(this);
	}
	
	public Rating rating() {
		return new Rating(this);
	}
	
	public Gold gold() {
		return new Gold(this);
	}
	
	public Level level() {
		return new Level(this);
	}
}

