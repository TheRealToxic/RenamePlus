package me.t0x1c.renameplus.commands;

import java.util.ArrayList;
import java.util.List;

import me.t0x1c.renameplus.Main;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AddloreCommand implements CommandExecutor {
	
	private final Main pl;
	public AddloreCommand(Main pl) {
		this.pl = pl;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(pl.colorThis("messages.only-players"));
			return true;
		}
		Player player = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("addlore")) {
			if(player.hasPermission("RenamePlus.addlore")) {
				if(args.length == 0) {
					player.sendMessage(pl.colorThis("messages.addlore-usage"));
					return true;
				}
				for(String words : pl.getConfig().getStringList("banned-words")) {
					if(StringUtils.containsIgnoreCase(lore(args), words)) {
						player.sendMessage(pl.colorThis("messages.inappropriate-words"));
						if(pl.getConfig().getBoolean("sounds.enabled")) {
							player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("sounds.inappropriate-words")), 1, 1);
						}
					}
					return true;
				}
				if(lore(args).length() < pl.getConfig().getInt("settings.addlore-max-length")) {
					player.sendMessage(pl.colorThis("messages.addlore-max-length").replaceAll("%amount%", String.valueOf(lore(args).length())).replaceAll("%maxamount%", String.valueOf(pl.getConfig().getInt("settings.addlore-max-length"))));
					return true;
				}
				ItemStack i = player.getItemInHand();
				ItemMeta im = i.getItemMeta();
				if(i.getType() == Material.AIR) {
					player.sendMessage(pl.colorThis("messages.cannot-addlore-air"));
					return true;
				}
				if(lore(args).contains("&")) {
	        		if(!player.hasPermission("RenamePlus.colors")) {
	        			player.sendMessage(pl.colorThis("messages.no-colors"));
	        			return true;
	        		}
	        		if(im.hasLore()) {
	        			List<String> lore = im.getLore();
		        		lore.add(colorThis(lore(args)));
		        		im.setLore(lore);
		        		i.setItemMeta(im);
				        player.updateInventory();
				        player.sendMessage(pl.colorThis("messages.successfully-addedlore").replaceAll("%lore%", colorThis(lore(args))));
				        if(pl.getConfig().getBoolean("sounds.enabled")) {
				        	player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("sounds.success")), 1, 1);
				        }
	        		} else {
	        			List<String> lore = new ArrayList<String>();
		        		lore.add(colorThis(lore(args)));
		        		im.setLore(lore);
		        		i.setItemMeta(im);
				        player.updateInventory();
				        player.sendMessage(pl.colorThis("messages.successfully-addedlore"));
				        if(pl.getConfig().getBoolean("sounds.enabled")) {
				            player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("sounds.success")), 1, 1);
	        			}
	        		}
				} else if(!lore(args).contains("&")) {
		        	List<String> lore = im.getLore();
		            lore.add(lore(args));
		            im.setLore(lore);
		            i.setItemMeta(im);
		            player.updateInventory();
		            player.sendMessage(pl.colorThis("messages.successfully-addedlore").replaceAll("%lore%", colorThis(lore(args))));
		            if(pl.getConfig().getBoolean("sounds.enabled")) {
		                player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("sounds.success")), 1, 1);
		            }
		        }
				return true;
			}
			player.sendMessage(pl.colorThis("messages.no-permissions"));
			if(pl.getConfig().getBoolean("sounds.enabled")) {
				player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("sounds.no-permissions")), 1, 1);
			}
		}
		return true;
	}
	
	String lore(String[] args) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < args.length; i++) {
			builder.append(args[i]);
			builder.append(" ");
		}
		return builder.toString().trim();
	}
	
	String colorThis(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
}