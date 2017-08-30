package me.waffle.api.users;

public class Ranks {
	public static enum Rank {
		OP("op", "Operator", "ยง7"),
		OWNER("owner", "Owner", "&4[Owner]"),
		ADMIN("admin", "Admin", "&4[Admin]"),
		SRDEV("srdev", "Sr.Dev", "&4[Sr.Dev]"),
		DEV("dev", "Developer", "&4[Dev]"),
		JRDEV("jrdev", "Jr.Dev", "&4[Jr.Dev]"),
		HOST("host", "Host", "&c[Host]"),
		SRMOD("srmod", "Sr.Mod", "&c[Sr.Mod]"),
		MOD("mod", "Mod", "&c[Mod]"),
		HELPER("helper", "Helper", "&3[Helper]"),
		TRIAL("trial", "Trial", "&3[Trial]"),
		RETIRED("retired", "Retired", "&6[Retired]"),
		FAMOUS("famous", "Famous", "&5[Famous]"),
		DONATOR_PRO("donator-pro", "Donator Pro", "&a[$$$]"),
		DONATOR_PLUS("donator-plus", "Donator+", "&a[$$]"),
		DONATOR("donator", "Donator", "&a[$]"),
		DEFAULT("default", "Default", "&7"),
		;
		
		private final Object[] values;
		Rank(Object... vals) { values = vals; }
	    public String getValue() { return (String) values[0]; }
	    public String getDisplay() { return (String) values[1]; }
	    public String getPrefix() { return ((String) values[2]).replace("&", "ยง"); }
	    public String getColor() { return "ยง"+((String) values[2]).split("")[1]; }
	}
}

