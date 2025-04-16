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
import java.io.*;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

// MEDIA PLAYER
import java.io.File;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

// NOTEPAD
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.nio.file.*;
import java.util.List;

// CALCULATOR

/**
 *
 * @author 22156
 */
public class MainGUI extends javax.swing.JFrame {    
    private Clip clip;
    
    // MUSIC
    private int currentTrackIndex = 0;
    private String currentAlbum = "";
    
    // NOTEPAD
    private DefaultTableModel tableModel;
    private File notesDir = new File("notes");
    
    // CALCULATOR
    private int operationType;
    
    public MainGUI() {
        initComponents();
        // NOTEPAD
        if (!notesDir.exists()) notesDir.mkdir(); // creates notes directory
        tableModel = (DefaultTableModel) notepadSelectTable.getModel(); // gets tableModel
        
        // CLOCK
        javax.swing.Timer timer = new javax.swing.Timer(1000, (java.awt.event.ActionEvent evt) -> { // updates every second
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); // format for the clock
            String currentTime = sdf.format(new Date()); // define clock
            Clock.setText(currentTime); // clock
        });
        timer.start(); // begins clock
        
        // ICON
        ImageIcon icon = new ImageIcon("src/GUI/Images/desktop.png"); // cant test since my desktop environment doesnt render window icons
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
        
        // STARTUP (initially hides all windows, i know kinda dumb)
        Calculator.setVisible(false);
        calculatorSelector.setVisible(false);
        Notepad.setVisible(false);
        notepadSelector.setVisible(false);
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
    final Point[] initialClick = { null }; // one point per window

