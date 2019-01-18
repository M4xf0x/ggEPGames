package de.m4xf0x.ws;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.bukkit.Bukkit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ViewEditWeb {
	static File input;

	static void editWeb() {
		try {

			Main.sendConsoleMsg("Editing Website");

			input = new File("index.html");
			Document doc = Jsoup.connect("http://m4x.bplaced.net/").get();
			PrintWriter writer = new PrintWriter(input, "UTF-8");
			Element div = doc.getElementById("online");
			int online = Bukkit.getOnlinePlayers().size();
			int offline = Bukkit.getOfflinePlayers().length - online;
			int slots = Bukkit.getServer().getMaxPlayers();
			Element offlineDiv = doc.getElementById("offline");

			offlineDiv.text(offline + " SPIELER SIND OFFLINE");

			div.text(online + " / " + slots + " ONLINE");

			writer.write(doc.html());
			writer.flush();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	static void openConnection() {
		try {

			Main.sendConsoleMsg("Opening Connection");

			if (Main.main.getConfig().getBoolean("ftp.ready")) {

				FTP.server = Main.main.getConfig().getString("ftp.server");
				FTP.port = Main.main.getConfig().getInt("ftp.port");
				FTP.user = Main.main.getConfig().getString("ftp.user");
				FTP.password = Main.main.getConfig().getString("ftp.password");

				FTP.open();
			} else {
				Bukkit.broadcastMessage("§6§lWerte in Config setzen!");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	static void uploadFile() {
		try {

			Main.sendConsoleMsg("Uploading File");

			FTP.upload(input, "/www/index.html");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
