package SkyWorld;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import org.tritonus.share.sampled.file.TAudioFileReader;

/**
 * @version 1.0
 * @Author zbxzbx98
 * @Date 2024/4/8 下午 3:03
 */
public class AudioPlayer {
    public static void play(String name)
    {
        URL url = AudioPlayer.class.getResource(name);
        if (url != null) {
            if(".mp3".equals(name.substring(name.lastIndexOf(".")))){
                TAudioFileReader reader = new MpegAudioFileReader();
                try (AudioInputStream audioInputStream = reader.getAudioInputStream(url)) {
                    Player player = new Player(audioInputStream);
                    player.play();
                } catch (IOException | UnsupportedAudioFileException e) {
                    System.err.println("Error opening MP3 file: " + e.getMessage());
                } catch (JavaLayerException e) {
                    throw new RuntimeException(e);
                }
            }
//            else if(".wav".equals(name.substring(name.lastIndexOf(".")))){
//                try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url)) {
//                    playClip(audioInputStream);
//                } catch (UnsupportedAudioFileException | IOException e) {
//                    System.err.println("Failed to open audio file: " + e.getMessage());
//                }
//            }
            else
                System.err.println("Unsupported audio file format: " + name);
        }
    }

    private static void playClip(AudioInputStream audioInputStream) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        }
        catch (LineUnavailableException | IOException e){
            System.err.println("Failed to play audio clip: " + e.getMessage());
        }

    }
}
