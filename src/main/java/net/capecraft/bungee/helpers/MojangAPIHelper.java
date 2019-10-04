package net.capecraft.bungee.helpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MojangAPIHelper {

	/**
	 * Returns player UUID
	 * @param username Username to get UUID from
	 * @return Players uuid
	 */
	public static UUID getUUID(String username) {
		//Try get online player first
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(username);
		if(player != null) {			
			return player.getUniqueId();
		}

		String uuid = getApiData(username).get("id").getAsString();
		if(uuid != null) {			
			return UUID.fromString(uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32));
		}
		
		return null;
	}
	
	/**
	 * Returns player username
	 * @param uuid Players uuid
	 * @return Players username
	 */
	public static String getUsername(UUID uuid) {
		//Try get online player first
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
		if(player != null) {			
			return player.getName();
		}
		
		String username = getApiData(uuid.toString().replace("-", "")).get("name").getAsString();
		if(username != null) {
			return username;
		}
		
		return null;
	}
	
	/**
	 * Request API call for user data
	 * @param username
	 * @return
	 */
	private static JsonObject getApiData(String data) {
		try {
			//Url for request
			String url = "https://api.minetools.eu/uuid/" + data;
			
			//Create connection
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
	
			//Create reader
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			//Read response
			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);
			
			//Convert response to JSON
			JsonElement uuidJsonEle = new JsonParser().parse(response.toString());
			
			//Check response is valid
			if(response.toString().isEmpty() && uuidJsonEle.getAsJsonObject().get("error") == null) {
				return null; 				
			}		
	
			//Return UUID
		    JsonObject uuidJsonObj = uuidJsonEle.getAsJsonObject();
		    return uuidJsonObj;
		} catch (Exception e) {			
			e.printStackTrace();
			return null;
		}
	}
	
}
