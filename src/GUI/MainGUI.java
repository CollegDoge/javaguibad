/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

// GENERAL IMPORTS
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

// MEDIA PLAYER
import java.io.File;
import javax.sound.sampled.*;


/**
 *
 * @author 22156
 */
public class MainGUI extends javax.swing.JFrame {

    /**
     * Creates new form MainGUI
     */
    
    private Clip clip;
    private int currentTrackIndex = 0;
    private String currentAlbum = "";
    
    public MainGUI() {
        initComponents();
        
        // CLOCK
        javax.swing.Timer timer = new javax.swing.Timer(1000, (java.awt.event.ActionEvent evt) -> { // updates every second
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); // format for the clock
            String currentTime = sdf.format(new Date()); // define clock
            Clock.setText(currentTime); // clock
        });
        timer.start();
        
        // ICON
        ImageIcon icon = new ImageIcon("C:\\Users\\22156\\OneDrive - northcote.school.nz\\Documents\\NetBeansProjects\\javaguibad\\src\\GUI\\Images\\desktop.png");
        setIconImage(icon.getImage());
        
        // STYLING
        styleButton(startBtn);
        styleButton(calculatorBtn);
        styleButton(notepadBtn);
        styleButton(musicBtn);
        styleButton(wallpaperBtn);
        styleTextField(Clock);
        
        // DRAG
        makeDraggable(Calculator, calcTitlebar);
        makeDraggable(Player, playerTitlebar);
        makeDraggable(Notepad, notepadTitlebar);
        
        // STARTUP
        Calculator.setVisible(false);
        Notepad.setVisible(false);
        Player.setVisible(false);
        playerSelector.setVisible(false);
        startMenu.setVisible(false);
        
