package skyworld.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * 模拟用户输入
 * @version 1.0
 * @Author zbxzbx98
 * @Date 2024/4/7 下午 1:25
 */
public class SimulatedUserInputStream extends InputStream {
    private final StringBuilder buffer = new StringBuilder();

    public synchronized void addInput(String input) {
        buffer.append(input);
        notifyAll(); // Notify any waiting readers that new input is available
    }

    @Override
    public synchronized void reset(){
        buffer.setLength(0);
    }

    @Override
    public synchronized int read() throws IOException {
        while (buffer.isEmpty()) { // Wait until there is input available
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Interrupted while waiting for input", e);
            }
        }

        char c = buffer.charAt(0);
        buffer.deleteCharAt(0); // Remove the first character from the buffer
        return c; // Return the read character

    }

    @Override
    public synchronized int read(byte[] b, int off, int len) throws IOException {
        Objects.checkFromIndexSize(off, len, b.length);
        if (len == 0) {
            return 0;
        }

        int c = read();
        if (c == -1) {
            return -1;
        }
        b[off] = (byte)c;
        if(c=='\n')
            return 1;

        int i = 1;
        try {
            for (; i < len ; i++) {
                c = read();
                if (c == -1) {
                    break;
                }
                b[off + i] = (byte)c;
                if(c=='\n'){
                    i++;
                    break;
                }
            }
        } catch (IOException ignored) {
        }
        return i;
    }

    @Override
    public synchronized int available(){
        return buffer.length();
    }
}
