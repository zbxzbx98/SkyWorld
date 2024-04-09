package SkyWorld;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

//光遇界面
public class SkyWindowApp {
    static YouSelf you = null;

    static JFrame mainFrame;

    static BackgroundImage backgroundImage;

    static ImageIcon imageIcon = new ImageIcon(SkyWindowApp.class.getResource("/Images/favicon.ico"));

    static SkyMap theMap = new SkyMap();

    static boolean isRunning;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        isRunning = true;
        mainFrame = new JFrame("光遇极简版--by zbxzbx98");
        mainFrame.setSize(900, 550);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.getContentPane().setBackground(Color.black);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setIconImage(imageIcon.getImage());
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                isRunning = false;
            }
        });
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("设置");
        JMenuItem menu1 = new JMenuItem("遇境");
        JMenuItem menu2 = new JMenuItem("音频");
        JMenuItem menu3 = new JMenuItem("精灵");
        menu.add(menu1);
        menu.add(menu2);
        menu.add(menu3);
        bar.add(menu);
        mainFrame.setJMenuBar(bar);
//        AudioPlayer.startPlay("/test.mp3");
        theMap.mapEnum = MapEnum.home;
        theMap.mapID = 0;
        JLabel jLabel = new JLabel();
        ImageIcon imageIcon = new ImageIcon(SkyWindowApp.class.getResource("/Images/BackGround.png"));
        backgroundImage = new BackgroundImage(imageIcon.getImage(), jLabel);
        backgroundImage.setBounds(0, 0, 900, 550);
        mainFrame.add(backgroundImage);
        mainFrame.add(jLabel);
        JButton button = new JButton("登录游戏");
        button.setBounds(360, 300, 180, 70);
        button.setBackground(new Color(238, 238, 238));
        button.addActionListener(e -> {
            mainFrame.setEnabled(false);
            Longin(jLabel);
        });
        jLabel.add(button);
        mainFrame.setVisible(true);
    }

    /**
     * 登录
     *
     * @param oldLabel 旧界面
     */
    public static void Longin(JLabel oldLabel) {
        JFrame f = new JFrame("光遇极简版--登录");
        f.setSize(300, 200);
        f.setResizable(false);
        f.setLocationRelativeTo(mainFrame);
        f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        f.setIconImage(imageIcon.getImage());
        f.setLayout(null);

        JTextField username = new JTextField();
        JLabel j1 = new JLabel("账号");
        JPasswordField password = new JPasswordField();
        JLabel j2 = new JLabel("密码");
        username.setBounds(70, 20, 190, 30);
        j1.setBounds(30, 18, 40, 30);
        password.setBounds(70, 70, 190, 30);
        j2.setBounds(30, 68, 40, 30);
        f.add(username);
        f.add(password);
        f.add(j1);
        f.add(j2);

        JButton LonginButton = new JButton("登录");
        LonginButton.setBounds(35, 120, 80, 20);
        LonginButton.addActionListener(e -> {
            if (username.getText().isEmpty() || password.getPassword().length == 0) {
                JOptionPane.showMessageDialog(null, "用户名或密码不能为空！", "错误", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String name = username.getText();

            File file = new File("SkyWorld\\" + name + "data.txt");
            if (file.exists()) {
                String info = load(name);
                String[] infos = info.split(" ");
                if (infos[8].equals(MD5.encode(new String(password.getPassword())))) {
                    JOptionPane.showMessageDialog(null, "登录成功！");
                    you = new YouSelf(Integer.parseInt(infos[1]), Integer.parseInt(infos[2]), 1, infos[0], infos[8]);
                    you.setLastMap(MapEnum.valueOf(infos[6]));
                    you.setLastMapID(Integer.parseInt(infos[7]));
                    you.candle = Double.parseDouble(infos[3]);
                    you.heart = Double.parseDouble(infos[4]);
                    you.redCandle = Double.parseDouble(infos[5]);
                    f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
                    mainFrame.setTitle("光遇极简版--by zbxzbx98--已登录：" + name);
//                    oldFrame.setEnabled(true);
                    ToGame(oldLabel);
                } else {
                    JOptionPane.showMessageDialog(null, "密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
                    password.setText("");
                }
            } else {
                JOptionPane.showMessageDialog(null, "账号不存在！", "错误", JOptionPane.WARNING_MESSAGE);
            }
        });
        JButton registerButton = new JButton("注册");
        registerButton.setBounds(185, 120, 80, 20);
        registerButton.addActionListener(e -> {
            if (!username.getText().isEmpty() && password.getPassword().length != 0) {
                String name = username.getText();
                File file = new File("SkyWorld\\" + name + "data.txt");
                if (file.exists()) {
                    JOptionPane.showMessageDialog(null, "此账号已被注册！", "错误", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(null, "注册成功！");
                you = new YouSelf(username.getText(), MD5.encode(new String(password.getPassword())));
                you.save();
                f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
                mainFrame.setTitle("光遇极简版--by zbxzbx98--已登录：" + username.getText());
                ToGame(oldLabel);
            } else {
                JOptionPane.showMessageDialog(null, "用户名或密码不能为空！", "错误", JOptionPane.WARNING_MESSAGE);
            }
        });
        f.add(LonginButton);
        f.add(registerButton);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mainFrame.setEnabled(true);
            }
        });
        f.setVisible(true);
    }

    /**
     * 进入游戏
     *
     * @param oldLabel 旧界面
     */
    public static void ToGame(JLabel oldLabel) {
        oldLabel.removeAll();
        JTextArea newTextDisplay = new JTextArea("光遇极简版--by zbxzbx98\n");
        newTextDisplay.setLineWrap(true);        //激活自动换行功能
        newTextDisplay.setWrapStyleWord(true);
        JScrollPane scrollpane = new JScrollPane(newTextDisplay);
        //取消显示水平滚动条
        scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //显示垂直滚动条
        scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollpane.setBounds(50, 30, 400, 430);
        PrintStream printStream = new PrintStream(new TextAreaOutputStream(newTextDisplay));
        System.setOut(printStream);
        newTextDisplay.setEditable(false);
        oldLabel.add(scrollpane);

        SimulatedUserInputStream simulatedUserInput = new SimulatedUserInputStream();
        System.setIn(simulatedUserInput);

        JButton[] numberButtons = createNumberButtons();
        JPanel buttonPanel = createButtonPanel(numberButtons, simulatedUserInput);
        buttonPanel.setBounds(500, 350, 300, 100);
        oldLabel.add(buttonPanel);

        theMap.addPlayer(you);
        theMap.you = you;
        you.nowMap = theMap;
        theMap.refreshMap();
        Thread choose = new Thread(() -> {
            while (isRunning) you.choose();
        });
        choose.start();
        Thread changeImage = new Thread(() -> {
            MapEnum beforMapEnum = theMap.mapEnum;
            while (isRunning) {
                if (theMap.mapEnum != beforMapEnum) {
                    beforMapEnum = theMap.mapEnum;
                    backgroundImage.setBackgroundImage(theMap.mapIcon.get(theMap.mapEnum).getImage());
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        changeImage.start();
        backgroundImage.setBackgroundImage(theMap.mapIcon.get(theMap.mapEnum).getImage());
        oldLabel.validate();
    }

    /**
     * 创建数字按钮
     *
     * @return 数字按钮数组
     */
    private static JButton[] createNumberButtons() {
        JButton[] numberButtons = new JButton[10];
        for (int i = 0; i < numberButtons.length; i++) {
            numberButtons[i] = new JButton(Integer.toString(i));
        }
        return numberButtons;
    }

    /**
     * 创建按钮面板
     *
     * @param numberButtons      数字按钮数组
     * @param simulatedUserInput 模拟用户输入流
     * @return 按钮面板
     */
    private static JPanel createButtonPanel(JButton[] numberButtons, SimulatedUserInputStream simulatedUserInput) {
        JPanel buttonPanel = new JPanel(new GridLayout(2, 5));
        for (JButton button : numberButtons) {
            button.addActionListener(e -> simulatedUserInput.addInput(button.getText() + "\n"));
            buttonPanel.add(button);
        }
        return buttonPanel;
    }

    /**
     * 加载文件
     *
     * @param name 玩家名
     * @return 玩家信息
     */
    public static String load(String name) {
        try (BufferedReader isr = new BufferedReader(new FileReader("SkyWorld\\" + name + "data.txt"))) {
            return isr.readLine();
        } catch (IOException e) {
            System.err.println("读取文件失败");
        }
        return "";
    }
}
