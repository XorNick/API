package me.waffle.api.users;

import java.sql.SQLException;

import me.waffle.api.mysql.QueryType;
import me.waffle.api.mysql.exceptions.NotConnectedException;
import me.waffle.api.mysql.MySQL;

public class Stats {
	/*
	 * DB Layout:
	 * uuid | game | kills | wins | deaths
	 */
	
	public enum Stat {
		KILLS("kills"),
		WINS("wins"),
		DEATHS("deaths"),
		;
		
		private final Object[] values;
		Stat(Object... vals) { values = vals; }
		public String getID() { return (String) values[0]; }
	}
	
	protected User user;
	
	public Stats(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public void add(String game, Stat stat) {
		actuallyAdd(game, stat, 1);
	}
	
	public void add(String game, Stat stat, int amount) {
		actuallyAdd(game, stat, amount);
	}
	
	private void actuallyAdd(String game, Stat stat, int amount) {
		try {
			String check = (String) MySQL.get().query(QueryType.GET, "select uuid from stats where uuid = '" + this.user.getUUID() + "' and game = '" + game + "';");
			if(check != null) {
				MySQL.get().query(QueryType.POST, "update stats set " + stat.getID() + " = " + stat.getID() + " + " + amount + " where uuid = '" + this.user.getUUID() + "' and game = '" + game + "';");
			} else {
				MySQL.get().query(QueryType.POST, "replace into stats (uuid, game, " + stat.getID() + ") values ('" + this.user.getUUID() + "','" + game + "',1);");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
	}
	
	public int get(String game, Stat stat) {
		try {
			String query = (String) MySQL.get().query(QueryType.GET, "select " + stat.getID() + " from stats where uuid = '" + this.user.getUUID() + "' and game = '" + game + "';");
			if(query != null) {
				return Integer.parseInt(query);
			} return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} catch (NotConnectedException e) {
			e.printStackTrace();
			return 0;
		}
	}
}

