package me.waffle.api.item;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class Item {
	public static class Banner {
		protected boolean isSet;
		protected ArrayList<Pattern> patterns = new ArrayList<Pattern>();
		protected DyeColor baseColor;
		
		public Banner() {
			this.isSet = false;
			this.patterns = new ArrayList<Pattern>();
			this.baseColor = DyeColor.BLACK;
		}
		
		public boolean created() {
			return this.isSet;
		}
		
		public Banner add(Pattern pattern) {
			this.isSet = true;
			this.patterns.add(pattern);
			return this;
		}
		
		public Banner setBase(DyeColor dye) {
			this.isSet = true;
			this.baseColor = dye;
			return this;
		}
		
		public ArrayList<Pattern> getPatterns() {
			return this.patterns;
		}
		
		public DyeColor getBase() {
			return this.baseColor;
		}
	}
	
	public static class Enchants {
		protected HashMap<Enchantment, Integer> enchs;
		
		public Enchants() {
			enchs = new HashMap<Enchantment, Integer>();
		}
		
		public Enchants add(Enchantment e, int l) {
			enchs.put(e, l);
			return this;
		}
		
		public HashMap<Enchantment, Integer> get() {
			return this.enchs;
		}
	}
	
	protected ItemStack item;
	protected Color color;
	protected String name;
	protected String[] lore;
	protected Enchants enchs;
	protected Banner banner;
	protected boolean unbreakable;
	protected boolean hideStuff;
	protected String skullTexture;
	protected String skullOwner;
	
	public Item() {
		this.item = null;
		this.color = null;
		this.name = null;
		this.lore = null;
		this.enchs = new Enchants();
		this.banner = new Banner();
		this.unbreakable = false;
		this.hideStuff = false;
		this.skullTexture = null;
		this.skullOwner = null;
	}
	
	public Item setItem(ItemStack item) {
		this.item = item;
		return this;
	}
	
	public Item setColor(Color color) {
		this.color = color;
		return this;
	}
	
	public Item setName(String name) {
		this.name = name;
		return this;
	}
	
	public Item setLore(String[] lore) {
		this.lore = lore;
		return this;
	}
	
	public Item setEnchants(Enchants enchs) {
		this.enchs = enchs;
		return this;
	}
	
	public Item setBanner(Banner banner) {
		this.banner = banner;
		return this;
	}
	
	public Item setUnbreakable(boolean unbreakable) {
		this.unbreakable = unbreakable;
		return this;
	}
	
	public Item setHideStuff(boolean hideStuff) {
		this.hideStuff = hideStuff;
		return this;
	}

	public Item setSkullOwner(String name) {
		this.skullOwner = name;
		return this;
	}
	
	public Item setSkullTexture(String url) {
		this.skullTexture = url;
		return this;
	}
	
	public ItemStack build() {
		try {
			ItemMeta im = this.item.getItemMeta();
			if(this.name != null) {
				im.setDisplayName(this.name);
			} if(this.lore != null) {
				im.setLore(Arrays.asList(this.lore));
			} if(this.enchs != null) {
				for(Enchantment le : this.enchs.get().keySet()) {
					im.addEnchant(le, this.enchs.get().get(le), true);
				}
			} if(this.hideStuff) {
				im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				im.addItemFlags(ItemFlag.HIDE_DESTROYS);
				im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				im.addItemFlags(ItemFlag.HIDE_PLACED_ON);
				im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
			} im.spigot().setUnbreakable(this.unbreakable);
			
			if(this.banner.created()) {
				BannerMeta bm = (BannerMeta) im.clone();
				bm.setBaseColor(this.banner.getBase());
				for(Pattern lp : this.banner.getPatterns()) {
					bm.addPattern(lp);
				} im = bm.clone();
			}
			
			if(this.skullOwner != null && this.item.getType() == Material.SKULL_ITEM) {
				SkullMeta sm = (SkullMeta) im.clone();
				sm.setOwner(this.skullOwner);
				im = sm.clone();
			} else if(this.skullTexture != null && this.item.getType() == Material.SKULL_ITEM) {
				SkullMeta hiSM = (SkullMeta) im.clone();
				GameProfile profile = new GameProfile(UUID.randomUUID(), null);
				byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", this.skullTexture).getBytes());
				profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
				Field profileField = null;
				profileField = hiSM.getClass().getDeclaredField("profile");
				profileField.setAccessible(true);
				profileField.set(hiSM, profile);
				im = hiSM.clone();
			}
			
			if(item.getType() == Material.LEATHER_BOOTS || item.getType() == Material.LEATHER_LEGGINGS || item.getType() == Material.LEATHER_CHESTPLATE || item.getType() == Material.LEATHER_HELMET) {
				if(this.color != null) {
					LeatherArmorMeta lm = (LeatherArmorMeta) im.clone();
					lm.setColor(this.color);
					im = lm.clone();
				}
			}
			
			item.setItemMeta(im);
			return item;
		} catch(Exception e) {
			e.printStackTrace();
			return this.item;
		}
	}
}

