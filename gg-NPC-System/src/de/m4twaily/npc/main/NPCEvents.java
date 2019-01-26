package de.m4twaily.npc.main;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class NPCEvents implements Listener {

	PacketReader pr;

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();

		pr = new PacketReader(p);
		pr.inject();

		Main.npc.sendPackets(p);

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		try {

			pr.uninject();

		} catch (NullPointerException ex) {
			System.out.println("[ERROR] REJOIN TO FIX: " + ex.getCause());
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();

		Main.npc.sendPackets(p);
	}

	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent e) {
		System.out.println(e.getRightClicked().getType());

		System.out.println(e.getRightClicked());

	}

}
