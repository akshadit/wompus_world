import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

public class WumpusGame extends JFrame {

    // --- CONFIGURATION ---
    private static final int GRID_SIZE = 4;
    private static final int TILE_SIZE = 100;
    private static final int WINDOW_WIDTH = GRID_SIZE * TILE_SIZE;

    // --- GAME STATE ---
    private Cell[][] grid;
    private int playerRow, playerCol;
    private String playerDir = "DOWN";
    private boolean gameOver, victory, wumpusAlive;
    private boolean playerHasGold;
    private int arrows = 1;

    // --- ASSETS ---
    private Image imgPlayerUp, imgPlayerDown, imgPlayerLeft, imgPlayerRight;
    private Image imgWall, imgFloor, imgFloorGold, imgPit, imgWumpus, imgGold;
    private Image imgGameOver, imgControls, imgArrowIcon, imgScoreIcon;

    // --- AUDIO ---
    private Clip musicClip; // To control background music

    // --- UI COMPONENTS ---
    private GamePanel gamePanel;
    private JLabel statusLabel;

    public WumpusGame() {
        setTitle("Wumpus World: Audio Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        loadAssets();

        // UI Setup
        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(WINDOW_WIDTH, GRID_SIZE * TILE_SIZE));
        add(gamePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(30, 30, 30));
        bottomPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, 160));

        statusLabel = new JLabel("Mission: Get Gold AND Kill Wumpus", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        statusLabel.setForeground(Color.CYAN);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        bottomPanel.add(statusLabel, BorderLayout.NORTH);

        if (imgControls != null) {
            JLabel controls = new JLabel(new ImageIcon(imgControls.getScaledInstance(350, 90, Image.SCALE_SMOOTH)));
            controls.setHorizontalAlignment(SwingConstants.CENTER);
            bottomPanel.add(controls, BorderLayout.CENTER);
        }

        add(bottomPanel, BorderLayout.SOUTH);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleInput(e.getKeyCode());
            }
        });

        pack();
        setLocationRelativeTo(null);

        initGame();
        setVisible(true);
    }

    private void loadAssets() {
        try {
            imgPlayerUp = loadImage("assets/images/player_facing_to_up.png");
            imgPlayerDown = loadImage("assets/images/player_facing_to_down.png");
            imgPlayerLeft = loadImage("assets/images/player_facing_to_left.png");
            imgPlayerRight = loadImage("assets/images/player_facing_to_right.png");
            imgWall = loadImage("assets/images/wall.png");
            imgFloor = loadImage("assets/images/floor.png");
            imgFloorGold = loadImage("assets/images/floor_gold.png");
            imgPit = loadImage("assets/images/hole.png");
            imgWumpus = loadImage("assets/images/wumpus.png");
            imgGold = loadImage("assets/images/gold.png");
            imgGameOver = loadImage("assets/images/game_over.png");
            imgControls = loadImage("assets/images/controls.png");
            imgArrowIcon = loadImage("assets/images/arrow-icon.png");
            imgScoreIcon = loadImage("assets/images/score-icon.png");
        } catch (Exception e) {
        }
    }

    private Image loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            return null;
        }
    }

    private void initGame() {
        grid = new Cell[GRID_SIZE][GRID_SIZE];
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                grid[r][c] = new Cell();
            }
        }
        playerRow = GRID_SIZE - 1;
        playerCol = 0;
        playerDir = "DOWN";
        gameOver = false;
        victory = false;
        wumpusAlive = true;
        playerHasGold = false;
        arrows = 1;
        grid[playerRow][playerCol].visited = true;

        placeHazards();
        updateStatus("Sensors: Safe");

        // Start Background Music
        playMusic("assets/audio/background.wav");
    }

    private void placeHazards() {
        placeItem("Wumpus");
        placeItem("Gold");

        // Determine number of pits (2 or 3)
        int numPits = 2 + (int) (Math.random() * 2);
        int pitsPlaced = 0;

        while (pitsPlaced < numPits) {
            int r = (int) (Math.random() * GRID_SIZE);
            int c = (int) (Math.random() * GRID_SIZE);

            // Conditions: Not start, not Wumpus, not Gold, not already a pit
            if ((r == GRID_SIZE - 1 && c == 0))
                continue;
            // IMPORTANT: We still ensure Pits do not overlap with Gold or Wumpus
            if (grid[r][c].hasWumpus || grid[r][c].hasGold || grid[r][c].hasPit)
                continue;

            grid[r][c].hasPit = true;
            pitsPlaced++;
        }
    }

    private void placeItem(String type) {
        int r, c;
        do {
            r = (int) (Math.random() * GRID_SIZE);
            c = (int) (Math.random() * GRID_SIZE);
            // CHANGED: We now allow Wumpus and Gold to potentially overlap (Randomly)
            // We only ensure they don't spawn on the player start position.
        } while (r == GRID_SIZE - 1 && c == 0);

        if (type.equals("Wumpus"))
            grid[r][c].hasWumpus = true;
        if (type.equals("Gold"))
            grid[r][c].hasGold = true;
    }

    private void handleInput(int key) {
        if (key == KeyEvent.VK_R) {
            initGame();
            gamePanel.repaint();
            return;
        }
        if (gameOver || victory)
            return;

        boolean actionSoundPlayed = false;
        boolean attemptedMove = false;

        // Tactical Movement logic
        if (key == KeyEvent.VK_UP) {
            if (playerDir.equals("UP")) {
                actionSoundPlayed = movePlayer(-1, 0);
                attemptedMove = true;
            } else {
                playerDir = "UP";
                gamePanel.repaint();
            }
        } else if (key == KeyEvent.VK_DOWN) {
            if (playerDir.equals("DOWN")) {
                actionSoundPlayed = movePlayer(1, 0);
                attemptedMove = true;
            } else {
                playerDir = "DOWN";
                gamePanel.repaint();
            }
        } else if (key == KeyEvent.VK_LEFT) {
            if (playerDir.equals("LEFT")) {
                actionSoundPlayed = movePlayer(0, -1);
                attemptedMove = true;
            } else {
                playerDir = "LEFT";
                gamePanel.repaint();
            }
        } else if (key == KeyEvent.VK_RIGHT) {
            if (playerDir.equals("RIGHT")) {
                actionSoundPlayed = movePlayer(0, 1);
                attemptedMove = true;
            } else {
                playerDir = "RIGHT";
                gamePanel.repaint();
            }
        } else if (key == KeyEvent.VK_SPACE) {
            shootArrow();
            // Shoot arrow plays its own sound always, we don't need click overlap logic
            // here
            // because clicks are only for movement keys.
            return;
        }

        // Only play click if we ATTEMPTED to move and no other significant sound (bump,
        // coin, die, win) was played
        // Turning (attemptedMove == false) will stay silent
        if (attemptedMove && !actionSoundPlayed) {
            playSound("assets/audio/click.wav");
        }
    }

    private boolean movePlayer(int dr, int dc) {
        int nr = playerRow + dr;
        int nc = playerCol + dc;

        if (nr >= 0 && nr < GRID_SIZE && nc >= 0 && nc < GRID_SIZE) {
            // Valid Move
            playerRow = nr;
            playerCol = nc;
            grid[playerRow][playerCol].visited = true;

            // Check hazards and capture if any sound was played there
            boolean hazardSound = checkHazards();
            gamePanel.repaint();
            return hazardSound;
        } else {
            // Invalid Move (Hit Wall)
            playSound("assets/audio/bump.wav");
            return true; // We played a sound
        }
    }

    private void shootArrow() {
        if (arrows > 0) {
            arrows--;

            playSound("assets/audio/arrow.wav"); // Play shoot sound

            int targetR = playerRow;
            int targetC = playerCol;

            if (playerDir.equals("UP"))
                targetR--;
            if (playerDir.equals("DOWN"))
                targetR++;
            if (playerDir.equals("LEFT"))
                targetC--;
            if (playerDir.equals("RIGHT"))
                targetC++;

            boolean hit = false;
            if (targetR >= 0 && targetR < GRID_SIZE && targetC >= 0 && targetC < GRID_SIZE) {
                if (grid[targetR][targetC].hasWumpus && wumpusAlive) {
                    wumpusAlive = false;
                    hit = true;
                }
            }

            if (hit) {
                updateStatus("TARGET ELIMINATED! Now find the Gold!");

                if (playerHasGold) {
                    victory = true;
                    stopMusic();
                    playSound("assets/audio/win.wav");
                    updateStatus("MISSION COMPLETE! Wumpus dead & Gold secured!");
                }
            } else {
                updateStatus("Missed! Arrow flew " + playerDir);
            }
            gamePanel.repaint();
        }
    }

    private boolean checkHazards() {
        Cell c = grid[playerRow][playerCol];
        String status = "";
        boolean soundPlayed = false;

        // 1. Pick up Gold
        if (c.hasGold && !playerHasGold) {
            playerHasGold = true;
            playSound("assets/audio/coin.wav"); // Play Coin Sound
            soundPlayed = true;
        }

        // 2. Check for Instant Death
        if (c.hasPit) {
            gameOver = true;
            status = "GAME OVER! You fell in a pit.";
            stopMusic();
            playSound("assets/audio/game-over.wav");
            soundPlayed = true;
        } else if (c.hasWumpus && wumpusAlive) {
            gameOver = true;
            status = "GAME OVER! Wumpus ate you.";
            stopMusic();
            playSound("assets/audio/game-over.wav");
            soundPlayed = true;
        }

        // 3. Check for Victory
        else if (playerHasGold && !wumpusAlive) {
            victory = true;
            status = "MISSION COMPLETE! Gold found & Wumpus dead!";
            stopMusic();
            playSound("assets/audio/win.wav");
            soundPlayed = true;
        }

        // 4. Update Status Text
        else {
            status = "Sensors: ";
            boolean nearPit = checkNeighbor(playerRow, playerCol, "Pit");
            boolean nearWumpus = checkNeighbor(playerRow, playerCol, "Wumpus");

            if (nearPit)
                status += "Breeze ";
            if (nearWumpus && wumpusAlive)
                status += "Stench ";
            if (!nearPit && (!nearWumpus || !wumpusAlive))
                status += "Safe";

            if (playerHasGold)
                status += " [Gold Secured]";
            if (!wumpusAlive)
                status += " [Wumpus Neutralized]";
        }

        updateStatus(status);
        return soundPlayed;
    }

    private boolean checkNeighbor(int r, int c, String type) {
        int[] dr = { -1, 1, 0, 0 };
        int[] dc = { 0, 0, -1, 1 };
        for (int i = 0; i < 4; i++) {
            int nr = r + dr[i], nc = c + dc[i];
            if (nr >= 0 && nr < GRID_SIZE && nc >= 0 && nc < GRID_SIZE) {
                if (type.equals("Pit") && grid[nr][nc].hasPit)
                    return true;
                if (type.equals("Wumpus") && grid[nr][nc].hasWumpus)
                    return true;
            }
        }
        return false;
    }

    private void updateStatus(String msg) {
        if (statusLabel != null) {
            statusLabel.setText(msg);
            if (msg.contains("GAME OVER"))
                statusLabel.setForeground(Color.RED);
            else if (msg.contains("COMPLETE"))
                statusLabel.setForeground(Color.GREEN);
            else
                statusLabel.setForeground(Color.CYAN);
        }
    }

    // --- AUDIO SYSTEM ---

    // For sound effects (One-shot)
    public void playSound(String filepath) {
        try {
            File soundFile = new File(filepath);
            if (!soundFile.exists())
                return;
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            // System.out.println("Audio Error: " + e.getMessage());
        }
    }

    // For background music (Looping)
    public void playMusic(String filepath) {
        stopMusic(); // Stop any existing music first
        try {
            File soundFile = new File(filepath);
            if (!soundFile.exists()) {
                System.out.println("Music file not found: " + filepath);
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            musicClip = AudioSystem.getClip();
            musicClip.open(audioIn);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop forever
            musicClip.start();
        } catch (Exception e) {
            System.out.println("Music Error (Try converting mp3 to wav): " + e.getMessage());
        }
    }

    public void stopMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
            musicClip.close();
        }
    }

    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            for (int r = 0; r < GRID_SIZE; r++) {
                for (int c = 0; c < GRID_SIZE; c++) {
                    int x = c * TILE_SIZE;
                    int y = r * TILE_SIZE;
                    Cell cell = grid[r][c];

                    if (cell.visited || gameOver || victory) {
                        if (cell.hasGold) {
                            drawTile(g, imgFloorGold, new Color(255, 215, 0), x, y);
                            if (!playerHasGold)
                                drawTile(g, imgGold, null, x, y);
                        } else {
                            drawTile(g, imgFloor, Color.LIGHT_GRAY, x, y);
                        }

                        if (cell.hasPit)
                            drawTile(g, imgPit, Color.BLACK, x, y);

                        if (cell.hasWumpus) {
                            if (wumpusAlive) {
                                drawTile(g, imgWumpus, Color.RED, x, y);
                            } else {
                                drawTile(g, imgWumpus, Color.DARK_GRAY, x, y);
                                g.setColor(Color.RED);
                                g.setFont(new Font("Arial", Font.BOLD, 40));
                                g.drawString("X", x + 35, y + 65);
                            }
                        }
                    } else {
                        drawTile(g, imgWall, Color.DARK_GRAY, x, y);
                    }
                }
            }

            if (!gameOver || victory) {
                Image pImg = imgPlayerDown;
                if (playerDir.equals("UP"))
                    pImg = imgPlayerUp;
                if (playerDir.equals("LEFT"))
                    pImg = imgPlayerLeft;
                if (playerDir.equals("RIGHT"))
                    pImg = imgPlayerRight;

                drawTile(g, pImg, Color.BLUE, playerCol * TILE_SIZE, playerRow * TILE_SIZE);
            }

            if (gameOver && imgGameOver != null) {
                g.drawImage(imgGameOver, (getWidth() / 2) - 100, (getHeight() / 2) - 100, 200, 200, null);
            }

            if (arrows > 0) {
                if (imgArrowIcon != null)
                    g.drawImage(imgArrowIcon, 10, 10, 30, 30, null);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("x " + arrows, 45, 32);
            }

            if (playerHasGold) {
                if (imgScoreIcon != null)
                    g.drawImage(imgScoreIcon, 90, 10, 30, 30, null);
                else {
                    g.setColor(Color.YELLOW);
                    g.fillOval(90, 10, 30, 30);
                }
            }
        }

        private void drawTile(Graphics g, Image img, Color fallback, int x, int y) {
            if (img != null) {
                g.drawImage(img, x, y, TILE_SIZE, TILE_SIZE, null);
            } else if (fallback != null) {
                g.setColor(fallback);
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    private class Cell {
        boolean hasPit, hasWumpus, hasGold, visited;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WumpusGame::new);
    }
}