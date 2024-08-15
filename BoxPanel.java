// Artem Tagintsev, Dots
// This class handles the logic of the game which includes initializing the grid of ovals to represent dots, initialized the 2D array of boxes, drawing lines
// between the respective dots where user clicked using coordinate arithmetic, and after each click update the board, scores of players, and checking if end
// of game has been reached by every box being filled and declaring who the winner is...or if it's a tie. Also checks for respective errors in the gameplay and
// makes a lot of calls to Dots.java class for updating player information.
// Also FYI: this class includes methods with super repetitive logic so the comments for those methods will just be included for one part of the box because 
// it is the same logic for all 4 sides. 
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoxPanel extends JPanel{
	boolean gameStartedFlag = false; // Flag to keep track if game started or not
	private JLabel messageLabel; // JLabel that displays error messages to user
	private JLabel playerTurnLabel; // JLabel that displays which player's turn it is
	private boolean player1Turn = true; // // Player 1 starts the game with the first move by default
	private String player1Initial;
	private String player2Initial;
	private JLabel player1Score;
	private JLabel player2Score;
	int p1ScoreCount = 0; // Initialize player 1 score as 0
	int p2ScoreCount = 0; // Initialize player 2 score as 0


	// Variables for lines
	Boxes[][] grid; // A 2D array of Boxes which represent the grid
	int gridParam = 8; // n x n grid 
	int boxSize = 50; // Size of each box
	double xmax, xmin, ymax, ymin; // The limits for user coordinate system
	
	// Variables for ovals
	int ovalSize = 5; // Size of ovals
	double gridSize = 401; // Size of grid
	double ovalAmount = 8; // ovalAmount+1 is the number of ovals in a n x n grid
	double ovalDistance = gridSize / ovalAmount; // Distance between ovals

	// Constructor for BoxPanel
	public BoxPanel() {
		setLayout(null);
		setBounds(100, 100, 401, 401); // Sets the bounds of BoxPanel
		grid = new Boxes[gridParam][gridParam]; // Initialize the array for the grid
		// Initialize each box in the grid
		for(int i = 0; i < gridParam; i++) {
			for(int j = 0; j < gridParam; j++) {
				grid[i][j] = new Boxes();
			}
		}
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(gameStartedFlag) {
					playGame(e.getX(), e.getY()); // Call playGame method on every mouse click to update the game
				}
			}
		});
	}
	
	// Pass the score labels to BoxPanel
	public void passScores(JLabel player1Score, JLabel player2Score) {
		this.player1Score = player1Score;
		this.player2Score = player2Score;
	}
	// Pass the initials of players to BoxPanel
	public void passPlayerInitial(String player1Initial, String player2Initial) {
		this.player1Initial = player1Initial;
		this.player2Initial = player2Initial;
	}
	
	// Method to handle the game play when an event, like a mouse click happens
	// This method does coordinate arithmetic and makes calls to Boxes.java to keep track of which sides of boxes were clicked,
	// if a box has been completed and which player's score to update. Also calls paint and repaint to update the board
	// and checks on each click if the end of the game has been reach. This method is the heart of the program as it handles most logic
	private void playGame(int x, int y) {
		// Checks if the click from players is within the bounds of the grid, prints error message if not
		if(x < 69 || x > 69 + gridSize || y < 69 || y > 69 + gridSize) {
			messageLabel.setText("<ERROR: Click has to be in the grid!>");
			return;
		}
		
		// Calculate row (based on x-coordinate) and col (based on y-coordinate)
        int row = (y-69)/boxSize;
        int col = (x-69)/boxSize;
        
        // I added this if statement to check if a player clicks on the grid specifically because for some reason if a player clicked 
        // on the right or bottom of the grid exactly, the console would show errors, this if statement prevents those errors from occuring
        // by checking if the row or col are greater than or equal to the gridParam and popping up an error message if yes. 
        if(row >= gridParam || col >= gridParam) {
        	messageLabel.setText("<ERROR: Click has to be in the grid!>");
        	return;
        }
        
        // Calculate the respective x or y-coordinate of the clicked edge (top, bottom, left, right)
        double yTop = 69 + row * boxSize;
        double yBottom = yTop + boxSize;
        double xLeft = 69 + col * boxSize;
        double xRight = xLeft + boxSize;
        
        // Calculate distance from the click to the respective edge of the box (top, bottom, left, right)
        double dTop = y - yTop;
        double dBottom = yBottom - y;
        double dLeft = x - xLeft;
        double dRight = xRight - x;

       	// validTurn tracks if the player's move was valid by checking if a line was drawn, if it was drawn then validTurn is set to true
        boolean validTurn = false;
       	// invalidTurn tracks if the player attempts to draw the same line more than once, which if they attempt that then invalidTurn is set to true and triggers an error message
       	boolean invalidTurn = false;
       	// boxMade tracks if a box has been completed, meaning all 4 sides are drawn
       	boolean boxMade = false;
        	
        // Set dTop is the smallest distance by default then compare with the other 3 sides of the square to find the minimum distance to a box edge
        double minDistance = dTop;
        if(dBottom < minDistance) minDistance = dBottom;
        if(dLeft < minDistance) minDistance = dLeft;
        if(dRight < minDistance) minDistance = dRight;
        
        // Method to call to the Boxes class methods and set their boolean values based off which side is the minDistance
        // Also checks if the adjacent box has been drawn, prints error message if the same line was clicked more than once
        if(minDistance == dTop) { // Check which edge minDistance is, dTop in this case
            if(grid[row][col].top) { // Check if top edge of current box is already drawn
                invalidTurn = true; // If top edge of current box is already drawn, set invalidTurn to true
            } else {
                validTurn = grid[row][col].drawTop(); // Draw the top edge of the current box which makes a call to drawTop from Boxes class and sets validTurn to true
                if (row > 0) { // Check if the current box is not in the first row which prevents making adjacent lines being set in spaces where more boxes don't exist
                    grid[row - 1][col].drawBottom(); // Draw the line in the adjacent box, which would be drawBottom of the box below in this case
                }
            }
        }
        if(minDistance == dBottom) {
            if(grid[row][col].bottom) {
                invalidTurn = true;
            } else {
                validTurn = grid[row][col].drawBottom();
                if (row < gridParam - 1) {
                    grid[row + 1][col].drawTop();
                }
            }
        }
        if(minDistance == dLeft) {
            if(grid[row][col].left) {
                invalidTurn = true;
            } else {
                validTurn = grid[row][col].drawLeft();
                if (col > 0) {
                    grid[row][col - 1].drawRight();
                }
            }
        }
        if(minDistance == dRight) {
            if(grid[row][col].right) {
                invalidTurn = true;
            } else {
                validTurn = grid[row][col].drawRight();
                if (col < gridParam - 1) {
                    grid[row][col + 1].drawLeft();
                }
            }
        }
        
        // If the player's move is valid, check if the player completed any boxes, give them to the player and update score
       	if (validTurn) {
       	    // Check if the current box is completed and if a player hasn't completed it yet
       	    if (grid[row][col].boxComplete() && grid[row][col].whosBox.equals(" ")) {
       	        if (player1Turn) {
       	            grid[row][col].whosBox = player1Initial; // Gives the box to player 1
       	            p1ScoreCount++; // Increment player 1's score
       	        } else {
       	            grid[row][col].whosBox = player2Initial; // Gives the box to player 2
       	            p2ScoreCount++; // Increment player 2's score
       	        }
       	        boxMade = true; // Set the flag boxMade to true to show the box has been completed (all 4 sides are drawn)
       	    }
       	    // Check if the box above is completed and if a player hasn't completed it yet
       	    // I added these 4 if statements below to check if any boxes around a completed box are complete because I would run
       	    // into an issue of if a player draws a line and 1 line completes lets say 2 boxes, only one of the boxes would count as complete
       	    if (row > 0 && grid[row - 1][col].boxComplete() && grid[row - 1][col].whosBox.equals(" ")) {
       	        if (player1Turn) {
       	            grid[row - 1][col].whosBox = player1Initial;
       	            p1ScoreCount++;
       	        } else {
       	            grid[row - 1][col].whosBox = player2Initial;
       	            p2ScoreCount++;
       	        }
       	        boxMade = true;
       	    }
       	    if (row < gridParam - 1 && grid[row + 1][col].boxComplete() && grid[row + 1][col].whosBox.equals(" ")) {
       	        if (player1Turn) {
       	            grid[row + 1][col].whosBox = player1Initial;
       	            p1ScoreCount++;
       	        } else {
       	            grid[row + 1][col].whosBox = player2Initial;
       	            p2ScoreCount++;
       	        }
       	        boxMade = true;
       	    }
       	    if (col > 0 && grid[row][col - 1].boxComplete() && grid[row][col - 1].whosBox.equals(" ")) {
       	        if (player1Turn) {
       	            grid[row][col - 1].whosBox = player1Initial;
       	            p1ScoreCount++;
       	        } else {
       	            grid[row][col - 1].whosBox = player2Initial;
       	            p2ScoreCount++;
       	        }
       	        boxMade = true;
       	    }
       	    if (col < gridParam - 1 && grid[row][col + 1].boxComplete() && grid[row][col + 1].whosBox.equals(" ")) {
       	        if (player1Turn) {
       	            grid[row][col + 1].whosBox = player1Initial;
       	            p1ScoreCount++;
       	        } else {
       	            grid[row][col + 1].whosBox = player2Initial;
       	            p2ScoreCount++;
       	        }
       	        boxMade = true;
       	    }
       	}
       	
        // If move is invalid print error message and don't paint
        if(invalidTurn) {
        	messageLabel.setText("<ERROR: This line has already been drawn!>");
        }
        // If move is valid clear error message (if any was displayed) and call repaint
        else if(validTurn) {
        	messageLabel.setText("");
        	// Update scores after each click
        	player1Score.setText("Score: " + p1ScoreCount);
        	player2Score.setText("Score: " + p2ScoreCount);
        	// If a box hasn't been made, swap which player's turn it is
        	if(!boxMade) {
        		player1Turn = !player1Turn;
        		if(player1Turn) {
        			playerTurnLabel.setForeground(Color.BLUE);
        			playerTurnLabel.setText("Player 1 Go!");
        		}
        		else {
        			playerTurnLabel.setForeground(Color.GREEN);
        			playerTurnLabel.setText("Player 2 Go!");
        		}
        	}
        	endGame(); // Check if end of game has been reached
        	repaint(); // Update the board
        }
	}
	
	// Method to set messageLabel
	public void passMessageLabel(JLabel messageLabel) {
		this.messageLabel = messageLabel;
	}
	
	// Method to set playerTurnLabel
	public void passPlayerTurnLabel(JLabel playerTurnLabel) {
		this.playerTurnLabel = playerTurnLabel;
	}
	
	// override the paint method
	public void paint(Graphics g) {
		super.paint(g);
			
		// Print out grid of ovals
		for(int x = 0; x < gridSize; x+=ovalDistance) {
			for(int y = 0; y < gridSize; y+=ovalDistance) {
				g.fillOval(x + 69, y + 69, ovalSize, ovalSize); // draw oval
			}
		}
		
		// Move through each box in the grid and draw the respective lines and initials of players if a box is complete
		for(int i = 0; i < gridParam; i++) {
			for(int j = 0; j < gridParam; j++) {
				int drawX = 71 + j*boxSize; // Calculate x-coordinate for drawing lines
				int drawY = 71 + i*boxSize; // Calculate y-coordinate for drawing lines
				// Draw the respective line if it exists (top, bottom, left, or right)
				if(grid[i][j].top) g.drawLine(drawX,  drawY, drawX + boxSize, drawY); 
				if(grid[i][j].bottom) g.drawLine(drawX,  drawY + boxSize, drawX + boxSize, drawY + boxSize);
				if(grid[i][j].left) g.drawLine(drawX,  drawY, drawX, drawY + boxSize);
				if(grid[i][j].right) g.drawLine(drawX + boxSize,  drawY, drawX + boxSize, drawY + boxSize);
				// Draw the initial of the player which drew the full box
				if(!grid[i][j].whosBox.equals(" ")) {
					g.drawString(grid[i][j].whosBox, (drawX - 4) + boxSize / 2, (drawY + 4) + boxSize / 2); // -4 and +4 added to the calculations to center the initial better
				}
			}
		}
	}

	// Method to reset the board display
	public void resetBoard() {
		gameStartedFlag = false;
		// Reinitialize the 2D array of boxes
		for(int i = 0; i < gridParam; i++) {
			for(int j = 0; j < gridParam; j++) {
				grid[i][j] = new Boxes();
			}
		}
		// Reset the turn to player 1, score counts to 0, and repaint the board to it's default look
		player1Turn = true;
		p1ScoreCount = 0;
		p2ScoreCount = 0;
		playerTurnLabel.setText("");
		repaint();
	}
	
	// Method that sets the flag, this decides if the BoxPanel responds or doesn't to user input
	public void setGameStartedFlag(boolean gameStartedFlag) {
		this.gameStartedFlag = gameStartedFlag;
	}
	
	// Compare player scores and print which player won or if they tied and set gameStartedFlag to false
	public void endGame() {
		if(p1ScoreCount + p2ScoreCount == gridParam*gridParam) {
			gameStartedFlag = false;
			if (p1ScoreCount > p2ScoreCount) playerTurnLabel.setText("Player 1 Won!");
			else if (p2ScoreCount > p1ScoreCount) playerTurnLabel.setText("Player 2 Won!");
			else {
				playerTurnLabel.setForeground(Color.ORANGE);
				playerTurnLabel.setText("Tie Game!");
			}
		}
	}
}

