package net.capecraft.bungee.helpers;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.manager.UserManager;

public class LuckPermsHelper {
	
	//Get API
	private static LuckPermsApi luckPermsApi = LuckPerms.getApi();

	/**
	 * Get a luckperms user from uuid
	 * @param uuid Player uuid
	 * @return LuckPermsUser
	 */
	public static User getUser(UUID uuid) {
		UserManager userManager = luckPermsApi.getUserManager();
	    CompletableFuture<User> userFuture = userManager.loadUser(uuid);
	    return userFuture.join();
	}
	
	/**
	 * Add a permission to the user
	 * @param user User instance
	 * @param permission permission instance
	 */
	public static void addPermission(User user, String permission) {
		Node node = luckPermsApi.getNodeFactory().newBuilder(permission).build();
		user.setPermission(node);		
		luckPermsApi.getUserManager().saveUser(user);
	}
	
	/**
	 * Remove a permission to the user
	 * @param user User instance
	 * @param permission permission instance
	 */
	public static void removePermission(User user, String permission) {
		Node node = luckPermsApi.getNodeFactory().newBuilder(permission).build();
		user.unsetPermission(node);		
		luckPermsApi.getUserManager().saveUser(user);
	}
}