    titlebar.addMouseListener(new MouseAdapter() { // looks for mouse 
        public void mousePressed(MouseEvent e) {
            initialClick[0] = e.getPoint(); // gets click point
            Container parent = window.getParent();
            if (parent != null) {
                parent.setComponentZOrder(window, 0); // moves to front (kinda buggy)
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
    
    private void styleButton(javax.swing.JButton button) { // Styling for some buttons
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
    }
    
    private void styleTextField(javax.swing.JTextField textField) { // Styling for some text fields
        textField.setBorder(null);
        textField.setFocusable(false);
    }
    
    // PLAYER 
    
    private void playSound(String filePath) {
        try {
            if (clip != null && clip.isRunning()) { // stops previous song
                clip.stop();
                clip.close();
            }
            
            File soundFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile); // AudioInputStream getting file

            clip = AudioSystem.getClip(); // starts song
            clip.open(audioStream);
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }
    
    private void playCurrentTrack() {
        String[] selectedAlbum;

        if (currentAlbum.equals("Random Music 1")) { // checks currentAlbum (defined on button press)
            selectedAlbum = album1;
        } else if (currentAlbum.equals("Random Music 2")) {
            selectedAlbum = album2;
        } else {
            playerTitle.setText("No Album Selected!!"); // if ur silly and somehow dont select an album
            return;
        }

        if (currentTrackIndex >= 0 && currentTrackIndex < selectedAlbum.length) {
            playSound(selectedAlbum[currentTrackIndex]);
            String path = selectedAlbum[currentTrackIndex];
            playerTitle.setText(path.substring(path.lastIndexOf("/") + 1));
        }
    }

    private String[] album1 = { // add ur songs here (will need more buttons/song titles tho)
        "src/GUI/Music/1/autumn.wav",
        "src/GUI/Music/1/cafe.wav",
        "src/GUI/Music/1/jumpy.wav",
        "src/GUI/Music/1/lights.wav",
        "src/GUI/Music/1/waves.wav"};
    
    private String[] album2 = { // add ur songs here (will need more buttons/song titles tho)
        "src/GUI/Music/2/leap.wav",
        "src/GUI/Music/2/refrain.wav",
        "src/GUI/Music/2/sleep.wav",
        "src/GUI/Music/2/slip.wav",
        "src/GUI/Music/2/slow fall.wav"};
    
    // NOTEPAD
    
    private void saveNote() {
        String title = JOptionPane.showInputDialog(this, "Enter note title:");  // asks user for a title
        if (title == null || title.trim().isEmpty()) return; // if they dont set a title, nothing happens

        File file = new File(notesDir, title + ".txt"); // creates file with title + .txt
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(notepadArea.getText());
            JOptionPane.showMessageDialog(this, "Note saved!"); // dialog
            System.out.println("path: " + file.getAbsolutePath()); // for debug purposes, not nessecary.
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving note."); // fail dialog (surely this never happens)
        }
    }
    
    private void loadNotesIntoTable() { // Loads saved notes into notepadSelectTable
        tableModel.setRowCount(0);
        for (File file : Objects.requireNonNull(notesDir.listFiles())) {
            if (file.getName().endsWith(".txt")) { // gets all files with .txt
                try {
                    List<String> lines = Files.readAllLines(file.toPath()); // reads and applies data to notepadSelectTable 
                    String content = String.join(" ", lines);
                    String preview = String.join(" ", Arrays.stream(content.split(" ")).toArray(String[]::new));
                    tableModel.addRow(new Object[]{file.getName().replace(".txt", ""), preview});
                } catch (IOException e) { // java exception if things fail (netbeans reccomeneded it)
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void openSelectedNote() { // OPEN BUTTON
        int row = notepadSelectTable.getSelectedRow(); // selection antics
        if (row == -1) return; // if theres no row

        String title = (String) tableModel.getValueAt(row, 0);
        File file = new File(notesDir, title + ".txt"); // checks for note title + txt
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            notepadArea.setText(String.join("\n", lines)); // opens saved file in notepadArea
            notepadSelector.setVisible(false); 
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to open note.");
        }
    }

    private void deleteSelectedNote() { // DELETE BUTTON
        int row = notepadSelectTable.getSelectedRow(); // gets selected row
        if (row == -1) return; // if theres no row

        String title = (String) tableModel.getValueAt(row, 0);
        File file = new File(notesDir, title + ".txt"); // checks for note title + txt
        if (file.delete()) {
            JOptionPane.showMessageDialog(this, "Note deleted.");
            loadNotesIntoTable(); // could put this before idk
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete note."); // realistically, when is this gonna happen smh
        }
    }
    
    // CALCULATOR
    
    private void calcCalculate() {
        String operation = calcOperation.getText(); // get text from button
        String num1uf = calcExpression1.getText().trim(); // get numbers from fields
        String num2uf = calcExpression2.getText().trim();

        try {
            long longNum1 = Long.parseLong(num1uf); // To check for longer numbers first, also converts the string to a recognizeable format
            long longNum2 = Long.parseLong(num2uf);

            if (longNum1 > Integer.MAX_VALUE || longNum1 < Integer.MIN_VALUE | // checks if larger than int max value, less than int min value
                longNum2 > Integer.MAX_VALUE || longNum2 < Integer.MIN_VALUE) {
                calcField.setText("Numbers too large!");
                calcCalculation.setText("dont be silly");
                return;
            }
            
            int num1 = Integer.parseInt(num1uf); // convert string to integers
            int num2 = Integer.parseInt(num2uf);

            if ((operation.equals("Divide") || operation.equals("Modulus")) && num2 == 0) { // Check for division by 0
                calcField.setText("Error: Division by zero");
                calcCalculation.setText("you cant divide by zero smh");
            } else if (operation.equals("Plus")) { // plus
                int sum = num1 + num2;
                calcField.setText(String.valueOf(sum)); 
                calcCalculation.setText(num1 + " + " + num2);
            } else if (operation.equals("Minus")) { // minus
                int sum = num1 - num2;
                calcField.setText(String.valueOf(sum));
                calcCalculation.setText(num1 + " - " + num2);
            } else if (operation.equals("Multiply")) { // multiply
                int sum = num1 * num2;
                calcField.setText(String.valueOf(sum)); 
                calcCalculation.setText(num1 + " x " + num2);
            } else if (operation.equals("Divide")) { // divide
                int sum = num1 / num2;
                calcField.setText(String.valueOf(sum)); 
                calcCalculation.setText(num1 + " ÷ " + num2);
            } else if (operation.equals("Modulus")) { // modulus
                int sum = num1 % num2;
                calcField.setText(String.valueOf(sum)); 
                calcCalculation.setText(num1 + " mod " + num2);
            } else if (operation.equals("Percent Of")) { // percent of
                int sum = (num1 * num2) / 100;
                calcField.setText(String.valueOf(sum)); 
                calcCalculation.setText(num1 + "% of " + num2);
            } else { // 
                calcField.setText("Unknown operation - how did we get here ??");
                calcCalculation.setText("Try closing the calculator");
            }
        } catch (ArithmeticException e) {
            calcField.setText("Error: Your math sucks");
            calcCalculation.setText("That operation aint it chief");
        } catch (NumberFormatException e) {
            calcField.setText("Error: Letters/bad format detected");
            calcCalculation.setText("You typed letters, or nothing smh");
        } catch (Exception e) {
            calcField.setText("Unexpected Error - wth");
            calcCalculation.setText("Something broke... hard");
        } finally {
            calcExpression1.setText("");
            calcExpression2.setText("");
        }
    }
    
    // WALLPAPER SELECT
    
    private String[] wallpapers = {"/GUI/Images/Wall1.jpg", "/GUI/Images/Wall2.jpg", "/GUI/Images/Wall3.jpg"}; // array for three wallpapers, might add more. you can (marker) if you want
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
        playerSelector = new javax.swing.JPanel();
        playerSelectTitle = new javax.swing.JLabel();
        album2Image = new javax.swing.JLabel();
        album1Image = new javax.swing.JLabel();
        album2Select = new javax.swing.JButton();
        album1Select = new javax.swing.JButton();
        albumSelectClose = new javax.swing.JButton();
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
        notepadSelector = new javax.swing.JPanel();
        notepadSelectOpen = new javax.swing.JButton();
        notepadSelectDelete = new javax.swing.JButton();
        notepadSelectTitle = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        notepadSelectTable = new javax.swing.JTable();
        notepadSelectReturn = new javax.swing.JButton();
        notepadSelectClose = new javax.swing.JButton();
        Notepad = new javax.swing.JPanel();
        notepadTitlebar = new javax.swing.JPanel();
        NotepadTitle = new javax.swing.JLabel();
        NotepadClose = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        notepadArea = new javax.swing.JTextArea();
        notepadOpen = new javax.swing.JButton();
        notepadSave = new javax.swing.JButton();
        notepadClear = new javax.swing.JButton();
        calculatorSelector = new javax.swing.JPanel();
        calculatorSelectText = new javax.swing.JLabel();
        calculatorOpPlus = new javax.swing.JButton();
        calculatorOpMinus = new javax.swing.JButton();
        calculatorOpMulti = new javax.swing.JButton();
        calculatorOpDivide = new javax.swing.JButton();
        calculatorOpMod = new javax.swing.JButton();
        calculatorOpPercent = new javax.swing.JButton();
        calculatorSelectClose = new javax.swing.JButton();
        Calculator = new javax.swing.JPanel();
        calcTitlebar = new javax.swing.JPanel();
        CalcTitle = new javax.swing.JLabel();
        CalcClose = new javax.swing.JButton();
        calcField = new javax.swing.JTextField();
        calcExpression1 = new javax.swing.JTextField();
        calcEquals = new javax.swing.JButton();
        calcClear = new javax.swing.JButton();
        calcNum1 = new javax.swing.JLabel();
        calcNum2 = new javax.swing.JLabel();
        calcExpression2 = new javax.swing.JTextField();
        calcOperationText = new javax.swing.JLabel();
        calcOperation = new javax.swing.JButton();
        calcCalculation = new javax.swing.JTextField();
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

        playerSelector.setBackground(new java.awt.Color(51, 51, 51));

        playerSelectTitle.setBackground(new java.awt.Color(255, 255, 255));
        playerSelectTitle.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        playerSelectTitle.setForeground(new java.awt.Color(255, 255, 255));
        playerSelectTitle.setText("Choose an Album");

        album2Image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Images/random-music-2-small.png"))); // NOI18N

        album1Image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Images/random-music-1-small.png"))); // NOI18N

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

        albumSelectClose.setBackground(new java.awt.Color(0, 0, 0));
        albumSelectClose.setForeground(new java.awt.Color(255, 255, 255));
        albumSelectClose.setText("Close");
        albumSelectClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                albumSelectCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout playerSelectorLayout = new javax.swing.GroupLayout(playerSelector);
        playerSelector.setLayout(playerSelectorLayout);
        playerSelectorLayout.setHorizontalGroup(
            playerSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(playerSelectorLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(playerSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(playerSelectorLayout.createSequentialGroup()
                        .addComponent(playerSelectTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(albumSelectClose))
                    .addGroup(playerSelectorLayout.createSequentialGroup()
                        .addComponent(album2Image, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(album2Select, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(playerSelectorLayout.createSequentialGroup()
                        .addComponent(album1Image, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(album1Select, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 24, Short.MAX_VALUE))
        );
        playerSelectorLayout.setVerticalGroup(
            playerSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(playerSelectorLayout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(playerSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(playerSelectTitle, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(albumSelectClose, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(playerSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(album1Image)
                    .addComponent(album1Select, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(playerSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(album2Image, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        PlayerClose.setBackground(new java.awt.Color(51, 0, 0));
        PlayerClose.setForeground(new java.awt.Color(255, 255, 255));
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
        playerTitle.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        playerTitle.setForeground(new java.awt.Color(255, 255, 255));
        playerTitle.setText("No Song Playing");

        playerArtist.setBackground(new java.awt.Color(255, 255, 255));
        playerArtist.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        playerArtist.setForeground(new java.awt.Color(255, 255, 255));
        playerArtist.setText("No Artist");

        playerArtwork.setBackground(new java.awt.Color(0, 0, 0));
        playerArtwork.setForeground(new java.awt.Color(0, 0, 0));
        playerArtwork.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Images/test.png"))); // NOI18N

        playerRelease.setBackground(new java.awt.Color(255, 255, 255));
        playerRelease.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        playerRelease.setForeground(new java.awt.Color(255, 255, 255));
        playerRelease.setText("No Release");

        playerPrev.setBackground(new java.awt.Color(0, 0, 0));
        playerPrev.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        playerPrev.setForeground(new java.awt.Color(255, 255, 255));
        playerPrev.setText("<");
        playerPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playerPrevActionPerformed(evt);
            }
        });

        playerStop.setBackground(new java.awt.Color(0, 0, 0));
        playerStop.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        playerStop.setForeground(new java.awt.Color(255, 255, 255));
        playerStop.setText("O");
        playerStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playerStopActionPerformed(evt);
            }
        });

        playerNext.setBackground(new java.awt.Color(0, 0, 0));
        playerNext.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        playerNext.setForeground(new java.awt.Color(255, 255, 255));
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

        player2Play.setBackground(new java.awt.Color(0, 0, 0));
        player2Play.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        player2Play.setForeground(new java.awt.Color(255, 255, 255));
        player2Play.setText("Play");
        player2Play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                player2PlayActionPerformed(evt);
            }
        });

        player1Play.setBackground(new java.awt.Color(0, 0, 0));
        player1Play.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        player1Play.setForeground(new java.awt.Color(255, 255, 255));
        player1Play.setText("Play");
        player1Play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                player1PlayActionPerformed(evt);
            }
        });

        player3Play.setBackground(new java.awt.Color(0, 0, 0));
        player3Play.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        player3Play.setForeground(new java.awt.Color(255, 255, 255));
        player3Play.setText("Play");
        player3Play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                player3PlayActionPerformed(evt);
            }
        });

        player4Play.setBackground(new java.awt.Color(0, 0, 0));
        player4Play.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        player4Play.setForeground(new java.awt.Color(255, 255, 255));
        player4Play.setText("Play");
        player4Play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                player4PlayActionPerformed(evt);
            }
        });

        player5Play.setBackground(new java.awt.Color(0, 0, 0));
        player5Play.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        player5Play.setForeground(new java.awt.Color(255, 255, 255));
        player5Play.setText("Play");
        player5Play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                player5PlayActionPerformed(evt);
            }
        });

        playerSelectbtn.setBackground(new java.awt.Color(0, 0, 0));
        playerSelectbtn.setForeground(new java.awt.Color(255, 255, 255));
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
                        .addGroup(PlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(PlayerLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(playerPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(playerStop, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(playerNext, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(PlayerLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(playerTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(PlayerLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(PlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(playerArtist, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(playerRelease, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(8, 8, 8))))
        );
        PlayerLayout.setVerticalGroup(
            PlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlayerLayout.createSequentialGroup()
                .addComponent(playerTitlebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(PlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PlayerLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(playerTitle)
                        .addGap(12, 12, 12)
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

        notepadSelector.setBackground(new java.awt.Color(51, 51, 51));

        notepadSelectOpen.setBackground(new java.awt.Color(0, 0, 0));
        notepadSelectOpen.setForeground(new java.awt.Color(255, 255, 255));
        notepadSelectOpen.setText("Open");
        notepadSelectOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notepadSelectOpenActionPerformed(evt);
            }
        });

        notepadSelectDelete.setBackground(new java.awt.Color(0, 0, 0));
        notepadSelectDelete.setForeground(new java.awt.Color(255, 255, 255));
        notepadSelectDelete.setText("Delete");
        notepadSelectDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notepadSelectDeleteActionPerformed(evt);
            }
        });

        notepadSelectTitle.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        notepadSelectTitle.setForeground(new java.awt.Color(255, 255, 255));
        notepadSelectTitle.setText("Choose A Note");

        notepadSelectTable.setBackground(new java.awt.Color(51, 51, 51));
        notepadSelectTable.setForeground(new java.awt.Color(255, 255, 255));
        notepadSelectTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Name", "Content"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        notepadSelectTable.setRowSelectionAllowed(false);
        jScrollPane2.setViewportView(notepadSelectTable);

        notepadSelectReturn.setBackground(new java.awt.Color(0, 0, 0));
        notepadSelectReturn.setForeground(new java.awt.Color(255, 255, 255));
        notepadSelectReturn.setText("Return");
        notepadSelectReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notepadSelectReturnActionPerformed(evt);
            }
        });

        notepadSelectClose.setBackground(new java.awt.Color(0, 0, 0));
        notepadSelectClose.setForeground(new java.awt.Color(255, 255, 255));
        notepadSelectClose.setText("Close");
        notepadSelectClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notepadSelectCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout notepadSelectorLayout = new javax.swing.GroupLayout(notepadSelector);
        notepadSelector.setLayout(notepadSelectorLayout);
        notepadSelectorLayout.setHorizontalGroup(
            notepadSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notepadSelectorLayout.createSequentialGroup()
                .addGroup(notepadSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(notepadSelectorLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE))
                    .addGroup(notepadSelectorLayout.createSequentialGroup()
                        .addGap(183, 183, 183)
                        .addComponent(notepadSelectTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(notepadSelectClose))
                    .addGroup(notepadSelectorLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(notepadSelectReturn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(notepadSelectOpen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(notepadSelectDelete)))
                .addContainerGap())
        );
        notepadSelectorLayout.setVerticalGroup(
            notepadSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notepadSelectorLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(notepadSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(notepadSelectTitle)
                    .addComponent(notepadSelectClose))
                .addGap(18, 18, 18)
                .addGroup(notepadSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(notepadSelectOpen)
                    .addComponent(notepadSelectDelete)
                    .addComponent(notepadSelectReturn))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(notepadSelector, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 320, 480, 400));

        Notepad.setBackground(new java.awt.Color(102, 102, 102));
        Notepad.setForeground(new java.awt.Color(102, 102, 102));

        notepadTitlebar.setBackground(new java.awt.Color(51, 51, 51));
        notepadTitlebar.setForeground(new java.awt.Color(51, 51, 51));

        NotepadTitle.setBackground(new java.awt.Color(255, 255, 255));
        NotepadTitle.setForeground(new java.awt.Color(255, 255, 255));
        NotepadTitle.setText("Notepad");

        NotepadClose.setBackground(new java.awt.Color(51, 0, 0));
        NotepadClose.setForeground(new java.awt.Color(255, 255, 255));
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

        notepadArea.setBackground(new java.awt.Color(51, 51, 51));
        notepadArea.setColumns(20);
        notepadArea.setForeground(new java.awt.Color(255, 255, 255));
        notepadArea.setRows(5);
        jScrollPane1.setViewportView(notepadArea);

        notepadOpen.setBackground(new java.awt.Color(0, 0, 0));
        notepadOpen.setForeground(new java.awt.Color(255, 255, 255));
        notepadOpen.setText("Open/Modify");
        notepadOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notepadOpenActionPerformed(evt);
            }
        });

