package com.marty.entrance;

import javax.sound.sampled.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AudioPlayer {
    private static Clip clip = null;
    public static void play(String fileName, MartyEntranceConfig config) throws UnsupportedAudioFileException,
                                                                                    IOException,
                                                                                    IllegalArgumentException,
                                                                                    SecurityException,
                                                                                    LineUnavailableException
    {
        if (config.mute())
        {
            return;
        }
        File audioFile = new File(fileName);
        URL  url = audioFile.toURI().toURL();
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);

        AudioPlayer.setVolume(clip, (float) config.volume());
        if (AudioPlayer.clip == null || !AudioPlayer.clip.isRunning())
        {
            AudioPlayer.clip = clip;
            AudioPlayer.clip.start();
        } else
        {
            long timeRemainingMS = AudioPlayer.clip.getMicrosecondLength() - AudioPlayer.clip.getMicrosecondPosition();
            try {
                if (config.interrupt()) {
                    if (AudioPlayer.clip != null && AudioPlayer.clip.isRunning()) {
                        AudioPlayer.clip.stop();
                    }
                    AudioPlayer.clip = clip;
                    AudioPlayer.clip.start();
                } else {
                    while (AudioPlayer.clip.isRunning()) {
                        TimeUnit.MICROSECONDS.sleep(timeRemainingMS);
                        if (!AudioPlayer.clip.isRunning()) {
                            AudioPlayer.clip = clip;
                            AudioPlayer.clip.start();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                log.info("Failed to sleep: " + e);
            }
        }
    }

    private static void setVolume(Clip c, float volume)
    {
        FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        System.out.println("Max: " + gainControl.getMaximum() + " Min: " + gainControl.getMinimum());
        System.out.println("Range: " + range);
        if (volume < 0f || volume > 100f)
        {
            throw new IllegalArgumentException("Volume Not Valid: " + volume);
        }
        volume = volume / 100;
        float gain = (range * volume) + gainControl.getMinimum();
        System.out.println("Range * volume: " + range * volume);
        System.out.println("Gain: " + gain);
        gainControl.setValue(gain);
    }

}
