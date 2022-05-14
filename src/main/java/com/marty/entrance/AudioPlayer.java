package com.marty.entrance;
import com.google.inject.Provides;
import net.runelite.client.config.ConfigManager;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.*;
import java.io.*;
import javax.inject.Inject;
import javax.inject.Provider;
public class AudioPlayer {
    public static boolean play(String fileName, MartyEntranceConfig config) {
        File audioFile = new File(fileName);
        try {
            URL  url = audioFile.toURI().toURL();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            setVolume(clip, (float) config.volume());
            clip.start();
            return true;
        } catch (Exception e) {
            // TODO raise exception up so error can be pushed to chatbox
            System.out.println(e.toString());
            e.getStackTrace();
            return false;
        }
    }

    /*
    private float getVolume(Clip c) {
        // TODO get volume level from config
        int volume = 50;
        FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, (float)volume / 20f);
    }
    */
    private static void setVolume(Clip c, float volume) {
        FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        System.out.println("Max: " + gainControl.getMaximum() + " Min: " + gainControl.getMinimum());
        System.out.println("Range: " + range);
        if (volume < 0f || volume > 100f) {
            throw new IllegalArgumentException("Volume Not Valid: " + volume);
        }
        volume = volume / 100;
        float gain = (range * volume) + gainControl.getMinimum();
        System.out.println("Range * volume: " + range * volume);
        System.out.println("Gain: " + gain);
        gainControl.setValue(gain);
    }

}
