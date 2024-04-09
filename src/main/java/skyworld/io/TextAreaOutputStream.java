package skyworld.io;


import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 输出流到文本框中
 *
 * @version 1.0
 * @Author zbxzbx98
 * @Date 2024/4/7 上午 11:21
 */
public class TextAreaOutputStream extends OutputStream {

    private final PipedOutputStream out = new PipedOutputStream();
    private final Reader reader;
    private final JTextArea outputArea;

    public TextAreaOutputStream(JTextArea outputArea) {
        this.outputArea = outputArea;
        // Set the input to be piped to our output stream
        PipedInputStream in;
        try {
            in = new PipedInputStream(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        reader = new InputStreamReader(in, StandardCharsets.UTF_8);
    }

    public synchronized void write(int i) throws IOException {
        out.write(i);
    }

    public synchronized void write(byte[] bytes, int i, int i1) throws IOException {
        out.write(bytes, i, i1);
        flush();
    }

    public synchronized void flush() throws IOException {
        if (reader.ready()) {
            char[] chars = new char[1024];
            int n = reader.read(chars);

            String text = new String(chars, 0, n);
            // Write the text to the JTextArea
            outputArea.append(text);
            if (outputArea.getLineCount() > 22) {
                outputArea.setText(outputArea.getText().substring(outputArea.getText().indexOf("\n") + 1));
            }
        } else {
            System.err.println("未准备好");
        }
    }
}
