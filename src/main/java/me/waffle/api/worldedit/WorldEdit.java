package me.waffle.api.worldedit;

import java.io.File;

import org.bukkit.Location;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;

@SuppressWarnings("deprecation")
public class WorldEdit {
	public static void paste(Location loc, String schematic, boolean FastMode, boolean RandomRotate) {
		File file = new File("plugins/WorldEdit/schematics/" + schematic + ".schematic");
		EditSession es = new EditSession(new BukkitWorld(loc.getWorld()), 999999999);
		es.setFastMode(FastMode);
        CuboidClipboard cc;
		try {
			cc = CuboidClipboard.loadSchematic(file);
			
			if(RandomRotate) {
				int rotate = 0;
				int chance = (int) (Math.random()*100);
				if(chance <= 25) {
					rotate = 90;
				} else if(chance <= 50) {
					rotate = 180;
				} else if(chance <= 75) {
					rotate = 270;
				} cc.rotate2D(rotate);
			}
			
			cc.paste(es, new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}