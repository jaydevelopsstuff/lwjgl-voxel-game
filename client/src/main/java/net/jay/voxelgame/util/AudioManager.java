package net.jay.voxelgame.util;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class AudioManager {
    private Clip dirtBreak;
    private Clip stoneBreak;

    private final DLList<Integer> audioQueue = new DLList<>();
    private final boolean running = true;

    public void init() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        this.dirtBreak = AudioSystem.getClip();
        this.stoneBreak = AudioSystem.getClip();
        this.dirtBreak.open(AudioSystem.getAudioInputStream(new BufferedInputStream(FileUtil.getResourceInputStream("assets/sounds/dirtbreak.wav"))));
        this.stoneBreak.open(AudioSystem.getAudioInputStream(new BufferedInputStream(FileUtil.getResourceInputStream("assets/sounds/stonebreak.wav"))));
        new Thread(this::queueLoop).start();
    }

    public void queueSound(int sound) {
        audioQueue.add(sound);
    }

    private void queueLoop() {
        while(running) {
            Integer audioId = audioQueue.get(0);
            audioQueue.remove(0);

            if(audioId != null) {
                if(audioId == 0) {
                    dirtBreak.stop();
                    dirtBreak.setFramePosition(0);
                    dirtBreak.start();
                } else if(audioId == 1) {
                    stoneBreak.stop();
                    stoneBreak.setFramePosition(0);
                    stoneBreak.start();
                }
            }

            try {
                Thread.sleep(100);
            } catch(InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
