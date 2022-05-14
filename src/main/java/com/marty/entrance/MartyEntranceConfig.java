package com.marty.entrance;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;
@ConfigGroup("example")
public interface MartyEntranceConfig extends Config
{
	// TODO: update description based on if it's comma sepearted or newline
	@ConfigItem(
		keyName = "friendsAndSounds",
		name = "Friends to announce",
		description = "A comma seperated list of friends and the sounds to use when they log in."
	)
	default String friendsAndSounds()
	{
		return "Martyrshot=marty.mp3,ItsAirdog=baseball.mp3,Dead Leaner=marty.mp3";
	}

	@ConfigItem(
			keyName = "volume",
			name = "Volume",
			description = "Set the volume of the sounds to be played."
	)
	@Range(
			min = 0,
			max = 100
	)
	default int volume() {
		return 60;
	}
}