        // MUSIC
        playerStop.addActionListener(e -> {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }
        });
    }
    
     // DRAGGING
    
    private void makeDraggable(JPanel window, JPanel titlebar) {
    final Point[] initialClick = { null }; // one Point per window

    titlebar.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            initialClick[0] = e.getPoint();
            Container parent = window.getParent();
            if (parent != null) {
                parent.setComponentZOrder(window, 0);
                parent.revalidate();
                parent.repaint();
            }
        }
    });

    titlebar.addMouseMotionListener(new MouseMotionAdapter() {
        public void mouseDragged(MouseEvent e) {
            if (initialClick[0] == null) return;

            int thisX = window.getLocation().x;
            int thisY = window.getLocation().y;

            int xMoved = e.getX() - initialClick[0].x;
            int yMoved = e.getY() - initialClick[0].y;

            int X = thisX + xMoved;
            int Y = thisY + yMoved;

            int parentWidth = getContentPane().getWidth();
            int parentHeight = getContentPane().getHeight();
            int w = window.getWidth();
            int h = window.getHeight();
            X = Math.max(0, Math.min(X, parentWidth - w));
            Y = Math.max(0, Math.min(Y, parentHeight - h));

            window.setLocation(X, Y);
        }
    });
    }
    
    private void styleButton(javax.swing.JButton button) { // Styling for the buttons
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
    }
    
    private void styleTextField(javax.swing.JTextField textField) { // Styling for the text field
        textField.setBorder(null);
        textField.setFocusable(false);
    }
    
    // Music Player
    private void playSound(String filePath) {
        try {
            // Stop and close previous clip if it exists
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }

            File soundFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }
    
    private void playCurrentTrack() {
        String[] selectedAlbum;

        if (currentAlbum.equals("Random Music 1")) {
            selectedAlbum = album1;
        } else if (currentAlbum.equals("Random Music 2")) {
            selectedAlbum = album2;
        } else {
            playerTitle.setText("No Album Selected!!");
            return;
        }

        if (currentTrackIndex >= 0 && currentTrackIndex < selectedAlbum.length) {
            playSound(selectedAlbum[currentTrackIndex]);
            // Optional: set the title
            String path = selectedAlbum[currentTrackIndex];
            playerTitle.setText(path.substring(path.lastIndexOf("/") + 1));
        }
    }

    private String[] album1 = {
        "src/GUI/Music/1/autumn.wav",
        "src/GUI/Music/1/cafe.wav",
        "src/GUI/Music/1/jumpy.wav",
        "src/GUI/Music/1/lights.wav",
        "src/GUI/Music/1/waves.wav"};
    
    private String[] album2 = {
        "src/GUI/Music/2/leap.wav",
        "src/GUI/Music/2/refrain.wav",
        "src/GUI/Music/2/sleep.wav",
        "src/GUI/Music/2/slip.wav",
        "src/GUI/Music/2/slow fall.wav"};
    
    private String[] wallpapers = {"/GUI/Images/Wall1.jpg", "/GUI/Images/Wall2.jpg", "/GUI/Images/Wall3.jpg"};
    private int currentWallpaper = 0;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        startMenu = new javax.swing.JPanel();
        Notepad = new javax.swing.JPanel();
        notepadTitlebar = new javax.swing.JPanel();
        NotepadTitle = new javax.swing.JLabel();
        NotepadClose = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        playerSelector = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        album2Select = new javax.swing.JButton();
        album1Select = new javax.swing.JButton();
        Player = new javax.swing.JPanel();
        playerTitlebar = new javax.swing.JPanel();
        PlayerTitle = new javax.swing.JLabel();
        PlayerClose = new javax.swing.JButton();
        playerTitle = new javax.swing.JLabel();
        playerArtist = new javax.swing.JLabel();
        playerArtwork = new javax.swing.JLabel();
        playerRelease = new javax.swing.JLabel();
        playerPrev = new javax.swing.JButton();
        playerStop = new javax.swing.JButton();
        playerNext = new javax.swing.JButton();
        player2Song = new javax.swing.JLabel();
        player1Song = new javax.swing.JLabel();
        player3Song = new javax.swing.JLabel();
        player4Song = new javax.swing.JLabel();
        player5Song = new javax.swing.JLabel();
        player2Play = new javax.swing.JButton();
        player1Play = new javax.swing.JButton();
        player3Play = new javax.swing.JButton();
        player4Play = new javax.swing.JButton();
        player5Play = new javax.swing.JButton();
        playerSelectbtn = new javax.swing.JButton();
        Calculator = new javax.swing.JPanel();
        calcTitlebar = new javax.swing.JPanel();
        CalcTitle = new javax.swing.JLabel();
        CalcClose = new javax.swing.JButton();
        calcField = new javax.swing.JTextField();
        calcExpression = new javax.swing.JTextField();
        calcEquals = new javax.swing.JButton();
        calcClear = new javax.swing.JButton();
        calcPlus = new javax.swing.JButton();
        calcMinus = new javax.swing.JButton();
        calcMulti = new javax.swing.JButton();
        calcDiv = new javax.swing.JButton();
        calcMod = new javax.swing.JButton();
        calcPeriod = new javax.swing.JButton();
        Calc0 = new javax.swing.JButton();
        Calc1 = new javax.swing.JButton();
        Calc2 = new javax.swing.JButton();
        Calc3 = new javax.swing.JButton();
        Calc4 = new javax.swing.JButton();
        Calc5 = new javax.swing.JButton();
        Calc6 = new javax.swing.JButton();
        Calc7 = new javax.swing.JButton();
        Calc8 = new javax.swing.JButton();
        Calc9 = new javax.swing.JButton();
        Taskbar = new javax.swing.JPanel();
        startBtn = new javax.swing.JButton();
        calculatorBtn = new javax.swing.JButton();
        notepadBtn = new javax.swing.JButton();
        wallpaperBtn = new javax.swing.JButton();
        Clock = new javax.swing.JTextField();
        musicBtn = new javax.swing.JButton();
        Wallpaper = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Desktop");
        setResizable(false);
        setSize(new java.awt.Dimension(1368, 770));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        startMenu.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout startMenuLayout = new javax.swing.GroupLayout(startMenu);
        startMenu.setLayout(startMenuLayout);
        startMenuLayout.setHorizontalGroup(
            startMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );
        startMenuLayout.setVerticalGroup(
            startMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 320, Short.MAX_VALUE)
        );

        getContentPane().add(startMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 400, 260, 320));

        Notepad.setBackground(new java.awt.Color(102, 102, 102));
        Notepad.setForeground(new java.awt.Color(102, 102, 102));

        notepadTitlebar.setBackground(new java.awt.Color(51, 51, 51));
        notepadTitlebar.setForeground(new java.awt.Color(51, 51, 51));

        NotepadTitle.setBackground(new java.awt.Color(255, 255, 255));
        NotepadTitle.setForeground(new java.awt.Color(255, 255, 255));
        NotepadTitle.setText("Notepad");

        NotepadClose.setText("X");
        NotepadClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NotepadCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout notepadTitlebarLayout = new javax.swing.GroupLayout(notepadTitlebar);
        notepadTitlebar.setLayout(notepadTitlebarLayout);
        notepadTitlebarLayout.setHorizontalGroup(
            notepadTitlebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notepadTitlebarLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(NotepadTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(NotepadClose, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        notepadTitlebarLayout.setVerticalGroup(
            notepadTitlebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notepadTitlebarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(notepadTitlebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NotepadTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(NotepadClose))
                .addGap(5, 5, 5))
        );

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton1.setText("Open");

        jButton2.setText("Save File");

        jButton3.setText("Pin To Desktop");

        javax.swing.GroupLayout NotepadLayout = new javax.swing.GroupLayout(Notepad);
        Notepad.setLayout(NotepadLayout);
        NotepadLayout.setHorizontalGroup(
            NotepadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(notepadTitlebar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(NotepadLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(NotepadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(NotepadLayout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 148, Short.MAX_VALUE)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        NotepadLayout.setVerticalGroup(
            NotepadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NotepadLayout.createSequentialGroup()
                .addComponent(notepadTitlebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(NotepadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(Notepad, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 0, 480, 320));

        playerSelector.setBackground(new java.awt.Color(51, 51, 51));

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Choose an Album");

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Images/random-music-2-small.png"))); // NOI18N

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Images/random-music-1-small.png"))); // NOI18N

        album2Select.setBackground(new java.awt.Color(0, 0, 0));
        album2Select.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        album2Select.setForeground(new java.awt.Color(255, 255, 255));
        album2Select.setText("Random Music 2");
        album2Select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                album2SelectActionPerformed(evt);
            }
        });

        album1Select.setBackground(new java.awt.Color(0, 0, 0));
        album1Select.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        album1Select.setForeground(new java.awt.Color(255, 255, 255));
        album1Select.setText("Random Music 1");
        album1Select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                album1SelectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout playerSelectorLayout = new javax.swing.GroupLayout(playerSelector);
        playerSelector.setLayout(playerSelectorLayout);
        playerSelectorLayout.setHorizontalGroup(
            playerSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(playerSelectorLayout.createSequentialGroup()
                .addGroup(playerSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(playerSelectorLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(playerSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(playerSelectorLayout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(album2Select, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(playerSelectorLayout.createSequentialGroup()
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(album1Select, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(playerSelectorLayout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(jLabel10)))
                .addGap(0, 24, Short.MAX_VALUE))
        );
        playerSelectorLayout.setVerticalGroup(
            playerSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(playerSelectorLayout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addGroup(playerSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12)
                    .addComponent(album1Select, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(playerSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(album2Select, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        getContentPane().add(playerSelector, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 470, 310, 250));

        Player.setBackground(new java.awt.Color(102, 102, 102));
        Player.setForeground(new java.awt.Color(102, 102, 102));

        playerTitlebar.setBackground(new java.awt.Color(51, 51, 51));
        playerTitlebar.setForeground(new java.awt.Color(0, 0, 0));

        PlayerTitle.setBackground(new java.awt.Color(255, 255, 255));
        PlayerTitle.setForeground(new java.awt.Color(255, 255, 255));
        PlayerTitle.setText("Music Player");

        PlayerClose.setText("X");
        PlayerClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlayerCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout playerTitlebarLayout = new javax.swing.GroupLayout(playerTitlebar);
        playerTitlebar.setLayout(playerTitlebarLayout);
        playerTitlebarLayout.setHorizontalGroup(
            playerTitlebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(playerTitlebarLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(PlayerTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PlayerClose, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        playerTitlebarLayout.setVerticalGroup(
            playerTitlebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, playerTitlebarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(playerTitlebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PlayerTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PlayerClose))
                .addContainerGap())
        );

        playerTitle.setBackground(new java.awt.Color(255, 255, 255));
        playerTitle.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        playerTitle.setForeground(new java.awt.Color(255, 255, 255));
        playerTitle.setText("No Song Playing");

        playerArtist.setBackground(new java.awt.Color(255, 255, 255));
        playerArtist.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        playerArtist.setForeground(new java.awt.Color(255, 255, 255));
        playerArtist.setText("No Artist");

        playerArtwork.setBackground(new java.awt.Color(0, 0, 0));
        playerArtwork.setForeground(new java.awt.Color(0, 0, 0));
        playerArtwork.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Images/test.png"))); // NOI18N

        playerRelease.setBackground(new java.awt.Color(255, 255, 255));
        playerRelease.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        playerRelease.setForeground(new java.awt.Color(255, 255, 255));
        playerRelease.setText("No Release");

        playerPrev.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        playerPrev.setText("<");
        playerPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playerPrevActionPerformed(evt);
            }
        });

        playerStop.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        playerStop.setText("O");
        playerStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playerStopActionPerformed(evt);
            }
        });

        playerNext.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        playerNext.setText(">");
        playerNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playerNextActionPerformed(evt);
            }
        });

        player2Song.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        player2Song.setForeground(new java.awt.Color(255, 255, 255));
        player2Song.setText("Song 2");

        player1Song.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        player1Song.setForeground(new java.awt.Color(255, 255, 255));
        player1Song.setText("Song 1");

        player3Song.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        player3Song.setForeground(new java.awt.Color(255, 255, 255));
        player3Song.setText("Song 3");

        player4Song.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        player4Song.setForeground(new java.awt.Color(255, 255, 255));
        player4Song.setText("Song 4");

        player5Song.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        player5Song.setForeground(new java.awt.Color(255, 255, 255));
        player5Song.setText("Song 5");

        player2Play.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        player2Play.setText("Play");
        player2Play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                player2PlayActionPerformed(evt);
            }
        });

        player1Play.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        player1Play.setText("Play");
        player1Play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                player1PlayActionPerformed(evt);
            }
        });

        player3Play.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        player3Play.setText("Play");
        player3Play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                player3PlayActionPerformed(evt);
            }
        });

        player4Play.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        player4Play.setText("Play");
        player4Play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                player4PlayActionPerformed(evt);
            }
        });

        player5Play.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        player5Play.setText("Play");
        player5Play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                player5PlayActionPerformed(evt);
            }
        });

        playerSelectbtn.setText("Album");
        playerSelectbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playerSelectbtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PlayerLayout = new javax.swing.GroupLayout(Player);
        Player.setLayout(PlayerLayout);
        PlayerLayout.setHorizontalGroup(
            PlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(playerTitlebar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(PlayerLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(PlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PlayerLayout.createSequentialGroup()
                        .addComponent(player2Song)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(player2Play)
                        .addContainerGap())
                    .addGroup(PlayerLayout.createSequentialGroup()
                        .addGroup(PlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PlayerLayout.createSequentialGroup()
                                .addComponent(player5Song)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(player5Play))
                            .addGroup(PlayerLayout.createSequentialGroup()
                                .addComponent(player4Song)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(player4Play))
                            .addGroup(PlayerLayout.createSequentialGroup()
                                .addComponent(player3Song)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(player3Play)))
                        .addContainerGap())
                    .addGroup(PlayerLayout.createSequentialGroup()
                        .addComponent(player1Song)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(player1Play)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PlayerLayout.createSequentialGroup()
                        .addGroup(PlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(playerArtwork)
                            .addComponent(playerSelectbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PlayerLayout.createSequentialGroup()
                                .addComponent(playerPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(playerStop, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(playerNext, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(PlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(playerArtist, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(playerTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                                .addComponent(playerRelease, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(8, 8, 8))))
        );
        PlayerLayout.setVerticalGroup(
            PlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlayerLayout.createSequentialGroup()
                .addComponent(playerTitlebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(PlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PlayerLayout.createSequentialGroup()
                        .addComponent(playerTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(playerArtist)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(playerRelease))
                    .addComponent(playerArtwork, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(playerNext, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(playerStop, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(playerPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(playerSelectbtn, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addGroup(PlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(player1Song)
                    .addComponent(player1Play))
                .addGap(18, 18, 18)
                .addGroup(PlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(player2Play)
                    .addComponent(player2Song))
                .addGap(18, 18, 18)
                .addGroup(PlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(player3Song)
                    .addComponent(player3Play))
                .addGap(18, 18, 18)
                .addGroup(PlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(player4Song)
                    .addComponent(player4Play))
                .addGap(18, 18, 18)
                .addGroup(PlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(player5Song)
                    .addComponent(player5Play))
                .addGap(22, 22, 22))
        );

        getContentPane().add(Player, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 0, 310, 470));

        Calculator.setBackground(new java.awt.Color(102, 102, 102));

        calcTitlebar.setBackground(new java.awt.Color(51, 51, 51));
        calcTitlebar.setForeground(new java.awt.Color(51, 51, 51));

        CalcTitle.setBackground(new java.awt.Color(255, 255, 255));
        CalcTitle.setForeground(new java.awt.Color(255, 255, 255));
        CalcTitle.setText("Calculator");

        CalcClose.setText("X");
        CalcClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CalcCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout calcTitlebarLayout = new javax.swing.GroupLayout(calcTitlebar);
        calcTitlebar.setLayout(calcTitlebarLayout);
        calcTitlebarLayout.setHorizontalGroup(
            calcTitlebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(calcTitlebarLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(CalcTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(CalcClose, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        calcTitlebarLayout.setVerticalGroup(
            calcTitlebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(calcTitlebarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(calcTitlebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CalcTitle)
                    .addComponent(CalcClose))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        calcExpression.setText("Expression");

        calcEquals.setText("=");

        calcClear.setText("Clear");

        calcPlus.setText("+");

        calcMinus.setText("-");

        calcMulti.setText("X");

        calcDiv.setText("/");

        calcMod.setText("M");

        calcPeriod.setText(".");

        Calc0.setText("0");

        Calc1.setText("1");

        Calc2.setText("2");

        Calc3.setText("3");

        Calc4.setText("4");

        Calc5.setText("5");

        Calc6.setText("6");

        Calc7.setText("7");

        Calc8.setText("8");

        Calc9.setText("9");

        javax.swing.GroupLayout CalculatorLayout = new javax.swing.GroupLayout(Calculator);
        Calculator.setLayout(CalculatorLayout);
        CalculatorLayout.setHorizontalGroup(
            CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(calcTitlebar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(CalculatorLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(calcField, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(calcExpression, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(CalculatorLayout.createSequentialGroup()
                            .addGroup(CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(CalculatorLayout.createSequentialGroup()
                                    .addGroup(CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(Calc0, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(Calc7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(Calc1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(Calc4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(Calc2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(calcPeriod, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(Calc5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(Calc8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(calcClear, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(CalculatorLayout.createSequentialGroup()
                                    .addGroup(CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(Calc3, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                                        .addComponent(Calc9, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                                        .addComponent(Calc6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(calcMinus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(calcMulti, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(calcDiv, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CalculatorLayout.createSequentialGroup()
                                    .addComponent(calcMod, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(calcPlus, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(calcEquals, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        CalculatorLayout.setVerticalGroup(
            CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CalculatorLayout.createSequentialGroup()
                .addComponent(calcTitlebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(calcField, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(calcExpression, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(CalculatorLayout.createSequentialGroup()
                            .addGap(50, 50, 50)
                            .addComponent(calcMulti, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(calcMinus, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(calcPlus, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(CalculatorLayout.createSequentialGroup()
                            .addComponent(Calc7, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(112, 112, 112)
                            .addComponent(Calc0, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CalculatorLayout.createSequentialGroup()
                        .addGroup(CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Calc8, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Calc9, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(calcDiv, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Calc5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Calc4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Calc6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Calc2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Calc1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(calcPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(calcMod, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CalculatorLayout.createSequentialGroup()
                        .addComponent(Calc3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)))
                .addGap(12, 12, 12)
                .addGroup(CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(calcEquals, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(calcClear, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );

        getContentPane().add(Calculator, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 0, 270, 450));

        Taskbar.setBackground(new java.awt.Color(0, 0, 0));
        Taskbar.setForeground(new java.awt.Color(255, 255, 255));

        startBtn.setBackground(new java.awt.Color(0, 0, 0));
        startBtn.setForeground(new java.awt.Color(0, 0, 0));
        startBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Images/start.png"))); // NOI18N
        startBtn.setToolTipText("");
        startBtn.setBorder(null);
        startBtn.setBorderPainted(false);
        startBtn.setMaximumSize(new java.awt.Dimension(110, 111));
        startBtn.setPreferredSize(new java.awt.Dimension(110, 111));
        startBtn.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Images/startclick.png"))); // NOI18N
        startBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startBtnActionPerformed(evt);
            }
        });

        calculatorBtn.setBackground(new java.awt.Color(0, 0, 0));
        calculatorBtn.setForeground(new java.awt.Color(255, 255, 255));
        calculatorBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Images/calc.png"))); // NOI18N
        calculatorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculatorBtnActionPerformed(evt);
            }
        });

        notepadBtn.setBackground(new java.awt.Color(0, 0, 0));
        notepadBtn.setForeground(new java.awt.Color(255, 255, 255));
        notepadBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Images/notepad.png"))); // NOI18N
        notepadBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notepadBtnActionPerformed(evt);
            }
        });

        wallpaperBtn.setBackground(new java.awt.Color(0, 0, 0));
        wallpaperBtn.setForeground(new java.awt.Color(255, 255, 255));
        wallpaperBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Images/wallpaper.png"))); // NOI18N
        wallpaperBtn.setText("Change Wallpaper");
        wallpaperBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wallpaperBtnActionPerformed(evt);
            }
        });

        Clock.setEditable(false);
        Clock.setBackground(new java.awt.Color(0, 0, 0));
        Clock.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Clock.setForeground(new java.awt.Color(255, 255, 255));
        Clock.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Clock.setText("Loading");
        Clock.setAutoscrolls(false);
        Clock.setMaximumSize(new java.awt.Dimension(68, 26));
        Clock.setOpaque(true);
        Clock.setPreferredSize(new java.awt.Dimension(68, 26));
        Clock.setSelectionColor(new java.awt.Color(255, 255, 255));

        musicBtn.setBackground(new java.awt.Color(0, 0, 0));
        musicBtn.setForeground(new java.awt.Color(255, 255, 255));
        musicBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Images/music.png"))); // NOI18N
        musicBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                musicBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout TaskbarLayout = new javax.swing.GroupLayout(Taskbar);
        Taskbar.setLayout(TaskbarLayout);
        TaskbarLayout.setHorizontalGroup(
            TaskbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TaskbarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(startBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(calculatorBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(notepadBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(musicBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 883, Short.MAX_VALUE)
                .addComponent(wallpaperBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Clock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
        TaskbarLayout.setVerticalGroup(
            TaskbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TaskbarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(TaskbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(musicBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(notepadBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(calculatorBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(TaskbarLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(startBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Clock, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(wallpaperBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(Taskbar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 720, 1370, 50));

        Wallpaper.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Images/Wall1.jpg"))); // NOI18N
        Wallpaper.setMaximumSize(new java.awt.Dimension(1366, 770));
        Wallpaper.setMinimumSize(new java.awt.Dimension(1366, 770));
        Wallpaper.setPreferredSize(new java.awt.Dimension(1366, 770));
        getContentPane().add(Wallpaper, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, 770));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void startBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startBtnActionPerformed
        // TODO add your handling code here:
        startMenu.setVisible(true);
    }//GEN-LAST:event_startBtnActionPerformed

    private void notepadBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notepadBtnActionPerformed
        // TODO add your handling code here:
        Notepad.setVisible(true);
    }//GEN-LAST:event_notepadBtnActionPerformed

    private void calculatorBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculatorBtnActionPerformed
        // TODO add your handling code here:
        Calculator.setVisible(true);
    }//GEN-LAST:event_calculatorBtnActionPerformed

    private void wallpaperBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wallpaperBtnActionPerformed
        currentWallpaper = (currentWallpaper + 1) % wallpapers.length;
        Wallpaper.setIcon(new javax.swing.ImageIcon(getClass().getResource(wallpapers[currentWallpaper])));
    }//GEN-LAST:event_wallpaperBtnActionPerformed

    private void CalcCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CalcCloseActionPerformed
        // TODO add your handling code here:
        Calculator.setVisible(false);
    }//GEN-LAST:event_CalcCloseActionPerformed

    private void NotepadCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NotepadCloseActionPerformed
        // TODO add your handling code here:
        Notepad.setVisible(false);
    }//GEN-LAST:event_NotepadCloseActionPerformed

    private void PlayerCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlayerCloseActionPerformed
        // TODO add your handling code here:
        Player.setVisible(false);
        playerSelector.setVisible(false);
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }//GEN-LAST:event_PlayerCloseActionPerformed

    private void musicBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_musicBtnActionPerformed
        // TODO add your handling code here:
        String albumCheck = playerRelease.getText();
        
        Player.setVisible(true);
        playerSelector.setVisible(true);
        
        if (albumCheck.equals("No Release")) {
            playerSelector.setVisible(true);
        } else {
            playerSelector.setVisible(false);
        }
    }//GEN-LAST:event_musicBtnActionPerformed

    private void album1SelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_album1SelectActionPerformed
        // TODO add your handling code here:
        playerSelector.setVisible(false);
        
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
        
        playerTitle.setText("Choose a Song");
        playerArtist.setText("Random Artist");
        playerRelease.setText("Random Music 1");
        currentAlbum = "Random Music 1";
        
        player1Song.setText("Autumn");
        player2Song.setText("Cafe");
        player3Song.setText("Jumpy");
        player4Song.setText("Lights");
        player5Song.setText("Waves");
        
        try {
            playerArtwork.setIcon( new ImageIcon(ImageIO.read( new File("src/GUI/Images/random-music-1-large.png"))));
        } catch (IOException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_album1SelectActionPerformed

    private void album2SelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_album2SelectActionPerformed
        // TODO add your handling code here:
        playerSelector.setVisible(false);
        
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
        
        playerTitle.setText("Choose a Song");
        playerArtist.setText("Random Artist");
        playerRelease.setText("Random Music 2");
        currentAlbum = "Random Music 2";
        
        player1Song.setText("Leap");
        player2Song.setText("Refrain");
        player3Song.setText("Sleep");
        player4Song.setText("Slip");
        player5Song.setText("Slow Fall");
        
        try {
            playerArtwork.setIcon( new ImageIcon(ImageIO.read( new File("src/GUI/Images/random-music-2-large.png"))));
        } catch (IOException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_album2SelectActionPerformed

    private void playerStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playerStopActionPerformed
        // TODO add your handling code here:
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
        playerTitle.setText("Song Stopped");
    }//GEN-LAST:event_playerStopActionPerformed

    private void player1PlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_player1PlayActionPerformed
        // TODO add your handling code here:
        currentAlbum = playerRelease.getText(); // Get album
        currentTrackIndex = 0; // Track
        playCurrentTrack();
    }//GEN-LAST:event_player1PlayActionPerformed

    private void playerSelectbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playerSelectbtnActionPerformed
        // TODO add your handling code here:
        playerSelector.setVisible(true);
    }//GEN-LAST:event_playerSelectbtnActionPerformed

    private void player2PlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_player2PlayActionPerformed
        // TODO add your handling code here:
        currentAlbum = playerRelease.getText(); // Get album
        currentTrackIndex = 1; // Track
        playCurrentTrack();
    }//GEN-LAST:event_player2PlayActionPerformed

    private void player3PlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_player3PlayActionPerformed
        // TODO add your handling code here:
        currentAlbum = playerRelease.getText(); // Get album
        currentTrackIndex = 2; // Track
        playCurrentTrack();
    }//GEN-LAST:event_player3PlayActionPerformed

    private void player4PlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_player4PlayActionPerformed
        // TODO add your handling code here:
        currentAlbum = playerRelease.getText(); // Get album
        currentTrackIndex = 3; // Track
        playCurrentTrack();
    }//GEN-LAST:event_player4PlayActionPerformed

    private void player5PlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_player5PlayActionPerformed
        // TODO add your handling code here:
        currentAlbum = playerRelease.getText(); // Get album
        currentTrackIndex = 4; // Track
        playCurrentTrack();
    }//GEN-LAST:event_player5PlayActionPerformed

    private void playerPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playerPrevActionPerformed
        // TODO add your handling code here:
        if (currentTrackIndex > 0) {
            currentTrackIndex--;
            playCurrentTrack();
        }
    }//GEN-LAST:event_playerPrevActionPerformed

    private void playerNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playerNextActionPerformed
        // TODO add your handling code here:
        String[] selectedAlbum = currentAlbum.equals("Random Music 1") ? album1 : album2;
        
        if (currentTrackIndex < selectedAlbum.length - 1) {
            currentTrackIndex++;
            playCurrentTrack();
        }
    }//GEN-LAST:event_playerNextActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainGUI().setVisible(true);
            }
        });
        
        SwingUtilities.invokeLater(MainGUI::new);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Calc0;
    private javax.swing.JButton Calc1;
    private javax.swing.JButton Calc2;
    private javax.swing.JButton Calc3;
    private javax.swing.JButton Calc4;
    private javax.swing.JButton Calc5;
    private javax.swing.JButton Calc6;
    private javax.swing.JButton Calc7;
    private javax.swing.JButton Calc8;
    private javax.swing.JButton Calc9;
    private javax.swing.JButton CalcClose;
    private javax.swing.JLabel CalcTitle;
    private javax.swing.JPanel Calculator;
    private javax.swing.JTextField Clock;
    private javax.swing.JPanel Notepad;
    private javax.swing.JButton NotepadClose;
    private javax.swing.JLabel NotepadTitle;
    private javax.swing.JPanel Player;
    private javax.swing.JButton PlayerClose;
    private javax.swing.JLabel PlayerTitle;
    private javax.swing.JPanel Taskbar;
    private javax.swing.JLabel Wallpaper;
    private javax.swing.JButton album1Select;
    private javax.swing.JButton album2Select;
    private javax.swing.JButton calcClear;
    private javax.swing.JButton calcDiv;
    private javax.swing.JButton calcEquals;
    private javax.swing.JTextField calcExpression;
    private javax.swing.JTextField calcField;
    private javax.swing.JButton calcMinus;
    private javax.swing.JButton calcMod;
    private javax.swing.JButton calcMulti;
    private javax.swing.JButton calcPeriod;
    private javax.swing.JButton calcPlus;
    private javax.swing.JPanel calcTitlebar;
    private javax.swing.JButton calculatorBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JButton musicBtn;
    private javax.swing.JButton notepadBtn;
    private javax.swing.JPanel notepadTitlebar;
    private javax.swing.JButton player1Play;
    private javax.swing.JLabel player1Song;
    private javax.swing.JButton player2Play;
    private javax.swing.JLabel player2Song;
    private javax.swing.JButton player3Play;
    private javax.swing.JLabel player3Song;
    private javax.swing.JButton player4Play;
    private javax.swing.JLabel player4Song;
    private javax.swing.JButton player5Play;
    private javax.swing.JLabel player5Song;
    private javax.swing.JLabel playerArtist;
    private javax.swing.JLabel playerArtwork;
    private javax.swing.JButton playerNext;
    private javax.swing.JButton playerPrev;
    private javax.swing.JLabel playerRelease;
    private javax.swing.JButton playerSelectbtn;
    private javax.swing.JPanel playerSelector;
    private javax.swing.JButton playerStop;
    private javax.swing.JLabel playerTitle;
    private javax.swing.JPanel playerTitlebar;
    private javax.swing.JButton startBtn;
    private javax.swing.JPanel startMenu;
    private javax.swing.JButton wallpaperBtn;
    // End of variables declaration//GEN-END:variables
}
