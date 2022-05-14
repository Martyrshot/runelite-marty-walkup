package com.marty.entrance;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;
@ConfigGroup("example")
public interface MartyEntranceConfig extends Config
{
	// TODO: update description based on if it's comma seperated or newline
	@ConfigItem(
		keyName = "friendsAndSounds",
		name = "Friends to announce",
		description = "A comma seperated list of friends and the sounds to use when they log in."
	)
	default String friendsAndSounds()
	{
		//noinspection SpellCheckingInspection
		return "Martyrshot=marty.wav,ItsAirdog=baseball.wav,Dead Leaner=marty.wav";
	}

	@ConfigItem(
			keyName = "volume",
			name = "Volume",
			description = "Set the volume of the sounds to be played."
	)
	@Range(
			max = 100
	)
	default int volume() {
		return 60;
	}

	@ConfigItem(
			keyName = "mute",
			name = "Mute",
			description = "Mute entrance sounds."
	)
	default boolean mute() {
		return false;
	}

	@ConfigItem(
			keyName = "interrupt",
			name = "Interrupt Sound",
			description = "Interrupt entrance sounds to play the most recent."
	)
	default boolean interrupt() {
		return false;
	}
}
