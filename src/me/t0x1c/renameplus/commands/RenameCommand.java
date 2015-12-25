package me.t0x1c.renameplus.commands;

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

public class RenameCommand implements CommandExecutor {
	
	private final Main pl;
	public RenameCommand(Main pl) {
		this.pl = pl;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(pl.colorThis("messages.only-players"));
			return true;
		}
		Player player = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("rename")) {
			if(player.hasPermission("RenamePlus.rename")) {
				if(args.length == 0) {
					player.sendMessage(pl.colorThis("messages.rename-usage"));
					return true;
				}
				for(String words : pl.getConfig().getStringList("banned-words")) {
					if(StringUtils.containsIgnoreCase(name(args), words)) {
						player.sendMessage(pl.colorThis("messages.inappropriate-words"));
						if(pl.getConfig().getBoolean("sounds.enabled")) {
							player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("sounds.inappropriate-words")), 1, 1);
						}
					}
					return true;
				}
				if(name(args).length() < pl.getConfig().getInt("settings.rename-max-length")) {
					player.sendMessage(pl.colorThis("messages.rename-max-length").replaceAll("%amount%", String.valueOf(name(args).length())).replaceAll("%maxamount%", String.valueOf(pl.getConfig().getInt("settings.rename-max-length"))));
					return true;
				}
				ItemStack i = player.getItemInHand();
				ItemMeta im = i.getItemMeta();
				if(i.getType() == Material.AIR) {
					player.sendMessage(pl.colorThis("messages.cannot-rename-air"));
					return true;
				}
				if(name(args).contains("&")) {
	        		if(!player.hasPermission("RenamePlus.colors")) {
			            player.sendMessage(pl.colorThis("messages.no-colors"));
			            return true;
	        		}
	        		im.setDisplayName(colorThis(name(args)));
			        i.setItemMeta(im);
			        player.updateInventory();
			        player.sendMessage(pl.colorThis("messages.successfully-renamed").replaceAll("%name%", colorThis(name(args))));
			        if(pl.getConfig().getBoolean("sounds.enabled")) {
			            player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("sounds.success")), 1, 1);
			        }
			        return true;
	        	}
				im.setDisplayName(name(args));
	            i.setItemMeta(im);
	            player.updateInventory();
	            player.sendMessage(pl.colorThis("messages.successfully-renamed").replaceAll("%name%", colorThis(name(args))));
	            if(pl.getConfig().getBoolean("sounds.enabled")) {
	                player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("sounds.success")), 1, 1);
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
	
	String name(String[] args) {
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