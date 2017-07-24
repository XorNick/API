package me.waffle.api.users;

import me.waffle.api.users.Ranks.Rank;

public enum Permission {
	HIGHEST(new Rank[] {Rank.OP, Rank.OWNER}),
	HIGH(new Rank[] {Rank.OP, Rank.OWNER, Rank.ADMIN, Rank.SRDEV}),
	HOST(new Rank[] {Rank.OP, Rank.OWNER, Rank.ADMIN, Rank.SRDEV, Rank.DEV, Rank.HOST}),
	STAFF_HIGH(new Rank[] {Rank.OP, Rank.OWNER, Rank.ADMIN, Rank.SRDEV, Rank.DEV, Rank.HOST, Rank.SRMOD}),
	STAFF_MED(new Rank[] {Rank.OP, Rank.OWNER, Rank.ADMIN, Rank.DEV, Rank.JRDEV, Rank.HOST, Rank.SRMOD, Rank.MOD}),
	STAFF_LOW(new Rank[] {Rank.OP, Rank.OWNER, Rank.ADMIN, Rank.SRDEV, Rank.DEV, Rank.JRDEV, Rank.HOST, Rank.SRMOD, Rank.MOD, Rank.HELPER}),
	STAFF(new Rank[] {Rank.OP, Rank.OWNER, Rank.ADMIN, Rank.SRDEV, Rank.DEV, Rank.JRDEV, Rank.HOST, Rank.SRMOD, Rank.MOD, Rank.HELPER, Rank.TRIAL}),
	PRO(new Rank[] {Rank.OP, Rank.OWNER, Rank.ADMIN, Rank.SRDEV, Rank.DEV, Rank.JRDEV, Rank.HOST, Rank.SRMOD, Rank.MOD, Rank.HELPER, Rank.TRIAL, Rank.DONATOR_PRO, Rank.FAMOUS, Rank.RETIRED}),
	PLUS(new Rank[] {Rank.OP, Rank.OWNER, Rank.ADMIN, Rank.SRDEV, Rank.DEV, Rank.JRDEV, Rank.HOST, Rank.SRMOD, Rank.MOD, Rank.HELPER, Rank.TRIAL, Rank.DONATOR_PRO, Rank.DONATOR_PLUS, Rank.FAMOUS, Rank.RETIRED}),
	DONATOR(new Rank[] {Rank.OP, Rank.OWNER, Rank.ADMIN, Rank.SRDEV, Rank.DEV, Rank.JRDEV, Rank.HOST, Rank.SRMOD, Rank.MOD, Rank.HELPER, Rank.TRIAL, Rank.DONATOR_PRO, Rank.DONATOR_PLUS, Rank.DONATOR, Rank.FAMOUS, Rank.RETIRED}),
	;
	
	private final Object[] values;
	Permission(Object[]... vals) { values = vals; }
    public Rank[] getRanks() { return (Rank[]) values[0]; }
}
