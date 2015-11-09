package me.t0x1c.renameplus.commands;

import java.util.List;

import me.t0x1c.renameplus.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
			Bukkit.getConsoleSender().sendMessage(colorThis(pl.getConfig().getString("Messages.OnlyPlayers")));
			return true;
		}
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("removelore")) {
			if(p.hasPermission("RenamePlus.removelore")) {
				if(args.length == 0) {
					p.sendMessage(colorThis(pl.getConfig().getString("Messages.RemoveloreUsage")));
					return true;
				}
				ItemStack i = p.getItemInHand();
				ItemMeta im = i.getItemMeta();
				if(i.getType() == Material.AIR) {
					p.sendMessage(colorThis(pl.getConfig().getString("Messages.CannotRemoveloreAir")));
					return true;
				}
				if(im.hasLore()) {
					List<String> lore = im.getLore();
					int number = Integer.parseInt(args[0]) - 1;
					if(lore.size() < number) {
						p.sendMessage(colorThis(pl.getConfig().getString("Messages.InvalidLoreNumber").replaceAll("%amount%", String.valueOf(im.getLore().size()))));
						return true;
					}
					if(args[0].contains("-")) {
						p.sendMessage(colorThis(pl.getConfig().getString("Messages.No-")));
						return true;
					}
					if(args[0].contains("0")) {
						p.sendMessage(colorThis(pl.getConfig().getString("Messages.No0")));
						return true;
					}
					lore.remove(number);
					im.setLore(lore);
					i.setItemMeta(im);
					p.sendMessage(colorThis(pl.getConfig().getString("Messages.SuccessfullyRemovedlore").replaceAll("%line%", args[0])));
					return true;
				}
				if(!im.hasLore()) {
					p.sendMessage(colorThis(pl.getConfig().getString("Messages.NoLore")));
					return true;
				}
			}
			if(!p.hasPermission("RenamePlus.removelore")) {
				p.sendMessage(colorThis(pl.getConfig().getString("Messages.NoPermissions")));
			}
		}
		return true;
	}
	
	String colorThis(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
}