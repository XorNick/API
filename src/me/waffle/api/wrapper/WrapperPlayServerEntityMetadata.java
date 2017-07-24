package me.waffle.api.wrapper;

import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Entity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;

public class WrapperPlayServerEntityMetadata extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.ENTITY_METADATA;
    
    public WrapperPlayServerEntityMetadata() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerEntityMetadata(PacketContainer packet) {
        super(packet, TYPE);
    }
  
    public int getEntityId() {
        return handle.getIntegers().read(0);
    }
    
    public void setEntityId(int value) {
        handle.getIntegers().write(0, value);
    }
    
    public Entity getEntity(World world) {
    	return handle.getEntityModifier(world).read(0);
    }

    public Entity getEntity(PacketEvent event) {
    	return getEntity(event.getPlayer().getWorld());
    }
    
    
    public List<WrappedWatchableObject> getEntityMetadata() {
        return handle.getWatchableCollectionModifier().read(0);
    }
    
    public void setEntityMetadata(List<WrappedWatchableObject> value) {
        handle.getWatchableCollectionModifier().write(0, value);
    }
}
