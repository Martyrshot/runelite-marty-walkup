package com.marty.entrance;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;
@ConfigGroup("Marty's Entrance Plugin")
public interface MartyEntranceConfig extends Config
{
	@ConfigItem(
		keyName = "friendsAndSounds",
		name = "Friends to announce",
		description = "Usernames and their entrance sound."
	)
	default String friendsAndSounds()
	{
		//noinspection SpellCheckingInspection
		return "Martyrshot=marty.wav\nItsAirdog=baseball.wav\nDead Leaner=marty.wav";
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
		return 75;
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
			description = "Immediately start playing the latest sound."
	)
	default boolean interrupt() {
		return false;
	}

	@ConfigItem(
			keyName = "playOwnSound",
			name = "Play your sound on login",
			description = "Play your sound on login."
	)
	default boolean playOwnSound() {
		return false;
	}
}
