package net.capecraft.helpers;

import java.util.concurrent.CountDownLatch;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;

public class ServerPinger {

	private ServerInfo serverInfo;
	private ServerPing serverPing;	
	
	/**
	 * Construct to start the initial ping instance
	 * @param The Server name to ping
	 */
	public ServerPinger(String serverName) {
		this.serverInfo = ProxyServer.getInstance().getServerInfo(serverName);
		this.updatePing();
	}
	
	/**
	 * Sets the serverPing property from the callback
	 * @param result ServerPing result
	 */
	private void setServerInfo(ServerPing result) {
		this.serverPing = result;
	}
	
	/**
	 * Updates the server ping with the latest result
	 */
	public void updatePing() {
		Callback<ServerPing> callback = new Callback<ServerPing>() {
			public ServerPing result = null;
			@Override
			public void done(ServerPing result, Throwable error) {				
				this.result = result;
			}
		};
		serverInfo.ping(callback);
		return callback.
	}
	
	/**
	 * Gets the online player count
	 * @return Online player count
	 */
	public int getPlayers() {		
		return this.serverPing.getPlayers().getOnline();
	}
	
	/**
	 * Gets the max player count
	 * @return Max Player count
	 */
	public int getMaxPlayers() {		
		return this.serverPing.getPlayers().getMax();
	}
}
