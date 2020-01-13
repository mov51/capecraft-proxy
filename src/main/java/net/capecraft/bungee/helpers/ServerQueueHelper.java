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
	
	//Survival/Creative Queue Loop
	private static int survivalIndex = 0;
	private static int creativeIndex = 0;
	
	private static int playersOnline = 0;
	private static int playersMax = 0;
	

	/**
	 * Polls the server, checks the payer count and connects them to the server
	 * @param serverName Server name to poll
	 */
	public static void pollServer(String serverName) {
		//Check server has queued players
		if(getQueueSize(serverName) <= 0) {
			return;
		}

		//Starts a server ping to serverName
		ProxyServer.getInstance().getServerInfo(serverName).ping(new Callback<ServerPing>() {
			@Override
			public void done(ServerPing result, Throwable error) {
				//Makes sure there isn't an error
				if(error != null) {
					return;
				}

				//Sets dynamic variables
				playersOnline = result.getPlayers().getOnline();
				playersMax = result.getPlayers().getMax();

				while(getQueueSize(serverName) > 0) {

					//Gets the next queued player
					ProxiedPlayer queuedPlayer = getNextPlayer(serverName);			
					
					//Check if server is full
					if(playersOnline >= playersMax) {
						//If player is alt kick them
						if(queuedPlayer.hasPermission(Main.Groups.ALT)) {
							//Send disconnection messages
							BaseComponent[] disconnectMsg = TextComponent.fromLegacyText(PluginConfig.getPluginConfig().getString(PluginConfig.FULL_AFK));
							queuedPlayer.sendMessage(new ComponentBuilder(Main.PREFIX).append(disconnectMsg).reset().create());

							//Remove player from queue
							removePlayer(queuedPlayer.getUniqueId());
							return;
						}

						//Make sure there are players in AFK the server server
						if(AfkHelper.getQueueList(serverName).size() > 0) {
							//Get messages
							BaseComponent[] disconnectMsg = TextComponent.fromLegacyText(PluginConfig.getPluginConfig().getString(PluginConfig.KICK_AFK));
							BaseComponent[] disconnectBroadcast = TextComponent.fromLegacyText(PluginConfig.getPluginConfig().getString(PluginConfig.KICK_AFK_BROADCAST));
																					
							ProxiedPlayer afkPlayer = AfkHelper.getNextPlayer(serverName);
							while(afkPlayer != null) {
								afkPlayer.disconnect(new ComponentBuilder(Main.PREFIX).append(disconnectMsg).reset().create());
								afkPlayer = AfkHelper.getNextPlayer(serverName);
								playersOnline--;
							}
							
							//Broadcast to players in that server
							for(ProxiedPlayer players : ProxyServer.getInstance().getServerInfo(serverName).getPlayers()) {
								players.sendMessage(new ComponentBuilder(Main.PREFIX).append(disconnectBroadcast).reset().create());
						    }

							//Send queue message
							sendQueueMessages(serverName);							
						} else {
							//Generate queue message
							String queuePos = String.valueOf(ServerQueueHelper.getQueueSize(serverName));
							String msgRaw = PluginConfig.getPluginConfig().getString(PluginConfig.QUEUE_MESSAGE);
							msgRaw = msgRaw.replace("%place%", queuePos);
							BaseComponent[] msg = new ComponentBuilder(Main.PREFIX).append(TextComponent.fromLegacyText(msgRaw, ChatColor.WHITE)).reset().create();
							queuedPlayer.sendMessage(msg);
						}
					}

					//Send player to server if the playersonline is now less than the max
					if(playersOnline < playersMax) {
						ProxyServer.getInstance().getScheduler().schedule(BungeeMain.INSTANCE, new Runnable() {
							@Override
							public void run() {
								ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(serverName);
								queuedPlayer.connect(serverInfo, new Callback<Boolean>() {
									@Override
									public void done(Boolean result, Throwable error) {									
										if(result) {
											//Check if player is an alt and add to AFK queue
											if(queuedPlayer.hasPermission(Main.Groups.ALT)) {
												AfkHelper.addAltPlayer(queuedPlayer, serverInfo);
											}
											
											//Removes that player
											removePlayer(queuedPlayer.getUniqueId());
	
											//Increase online variable
											playersOnline++;
	
											//Send queue message
											sendQueueMessages(serverName);
										} else {
											queuedPlayer.sendMessage(new ComponentBuilder(Main.PREFIX).append("An error has occured").reset().create());
										}
									}
								});
							}
							
						}, 1, TimeUnit.SECONDS);
					}
				}
			}
		});
	}

	/**
	 * Gets all players in queue to send messages to
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
	 * @param uuid UUID Target
	 * @param pos Queue Position
	 */
	private static void sendQueueMessage(UUID uuid, int pos) {
		String msgRaw = PluginConfig.getPluginConfig().getString(PluginConfig.QUEUE_STATUS);
		msgRaw = msgRaw.replace("%place%", String.valueOf(pos));
		BaseComponent[] msg = new ComponentBuilder(Main.PREFIX)
				.append(TextComponent.fromLegacyText(msgRaw, ChatColor.WHITE))
				.color(ChatColor.AQUA).create();
		ProxyServer.getInstance().getPlayer(uuid).sendMessage(msg);
	}

	/**
	 * Gets the first player in the queue and then removed them. Returns proxied player to be handled
	 * @param serverName The server name
	 * @return The ProxiedPlayer object
	 */
	private static ProxiedPlayer getNextPlayer(String serverName) {
		if(serverName.equals(Main.Servers.SURVIVAL)) {
			UUID uuid = survivalQueue.poll();
			return ProxyServer.getInstance().getPlayer(uuid);
		} else if(serverName.equals(Main.Servers.CREATIVE)) {
			UUID uuid = creativeQueue.poll();
			return ProxyServer.getInstance().getPlayer(uuid);
		} else {
			return null;
		}
	}

	/**
	 * Gets the servers queue size
	 * @param serverName The name of the server
	 */
	public static int getQueueSize(String serverName) {
		if(serverName.equals(Main.Servers.SURVIVAL)) {
			return survivalQueue.size();
		} else if(serverName.equals(Main.Servers.CREATIVE)) {
			return creativeQueue.size();
		}
		return 0;
	}

	/**
	 * Adds a player to the queue or connects them straight away if they have permissions
	 * @param serverName The server name to queue
	 * @param uuid The players uuid
	 */
	public static void addPlayer(String serverName, ProxiedPlayer player) {
		//Check player isn't in the server
		if(player.getServer().getInfo().getName().equalsIgnoreCase(serverName)) {
			player.sendMessage(new ComponentBuilder(Main.PREFIX).append("You're already in this server...").reset().create());
			return;
		}
		
		//If player has full join command then make them skip queue
		if(player.hasPermission(Main.Permissions.FULL_JOIN)) {
			ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(serverName);
			player.connect(serverInfo);

			String msgRaw = PluginConfig.getPluginConfig().getString(PluginConfig.QUEUE_DONATOR_MESSAGE);
			BaseComponent[] msg = new ComponentBuilder(Main.PREFIX).append(TextComponent.fromLegacyText(msgRaw, ChatColor.WHITE)).reset().create();
			player.sendMessage(msg);
			return;
		}

		if(serverName.equals(Main.Servers.SURVIVAL)) {
			if(!survivalQueue.contains(player.getUniqueId())){
				survivalQueue.add(player.getUniqueId());
			}

		} else if(serverName.equals(Main.Servers.CREATIVE)) {
			if(!survivalQueue.contains(player.getUniqueId())) {
				creativeQueue.add(player.getUniqueId());
			}
		}
		pollServer(serverName);
	}

	/**
	 * Removes a player from the queue and updates other players
	 * @param uuid The players uuid
	 */
	public static void removePlayer(UUID uuid) {

		if(survivalQueue.contains(uuid)) {
			survivalQueue.remove(uuid);
			survivalQueue.forEach(sUUID -> {
				sendQueueMessage(sUUID, survivalIndex+1);
				survivalIndex++;
			});
		}
		
		if(creativeQueue.contains(uuid)) {
			creativeQueue.remove(uuid);		
			creativeQueue.forEach(cUUID -> {
				sendQueueMessage(cUUID, creativeIndex+1);
				creativeIndex++;
			});
		}
	}
}
