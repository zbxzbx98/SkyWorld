package skyworld.util;

import javax.sound.sampled.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackListener;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import org.tritonus.share.sampled.file.TAudioFileReader;

/**
 * 声音播放器，仅限播放MP3
 *
 * @version 1.0
 * @Author zbxzbx98
 * @Date 2024/4/8 下午 3:03
 */
public class AudioPlayer {
    private static AdvancedPlayer bgmPlayer;
    private static final TAudioFileReader reader= new MpegAudioFileReader();
    private static Thread breakThread;
    public static int breakCount = 0;

    /**
     * 启动新线程播放音频
     *
     * @param path 音频资源路径
     */
    public static void startPlay(String path) {
        new Thread(() -> play(path)).start();
    }

    public static void startBgm(String name) {
        stopBgm();
        new Thread(() -> playBgm(name)).start();
    }

    public static void stopBgm() {
        if(bgmPlayer!=null)
            bgmPlayer.stop();
    }

    public static void playLost(int breakCount) {
        new Thread(() -> {
            try {
                Player lostPlayer = new Player(reader.getAudioInputStream(AudioPlayer.class.getResource("/Audios/lost.mp3")));
                lostPlayer.play();
                Thread.sleep(2000);
                playBreak(breakCount);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private static void playBreak(int count) {
        breakCount+=count;
        if (breakThread==null || !breakThread.isAlive()) {
            breakThread = new Thread(() -> {
                while (breakCount>0) {
                    try {
                        new Thread(()->{
                            try {
                                Player breakPlayer = new Player(reader.getAudioInputStream(AudioPlayer.class.getResource("/Audios/break.mp3")));
                                breakPlayer.play();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }).start();
                        breakCount--;
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            breakThread.start();
        }
    }
    /**
     * 播放音频
     *
     * @param name 音频资源路径
     */
    private static void play(String name) {
        URL url = AudioPlayer.class.getResource(name);
        if (url != null) {
            if (".mp3".equals(name.substring(name.lastIndexOf(".")))) {
                try (AudioInputStream audioInputStream = reader.getAudioInputStream(url)) {
                    Player player = new Player(audioInputStream);
                    player.play();
                } catch (IOException | UnsupportedAudioFileException e) {
                    System.err.println("Error opening MP3 file: " + e.getMessage());
                } catch (JavaLayerException e) {
                    throw new RuntimeException(e);
                }
            }
            else
                System.err.println("Unsupported audio file format: " + name);
        } else
            System.err.println("Failed to find audio file: " + name);
    }

    private static void playBgm(String name) {
        URL url = AudioPlayer.class.getResource(name);
        if (url != null) {
            if (".mp3".equals(name.substring(name.lastIndexOf(".")))) {
                try (FileInputStream inputStream = new FileInputStream(url.getPath())) {
                    bgmPlayer = new AdvancedPlayer(inputStream);
                    bgmPlayer.setPlayBackListener(new PlaybackListener(){});
                    bgmPlayer.play();
                } catch (IOException e) {
                    System.err.println("Error opening file: " + e.getMessage());
                } catch (JavaLayerException e) {
                    System.err.println("Error playing audio: " + e.getMessage());
                }
            }
            else
                System.err.println("Unsupported audio file format: " + name);
        } else
            System.err.println("Failed to find audio file: " + name);
    }
}
