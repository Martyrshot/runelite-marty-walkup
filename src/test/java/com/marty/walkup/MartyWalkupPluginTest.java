package com.marty.walkup;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class MartyWalkupPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(MartyWalkupPlugin.class);
		RuneLite.main(args);
	}
}