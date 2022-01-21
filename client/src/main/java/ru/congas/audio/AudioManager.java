package ru.congas.audio;

import org.apache.logging.log4j.LogManager;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/**
 * @author Mr_Told
 */
public class AudioManager {

    public static void playClip(URL url) {

        class AudioListener implements LineListener {

            private boolean done = false;

            @Override public synchronized void update(LineEvent event) {
                LineEvent.Type eventType = event.getType();
                if (eventType == LineEvent.Type.STOP || eventType == LineEvent.Type.CLOSE) {
                    done = true;
                    notifyAll();
                }
            }

            public synchronized void waitUntilDone() throws InterruptedException {
                while (!done) wait();
            }

        }

        try {
            AudioListener listener = new AudioListener();
            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url)) {
                Clip clip = AudioSystem.getClip();
                clip.addLineListener(listener);
                clip.open(audioInputStream);
                new Thread(() -> {
                    try {
                        clip.start();
                        listener.waitUntilDone();
                    }
                    catch (InterruptedException e) {
                        LogManager.getLogger(AudioManager.class).warn("E: ", e);
                    } finally {
                        clip.close();
                    }
                }).start();

            }
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            LogManager.getLogger(AudioManager.class).warn("E: ", e);
        }


    }

}
