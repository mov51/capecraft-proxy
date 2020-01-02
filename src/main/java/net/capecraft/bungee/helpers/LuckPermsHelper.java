package net.capecraft.bungee.helpers;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;

public class LuckPermsHelper {

	//Get API
	private static LuckPerms luckPermsApi = LuckPermsProvider.get();
	
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
		Node node = luckPermsApi.getNodeBuilderRegistry().forPermission().permission(permission).build();
		user.data().add(node);
		luckPermsApi.getUserManager().saveUser(user);
	}

	/**
	 * Remove a permission to the user
	 * @param user User instance
	 * @param permission permission instance
	 */
	public static void removePermission(User user, String permission) {
		Node node = luckPermsApi.getNodeBuilderRegistry().forPermission().permission(permission).build();
		user.data().remove(node);
		luckPermsApi.getUserManager().saveUser(user);
	}
}
