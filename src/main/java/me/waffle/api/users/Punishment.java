package me.waffle.api.users;

import me.waffle.api.mysql.QueryType;
import me.waffle.api.mysql.MySQL;

public class Punishment {
	protected int id;
	protected String type;
	protected User punisher;
	protected User victim;
	protected Long time;
	protected Long length;
	protected String reason;
	protected boolean active;
	protected boolean valid;
	
	public Punishment(int id) {
		this.id = id;
		this.valid = true;
		
		try {
			this.type = (String) MySQL.get().query(QueryType.GET, "select type from punishments where id = " + id + ";");
			if(this.type == null) {
				this.valid = false;
				return;
			}
			
			this.punisher = new User((String) MySQL.get().query(QueryType.GET, "select punisher from punishments where id = " + id + ";"));
			this.victim = new User((String) MySQL.get().query(QueryType.GET, "select victim from punishments where id = " + id + ";"));
			this.time = Long.parseLong((String) MySQL.get().query(QueryType.GET, "select time from punishments where id = " + id + ";"));
			this.length = Long.parseLong((String) MySQL.get().query(QueryType.GET, "select length from punishments where id = " + id + ";"));
			this.reason = (String) MySQL.get().query(QueryType.GET, "select reason from punishments where id = " + id + ";");
			this.active = Boolean.parseBoolean((String) MySQL.get().query(QueryType.GET, "select active from punishments where id = " + id + ";"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getID() {
		return this.id;
	}
	
	public boolean isValid() {
		return this.valid;
	}
	
	public String getType() {
		return this.type;
	}
	
	public User getPunisher() {
		return this.punisher;
	}
	
	public User getVictim() {
		return this.victim;
	}
	
	public Long getTime() {
		return this.time;
	}
	
	public Long getLength() {
		return this.length;
	}
	
	public String getReason() {
		return this.reason;
	}
	
	public boolean isActive() {
		return this.active;
	}
}
