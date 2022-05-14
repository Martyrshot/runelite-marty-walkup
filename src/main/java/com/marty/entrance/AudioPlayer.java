package com.marty.entrance;
import com.google.inject.Provides;
import net.runelite.client.config.ConfigManager;

import javax.sound.sampled.*;
import java.net.*;
import java.io.*;
public class AudioPlayer {
    public static boolean play(String fileName, MartyEntranceConfig config) throws UnsupportedAudioFileException,
                                                                                    IOException, IllegalArgumentException,
                                                                                    SecurityException, LineUnavailableException{
        if (config.mute()) {
            return false;
        }
        File audioFile = new File(fileName);
        URL  url = audioFile.toURI().toURL();
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);

        setVolume(clip, (float) config.volume());
        clip.start();
        return true;
    }

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