        notepadSave.setBackground(new java.awt.Color(0, 0, 0));
        notepadSave.setForeground(new java.awt.Color(255, 255, 255));
        notepadSave.setText("Save File");
        notepadSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notepadSaveActionPerformed(evt);
            }
        });

        notepadClear.setBackground(new java.awt.Color(0, 0, 0));
        notepadClear.setForeground(new java.awt.Color(255, 255, 255));
        notepadClear.setText("Clear");
        notepadClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notepadClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout NotepadLayout = new javax.swing.GroupLayout(Notepad);
        Notepad.setLayout(NotepadLayout);
        NotepadLayout.setHorizontalGroup(
            NotepadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(notepadTitlebar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(NotepadLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(NotepadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
                    .addGroup(NotepadLayout.createSequentialGroup()
                        .addComponent(notepadOpen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(notepadClear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(notepadSave)))
                .addContainerGap())
        );
        NotepadLayout.setVerticalGroup(
            NotepadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NotepadLayout.createSequentialGroup()
                .addComponent(notepadTitlebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(NotepadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(notepadOpen)
                    .addComponent(notepadSave)
                    .addComponent(notepadClear))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(Notepad, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 0, 480, 320));

        calculatorSelector.setBackground(new java.awt.Color(51, 51, 51));

        calculatorSelectText.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        calculatorSelectText.setForeground(new java.awt.Color(255, 255, 255));
        calculatorSelectText.setText("Choose Operation");

        calculatorOpPlus.setBackground(new java.awt.Color(0, 0, 0));
        calculatorOpPlus.setForeground(new java.awt.Color(255, 255, 255));
        calculatorOpPlus.setText("Plus");
        calculatorOpPlus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculatorOpPlusActionPerformed(evt);
            }
        });

        calculatorOpMinus.setBackground(new java.awt.Color(0, 0, 0));
        calculatorOpMinus.setForeground(new java.awt.Color(255, 255, 255));
        calculatorOpMinus.setText("Minus");
        calculatorOpMinus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculatorOpMinusActionPerformed(evt);
            }
        });

        calculatorOpMulti.setBackground(new java.awt.Color(0, 0, 0));
        calculatorOpMulti.setForeground(new java.awt.Color(255, 255, 255));
        calculatorOpMulti.setText("Multiply");
        calculatorOpMulti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculatorOpMultiActionPerformed(evt);
            }
        });

        calculatorOpDivide.setBackground(new java.awt.Color(0, 0, 0));
        calculatorOpDivide.setForeground(new java.awt.Color(255, 255, 255));
        calculatorOpDivide.setText("Divide");
        calculatorOpDivide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculatorOpDivideActionPerformed(evt);
            }
        });

        calculatorOpMod.setBackground(new java.awt.Color(0, 0, 0));
        calculatorOpMod.setForeground(new java.awt.Color(255, 255, 255));
        calculatorOpMod.setText("Modulus");
        calculatorOpMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculatorOpModActionPerformed(evt);
            }
        });

        calculatorOpPercent.setBackground(new java.awt.Color(0, 0, 0));
        calculatorOpPercent.setForeground(new java.awt.Color(255, 255, 255));
        calculatorOpPercent.setText("Percent Of");
        calculatorOpPercent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculatorOpPercentActionPerformed(evt);
            }
        });

        calculatorSelectClose.setBackground(new java.awt.Color(0, 0, 0));
        calculatorSelectClose.setForeground(new java.awt.Color(255, 255, 255));
        calculatorSelectClose.setText("Close");
        calculatorSelectClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculatorSelectCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout calculatorSelectorLayout = new javax.swing.GroupLayout(calculatorSelector);
        calculatorSelector.setLayout(calculatorSelectorLayout);
        calculatorSelectorLayout.setHorizontalGroup(
            calculatorSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(calculatorSelectorLayout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(calculatorSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, calculatorSelectorLayout.createSequentialGroup()
                        .addGroup(calculatorSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(calculatorSelectorLayout.createSequentialGroup()
                                .addComponent(calculatorOpMulti, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(calculatorOpDivide, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(calculatorSelectorLayout.createSequentialGroup()
                                .addComponent(calculatorOpPlus, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(calculatorOpMinus, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, calculatorSelectorLayout.createSequentialGroup()
                                .addComponent(calculatorOpMod, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(calculatorOpPercent, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(15, 15, 15))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, calculatorSelectorLayout.createSequentialGroup()
                        .addComponent(calculatorSelectClose)
                        .addGap(92, 92, 92))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, calculatorSelectorLayout.createSequentialGroup()
                        .addComponent(calculatorSelectText)
                        .addGap(68, 68, 68))))
        );
        calculatorSelectorLayout.setVerticalGroup(
            calculatorSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(calculatorSelectorLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(calculatorSelectText)
                .addGap(28, 28, 28)
                .addGroup(calculatorSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(calculatorOpMinus)
                    .addComponent(calculatorOpPlus))
                .addGap(18, 18, 18)
                .addGroup(calculatorSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(calculatorOpDivide)
                    .addComponent(calculatorOpMulti))
                .addGap(18, 18, 18)
                .addGroup(calculatorSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(calculatorOpPercent)
                    .addComponent(calculatorOpMod))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(calculatorSelectClose)
                .addGap(24, 24, 24))
        );

        getContentPane().add(calculatorSelector, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 450, 270, 270));

        Calculator.setBackground(new java.awt.Color(102, 102, 102));

        calcTitlebar.setBackground(new java.awt.Color(51, 51, 51));
        calcTitlebar.setForeground(new java.awt.Color(51, 51, 51));

        CalcTitle.setBackground(new java.awt.Color(255, 255, 255));
        CalcTitle.setForeground(new java.awt.Color(255, 255, 255));
        CalcTitle.setText("Calculator");

        CalcClose.setBackground(new java.awt.Color(51, 0, 0));
        CalcClose.setForeground(new java.awt.Color(255, 255, 255));
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

        calcField.setBackground(new java.awt.Color(51, 51, 51));
        calcField.setForeground(new java.awt.Color(255, 255, 255));
        calcField.setText("Answer");

        calcExpression1.setBackground(new java.awt.Color(51, 51, 51));
        calcExpression1.setForeground(new java.awt.Color(255, 255, 255));

        calcEquals.setBackground(new java.awt.Color(0, 0, 0));
        calcEquals.setForeground(new java.awt.Color(255, 255, 255));
        calcEquals.setText("=");
        calcEquals.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calcEqualsActionPerformed(evt);
            }
        });

        calcClear.setBackground(new java.awt.Color(0, 0, 0));
        calcClear.setForeground(new java.awt.Color(255, 255, 255));
        calcClear.setText("Clear");
        calcClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calcClearActionPerformed(evt);
            }
        });

        calcNum1.setForeground(new java.awt.Color(255, 255, 255));
        calcNum1.setText("Number 1");

        calcNum2.setForeground(new java.awt.Color(255, 255, 255));
        calcNum2.setText("Number 2");

        calcExpression2.setBackground(new java.awt.Color(51, 51, 51));
        calcExpression2.setForeground(new java.awt.Color(255, 255, 255));

        calcOperationText.setForeground(new java.awt.Color(255, 255, 255));
        calcOperationText.setText("Operation: ");

        calcOperation.setBackground(new java.awt.Color(0, 0, 0));
        calcOperation.setForeground(new java.awt.Color(255, 255, 255));
        calcOperation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calcOperationActionPerformed(evt);
            }
        });

        calcCalculation.setBackground(new java.awt.Color(51, 51, 51));
        calcCalculation.setForeground(new java.awt.Color(255, 255, 255));
        calcCalculation.setText("Calculation");

        javax.swing.GroupLayout CalculatorLayout = new javax.swing.GroupLayout(Calculator);
        Calculator.setLayout(CalculatorLayout);
        CalculatorLayout.setHorizontalGroup(
            CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(calcTitlebar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(CalculatorLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(calcNum2)
                    .addGroup(CalculatorLayout.createSequentialGroup()
                        .addComponent(calcClear, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(calcEquals, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(calcExpression1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, CalculatorLayout.createSequentialGroup()
                            .addComponent(calcOperationText)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(calcOperation, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(calcExpression2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(calcCalculation, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(calcField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
                    .addComponent(calcNum1))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        CalculatorLayout.setVerticalGroup(
            CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CalculatorLayout.createSequentialGroup()
                .addComponent(calcTitlebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(calcField, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(calcCalculation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(calcNum1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(calcExpression1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(calcNum2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(calcExpression2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CalculatorLayout.createSequentialGroup()
                        .addComponent(calcOperation, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CalculatorLayout.createSequentialGroup()
                        .addComponent(calcOperationText)
                        .addGap(27, 27, 27)))
                .addGroup(CalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(calcEquals, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    // GENERAL WINDOW ACTIONS
    
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
        calculatorSelector.setVisible(true);
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
        notepadSelector.setVisible(false);
    }//GEN-LAST:event_NotepadCloseActionPerformed

    private void PlayerCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlayerCloseActionPerformed
        // TODO add your handling code here:
        Player.setVisible(false);
        playerSelector.setVisible(false);
        if (clip != null && clip.isRunning()) { // Stop audio if its playing when closed
            clip.stop();
            clip.close();
        }
    }//GEN-LAST:event_PlayerCloseActionPerformed
    
    private void musicBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_musicBtnActionPerformed
        // TODO add your handling code here:
        String albumCheck = playerRelease.getText();
        
        if (albumCheck.equals("No Release")) { // if no album is loaded (inital startup), laod the selector
            playerSelector.setVisible(true);
            Player.setVisible(false);
        } else {
            playerSelector.setVisible(false);
            Player.setVisible(true);
        }
    }//GEN-LAST:event_musicBtnActionPerformed

    // MUSIC PLAYER ACTIONS
    
    private void album1SelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_album1SelectActionPerformed
        // TODO add your handling code here:
        playerSelector.setVisible(false);
        Player.setVisible(true);
        
        if (clip != null && clip.isRunning()) { // stop if somethings already playing
            clip.stop();
            clip.close();
        }
        
        playerTitle.setText("Choose a Song"); // sets initial values
        playerArtist.setText("22156's Beats");
        playerRelease.setText("Random Music 1");
        currentAlbum = "Random Music 1";
        
        player1Song.setText("Autumn");
        player2Song.setText("Cafe");
        player3Song.setText("Jumpy");
        player4Song.setText("Lights");
        player5Song.setText("Waves");
        
        try { // thanks stack overflow
            playerArtwork.setIcon( new ImageIcon(ImageIO.read( new File("src/GUI/Images/random-music-1-large.png"))));
        } catch (IOException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_album1SelectActionPerformed

    private void album2SelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_album2SelectActionPerformed
        // TODO add your handling code here:
        playerSelector.setVisible(false);
        Player.setVisible(true);
        
        if (clip != null && clip.isRunning()) { // stop if somethings already playing
            clip.stop();
            clip.close();
        }
        
        playerTitle.setText("Choose a Song"); // sets initial values
        playerArtist.setText("22156's Beats");
        playerRelease.setText("Random Music 2");
        currentAlbum = "Random Music 2";
        
        player1Song.setText("Leap");
        player2Song.setText("Refrain");
        player3Song.setText("Sleep");
        player4Song.setText("Slip");
        player5Song.setText("Slow Fall");
        
        try { // gets image + overrides current one
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
        Player.setVisible(false);
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
            currentTrackIndex--; // plays previous
            playCurrentTrack();
        }
    }//GEN-LAST:event_playerPrevActionPerformed

    private void playerNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playerNextActionPerformed
        // TODO add your handling code here:
        String[] selectedAlbum = currentAlbum.equals("Random Music 1") ? album1 : album2;
        
        if (currentTrackIndex < selectedAlbum.length - 1) {
            currentTrackIndex++; // plays next
            playCurrentTrack();
        }
    }//GEN-LAST:event_playerNextActionPerformed

    // NOTEPAD BUTTON ACTIONS
    
    private void notepadSelectOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notepadSelectOpenActionPerformed
        // TODO add your handling code here:
        Notepad.setVisible(true);
        notepadSelector.setVisible(false);
        openSelectedNote();
    }//GEN-LAST:event_notepadSelectOpenActionPerformed

    private void notepadSelectDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notepadSelectDeleteActionPerformed
        // TODO add your handling code here:
        deleteSelectedNote();
    }//GEN-LAST:event_notepadSelectDeleteActionPerformed

    private void notepadSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notepadSaveActionPerformed
        // TODO add your handling code here:
        saveNote();
    }//GEN-LAST:event_notepadSaveActionPerformed

    private void notepadClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notepadClearActionPerformed
        // TODO add your handling code here:
        notepadArea.setText("");
    }//GEN-LAST:event_notepadClearActionPerformed

    private void notepadOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notepadOpenActionPerformed
        // TODO add your handling code here:
        loadNotesIntoTable();
        Notepad.setVisible(false);
        notepadSelector.setVisible(true);
    }//GEN-LAST:event_notepadOpenActionPerformed

    private void notepadSelectReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notepadSelectReturnActionPerformed
        // TODO add your handling code here:
        Notepad.setVisible(true);
        notepadSelector.setVisible(false);
    }//GEN-LAST:event_notepadSelectReturnActionPerformed

    // OTHER CLOSE BUTTONS
    
    private void albumSelectCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_albumSelectCloseActionPerformed
        // TODO add your handling code here:
        Player.setVisible(false);
        playerSelector.setVisible(false);
        if (clip != null && clip.isRunning()) { // Stop audio if its playing when closed
            clip.stop();
            clip.close();
        }
    }//GEN-LAST:event_albumSelectCloseActionPerformed

    private void notepadSelectCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notepadSelectCloseActionPerformed
        // TODO add your handling code here:
        Notepad.setVisible(false);
        notepadSelector.setVisible(false);
    }//GEN-LAST:event_notepadSelectCloseActionPerformed

    private void calculatorSelectCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculatorSelectCloseActionPerformed
        // TODO add your handling code here:
        Calculator.setVisible(false);
        calculatorSelector.setVisible(false);
    }//GEN-LAST:event_calculatorSelectCloseActionPerformed

    private void calculatorOpPlusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculatorOpPlusActionPerformed
        // TODO add your handling code here:
        Calculator.setVisible(true);
        calculatorSelector.setVisible(false);
        calcOperation.setText("Plus");
    }//GEN-LAST:event_calculatorOpPlusActionPerformed

    private void calculatorOpMinusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculatorOpMinusActionPerformed
        // TODO add your handling code here:
        Calculator.setVisible(true);
        calculatorSelector.setVisible(false);
        calcOperation.setText("Minus");
    }//GEN-LAST:event_calculatorOpMinusActionPerformed

    private void calculatorOpMultiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculatorOpMultiActionPerformed
        // TODO add your handling code here:
        Calculator.setVisible(true);
        calculatorSelector.setVisible(false);
        calcOperation.setText("Multiply");
    }//GEN-LAST:event_calculatorOpMultiActionPerformed

    private void calculatorOpDivideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculatorOpDivideActionPerformed
        // TODO add your handling code here:
        Calculator.setVisible(true);
        calculatorSelector.setVisible(false);
        calcOperation.setText("Divide");
    }//GEN-LAST:event_calculatorOpDivideActionPerformed

    private void calculatorOpModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculatorOpModActionPerformed
        // TODO add your handling code here:
        Calculator.setVisible(true);
        calculatorSelector.setVisible(false);
        calcOperation.setText("Modulus");
    }//GEN-LAST:event_calculatorOpModActionPerformed

    private void calculatorOpPercentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculatorOpPercentActionPerformed
        // TODO add your handling code here:
        Calculator.setVisible(true);
        calculatorSelector.setVisible(false);
        calcOperation.setText("Percent Of");
    }//GEN-LAST:event_calculatorOpPercentActionPerformed

    private void calcOperationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calcOperationActionPerformed
        // TODO add your handling code here:
        Calculator.setVisible(false);
        calculatorSelector.setVisible(true);
        calcField.setText("");
        calcCalculation.setText("");
    }//GEN-LAST:event_calcOperationActionPerformed

    private void calcClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calcClearActionPerformed
        // TODO add your handling code here:
        calcExpression1.setText("");
        calcExpression2.setText("");
        calcField.setText("");
        calcCalculation.setText("");
    }//GEN-LAST:event_calcClearActionPerformed

    private void calcEqualsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calcEqualsActionPerformed
        // TODO add your handling code here:
        calcCalculate();
    }//GEN-LAST:event_calcEqualsActionPerformed

    // CALCULATOR
    
    
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
    private javax.swing.JLabel album1Image;
    private javax.swing.JButton album1Select;
    private javax.swing.JLabel album2Image;
    private javax.swing.JButton album2Select;
    private javax.swing.JButton albumSelectClose;
    private javax.swing.JTextField calcCalculation;
    private javax.swing.JButton calcClear;
    private javax.swing.JButton calcEquals;
    private javax.swing.JTextField calcExpression1;
    private javax.swing.JTextField calcExpression2;
    private javax.swing.JTextField calcField;
    private javax.swing.JLabel calcNum1;
    private javax.swing.JLabel calcNum2;
    private javax.swing.JButton calcOperation;
    private javax.swing.JLabel calcOperationText;
    private javax.swing.JPanel calcTitlebar;
    private javax.swing.JButton calculatorBtn;
    private javax.swing.JButton calculatorOpDivide;
    private javax.swing.JButton calculatorOpMinus;
    private javax.swing.JButton calculatorOpMod;
    private javax.swing.JButton calculatorOpMulti;
    private javax.swing.JButton calculatorOpPercent;
    private javax.swing.JButton calculatorOpPlus;
    private javax.swing.JButton calculatorSelectClose;
    private javax.swing.JLabel calculatorSelectText;
    private javax.swing.JPanel calculatorSelector;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton musicBtn;
    private javax.swing.JTextArea notepadArea;
    private javax.swing.JButton notepadBtn;
    private javax.swing.JButton notepadClear;
    private javax.swing.JButton notepadOpen;
    private javax.swing.JButton notepadSave;
    private javax.swing.JButton notepadSelectClose;
    private javax.swing.JButton notepadSelectDelete;
    private javax.swing.JButton notepadSelectOpen;
    private javax.swing.JButton notepadSelectReturn;
    private javax.swing.JTable notepadSelectTable;
    private javax.swing.JLabel notepadSelectTitle;
    private javax.swing.JPanel notepadSelector;
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
    private javax.swing.JLabel playerSelectTitle;
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
