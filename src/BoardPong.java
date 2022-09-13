/************************************
 *
 * Name: Arsha Fazlollahi
 * Course: Grade 11 Computer Science
 * Teacher: Mr. Benum
 * Project: Final Culminating
 *
 * Sources:
 * https://classroom.google.com/c/MjY4NzUxNDE4MTAw
 * https://moodle2.yrdsb.ca/course/view.php?id=550
 * https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
 * https://stackoverflow.com/
 *
 ***********************************/

import java.lang.String;
import java.util.Scanner;
import java.util.Random;
import java.io.*;
import javax.swing.*;

//All of the above are used to import the required packages for this application to run.

public class BoardPong {

    static Board b;
    static int score = 0;  //Integer that stores the player's score.
    static String difficultyString;     //Stores the difficulty as a string. Used for displaying the difficulty under the board.
    static int paddle[] = {10, 11, 12, 2};    //An array of integers used to store the locations of the pegs making up the player's paddle. The first three integers are for row locations and last integer is for the column location.
    static int ball[] = {12, 4};  //An array of integers used to store the location of the ball. The first integer is for the ball's row location and the second integer is for it's column location.
    static int randomNumber = 1;   //This integer will later be used to store a random number. For now it's set to 1 for functions where the randomNumber is not generated yet.
    static int aiPaddle[] = {13, 14, 15, 38};    //An array of integers used to store the locations of the pegs making up the AI's paddle. The first three integers are for row locations and last integer is for the column location.
    static int direction = 0;  //This integer will later be used in multiple if statements to set the direction of the ball's animation.
    static int difficulty = 0;     //This will be used in an if statement to make it store the maximum gap between the ball and the AI within which the AI is able to respond to.
    static final int BOARD_ROWS = 20;
    static final int BOARD_COLUMNS = 41;

    /**
     *
     * The method below is the main method.
     * It contains the board, some important variables and objects, and the game's main loop.
     * Within this method there are calls made to other methods that are required to run the game.
     * This method contains most of the if statements inside this program.
     *
     */

