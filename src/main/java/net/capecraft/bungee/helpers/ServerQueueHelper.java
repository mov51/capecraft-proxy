package net.capecraft.bungee.helpers;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.capecraft.Main;
import net.capecraft.bungee.BungeeMain;
import net.capecraft.bungee.helpers.config.PluginConfig;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ServerQueueHelper {

	private static Queue<UUID> survivalQueue = new LinkedList<UUID>();
	private static Queue<UUID> creativeQueue = new LinkedList<UUID>();

	private static int playersOnline = 0;
	private static int playersMax = 0;

	/**
	 * Attempts to connect the player to the server otherwise queues them
	 * 
	 * @param player     the player
	 * @param serverName the server name
	 */
	public static void connectPlayer(ProxiedPlayer player, String serverName) {
		ServerInfo targetServer = ProxyServer.getInstance().getServerInfo(serverName);

		if (getQueueSize(serverName) == 0) {
			targetServer.ping(new Callback<ServerPing>() {

				@Override
				public void done(ServerPing result, Throwable error) {
					playersOnline = result.getPlayers().getOnline();
					playersMax = result.getPlayers().getMax();

					// If there is space, move player to server
					// Else if player is an ALT don't let them in
					// Else check for AFK players to make space
					if (playersOnline < playersMax) {
						player.connect(targetServer);
						return;
					} else if (player.hasPermission(Main.Groups.ALT)) {
						BaseComponent[] disconnectMsg = TextComponent.fromLegacyText(PluginConfig.getPluginConfig().getString(PluginConfig.FULL_AFK));
						player.sendMessage(new ComponentBuilder(Main.PREFIX).append(disconnectMsg).reset().create());
						return;
					} else {
						// Create disconnects message
						BaseComponent[] disconnectMsg = TextComponent.fromLegacyText(PluginConfig.getPluginConfig().getString(PluginConfig.KICK_AFK));
						BaseComponent[] disconnectBroadcast = TextComponent.fromLegacyText(PluginConfig.getPluginConfig().getString(PluginConfig.KICK_AFK_BROADCAST));

						// Try and kick AFK players until players are less than max
						ProxiedPlayer afkPlayer = AfkHelper.getNextPlayer(serverName);
						while (afkPlayer != null) {
							afkPlayer.disconnect(new ComponentBuilder(Main.PREFIX).append(disconnectMsg).reset().create());
							afkPlayer = AfkHelper.getNextPlayer(serverName);
							playersOnline--;

							if (playersOnline < playersMax)
								break;
						}

						// If players are less than max try connect our player
						// Else add player to the queue
						if (playersOnline < playersMax) {
							ProxyServer.getInstance().getScheduler().schedule(BungeeMain.INSTANCE, new Runnable() {
								@Override
								public void run() {
									player.connect(targetServer);
								}
							}, 1, TimeUnit.SECONDS);

							ProxyServer.getInstance().getServerInfo(serverName).getPlayers().forEach(player -> {
								player.sendMessage(new ComponentBuilder(Main.PREFIX).append(disconnectBroadcast).reset().create());
							});
							return;
						} else {
							addPlayerToQueue(player, serverName);
							sendQueueMessage(player.getUniqueId(), getQueueSize(serverName));
							return;
						}
					}
				}
			});
		}
	}

	/**
	 * Polls the server and attempts to join users from a queue
	 * 
	 * @param serverName The Server to test
	 */
	public static void pollServer(String serverName) {
		// The server to connect to
		ServerInfo targetServer = ProxyServer.getInstance().getServerInfo(serverName);

		// Make sure queue isn't empty
		if (getQueueSize(serverName) > 0) {

			// Pings the server and checks online < max. If so tries to join players
			targetServer.ping(new Callback<ServerPing>() {
				@Override
				public void done(ServerPing result, Throwable error) {
					if (result.getPlayers().getOnline() < result.getPlayers().getMax()) {
						ProxiedPlayer nextPlayer = getNextPlayer(serverName);
						nextPlayer.connect(targetServer);
						removePlayer(nextPlayer.getUniqueId());
					}
				}
			});
		}
	}

	/**
	 * Gets all players in queue to send messages to
	 * 
	 * @param serverName Server Name to send queue message for
	 */
	private static void sendQueueMessages(String serverName) {
		//The starting queue pos
		int queuePlace = 1;

		if(serverName.equals(Main.Servers.SURVIVAL)) {
			//Loops through survivalQueue to send messages
			for(UUID uuid : survivalQueue) {
				sendQueueMessage(uuid, queuePlace);
				queuePlace++;
			}
		} else if(serverName.equals(Main.Servers.CREATIVE)) {
			//Loops through creative queue to send messages
			for(UUID uuid : creativeQueue) {
				sendQueueMessage(uuid, queuePlace);
				queuePlace++;
			}
		}
	}

	/**
	 * Sends the final queue message
	 * 
	 * @param uuid UUID Target
	 * @param pos  Queue Position
	 */
	private static void sendQueueMessage(UUID uuid, int pos) {
		String msgRaw = PluginConfig.getPluginConfig().getString(PluginConfig.QUEUE_STATUS);
		msgRaw = msgRaw.replace("%place%", String.valueOf(pos));
		BaseComponent[] msg = new ComponentBuilder(Main.PREFIX)
				.append(TextComponent.fromLegacyText(msgRaw, ChatColor.WHITE)).color(ChatColor.AQUA).create();
		ProxyServer.getInstance().getPlayer(uuid).sendMessage(msg);
	}

	/**
	 * Gets the first player in the queue and then removed them. Returns proxied
	 * player to be handled
	 * 
	 * @param serverName The server name
	 * @return The ProxiedPlayer object
	 */
	private static ProxiedPlayer getNextPlayer(String serverName) {
		if (serverName.equals(Main.Servers.SURVIVAL)) {
			UUID uuid = survivalQueue.poll();
			return ProxyServer.getInstance().getPlayer(uuid);
		} else if (serverName.equals(Main.Servers.CREATIVE)) {
			UUID uuid = creativeQueue.poll();
			return ProxyServer.getInstance().getPlayer(uuid);
		} else {
			return null;
		}
	}

	/**
	 * Gets the servers queue size
	 * 
	 * @param serverName The name of the server
	 */
	public static int getQueueSize(String serverName) {
		if (serverName.equals(Main.Servers.SURVIVAL)) {
			return survivalQueue.size();
		} else if (serverName.equals(Main.Servers.CREATIVE)) {
			return creativeQueue.size();
		}
		return 0;
	}

	/**
	 * Adds a player to the queue or connects them straight away if they have
	 * permissions
	 * 
	 * @param serverName The server name to queue
	 * @param uuid       The players uuid
	 */
	public static void addPlayer(String serverName, ProxiedPlayer player) {
		// Check player isn't in the server
		if (player.getServer().getInfo().getName().equalsIgnoreCase(serverName)) {
			player.sendMessage(new ComponentBuilder(Main.PREFIX).append("You're already in this server...").reset().create());
			return;
		}

		// If player has full join command then make them skip queue
		if (player.hasPermission(Main.Permissions.FULL_JOIN)) {
			ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(serverName);
			player.connect(serverInfo);

			String msgRaw = PluginConfig.getPluginConfig().getString(PluginConfig.QUEUE_DONATOR_MESSAGE);
			BaseComponent[] msg = new ComponentBuilder(Main.PREFIX).append(TextComponent.fromLegacyText(msgRaw, ChatColor.WHITE)).reset().create();
			player.sendMessage(msg);
			return;
		}

		connectPlayer(player, serverName);
	}

	/**
	 * Removes the player from any queues
	 * 
	 * @param uuid Player uuid
	 */
	public static void removePlayer(UUID uuid) {
		if(survivalQueue.remove(uuid)) {
			sendQueueMessages(Main.Servers.SURVIVAL);
		} else if(creativeQueue.remove(uuid)) {
			sendQueueMessages(Main.Servers.CREATIVE);
		}
	}

	/**
	 * Insert the player into the correct queue
	 * 
	 * @param player     Player to insert
	 * @param serverName Server name
	 */
	private static void addPlayerToQueue(ProxiedPlayer player, String serverName) {
		if (serverName.equals(Main.Servers.SURVIVAL)) {
			survivalQueue.add(player.getUniqueId());
		} else if (serverName.equals(Main.Servers.CREATIVE)) {
			creativeQueue.add(player.getUniqueId());
		}
	}
}