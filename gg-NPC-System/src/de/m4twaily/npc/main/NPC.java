package de.m4twaily.npc.main;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;

public class NPC {

	boolean[] valuesSet = { false, false };
	EntityPlayer[] npc = new EntityPlayer[2];
	Location[] location = new Location[2];
	GameProfile[] gameProfile = new GameProfile[2];
	int id[] = new int[2];

	/** Set values locally **/
	public void setNPCValues(Player player, int number) {
		int value = 0;

		if (number == 1) {
			value = 0;
		} else {
			value = 1;
		}

		location[value] = player.getLocation();

		valuesSet[value] = true;

		createNPC();

		id[value] = npc[value].getId();

		saveValues();

		for (Player all : Bukkit.getOnlinePlayers()) {
			sendPackets(all);
		}
	}

	/** Change Skin **/
	private void changeSkin() {

		String texture0 = Main.i.getConfig().getString("Player1.Texture");
		String signature0 = Main.i.getConfig().getString("Player1.Signature");
		gameProfile[0].getProperties().put("textures", new Property("textures", texture0, signature0));

		if (valuesSet[1]) {

			String texture1 = Main.i.getConfig().getString("Player2.Texture");
			String signature1 = Main.i.getConfig().getString("Player2.Signature");
			gameProfile[1].getProperties().put("textures", new Property("textures", texture1, signature1));

		}
	}

	/** Save values in Config **/
	private void saveValues() {

		FileConfiguration config = Main.i.getConfig();

		config.set("Player1.location.world", location[0].getWorld().getName());
		config.set("Player1.location.x", location[0].getX());
		config.set("Player1.location.y", location[0].getY());
		config.set("Player1.location.z", location[0].getZ());
		config.set("Player1.location.yaw", location[0].getYaw());
		config.set("Player1.location.pitch", location[0].getPitch());
		config.set("Player1.id", id[0]);
		config.set("Player1.valuesSet", valuesSet[0]);

		if (valuesSet[1]) {

			config.set("Player2.location.world", location[1].getWorld().getName());
			config.set("Player2.location.x", location[1].getX());
			config.set("Player2.location.y", location[1].getY());
			config.set("Player2.location.z", location[1].getZ());
			config.set("Player2.location.yaw", location[1].getYaw());
			config.set("Player2.location.pitch", location[1].getPitch());
			config.set("Player2.id", id[1]);
			config.set("Player2.valuesSet", valuesSet[1]);

		}

		Main.i.saveConfig();
	}

	/** loading values from config **/
	public void loadValues() {

		FileConfiguration config = Main.i.getConfig();

		valuesSet[0] = config.getBoolean("Player1.valuesSet");
		valuesSet[1] = config.getBoolean("Player2.valuesSet");

		World world0 = Bukkit.getWorld(config.getString("Player1.location.world"));
		double x0 = config.getDouble("Player1.location.x");
		double y0 = config.getDouble("Player1.location.y");
		double z0 = config.getDouble("Player1.location.z");
		float yaw0 = (float) config.getDouble("Player1.location.yaw");
		float pitch0 = (float) config.getDouble("Player1.location.pitch");
		location[0] = new Location(world0, x0, y0, z0, yaw0, pitch0);
		id[0] = config.getInt("Player1.id");

		if (valuesSet[1]) {

			World world1 = Bukkit.getWorld(config.getString("Player2.location.world"));
			double x1 = config.getDouble("Player2.location.x");
			double y1 = config.getDouble("Player2.location.y");
			double z1 = config.getDouble("Player2.location.z");
			float yaw1 = (float) config.getDouble("Player2.location.yaw");
			float pitch1 = (float) config.getDouble("Player2.location.pitch");
			location[1] = new Location(world1, x1, y1, z1, yaw1, pitch1);
			id[1] = config.getInt("Player2.id");

		}

	}

	/** create Npc with local values **/
	public void createNPC() {

		if (valuesSet[0]) {

			MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
			WorldServer nmsWorld = ((CraftWorld) location[0].getWorld()).getHandle();
			gameProfile[0] = new GameProfile(UUID.randomUUID(), "§a§lRewards");

			npc[0] = new EntityPlayer(nmsServer, nmsWorld, gameProfile[0], new PlayerInteractManager(nmsWorld));

			npc[0].setLocation(location[0].getX(), location[0].getY(), location[0].getZ(), location[0].getYaw(),
					location[0].getPitch());

			if (valuesSet[1]) {

				MinecraftServer nmsServer1 = ((CraftServer) Bukkit.getServer()).getServer();
				WorldServer nmsWorld1 = ((CraftWorld) location[1].getWorld()).getHandle();
				gameProfile[1] = new GameProfile(UUID.randomUUID(), "§a§lRewards");

				npc[1] = new EntityPlayer(nmsServer1, nmsWorld1, gameProfile[1], new PlayerInteractManager(nmsWorld));

				npc[1].setLocation(location[1].getX(), location[1].getY(), location[1].getZ(), location[1].getYaw(),
						location[1].getPitch());
			}

			changeSkin();

		}
	}

	/** sending Packets with local values **/
	public void sendPackets(Player p) {

		if (valuesSet[0]) {
			try {

				TimeUnit.SECONDS.sleep(1);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
			connection.sendPacket(
					new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc[0]));
			connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc[0]));
			connection.sendPacket(
					new PacketPlayOutEntityHeadRotation(npc[0], (byte) ((location[0].getYaw() * 256.0F) / 360.0F)));
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.i, new Runnable() {

				@Override
				public void run() {
					connection.sendPacket(new PacketPlayOutPlayerInfo(
							PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc[0]));
				}
			}, 50);

			if (valuesSet[1]) {

				connection.sendPacket(
						new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc[1]));
				connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc[1]));
				connection.sendPacket(
						new PacketPlayOutEntityHeadRotation(npc[1], (byte) ((location[1].getYaw() * 256.0F) / 360.0F)));
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.i, new Runnable() {

					@Override
					public void run() {
						connection.sendPacket(new PacketPlayOutPlayerInfo(
								PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc[1]));
					}
				}, 50);
			}

		}
	}
	
	public int getID(int number) {
		return npc[number].getId();
		
	}
}