    public static void main(String[] args) throws InterruptedException {

        JFrame frame;   //Introduces the variable for the frame used for JOptionPane's GUI.
        frame = new JFrame();   //Uses the frame variable to create a new frame method.
        Random r = new Random();    //Introduces the random number variable.

        b = new Board(BOARD_ROWS, BOARD_COLUMNS);    //Introduces and draws the board used for the game using the Board Class.

        Object[] possibilities = {"Normal", "Hard"};
        String s = (String)JOptionPane.showInputDialog(
                frame,
                "Please choose difficulty.\n",
                "Pong options",
                JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                "Normal");

        //The section above creates a JOptionPane prompt that asks the user to choose a difficulty and stores the user's choice as a string that is later used in an if statement.

        if ((s != null) && (s.equals("Normal"))) {

            difficulty = 12;
            difficultyString = "Normal";    //Sets the difficultyString variable to the specified string.

        } else if ((s != null) && (s.equals("Hard"))){

            difficulty = 15;
            difficultyString = "Hard";

        }

        resetVariables();   //Calls for the method that resets the game. This is essential to do before the game starts to avoid any problems caused by the code section above.

        //The section above checks the string from the JOptionPne code section and if it equals "Normal" it will set the difficulty variable to 12(Refer to the difficulty variable comments for what that number means) and if it equals "Hard" it will set it to 15.

        b.putPeg("white", paddle[0], paddle[3]);
        b.putPeg("white", paddle[1], paddle[3]);
        b.putPeg("white", paddle[2], paddle[3]);

        //The section above draws the player's paddle for the first time using three b.putPeg commands for the three pixels of the paddle.

        b.drawLine(0, 20, b.getRows() - 1, b.getColumns()/2);     //Draws a line in the middle of the board representing the net.

        while (true) {    //This is the main "While" loop within which all the game events run. This is an infinite loop.

            drawPaddle();  //Calls for the method that draws the player's paddle.

            if (ball[1] == 3 && ball[0] == paddle[0] || ball[1] == 3 && ball[0] == paddle[1] || ball[1] == 3 && ball[0] == paddle[2]) {

                randomNumber = r.nextInt(18 - 1 + 1) + 1;

            }

            /*The above if statement is a debug for another debug. While the if-statement below runs the codes inside it
            continuously to avoid the functions updating and going back to their initial state, this if statement runs the section of the other if
            statement that is supposed to only run once. Basically the if statement above and the if statement below use the same conditions, however the
            if statement below is inside a temporary loop that continues to run until the other specified condition is met while the if statement above
            only runs once to avoid the randomNumber being generated too many times and avoid creating a completely random pattern for the methods
            inside the if statement.
             */

            if (ball[1] == 3 && ball[0] == paddle[0] || ball[1] == 3 && ball[0] == paddle[1] || ball[1] == 3 && ball[0] == paddle[2] || direction == 1) {

                if (ball[1] == b.getColumns() - 4 && ball[0] == aiPaddle[0] || ball[1] == b.getColumns() - 4 && ball[0] == aiPaddle[1] || ball[1] == b.getColumns() - 4 && ball[0] == aiPaddle[2]) {

                    direction = 2;

                } else {

                    direction = 1;

                }

            ballMoveRight();     //Calls the method responsible for the ball's animation when it's moving right.
            physics();     //Calls the method responsible for calculating the ball's up and down movement.

            /*
            The if statement above states that if:

            - The ball coordinates collide with the player's paddle's coordinates.(ball[n] == paddle[n])
            OR
            - The condition has been met once before((direction == 1) in the condition and direction = 1 in the execution).

            The methods "ballMoveRight(paddle, ball, b);" and "physics(randomNumber, paddle, ball, b);" will be called creating the illusion that
            the ball has bounced off the player's paddle.
            Now since this statement also runs if it has been true before, it will ignore the "else if" for when the ball hits the AI's paddle
            and to fix that I made another if statement within this if statement that checks the else if condition a second time in case the
            ball ever hits the AI's paddle, and if it does it will change the direction variable to 2 so that the loop within which this if
            statement is continuously running ends and starts the loop responsible for responding to the ball hitting the AI's paddle.
             */

            //The explanation for the section below is the same with the explanation on line 109 with different conditions.

                if (ball[1] == b.getColumns() - 4 && ball[0] == aiPaddle[0] || ball[1] == b.getColumns() - 4 && ball[0] == aiPaddle[1] || ball[1] == b.getColumns() - 4 && ball[0] == aiPaddle[2]){

                    randomNumber = r.nextInt(18 - 1 + 1) + 1;

                }

            /*The explanation for the section below is the same with the explanation on line 132 with different conditions.
            An important note about this section is that since this is an else if statement and is always checked second, it doesn't need a
            "loop-fixer" like the one I used for the other looping if statement.
             */

            } else if (ball[1] == b.getColumns() - 4 && ball[0] == aiPaddle[0] || ball[1] == b.getColumns() - 4 && ball[0] == aiPaddle[1] || ball[1] == b.getColumns() - 4 && ball[0] == aiPaddle[2] || direction == 2){

                ballMoveLeft();  //Calls the method responsible for the ball's animation when it's moving left.
                physics();
                direction = 2;

            } else{

                ballMoveLeft();
                physics();

            }

            paddleControl();     //Calls for the method responsible for the player's paddle control.
            opponent();  //Calls for the method responsible for the AI.

            if (ball[1] <= 1){  //If the ball is equal or less than column 1 the game will be reset and the player will lose 1 point. Column 1 is the column right behind the player's paddle.

                resetVariables();   //Calls the method that resets all variables and positions.
                score--;    //Reduces 1 point from the player's score.

                b.displayMessage("Difficulty: " + difficultyString + "     Score: " + score);   //Updates the score shown under the board.

                writeToFile();

            } else if (ball[1] >= b.getColumns() - 1){  //If the ball is equal or more than the board's last column - 1, the game will be reset and the player will gain 1 point. The board's last column - 1 is the column right behind the AI's paddle.

                resetVariables();
                score++;    //Adds 1 point to the player's score.

                b.displayMessage("Difficulty: " + difficultyString + "     Score: " + score);

                writeToFile();

            }

        }

    }

    /**
     *
     * This is the paddleControl method.
     * It is responsible for making the game respond to the player's key presses and the player paddle's animation.
     * It uses two main if statements to let the player control their paddle.
     *
     */

