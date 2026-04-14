import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.List;
import java.util.Random;

public class SnakeGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static class GamePanel extends JPanel {
        private static final int CELL_SIZE = 30;
        private static final int GRID_COUNT = 20;
        private static final Color BACKGROUND_COLOR = new Color(32, 32, 32);
        private static final Color GRID_COLOR = new Color(24, 24, 24);
        private Color snakeColor = new Color(0, 192, 0);
        private static final Color FOOD_COLOR = new Color(224, 64, 64);
        private static final Color TEXT_COLOR = Color.WHITE;
        private final List<int[]> snakeSegments = new ArrayList<>();
        private Direction direction = Direction.RIGHT;
        private Direction nextDirection = Direction.RIGHT;
        private final Timer timer;
        private int moveDelay = 150;
        private final Random random = new Random();
        private int foodX;
        private int foodY;
        private int score = 0;
        private static int highScore = 0;
        private boolean gameOver = false;
        private static boolean hasEverReachedHighScore = false;
        private boolean inStartScreen = true;

        private enum Direction {
            UP,
            DOWN,
            LEFT,
            RIGHT
        }

        public GamePanel() {
            setPreferredSize(new Dimension(GRID_COUNT * CELL_SIZE, GRID_COUNT * CELL_SIZE));
            setBackground(BACKGROUND_COLOR);
            setFocusable(true);

            timer = new Timer(moveDelay, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!gameOver) {
                        updateSnake();
                    }
                    repaint();
                }
            });

            resetGame();

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (inStartScreen) {
                        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                            inStartScreen = false;
                            timer.start();
                            playTone(880, 120, 0.8);
                            repaint();
                        }
                        return;
                    }

                    if (gameOver) {
                        if (hasEverReachedHighScore) {
                            switch (e.getKeyCode()) {
                                case KeyEvent.VK_G:
                                    snakeColor = new Color(0, 192, 0);
                                    repaint();
                                    return;
                                case KeyEvent.VK_P:
                                    snakeColor = new Color(255, 0, 192);
                                    repaint();
                                    return;
                                case KeyEvent.VK_B:
                                    snakeColor = new Color(0, 160, 255);
                                    repaint();
                                    return;
                                case KeyEvent.VK_Y:
                                    snakeColor = new Color(255, 216, 0);
                                    repaint();
                                    return;
                                default:
                                    break;
                            }
                        }
                        if (e.getKeyCode() == KeyEvent.VK_R) {
                            resetGame();
                            repaint();
                            return;
                        }
                        return;
                    }

                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            if (direction != Direction.DOWN) {
                                nextDirection = Direction.UP;
                            }
                            break;
                        case KeyEvent.VK_DOWN:
                            if (direction != Direction.UP) {
                                nextDirection = Direction.DOWN;
                            }
                            break;
                        case KeyEvent.VK_LEFT:
                            if (direction != Direction.RIGHT) {
                                nextDirection = Direction.LEFT;
                            }
                            break;
                        case KeyEvent.VK_RIGHT:
                            if (direction != Direction.LEFT) {
                                nextDirection = Direction.RIGHT;
                            }
                            break;
                        default:
                            break;
                    }
                }
            });

            // timer.start(); // Start on space press
        }

        @Override
        public void addNotify() {
            super.addNotify();
            requestFocusInWindow();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (inStartScreen) {
                drawStartScreen(g);
                return;
            }
            drawBackground(g);
            drawGrid(g);
            drawFood(g);
            drawSnake(g);
            drawScore(g);
            if (gameOver) {
                drawGameOver(g);
            }
        }

        private void updateSnake() {
            direction = nextDirection;
            int[] head = snakeSegments.get(snakeSegments.size() - 1);
            int newX = head[0];
            int newY = head[1];

            switch (direction) {
                case UP:
                    newY--;
                    break;
                case DOWN:
                    newY++;
                    break;
                case LEFT:
                    newX--;
                    break;
                case RIGHT:
                    newX++;
                    break;
            }

            if (newX < 0 || newX >= GRID_COUNT || newY < 0 || newY >= GRID_COUNT) {
                gameOver = true;
                playTone(220, 300, 0.7);
                return;
            }

            if (isBodyCollision(newX, newY)) {
                gameOver = true;
                playTone(220, 300, 0.7);
                return;
            }

            snakeSegments.add(new int[] {newX, newY});
            if (newX == foodX && newY == foodY) {
                score++;
                if (score > highScore) {
                    highScore = score;
                }
                if (score > 20 && !hasEverReachedHighScore) {
                    hasEverReachedHighScore = true;
                }
                playTone(1000, 90, 0.7);
                adjustSpeed();
                spawnFood();
            } else {
                snakeSegments.remove(0);
            }
        }

        private boolean isBodyCollision(int x, int y) {
            for (int[] segment : snakeSegments) {
                if (segment[0] == x && segment[1] == y) {
                    return true;
                }
            }
            return false;
        }

        private void resetGame() {
            snakeSegments.clear();
            direction = Direction.RIGHT;
            nextDirection = Direction.RIGHT;
            score = 0;
            moveDelay = 150;
            timer.setDelay(moveDelay);
            timer.setInitialDelay(moveDelay);
            gameOver = false;

            snakeSegments.add(new int[] {8, 10});
            snakeSegments.add(new int[] {9, 10});
            snakeSegments.add(new int[] {10, 10});
            spawnFood();
        }

        private void spawnFood() {
            do {
                foodX = random.nextInt(GRID_COUNT);
                foodY = random.nextInt(GRID_COUNT);
            } while (isBodyCollision(foodX, foodY));
        }

        private void drawBackground(Graphics g) {
            g.setColor(BACKGROUND_COLOR);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        private void drawGrid(Graphics g) {
            g.setColor(GRID_COLOR);
            for (int i = 0; i <= GRID_COUNT; i++) {
                int x = i * CELL_SIZE;
                int y = i * CELL_SIZE;
                g.drawLine(x, 0, x, GRID_COUNT * CELL_SIZE);
                g.drawLine(0, y, GRID_COUNT * CELL_SIZE, y);
            }
        }

        private void drawFood(Graphics g) {
            g.setColor(FOOD_COLOR);
            int x = foodX * CELL_SIZE;
            int y = foodY * CELL_SIZE;
            g.fillOval(x + 6, y + 6, CELL_SIZE - 12, CELL_SIZE - 12);
        }

        private void drawSnake(Graphics g) {
            g.setColor(snakeColor);
            for (int[] segment : snakeSegments) {
                int x = segment[0] * CELL_SIZE;
                int y = segment[1] * CELL_SIZE;
                g.fillRect(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2);
            }
        }

        private void drawScore(Graphics g) {
            g.setColor(TEXT_COLOR);
            g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
            g.drawString("Score: " + score, 10, 22);
            g.drawString("High: " + highScore, 10, 42);
        }

        private void drawGameOver(Graphics g) {
            g.setColor(TEXT_COLOR);
            g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 36));
            g.drawString("Game Over", 160, 280);
            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
            g.drawString("Final Score: " + score, 200, 315);
            if (hasEverReachedHighScore) {
                g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
                g.drawString("Choose your snake color!", 200, 340);
                g.drawString("Press G/P/B/Y to choose, then R to restart", 100, 360);
            } else {
                g.drawString("Press R to restart", 200, 340);
            }
        }

        private void adjustSpeed() {
            moveDelay = Math.max(50, 150 - score * 3);
            timer.setDelay(moveDelay);
            timer.setInitialDelay(moveDelay);
        }

        private void drawStartScreen(Graphics g) {
            drawBackground(g);
            drawGrid(g);
            // Draw a cool snake visual in the center
            g.setColor(snakeColor);
            int centerX = GRID_COUNT / 2;
            int centerY = GRID_COUNT / 2;
            // Draw a longer snake
            for (int i = 0; i < 10; i++) {
                int x = (centerX - i) * CELL_SIZE;
                int y = centerY * CELL_SIZE;
                g.fillRect(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4);
            }
            // Draw food nearby
            g.setColor(FOOD_COLOR);
            int foodX = (centerX + 2) * CELL_SIZE;
            int foodY = centerY * CELL_SIZE;
            g.fillOval(foodX + 6, foodY + 6, CELL_SIZE - 12, CELL_SIZE - 12);

            // Draw title and instructions
            g.setColor(TEXT_COLOR);
            g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 48));
            g.drawString("SNAKE", 200, 150);
            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
            g.drawString("Press SPACE to start", 180, 200);
            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
            g.drawString("Use arrow keys to move, eat food to grow!", 120, 230);
            g.drawString("Speed increases as you score points.", 140, 255);
        }

        private static void playTone(double hz, int msecs, double volume) {
            new Thread(() -> {
                float sampleRate = 44100;
                byte[] buf = new byte[1];
                AudioFormat af = new AudioFormat(sampleRate, 8, 1, true, false);
                try (SourceDataLine sdl = AudioSystem.getSourceDataLine(af)) {
                    sdl.open(af);
                    sdl.start();
                    for (int i = 0; i < sampleRate * msecs / 1000; i++) {
                        double angle = i / (sampleRate / hz) * 2.0 * Math.PI;
                        buf[0] = (byte) (Math.sin(angle) * 127.0 * volume);
                        sdl.write(buf, 0, 1);
                    }
                    sdl.drain();
                } catch (LineUnavailableException ex) {
                    // ignore sound errors
                }
            }).start();
        }
    }
}
