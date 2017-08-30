package me.waffle.api.math;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Maths {
	public static String toRomanNumeral(int number) {
		TreeMap<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1000, "M");
		map.put(900, "CM");
		map.put(500, "D");
		map.put(400, "CD");
		map.put(100, "C");
		map.put(90, "XC");
		map.put(50, "L");
		map.put(40, "XL");
		map.put(10, "X");
		map.put(9, "IX");
		map.put(5, "V");
		map.put(4, "IV");
		map.put(1, "I");
		
		int l = map.floorKey(number);
		if(number == l) {
			return map.get(number);
		} return map.get(l) + toRomanNumeral(number-l);
	}
	
	public static int round(int num, int multiple) {
		return multiple*(Math.round(num/multiple));
	}
	
	public static Long getHowMuchLonger(Long startingTime, Long waitTime) {
		return waitTime-(System.currentTimeMillis()-startingTime);
	}
	
	public static String secondsToString(long millis) {
		long second = (millis/(1000)) % 60;
		long minute = (millis/(1000 * 60)) % 60;
		long hour = (millis/(1000 * 60 * 60)) % 24;
		long day = (millis/(1000 * 60 * 60 * 24));
		
		String time = day + "d " + hour + "h " + minute + "m " + second + "s";
		for(String lt : time.split(" ")) {
			if(lt.contains("d")) {
				if(lt.equals("0d")) {
					time = time.replace("0d ", "");
				}
			} else if(lt.contains("h")) {
				if(lt.equals("0h")) {
					time = time.replace("0h ", "");
				}
			} else if(lt.contains("m")) {
				if(lt.equals("0m")) {
					time = time.replace("0m ", "");
				}
			} else if(lt.contains("s")) {
				if(lt.equals("0s")) {
					time = time.replace(" 0s", "");
					time = time.replace("0s", "");
				}
			}
		}
		
		return time;
		
		/*long seconds = milliseconds / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;
		String time = days + "d " + (hours % 24) + "h " + (minutes % 60) + "m " + (seconds % 60) + "s";

		if(time.split("").length >= 3) {
			if(time.split("")[0].equals("0") && time.split("")[1].equals("d") && time.split("")[2].equals(" ")) {
				time = time.replace("0d ", "");
			}
			if(time.split("")[0].equals("0") && time.split("")[1].equals("h") && time.split("")[2].equals(" ")) {
				time = time.replace("0h ", "");
			}
			if(time.split("")[0].equals("0") && time.split("")[1].equals("m") && time.split("")[2].equals(" ")) {
				time = time.replace("0m ", "");
			}
			if(time.split("")[0].equals("0") && time.split("")[1].equals("s") && time.split("")[2].equals(" ")) {
				time = time.replace(" 0s", "");
				time = time.replace("0s", "");
			}
		} else {
			time = "0s";
		}
		
		if(time.equalsIgnoreCase("")) {
			time = "null";
		}
		
		time = time.replace(" 0h", "").replace(" 0m", "").replace(" 0s", "");
		
		return time;*/
	}
	
	public static String secondsToString(int milliseconds) {
		return secondsToString(Long.parseLong(milliseconds+""));
	}

	public static double percent(double x, double z) {
		NumberFormat formatter = new DecimalFormat("#0.0");
		double perc = (x/z)*100;
		return Double.parseDouble(formatter.format(perc));
	}
	
	public static double distance(int x1, int z1, int x2, int z2) {
		return Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(z2-z1, 2));
	}
	
	public static List<Block> getBlocksInRadius(Location loc, int radius, boolean yCheck) {
		Location l = loc.clone();
    	World w = l.getWorld();
    	int xCoord = (int) l.getX();
    	int zCoord = (int) l.getZ();
    	int YCoord = (int) l.getY();
    	List<Block> tempList = new ArrayList<Block>();
		for(int x = -radius; x <= radius; x++) {
    		for(int z = -radius; z <= radius; z++) {
    			if(yCheck == true) {
	    			for(int y = -radius; y <= radius; y++) {
	    				tempList.add(new Location(w, xCoord + x, YCoord + y, zCoord + z).getBlock());
	    			}
    			} else {
    				tempList.add(new Location(w, xCoord + x, YCoord, zCoord + z).getBlock());
    			}
    		}
    	} return tempList;
    }
	
	public static List<Block> getBlocksInRadius(Location loc, int radius, boolean yCheck, Material material) {
		Location l = loc.clone();
    	World w = l.getWorld();
    	int xCoord = (int) l.getX();
    	int zCoord = (int) l.getZ();
    	int YCoord = (int) l.getY();
    	List<Block> tempList = new ArrayList<Block>();
		for(int x = -radius; x <= radius; x++) {
    		for(int z = -radius; z <= radius; z++) {
    			if(yCheck == true) {
	    			for(int y = -radius; y <= radius; y++) {
	    				Block block = new Location(w, xCoord + x, YCoord + y, zCoord + z).getBlock();
	    				if(block.getType() == material)
	    					tempList.add(block);
	    			}
    			} else {
    				Block block = new Location(w, xCoord + x, YCoord, zCoord + z).getBlock();
    				if(block.getType() == material)
    					tempList.add(block);
    			}
    		}
    	} return tempList;
    }
}

