/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradeworld.gui;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Koste (originaly from Kotuc)
 * @see http://www.anyexample.com/programming/java/java_play_wav_sound_file.xml
 */
public class Music {

    private String filename;
    private Position curPosition;
    private final int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb

    enum Position {

        LEFT, RIGHT, NORMAL
    };

    public Music(String wavfile) {
        filename = wavfile;
        curPosition = Position.NORMAL;
    }

    Music(String wavfile, Position p) {
        filename = wavfile;
        curPosition = p;
    }
    private float panpos;

    public void play() {
        new Thread() {

            @Override
            public void run() {

                File soundFile = new File(filename);
                if (!soundFile.exists()) {
                    System.err.println("Wave file not found: " + filename);
                    return;
                }

                AudioInputStream audioInputStream = null;
                try {
                    audioInputStream = AudioSystem.getAudioInputStream(soundFile);
                } catch (UnsupportedAudioFileException e1) {
                    e1.printStackTrace();
                    return;
                } catch (IOException e1) {
                    e1.printStackTrace();
                    return;
                }

                AudioFormat format = audioInputStream.getFormat();
                SourceDataLine auline = null;
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

                try {
                    auline = (SourceDataLine) AudioSystem.getLine(info);
                    auline.open(format);
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                if (auline.isControlSupported(FloatControl.Type.PAN)) {
                    FloatControl pan = (FloatControl) auline.getControl(FloatControl.Type.PAN);
                    panpos = (float) (Math.random() - Math.random());
                    System.out.println("panpos " + panpos);
//            pan.setValue(panpos);
                    if (curPosition == Position.RIGHT) {
                        pan.setValue(1.0f);
                    } else if (curPosition == Position.LEFT) {
                        pan.setValue(-1.0f);
                    }
                }

                auline.start();
                int nBytesRead = 0;
                byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];

                try {
                    while (nBytesRead != -1) {
                        nBytesRead = audioInputStream.read(abData, 0, abData.length);
                        if (nBytesRead >= 0) {
                            auline.write(abData, 0, nBytesRead);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                } finally {
                    auline.drain();
                    auline.close();
                }
            }
        }.start();
    }

    public static void main(String[] args) {

        for (Info info : AudioSystem.getMixerInfo()) {
            System.out.println("info: " + info);
//            AudioSystem.getClip(info);
        }
        for (Type type : AudioSystem.getAudioFileTypes()) {
            System.out.println("type: " + type);
        }

        for (int i = 1000; i > 0; i -= 10) {
            new Music("sounds/ding.wav").play();
            try {
                Thread.sleep(i);
            } catch (InterruptedException ex) {
                Logger.getLogger(Music.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}

