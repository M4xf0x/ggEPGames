package de.m4twaily.npc.main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	static Main i;
	static NPC npc;

	public void onEnable() {
		i = this;

		loadConfig();

		PluginManager pm = Bukkit.getPluginManager();

		pm.registerEvents(new NPCEvents(), this);

		this.getCommand("npc").setExecutor(new NCPCMD());
		
		npc = new NPC();
		
		npc.loadValues();
		npc.createNPC();

		for (Player all : Bukkit.getOnlinePlayers()) {
			npc.sendPackets(all);
		}

	}

	void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
}
