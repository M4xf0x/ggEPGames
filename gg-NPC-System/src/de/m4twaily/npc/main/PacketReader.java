package de.m4twaily.npc.main;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.lang.reflect.Field;
import java.util.List;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketReader {

	Player player;
	Channel channel;

	public PacketReader(Player player) {
		this.player = player;
	}

	public void inject() {

		CraftPlayer cPlayer = (CraftPlayer) this.player;
		channel = cPlayer.getHandle().playerConnection.networkManager.channel;
		channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet<?>>() {

			@Override
			protected void decode(ChannelHandlerContext arg0, Packet<?> packet, List<Object> arg2) throws Exception {
				arg2.add(packet);
				readPacket(packet);
			}
		});
	}

	public void uninject() {
		if (channel.pipeline().get("PacketInjector") != null) {
			channel.pipeline().remove("PacketInjector");
		}
	}

	public void readPacket(Packet<?> packet) {

		if (packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
			int id = (Integer) getValue(packet, "a");

			Main.npc.loadValues();
			System.out.println(Main.npc.id[0] + " - " + Main.npc.id[1]);
			System.out.println(Main.npc.getID(0) + " - " + Main.npc.getID(1));
			
			if (Main.npc.getID(0) == id || Main.npc.getID(1) == id || Main.npc.id[0] == id || Main.npc.id[1] == id) {

				if (getValue(packet, "action").toString().equalsIgnoreCase("INTERACT")) {

					Bukkit.broadcastMessage("§aWorking");
				}

			}
		}
	}

	public void setValue(Object obj, String name, Object value) {
		try {

			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(obj, value);

		} catch (Exception e) {
		}
	}

	public Object getValue(Object obj, String name) {
		try {

			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(obj);

		} catch (Exception e) {
		}
		return null;
	}

}