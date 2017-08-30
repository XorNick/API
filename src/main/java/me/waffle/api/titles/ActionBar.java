package me.waffle.api.titles;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import me.waffle.api.API;

public class ActionBar {
	protected String text;
	
	public ActionBar(String text) {
		this.text = text;
	}
	
	public void send(Player player) {
		try {
			
		PacketContainer packet = API.protocols().createPacket(PacketType.Play.Server.CHAT);
		packet.getChatComponents().write(0, WrappedChatComponent.fromText(this.text));
		packet.getBytes().write(0, (byte) 2);
		API.protocols().sendServerPacket(player, packet);
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}