package edu.austral.prog2_2018c2;

import javax.sound.sampled.*;
import java.io.IOException;

public class Audio {
    private Clip clip;

    public Audio(String a) {
        try {
            AudioInputStream aux = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(a));
            clip = AudioSystem.getClip();
            clip.open(aux);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip == null) return;
        stop();
        clip.setFramePosition(0);
        clip.start();
    }

    public void stop() {
        if(clip.isRunning()) {
            clip.stop();
        }
    }

    public void close() {
        stop();
        clip.close();
    }
}
