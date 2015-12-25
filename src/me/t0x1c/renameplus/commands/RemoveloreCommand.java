package me.t0x1c.renameplus.commands;

import java.util.List;

import me.t0x1c.renameplus.Main;

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

public class RemoveloreCommand implements CommandExecutor {
	
	private final Main pl;
	public RemoveloreCommand(Main pl) {
		this.pl = pl;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(pl.colorThis("messages.only-players"));
			return true;
		}
		Player player = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("removelore")) {
			if(player.hasPermission("RenamePlus.removelore")) {
				if(args.length == 0) {
					player.sendMessage(pl.colorThis("messages.removelore-usage"));
					return true;
				}
				ItemStack i = player.getItemInHand();
				ItemMeta im = i.getItemMeta();
				if(i.getType() == Material.AIR) {
					player.sendMessage(pl.colorThis("messages.cannot-removelore-air"));
					return true;
				}
				if(im.hasLore()) {
					List<String> lore = im.getLore();
					int number = Integer.parseInt(args[0]) - 1;
					if(lore.size() < number) {
						player.sendMessage(pl.colorThis("messages.invalid-lore-number").replaceAll("%amount%", String.valueOf(im.getLore().size())));
						return true;
					}
					if(args[0].contains("-")) {
						player.sendMessage(pl.colorThis("messages.no-"));
						return true;
					}
					if(args[0].contains("0")) {
						player.sendMessage(pl.colorThis("messages.no-0"));
						return true;
					}
					lore.remove(number);
					im.setLore(lore);
					i.setItemMeta(im);
					player.sendMessage(pl.colorThis("messages.successfully-removedlore").replaceAll("%lore%", colorThis(im.getLore().get(number))).replaceAll("%line%", args[0]));
					if(pl.getConfig().getBoolean("sounds.enabled")) {
			            player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("sounds.success")), 1, 1);
        			}
					return true;
				}
				if(!im.hasLore()) {
					player.sendMessage(pl.colorThis("messages.no-lore"));
					return true;
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
	
	String colorThis(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
}