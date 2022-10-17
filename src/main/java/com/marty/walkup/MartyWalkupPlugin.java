package com.marty.walkup;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import java.io.File;

@Slf4j
@PluginDescriptor(
	name = "Marty's Entrance Plugin"
)
public class MartyWalkupPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private MartyWalkupConfig config;

	private String displayName = null;
	private boolean playOnFirstTick = false;
	@Subscribe
	@SuppressWarnings("unused")
	public void onGameTick(GameTick event)
	{
		if (this.displayName == null && client.getGameState() == GameState.LOGGED_IN && this.playOnFirstTick)
		{
			if (client.getLocalPlayer() == null || client.getLocalPlayer().getName() == null)
			{
				log.info("No displayname yet...");
				return;
			}
			this.displayName = client.getLocalPlayer().getName();
			char[] displayNameArr = this.displayName.toCharArray();
			for (int i = 0; i < displayNameArr.length; i++)
			{
				if ((int)displayNameArr[i] == 160)
				{
					displayNameArr[i] = ' ';
				}
			}
			this.displayName = new String(displayNameArr);
			log.info("User " + displayName + " logged in.");
			String[] pairs = config.friendsAndSounds().split("\n");
			for (String p: pairs)
			{
				String[] pair = p.split("=");
				if (pair.length != 2) continue;
				String user = pair[0];
				String sound = pair[1];
				if (user.equals(this.displayName))
				{
					try {
						File audioFile = new File(AudioPlayer.getSoundsDir() + "/" + sound);
						AudioPlayer.play(audioFile.toString(), config);
					} catch (Exception e) {
						client.addChatMessage(ChatMessageType.CONSOLE, "", e.toString(),
								"[Marty's Entrance Error]");
					}
					return;
				}
			}
		}
	}
	@Subscribe
	@SuppressWarnings("unused")
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (client.getGameState() == GameState.LOGGED_IN) {
			this.playOnFirstTick = config.playOwnSound() && !config.mute();
		}
		if (this.displayName != null && client.getGameState() == GameState.LOGIN_SCREEN
				|| client.getGameState() == GameState.HOPPING) {
			this.displayName = null;
			this.playOnFirstTick = config.playOwnSound() && !config.mute();
		}
	}
	@Subscribe
	@SuppressWarnings("unused")
	public void onChatMessage(ChatMessage message)
	{
		if (message.getType() == ChatMessageType.LOGINLOGOUTNOTIFICATION)
		{
			String msgContent = message.getMessage();
			if (msgContent.contains("has logged in."))
			{
				String username = msgContent.split(" has")[0];
				log.info("Got sign in message for user: " + username + ".");
				String[] pairs = config.friendsAndSounds().split("\n");
				char [] usernameArr = username.toCharArray();
				for (int i = 0; i < usernameArr.length; i++)
				{
					if ((int)usernameArr[i] == 160)
					{
						usernameArr[i] = ' ';
					}
				}
				username = new String(usernameArr);
				for (String p: pairs)
				{
					String[] pair = p.split("=");
					String user = pair[0];
					String sound = pair[1];
					log.info("User: " + user + ", Username: " + username + ".");
					if (user.equals(username))
					{
						log.info("User matched");
						File audioFile = new File(AudioPlayer.getSoundsDir() + "/" + sound);
						try {
							AudioPlayer.play(audioFile.toString(), config);
						} catch (Exception e) {
							client.addChatMessage(ChatMessageType.CONSOLE, "", e.toString(),
												"[Marty's Entrance Error]");
						}
						return;
					} else {
						if (user.length() != username.length())
						{
							log.info("User: " + user + " length: " + user.length());
							log.info(" Username " + username + " length: " + username.length());
						}
						int len = username.length();
						if (user.length() <= username.length())
						{
							len = user.length();
						}
						for (int j = 0; j < len; j++)
						{
							if (user.charAt(j) != username.charAt(j))
							{
								log.info("Mismatch! User: " + (int)user.charAt(j) + " Username: "
																					+ (int)username.charAt(j));
							}
						}
					}
				}
			}
		}
	}

	@Provides
	MartyWalkupConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MartyWalkupConfig.class);
	}
}
