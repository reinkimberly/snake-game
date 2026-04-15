##Prompt 1 - Project setup
I'm starting a Java game project in VS Code using Swing. Create a single file called SnakeGame.java with a main method that opens a JFrame window (600x600 pixels) titled "Snake". Add a JPanel subclass called GamePanel inside the frame. No game logic yet — just get the window to open.
Produced: the file along with the game window and panek of where the game willl take place

##Prompt 2 - Grid and snake
Add a 20x20 grid to GamePanel. Represent the snake as a sequence of grid cells and start it with 3 segments near the center, facing right. Each cell should be drawn as a 30x30 pixel square. Draw the snake in green and the background in dark gray.\
Produced: This creates a grid based game as well as the structure of the snake and the initial snake postition. It also is making the background grey and the snake green.

##Prompt 3 - Game loop, movement, and keyboard input 
Make the snake move automatically using a Swing timer that ticks every 150 milliseconds — the snake should advance one cell per tick in its current direction. Add arrow key controls so the player can steer, but don't allow the snake to reverse direction. For now, have the snake wrap around the edges instead of dying. Make sure the panel can receive keyboard input.
Produced: Automatic movement from the snake, direction based movement using the keyboard contrtols, and also having a temporary rule that makes the snake wrap around when it goes off the map. 

##Prompt 4 - Food, collision, score, and restart 
Add a food pellet that spawns at a random empty cell. When the snake eats it, grow by one segment and spawn new food. Add collision detection: hitting a wall or the snake's own body should end the game, stop movement, and show a "Game Over" message with the final score in the center of the screen. Display the current score in the top-left corner during play. When the game is over, let the player press R to reset everything and play again.

##Prompt 5 - High Score
Can you add a high score feature that persists accross games and is recorded in the top left coner.
Produced: The food system and eating/growing of the snake, keeping the score, collision detection(into itself or the walls), and the Game Over state. 

For the original prompts, I didn't have to change anything.

##Prompt 6 - Color Changing
Part A: Can you edit this code to make it so that if the player gets a high enough score (over 20) they can choose their snakes color. They can choose between green, pink, blue, and yellow.
Part B: I want you to be able to choose the color of the snake, once you reach a high score of 20 points. But only give them the option after they have died. Also give them a message of congrats.
Part C: Thats great! the words are going off the screen. Can you fix that!
Part D: After you get a high score of 20 can you give them the option to change their color after every game.
Produced: A feature of the game, where, if you reach 20 points, you get the opportunity to change the snake's color. 
Problems: Right away I got the option to change the snake's color when I reached 20 (in the middle of the game). I needed to make it so the player got the option to change the color after the game was over. Then I needed the player to be able to see all of the words since they were extending off of the screen. Finally, I realized this would only last for that one round, and I wanted the player to be able to enjoy the feature any time after getting a high score of 20.

##Prompt 7 - Can you create a start screen that is the first thing a player sees when beginning the game. The game does not, therefore start when the player first opens the game. The screen should have a cool visual of the snake, and tell the player to begin by pressing the space bar. Can you also make sure all of the text can be seen on the game window and that the game begins when the player presses the space button.
Produced: A screen that has a visual of the snake eating the food + a short description of the game. 
Problem: Can you create a start screen that is the first thing a player sees when beginning the game. The game does not, therefore start when the player first opens the game. The screen should have a cool visual of the snake, and tell the player to begin by pressing the space bar.
Problem: At first nothing changed because I got an error message, The method drawStartScreen(Graphics) is undefined for the type SnakeGame.GamePanel, in which I asked AI to fix and it did. Afterwards it ran correctly. 

##Prompt 8 - Can you make the speed increase slightly as you go on throughout the game.
Produced: I first got an error message, but eventually it produced an updated version of the game that gets more difficult as you go on, because the snake starts to move faster. 
Probelm: There were four errors: Syntax error on token "javac", java connot be resolved to a type, Syntax error on otken "SnakeGame", and The final feild timer may already have been assigned. Once I told the AI this, it fixed the problems, and the game ran correctly. 

##Prompt 9 - Can you add sound using javax.sound.sampled
Produced: A beep sound everytime the snake eats its food!
Problem: There was originally a problem but the AI recognized it before I had to tell it what was wrong.