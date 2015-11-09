package me.t0x1c.renameplus;

import me.t0x1c.renameplus.commands.AddloreCommand;
import me.t0x1c.renameplus.commands.RemoveloreCommand;
import me.t0x1c.renameplus.commands.RenameCommand;
import me.t0x1c.renameplus.commands.SetloreCommand;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public void onEnable() {
		saveDefaultConfig();
		reloadConfig();
		loadCommands();
	}
	
	public void loadCommands() {
		getCommand("rename").setExecutor(new RenameCommand(this));
		getCommand("addlore").setExecutor(new AddloreCommand(this));
		getCommand("setlore").setExecutor(new SetloreCommand(this));
		getCommand("removelore").setExecutor(new RemoveloreCommand(this));
	}
}