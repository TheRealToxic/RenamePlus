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
			Bukkit.getConsoleSender().sendMessage(colorThis(pl.getConfig().getString("Messages.OnlyPlayers")));
			return true;
		}
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("rename")) {
			if(p.hasPermission("RenamePlus.rename")) {
				if(args.length == 0) {
					p.sendMessage(colorThis(pl.getConfig().getString("Messages.RenameUsage")));
					return true;
				}
				for(String words : pl.getConfig().getStringList("Banned-Words")) {
					if(StringUtils.containsIgnoreCase(name(args), words)) {
						p.sendMessage(colorThis(pl.getConfig().getString("Messages.InappropriateWords")));
						if(pl.getConfig().getBoolean("Sounds.Enabled")) {
							p.playSound(p.getLocation(), Sound.valueOf(pl.getConfig().getString("Sounds.InappropriateWords")), 1, 1);
						} else {
							return true;
						}
						return true;
					}
				}
				if(name(args).length() < pl.getConfig().getInt("RenameMaxLength")) {
					p.sendMessage(colorThis(pl.getConfig().getString("Messages.RenameMaxLength").replaceAll("%amount%", String.valueOf(name(args).length())).replaceAll("%maxamount%", String.valueOf(pl.getConfig().getInt("RenameMaxLength")))));
					return true;
				}
				ItemStack i = p.getItemInHand();
				ItemMeta im = i.getItemMeta();
				if(i.getType() == Material.AIR) {
					p.sendMessage(colorThis(pl.getConfig().getString("Messages.CannotRenameAir")));
					return true;
				}
				if(name(args).contains("&")) {
	        		if(!p.hasPermission("RenamePlus.colors")) {
			            p.sendMessage(colorThis(pl.getConfig().getString("Messages.NoColors")));
			            return true;
	        		}
	        		im.setDisplayName(colorThis(name(args)));
			        i.setItemMeta(im);
			        p.updateInventory();
			        p.sendMessage(colorThis(pl.getConfig().getString("Messages.SuccessfullyRenamed")));
			        if(pl.getConfig().getBoolean("Sounds.Enabled")) {
			            p.playSound(p.getLocation(), Sound.valueOf(pl.getConfig().getString("Sounds.Success")), 1, 1);
			        } else {
			            return true;
			        }
	        	} else {
	        		im.setDisplayName(name(args));
		            i.setItemMeta(im);
		            p.updateInventory();
		            p.sendMessage(colorThis(pl.getConfig().getString("Messages.SuccessfullyRenamed")));
		            if(pl.getConfig().getBoolean("Sounds.Enabled")) {
		                p.playSound(p.getLocation(), Sound.valueOf(pl.getConfig().getString("Sounds.Success")), 1, 1);
		            } else {
		                return true;
		            }
	        	}
			} else {
				p.sendMessage(colorThis(pl.getConfig().getString("Messages.NoPermissions")));
				if(pl.getConfig().getBoolean("Sounds.Enabled")) {
					p.playSound(p.getLocation(), Sound.valueOf(pl.getConfig().getString("Sounds.NoPermissions")), 1, 1);
				} else {
					return true;
				}
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