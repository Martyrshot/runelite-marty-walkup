package com.marty.entrance;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class MartyEntrancePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(MartyEntrancePlugin.class);
		RuneLite.main(args);
	}
}