    public static void paddleControl() throws InterruptedException {

            if (b.getKey() == 'w' && paddle[0] != 0) {  //This is an if statement that makes the player's paddle go up while the player is holding the 'w' key.

                b.removePeg(paddle[0], paddle[3]);
                b.removePeg(paddle[1], paddle[3]);
                b.removePeg(paddle[2], paddle[3]);
                paddle[0]--;
                paddle[1]--;
                paddle[2]--;

                //The above section removes the last positions of the player's paddle and decreases the values of the position so that the next time the player's paddle is drawn in the loop it's drawn one pixel higher (Higher because the lower the number, the closer the point is to the board's top).

            }
            if (b.getKey() == 's' && paddle[2] != b.getRows() - 1) {    //This is an if statement that makes the player's paddle go down while the player is holding the 's' key.

                b.removePeg(paddle[0], paddle[3]);
                b.removePeg(paddle[1], paddle[3]);
                b.removePeg(paddle[2], paddle[3]);
                paddle[0]++;
                paddle[1]++;
                paddle[2]++;

                //The above section removes the last positions of the player's paddle and increases the values of the position so that the next time the player's paddle is drawn in the loop it's drawn one pixel lower.

            }

    }

    /**
     *
     * This is the ballMoveLeft method.
     * It runs the animation for the ball when it's moving left.
     * It creates this animation by placing the ball's peg, putting the process to sleep for 17 milliseconds, removing the placed peg and reducing the value of the peg's column by 1 for the next loop.
     *
     */

    public static void ballMoveLeft() throws InterruptedException {

        b.putPeg("yellow", ball[0], ball[1]);
        Thread.sleep(17);   //This is the first main delay used for animation inside this game's loop. These delays create the illusion of a peg moving and not suddenly teleporting to a specific place.
        b.removePeg(ball[0], ball[1]);
        ball[1]--;

    }

    /**
     *
     * This is the ballMoveRight method.
     * It does the same thing as the ballMoveLeft method except it makes the ball move right by adding to it's column value.
     *
     */

    public static void ballMoveRight() throws InterruptedException {

        b.putPeg("yellow", ball[0], ball[1]);
        Thread.sleep(17);
        b.removePeg(ball[0], ball[1]);
        ball[1]++;

    }

    /**
     *
     * This is the physics method.
     * It is responsible for the moving the ball up and down as the other methods are moving it left and right.
     * It uses the random number generator to generate a random number and make the ball move up or down to that random number until it reaches it.
     * If the ball's row location is above the random number's row location, the ball will move down and if it's below the random number's row location, the ball will move up.
     *
     */

    public static void physics() {

        if (ball[0] > randomNumber && randomNumber <= b.getRows()/2 - 1){

            ball[0]--;

        } else if (ball[0] < randomNumber && randomNumber > b.getRows()/2 - 1){

            ball[0]++;

        }

    }

    /**
     *
     * This is the opponent method.
     * It is responsible for AI behaviour.
     * It uses multiple if statements to determine how the AI should move or whether or not it's supposed to move at all.
     *
     */

    public static void opponent () throws InterruptedException {

        b.putPeg("white", aiPaddle[0], aiPaddle[3]);
        b.putPeg("white", aiPaddle[1], aiPaddle[3]);
        b.putPeg("white", aiPaddle[2], aiPaddle[3]);
        Thread.sleep(15);
        b.removePeg(aiPaddle[0], aiPaddle[3]);
        b.removePeg(aiPaddle[1], aiPaddle[3]);
        b.removePeg(aiPaddle[2], aiPaddle[3]);

        //The section above draws the AI's paddle and then removes it after a 15 millisecond delay. The if statements below will determine which direction this animation takes.

        if (aiPaddle[1] > randomNumber && randomNumber <= b.getRows()/2 - 1 && aiPaddle[1] - randomNumber >= difficulty){

            b.putPeg("white", aiPaddle[0], aiPaddle[3]);
            b.putPeg("white", aiPaddle[1], aiPaddle[3]);
            b.putPeg("white", aiPaddle[2], aiPaddle[3]);

        } else if (aiPaddle[1] < randomNumber && randomNumber > b.getRows()/2 - 1 && randomNumber - aiPaddle[1] >= difficulty){

            b.putPeg("white", aiPaddle[0], aiPaddle[3]);
            b.putPeg("white", aiPaddle[1], aiPaddle[3]);
            b.putPeg("white", aiPaddle[2], aiPaddle[3]);

            /*The if and else if statements above check if the gap between the ball's destination (The random number) and the AI's paddleis is equal or
            higher than the difficulty(AKA maximum gap) set by the player at the start.
            The difference between them is that the first one calculates and checks the gap for when the AI paddle's row is bigger than the random
            number and the second one calculates and checks the gap for when the AI paddle's row is smaller than the random number.
            */

        } else if (aiPaddle[1] > randomNumber && randomNumber <= b.getRows()/2 - 1 && aiPaddle[1] - randomNumber < difficulty){

                aiPaddle[0]--;
                aiPaddle[1]--;
                aiPaddle[2]--;

        } else if (aiPaddle[1] < randomNumber && randomNumber > b.getRows()/2 - 1 && randomNumber - aiPaddle[1] < difficulty){

                aiPaddle[0]++;
                aiPaddle[1]++;
                aiPaddle[2]++;

        /*The else if statements above wil add to the AI paddle's row locations if the gap between the ball's destination and the AI's paddle
        is not equal or higher than the difficulty.
        */
            }

    }

