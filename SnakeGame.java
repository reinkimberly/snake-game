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
        private static final Color SNAKE_COLOR = new Color(0, 192, 0);
        private static final Color FOOD_COLOR = new Color(224, 64, 64);
        private static final Color TEXT_COLOR = Color.WHITE;
        private final List<int[]> snakeSegments = new ArrayList<>();
        private Direction direction = Direction.RIGHT;
        private Direction nextDirection = Direction.RIGHT;
        private final Timer timer;
        private final Random random = new Random();
        private int foodX;
        private int foodY;
        private int score = 0;
        private boolean gameOver = false;

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

            resetGame();

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (gameOver && e.getKeyCode() == KeyEvent.VK_R) {
                        resetGame();
                        repaint();
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

            timer = new Timer(150, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!gameOver) {
                        updateSnake();
                    }
                    repaint();
                }
            });
            timer.start();
        }

        @Override
        public void addNotify() {
            super.addNotify();
            requestFocusInWindow();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
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
                return;
            }

            if (isBodyCollision(newX, newY)) {
                gameOver = true;
                return;
            }

            snakeSegments.add(new int[] {newX, newY});
            if (newX == foodX && newY == foodY) {
                score++;
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
            g.setColor(SNAKE_COLOR);
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
        }

        private void drawGameOver(Graphics g) {
            g.setColor(TEXT_COLOR);
            g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 36));
            g.drawString("Game Over", 160, 280);
            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
            g.drawString("Final Score: " + score, 200, 315);
            g.drawString("Press R to restart", 180, 345);
        }
    }
}
