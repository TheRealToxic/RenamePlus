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

public class SetloreCommand implements CommandExecutor {
	
	private final Main pl;
	public SetloreCommand(Main pl) {
		this.pl = pl;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(colorThis(pl.getConfig().getString("Messages.OnlyPlayers")));
			return true;
		}
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("setlore")) {
			if(p.hasPermission("RenamePlus.setlore")) {
				if(args.length == 0) {
					p.sendMessage(colorThis(pl.getConfig().getString("Messages.SetloreUsage")));
					return true;
				}
				for(String words : pl.getConfig().getStringList("Banned-Words")) {
					if(StringUtils.containsIgnoreCase(lore(args), words)) {
						p.sendMessage(colorThis(pl.getConfig().getString("Messages.InappropriateWords")));
						if(pl.getConfig().getBoolean("Sounds.Enabled")) {
							p.playSound(p.getLocation(), Sound.valueOf(pl.getConfig().getString("Sounds.InappropriateWords")), 1, 1);
						} else {
							return true;
						}
						return true;
					}
				}
				if(lore(args).length() < pl.getConfig().getInt("SetloreMaxLength")) {
					p.sendMessage(colorThis(pl.getConfig().getString("Messages.SetloreMaxLength").replaceAll("%amount%", String.valueOf(lore(args).length())).replaceAll("%maxamount%", String.valueOf(pl.getConfig().getInt("AddloreMaxLength")))));
					return true;
				}
				ItemStack i = p.getItemInHand();
				ItemMeta im = i.getItemMeta();
				if(i.getType() == Material.AIR) {
					p.sendMessage(colorThis(pl.getConfig().getString("Messages.CannotSetloreAir")));
					return true;
				}
				if(lore(args).contains("&")) {
	        		if(!p.hasPermission("RenamePlus.colors")) {
			            p.sendMessage(colorThis(pl.getConfig().getString("Messages.NoColors")));
			            return true;
	        		}
	        		List<String> lore = new ArrayList<String>();
        			lore.add(colorThis(lore(args)));
        			im.setLore(lore);
        			i.setItemMeta(im);
		            p.updateInventory();
		            p.sendMessage(colorThis(pl.getConfig().getString("Messages.SuccessfullySetlore")));
		            if(pl.getConfig().getBoolean("Sounds.Enabled")) {
		                p.playSound(p.getLocation(), Sound.valueOf(pl.getConfig().getString("Sounds.Success")), 1, 1);
		            } else {
		               return true;
		            }
				} else if(!lore(args).contains("&")) {
		        	List<String> lore = new ArrayList<String>();
    				lore.add(lore(args));
		            im.setLore(lore);
		            i.setItemMeta(im);
		            p.updateInventory();
		            p.sendMessage(colorThis(pl.getConfig().getString("Messages.SuccessfullySetlore")));
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