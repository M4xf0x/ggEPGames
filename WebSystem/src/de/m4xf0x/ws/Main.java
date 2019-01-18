package de.m4xf0x.ws;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public static Main main;
	public static String consolePrefix = ChatColor.YELLOW + "WEBSYSTEM - ";

	public void onEnable() {
		main = this;
		
		loadConfig();
		new Timer();
	}

	public static void sendConsoleMsg(String msg) {

		Bukkit.getServer().getConsoleSender().sendMessage(consolePrefix + msg);

	}
	
	void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
}
