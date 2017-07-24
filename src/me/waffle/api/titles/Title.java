package me.waffle.api.titles;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers.TitleAction;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import me.waffle.api.API;

public class Title {
	protected String title;
	protected String subtitle;
	protected int fadeIn;
	protected int stay;
	protected int fadeOut;
	
	public Title(String title, String subtitle) {
		this.title = title;
		this.subtitle = subtitle;
		this.fadeIn = 20;
		this.stay = 20;
		this.fadeOut = 20;
	}
	
	public Title setTimes(int fadeIn, int stay, int fadeOut) {
		this.fadeIn = fadeIn;
		this.stay = stay;
		this.fadeOut = fadeOut;
		return this;
	}
	
	public void send(Player player) {
		try {
		
		if(this.title != null) {
			PacketContainer packet = API.protocols().createPacket(PacketType.Play.Server.TITLE);
			packet.getTitleActions().write(0, TitleAction.TITLE);
			packet.getChatComponents().write(0, WrappedChatComponent.fromText(this.title));
			API.protocols().sendServerPacket(player, packet);
		} 
		
		if(this.subtitle != null) {
			PacketContainer packet = API.protocols().createPacket(PacketType.Play.Server.TITLE);
			packet.getTitleActions().write(0, TitleAction.SUBTITLE);
			packet.getChatComponents().write(0, WrappedChatComponent.fromText(this.subtitle));
			API.protocols().sendServerPacket(player, packet);
		}
		
		PacketContainer packet = API.protocols().createPacket(PacketType.Play.Server.TITLE);
		packet.getTitleActions().write(0, TitleAction.TIMES);
		StructureModifier<Integer> ints = packet.getIntegers();
		ints.write(0, this.fadeIn);
		ints.write(1, this.stay);
		ints.write(2, this.fadeOut);
		API.protocols().sendServerPacket(player, packet);
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
