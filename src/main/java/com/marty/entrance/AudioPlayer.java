package com.marty.entrance;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuneLite;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Slf4j
public class AudioPlayer {
    private static Clip clip = null;

    private static File fileLocation = null;

    public static File getSoundsDir() {
        if (AudioPlayer.fileLocation == null || !AudioPlayer.fileLocation.exists()) {
            File runeliteDir = new File(RuneLite.RUNELITE_DIR.toString());
            if (!runeliteDir.exists()) {
                log.info("$HOME/.runelite doesn't exist");
                return null;
            }
            if (!runeliteDir.isDirectory()) {
                log.info("$HOME/.runelite isn't a directory");
                return null;
            }
            File soundsDir = new File(runeliteDir.getPath() + "/sounds/marty/entrance");
            log.info(soundsDir.getAbsolutePath());
            if (!soundsDir.exists()) {
                log.info("$HOME/.runelite/sounds doesn't exist");
                if (!soundsDir.mkdirs()) {
                    log.info("Failed to make soundsDir");
                }
                HttpUrl git = HttpUrl.parse("https://raw.githubusercontent.com/Martyrshot/runelite-marty-entrance/master/sounds/com/marty/entrance/");
                if (git == null) return null;
                HttpUrl martyUrl = git.newBuilder().addPathSegment("marty.wav").build();

                try {
                    OkHttpClient httpClient = new OkHttpClient();
                    try(Response res = httpClient.newCall(new Request.Builder().url(martyUrl).build()).execute()) {
                        if (res.body() != null) {
                            Files.copy(new BufferedInputStream(res.body().byteStream()), Paths.get(soundsDir + "/marty.wav"));
                        }
                    }
                } catch (Exception e) {
                    log.info("Couldn't download very important file");
                }
            }
            AudioPlayer.fileLocation = soundsDir;
        }
        return AudioPlayer.fileLocation;
    }
    public static void play(String fileName, MartyEntranceConfig config) throws UnsupportedAudioFileException,
                                                                                    IOException,
                                                                                    IllegalArgumentException,
                                                                                    SecurityException,
                                                                                    LineUnavailableException
    {
        if (config.mute())
        {
            log.info("Is muted");
            return;
        }
        File audioFile = new File(fileName);
        log.info(audioFile.getPath());
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
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
