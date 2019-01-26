package de.m4twaily.npc.main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NCPCMD implements CommandExecutor {

	NPC npc;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;

			npc = Main.npc;

			npc.setNPCValues(p, Integer.parseInt(args[0]));
			npc.sendPackets(p);

		}

		return false;
	}

}
