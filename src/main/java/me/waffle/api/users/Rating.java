package me.waffle.api.users;

import java.sql.SQLException;

import me.waffle.api.mysql.QueryType;
import me.waffle.api.mysql.exceptions.NotConnectedException;
import me.waffle.api.mysql.MySQL;

public class Rating {
	protected User user;
	
	public Rating(User user) {
		this.user = user;
	}
	
	public int getScore() {
		int rating = get();
		int score = (int) Math.floor(rating/1000);
		return score;
	}
	
	public int get() {
		try {
			String rating_string = (String) MySQL.get().query(QueryType.GET, "select rating from user where uuid = '" + this.user.getUUID() + "';");
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
			int rating = this.get();
			rating = rating+amount;
			if(rating > 10000) { rating = 10000; }
			String string = (String) MySQL.get().query(QueryType.GET, "select rating from user where uuid = '" + this.user.getUUID() + "';");
			if(string == null) {
				MySQL.get().query(QueryType.POST, "replace into user (uuid, rating) values ('" + this.user.getUUID() + "','" + rating + "');");
			} else {
				MySQL.get().query(QueryType.POST, "update user set rating = '" + rating + "' where uuid = '" + this.user.getUUID() + "';");
			} return rating;
		} catch (SQLException e) {
			e.printStackTrace();
			return 1000+amount;
		} catch (NotConnectedException e) {
			e.printStackTrace();
			return 1000+amount;
		}
	}
	
	public int subtract(int amount) {
		try {
			int rating = this.get();
			rating = rating-amount;
			if(rating < 0) { rating = 0; }
			
			String string = (String) MySQL.get().query(QueryType.GET, "select rating from user where uuid = '" + this.user.getUUID() + "';");
			if(string == null) {
				MySQL.get().query(QueryType.POST, "replace into user (uuid, rating) values ('" + this.user.getUUID() + "','" + rating + "');");
			} else {
				MySQL.get().query(QueryType.POST, "update user set rating = '" + rating + "' where uuid = '" + this.user.getUUID() + "';");
			} return rating;
		} catch (SQLException e) {
			e.printStackTrace();
			return 1000-amount;
		} catch (NotConnectedException e) {
			e.printStackTrace();
			return 1000-amount;
		}
	}
	
	public int calc(int rating1, int rating2) {
		int rating = (int) Math.round((double)((double)rating2/(double)rating1)*12);
		if(rating <= 0) {
			rating = 1;
		} else if(rating > 32) {
			rating = 32;
		} return rating;
	}
}
