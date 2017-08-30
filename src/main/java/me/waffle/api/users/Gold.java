package me.waffle.api.users;

import java.sql.SQLException;

import me.waffle.api.mysql.QueryType;
import me.waffle.api.mysql.exceptions.NotConnectedException;
import me.waffle.api.mysql.MySQL;

public class Gold {
	protected User user;
	
	public Gold(User user) {
		this.user = user;
	}
	
	public int get() {
		try {
			String rating_string = (String) MySQL.get().query(QueryType.GET, "select gold from user where uuid = '" + this.user.getUUID() + "';");
			if(rating_string != null) {
				return Integer.parseInt(rating_string);
			} else {
				return 1000;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1000;
		} catch (NotConnectedException e) {
			e.printStackTrace();
			return 1000;
		}
	}
	
	public int add(int amount) {
		try {
			int gold = this.get();
			gold = gold+amount;
			String string = (String) MySQL.get().query(QueryType.GET, "select gold from user where uuid = '" + this.user.getUUID() + "';");
			if(string == null) {
				MySQL.get().query(QueryType.POST, "replace into user (uuid, gold) values ('" + this.user.getUUID() + "','" + gold + "');");
			} else {
				MySQL.get().query(QueryType.POST, "update user set gold = '" + gold + "' where uuid = '" + this.user.getUUID() + "';");
			} return gold;
		} catch (SQLException e) {
			e.printStackTrace();
			return amount;
		} catch (NotConnectedException e) {
			e.printStackTrace();
			return amount;
		}
	}
	
	public int subtract(int amount) {
		try {
			int gold = this.get();
			gold = gold-amount;
			if(gold < 0) { gold = 0; }
			
			String string = (String) MySQL.get().query(QueryType.GET, "select gold from user where uuid = '" + this.user.getUUID() + "';");
			if(string == null) {
				MySQL.get().query(QueryType.POST, "replace into user (uuid, gold) values ('" + this.user.getUUID() + "','" + gold + "');");
			} else {
				MySQL.get().query(QueryType.POST, "update user set gold = '" + gold + "' where uuid = '" + this.user.getUUID() + "';");
			} return gold;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} catch (NotConnectedException e) {
			e.printStackTrace();
			return 0;
		}
	}
}