    /**
     *
     * This is the resetVariables method.
     * It is used for either when the round ends or when the game needs a reset for any other reason.
     * What it does is reset all the variables to their initial value and read from the game's save file to update the scores (This is useful for when the method runs at the start).
     *
     */

    public static void resetVariables(){

        b.removePeg(paddle[0], paddle[3]);
        b.removePeg(paddle[1], paddle[3]);
        b.removePeg(paddle[2], paddle[3]);

        b.removePeg(aiPaddle[0], aiPaddle[3]);
        b.removePeg(aiPaddle[1], aiPaddle[3]);
        b.removePeg(aiPaddle[2], aiPaddle[3]);

        paddle[0] = 10;
        paddle[1] = 11;
        paddle[2] = 12;
        paddle[3] = 2;
        ball[0] = 12;
        ball[1] = 4;
        randomNumber = 1;
        aiPaddle[0] = 13;
        aiPaddle[1] = 14;
        aiPaddle[2] = 15;
        aiPaddle[3] = 38;
        direction = 0;

        Scanner input = null;   //Introduces the scanner variable for reading the game's save file and assigns a null value to it.

        try {   //Tries the commands inside it and catches errors and exceptions if they appear.

            File saveData = new File("Save.txt");   //Creates a new file method called "saveData" and sets the path of the file to "Save.txt".
            input = new Scanner(saveData);  //Creates a new scanner method that reads from the saveData file.

        } catch (IOException e) {   //Catches any Input/Output exceptions and announces it in the console. It also handles them using the "e.printStackTrace();" tool.
            System.out.println("An error occurred.");
            e.printStackTrace();    //Handles exceptions.
        }

        while(input.hasNext()){     //This while loop scans the game's save file and assigns the first integer to the score variable for when the player loads the game a second time and wants their score saved.

            score = Integer.parseInt(input.next());

        }

        input.close();  //Finalizes the scanning process for the score value.

        b.displayMessage("Difficulty: " + difficultyString + "     Score: " + score);   //Displays the selected difficulty as well as the player's score at the start of the game.

    }

    /**
     *
     * This is the drawPaddle method.
     * It is a simple method that places pegs at the row and column locations of the paddle array to draw the player's paddle.
     *
     */

    public static void drawPaddle(){

        b.putPeg("white", paddle[0], paddle[3]);
        b.putPeg("white", paddle[1], paddle[3]);
        b.putPeg("white", paddle[2], paddle[3]);

    }

    /**
     *
     * This is the writeToFiles method.
     * It updates the save data file by adding the player's new scores by the end of the game's main loop.
     * The scores are written to the save file so that the next time the game starts up again they are read and the player is able to pick up where they left off.
     *
     */

    public static void writeToFile(){

        PrintWriter output = null;  //Introduces the print writer variable for printing to the game's save file and assigns a null value to it.

        try {   //Tries the commands inside it and catches errors and exceptions if they appear.

            File saveData = new File("BoardPong/Save.txt");   //Creates a new file method called "saveData" and sets the path of the file to "Save.txt".
            output = new PrintWriter(saveData);     //Creates a new print writer method that writes to the saveData file.

        } catch (IOException e) {   //Catches any Input/Output exceptions and announces it in the console. It also handles them using the "e.printStackTrace();" tool.
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        output.print(score);    //Prints the player's current score to the save file.
        output.close();     //Finalizes the print and saves it.

    }
}
