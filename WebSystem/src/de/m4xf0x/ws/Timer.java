package de.m4xf0x.ws;

import org.bukkit.Bukkit;

public class Timer {
	public int TID;
	public int lastPlayers = -1;
	public int lastOffline = -1;

	public Timer() {
		Main.sendConsoleMsg("Started Timer");

		TID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.main, new Runnable() {

			@Override
			public void run() {

				int online = Bukkit.getOnlinePlayers().size();
				int offline = Bukkit.getOfflinePlayers().length - online;

				if (lastPlayers != online || lastOffline != offline) {

					Main.sendConsoleMsg("Trying to edit Website");

					ViewEditWeb.openConnection();
					ViewEditWeb.editWeb();
					ViewEditWeb.uploadFile();

					Main.sendConsoleMsg("Edited Website");

					lastPlayers = Bukkit.getOnlinePlayers().size();
					lastOffline = Bukkit.getOfflinePlayers().length - Bukkit.getOnlinePlayers().size();
				}

			}

		}, 0L, 100L);

	}
}
