package me.waffle.api.wrapper;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.PacketConstructor;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.google.common.base.Preconditions;

public class WrapperPlayServerNamedEntitySpawn extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.NAMED_ENTITY_SPAWN;
 
    private static PacketConstructor entityConstructor;
 
    public WrapperPlayServerNamedEntitySpawn() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
 
    public WrapperPlayServerNamedEntitySpawn(PacketContainer packet) {
        super(packet, TYPE);
    }
 
    public WrapperPlayServerNamedEntitySpawn(Player player) {
        super(fromPlayer(player), TYPE);
    }
 
    // Useful constructor
    private static PacketContainer fromPlayer(Player player) {
        if (entityConstructor == null)
            entityConstructor = ProtocolLibrary.getProtocolManager().createPacketConstructor(TYPE, player);
        return entityConstructor.createPacket(player);
    }
 
    public int getEntityID() {
        return handle.getIntegers().read(0);
    }
 
    public void setEntityID(int value) {
        handle.getIntegers().write(0, value);
    }
 
    public Entity getEntity(World world) {
        return handle.getEntityModifier(world).read(0);
    }
 
    public Entity getEntity(PacketEvent event) {
        return getEntity(event.getPlayer().getWorld());
    }
 
    public String getPlayerName() {
        WrappedGameProfile profile = getProfile();
        return profile != null ? profile.getName() : null;
    }
 
    @SuppressWarnings("deprecation")
	public void setPlayerName(String value) {
        if (value != null && value.length() > 16)
            throw new IllegalArgumentException("Maximum player name lenght is 16 characters.");
        setProfile(new WrappedGameProfile(getPlayerUUID(), value));
    }
 
    public String getPlayerUUID() {
        WrappedGameProfile profile = getProfile();
        return profile != null ? profile.getId() : null;
    }
 
    @SuppressWarnings("deprecation")
	public void setPlayerUUID(String uuid) {
        setProfile(new WrappedGameProfile(uuid, getPlayerName()));
    }
 
    public WrappedGameProfile getProfile() {
        return handle.getGameProfiles().read(0);
    }
 
    public void setProfile(WrappedGameProfile value) {
        handle.getGameProfiles().write(0, value);
    }
 
    public Vector getPosition() {
        return new Vector(getX(), getY(), getZ());
    }
 
    public void setPosition(Vector position) {
        setX(position.getX());
        setY(position.getY());
        setZ(position.getZ());
    }
 
    public double getX() {
        return handle.getIntegers().read(1) / 32.0D;
    }
 
    public void setX(double value) {
        handle.getIntegers().write(1, (int) Math.floor(value * 32.0D));
    }
 
    public double getY() {
        return handle.getIntegers().read(2) / 32.0D;
    }
 
    public void setY(double value) {
        handle.getIntegers().write(2, (int) Math.floor(value * 32.0D));
    }
 
    public double getZ() {
        return handle.getIntegers().read(3) / 32.0D;
    }
 
    public void setZ(double value) {
        handle.getIntegers().write(3, (int) Math.floor(value * 32.0D));
    }
 
    public float getYaw() {
        return (handle.getBytes().read(0) * 360.F) / 256.0F;
    }
 
    public void setYaw(float value) {
        handle.getBytes().write(0, (byte) (value * 256.0F / 360.0F));
    }
 
    public float getPitch() {
        return (handle.getBytes().read(1) * 360.F) / 256.0F;
    }
 
    public void setPitch(float value) {
        handle.getBytes().write(1, (byte) (value * 256.0F / 360.0F));
    }
 
    public short getCurrentItem() {
        return handle.getIntegers().read(4).shortValue();
    }
 
    public void setCurrentItem(short value) {
        handle.getIntegers().write(4, (int) value);
    }
 
    public WrappedDataWatcher getMetadata() {
        return handle.getDataWatcherModifier().read(0);
    }
 
    public void setMetadata(WrappedDataWatcher value) {
        handle.getDataWatcherModifier().write(0, value);
    }
 
    @Override
    public PacketContainer getHandle() {
        Preconditions.checkNotNull(getPlayerName(), "Must specify a player name.");
        Preconditions.checkNotNull(getPlayerUUID(), "Must specify a player UUID.");
        return super.getHandle();
    }
}