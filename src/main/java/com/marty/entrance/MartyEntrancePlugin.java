package com.marty.entrance;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.HashMap;
import java.io.File;

@Slf4j
@PluginDescriptor(
	name = "Marty's Entrance Plugin"
)
public class MartyEntrancePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private MartyEntranceConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Example started!");
		/*
		userSounds = new HashMap<String, String>();
		String[] pairs = config.friendsAndSounds().split(",");
		for (int i = 0; i < pairs.length; i++) {
			String[] pair = pairs[i].split("=");
			String user = pair[0];
			String sound = pair[1];
			if (!userSounds.containsKey(user)) {
				userSounds.put(user, sound);
			} else {
				// Print to chatbox somehow
				System.out.println("Tried to add a second sound: " + sound + " to user: " + user);
			}
		}
		 */
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");
	}
/*
	@Subscribe
	public void onConfigUpdate() {

	}
*/
	@Subscribe
	public void onChatMessage(ChatMessage message)
	{
		if (message.getType() == ChatMessageType.LOGINLOGOUTNOTIFICATION)
		{
			String msgContent = message.getMessage();
			if (msgContent.contains("has logged in.")) {
				String username = msgContent.split(" has")[0];
				log.info("Got signin message for user: " + username + ".");
				String[] pairs = config.friendsAndSounds().split(",");
				char [] usernameArr = username.toCharArray();
				for (int i = 0; i < usernameArr.length; i++) {
					if ((int)usernameArr[i] == 160) {
						usernameArr[i] = ' ';
					}
				}
				username = new String(usernameArr);
				for (int i = 0; i < pairs.length; i++) {
					String[] pair = pairs[i].split("=");
					String user = pair[0];
					String sound = pair[1];
					log.info("User: " + user + ", Username: " + username + ".");
					if (user.equals(username)) {
						log.info("User matched");
						File audioFile = new File("sounds/" + sound);
						try {
							AudioPlayer.play(audioFile.toString(), config);
						} catch (Exception e) {
							client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", e.toString(), "[Marty's Entrance Error]");
						}
					} else {
						if (user.length() != username.length()) {
							log.info("User: " + user + "length: " + user.length());
							log.info(" Username length: ", username.length());
						}
						int len = username.length();
						if (user.length() <= username.length()) {
							len = user.length();
						}
						for (int j = 0; j < len; j++) {
							if (user.charAt(j) != username.charAt(j)) {
								log.info("Mismatch! User: " + (int)user.charAt(j) + " Username: " + (int)username.charAt(j));
							}
						}
					}
				}
			}
		}
	}

	@Provides
    MartyEntranceConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MartyEntranceConfig.class);
	}
}
