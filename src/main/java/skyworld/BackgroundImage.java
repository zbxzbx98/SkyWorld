package skyworld;

import java.awt.*;
import java.util.concurrent.Semaphore;
import javax.swing.*;

/**
 * @version 1.0
 * @Author zbxzbx98
 * @Date 2024/4/9 上午10:03
 */
public class BackgroundImage extends JComponent {
    private Image backgroundImage;
    private Image nextBackgroundImage;
    private final JLabel oldLabel;
    private float currentOpacity;
    private float startOpacity, endOpacity;
    private long animationDurationMs, animationStartTimestamp;
    private Timer fadeTimer;
    private static final Semaphore semaphore = new Semaphore(1);

    public BackgroundImage(Image backgroundImage, JLabel oldLabel) {
        this.backgroundImage = backgroundImage;
        this.currentOpacity = 1.0f;
        this.oldLabel = oldLabel;
        fadeTimer = new Timer(20, e -> {
            updateOpacity();
            repaint();
            if (currentOpacity == endOpacity) {
                fadeTimer.stop();
                if (nextBackgroundImage != this.backgroundImage) {
                    this.backgroundImage = nextBackgroundImage;
                    startFadeAnimation(0, 1, 500);
                } else {
                    oldLabel.setVisible(true);
                    semaphore.release();
                }
            }
        });
    }

    public void setBackgroundImage(Image backgroundImage) {
        if (backgroundImage == this.backgroundImage || backgroundImage == this.nextBackgroundImage)
            return;
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        oldLabel.setVisible(false);
        this.nextBackgroundImage = backgroundImage;
        startFadeAnimation(1, 0, 500);
    }

    private void startFadeAnimation(float startOpacity, float endOpacity, long animationDurationMs) {
        this.startOpacity = startOpacity;
        this.endOpacity = endOpacity;
        this.animationDurationMs = animationDurationMs;
        this.animationStartTimestamp = System.currentTimeMillis();
        fadeTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, currentOpacity));
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, this);
        }
        if (currentOpacity == 1)
            oldLabel.printComponents(g);
        g2d.dispose();
    }

    // 实现渐变动画逻辑的方法...
    private void updateOpacity() {
        long currentTime = System.currentTimeMillis();
        float progress = (currentTime - animationStartTimestamp) / (float) animationDurationMs;
        if (progress > 1f) progress = 1f;

        currentOpacity = interpolate(progress, startOpacity, endOpacity);
    }

    private float interpolate(float t, float a, float b) {
        return a + (b - a) * t;
    }

}
