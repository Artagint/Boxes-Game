// Artem Tagintsev,  Dots
// This class makes a main for the Dots game, initializes the game components and handles the majority of the user input like detecting if and when start/ restart
// button is clicked and also getting the user names and error checking for empty names or same initials after if grabs the first initial of the names of players.
// Methods included in this class are launching the application, initializing the frame, updating player names and information, starting the game, and restarting the game. 
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;

public class Dots extends JFrame {

	private static final long serialVersionUID = 1L;
	// Declare and initialize objects for the game
	private BoxPanel contentPane; // Panel where game is played
	private JTextField player1textField;
    private JTextField player2textField;
    private JLabel player1Name;
    private JLabel player2Name;
    private JLabel player1Score;
    private JLabel player2Score;
    private JLabel messageLabel;
    private JLabel playerTurnLabel;
    private JButton startButton;
    private boolean gameStarted = false; // gameStarted becomes true when required user information is acquired and 'Start' JButton is clicked
    int p1ScoreCount = 0; // Score count for player 1
    int p2ScoreCount = 0; // Score count for player 2

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Dots frame = new Dots();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Dots() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 600);
		contentPane = new BoxPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		startButton = new JButton("Start");
		startButton.addMouseListener(new MouseAdapter() {
			// Wait for mouseClicked on the start button or restart button
			@Override
			public void mouseClicked(MouseEvent e) {
				if(gameStarted) {
					restartGame();
				}
				else {
					startGame();
				}
			}
		});
		startButton.setBounds(530, 88, 100, 21);
		contentPane.add(startButton);
		

		
		// Player 1 information ----------------------------- Player 1 information ---//
		// JLabel that acts like a prompt for the name of Player 1
		JLabel player1Label = new JLabel("Player 1's Name:");
		player1Label.setBounds(530, 275, 100, 20);
		contentPane.add(player1Label);
		
		// testField that asks for the Player 1's name
		player1textField = new JTextField();
		player1textField.setBounds(530, 300, 100, 20);
		contentPane.add(player1textField);
		player1textField.setColumns(10);
		
		// JLabel that displays the score of Player 1
		player1Score = new JLabel("Score:");
		player1Score.setBounds(100, 10, 60, 15);
		contentPane.add(player1Score);
		
		// Shows the initial of Player 1's name below the score
		player1Name = new JLabel("Player 1");
		player1Name.setBounds(100, 35, 45, 15);
		contentPane.add(player1Name);
		
		
		// Player 2 information ----------------------------- Player 2 information ---//
		// JLabel that acts like a prompt for the name of Player 2
		JLabel player2Label = new JLabel("Player 2's Name:");
		player2Label.setBounds(530, 375, 200, 20);
		contentPane.add(player2Label);
		
		// testField that asks for the Player 2's name
		player2textField = new JTextField();
		player2textField.setBounds(530, 400, 100, 20);
		contentPane.add(player2textField);
		player2textField.setColumns(10);
		
		// JLabel that displays the score of Player 2
		player2Score = new JLabel("Score: ");
		player2Score.setBounds(200, 10, 60, 15);
		contentPane.add(player2Score);
		
		// Shows the initial of Player 2's name below the score
		player2Name = new JLabel("Player 2");
		player2Name.setBounds(200, 35, 45, 15);
		contentPane.add(player2Name);
		
		messageLabel = new JLabel("");
		messageLabel.setForeground(new Color(255, 0, 0));
		messageLabel.setHorizontalAlignment(SwingConstants.LEFT);
		messageLabel.setBounds(300, 23, 328, 13);
		contentPane.add(messageLabel);
		contentPane.passMessageLabel(messageLabel); // Pass messageLabel to BoxPanel
		
		playerTurnLabel = new JLabel("");
		playerTurnLabel.setBounds(530, 163, 133, 21);
		contentPane.add(playerTurnLabel);
		contentPane.passPlayerTurnLabel(playerTurnLabel);
	}
	
	// Method that updates the player names, information, and displays respective error messages if needed
	public void updatePlayerNames() {
		messageLabel.setText("");
		// Checks if the text fields for the players is empty and print error message, resets info, and returns
		if(player1textField.getText().equals("") || player2textField.getText().equals("")) {
			messageLabel.setText("<ERROR: Both of the players need to input a name!>");
			player1Name.setText("Player 1");
			player2Name.setText("Player 2");
			player1Score.setText("Score:");
			player2Score.setText("Score:");
			return;
		}
		
		// Capture the initial of the players and convert to upper case for proper input comparison
		String initialPlayer1 = player1textField.getText().substring(0,1).toUpperCase();
		String initialPlayer2 = player2textField.getText().substring(0,1).toUpperCase();
		
		// Checks if the players initials are the same, if they are then an error message is displayed and information is reset, and returns
		if(initialPlayer1.equals(initialPlayer2)) {
			messageLabel.setText("<ERROR: Both players can't have the same initial!>'");
			player1Name.setText("Player 1");
			player2Name.setText("Player 2");
			player1Score.setText("Score:");
			player2Score.setText("Score:");
			return;
		}
		// Update player names to their respective initials, added leading spaces just to center the initial a bit
		player1Name.setText("      " + initialPlayer1);
		player2Name.setText("      " + initialPlayer2);
		
		// Set both players scores initially as 0
		player1Score.setText("Score: 0");
		player2Score.setText("Score: 0");
		
		// Clear messageLabel if it popped up before
		messageLabel.setText("");
		
		// Change the start button to restart
		startButton.setText("Restart");
		
		contentPane.passPlayerInitial(initialPlayer1, initialPlayer2); // Pass player initials to BoxPanel
		contentPane.passScores(player1Score, player2Score); // Pass Scores to BoxPanel
	}
	
	// Method to start the game, updates player information (names and scores), and set the gameStarted flag to true to indicate the game has started
	public void startGame() {
		messageLabel.setText("");
		updatePlayerNames();
		// Start the game only if there are no errors
		if(messageLabel.getText().isEmpty()) {
			gameStarted = true;
			contentPane.setGameStartedFlag(true);
			playerTurnLabel.setForeground(Color.BLUE);
			playerTurnLabel.setText("Player 1 Go!");
		}
	}
	
	// Method to restart the Game, resets everything to how it was when we just opened the window to play the game
	public void restartGame() {
		player1textField.setText("");
		player2textField.setText("");
		player1Name.setText("Player 1");
		player2Name.setText("Player 2");
		player1Score.setText("Score: ");
		player2Score.setText("Score: ");
		messageLabel.setText("");
		startButton.setText("Start");
		playerTurnLabel.setText("");
		gameStarted = false;
		p1ScoreCount = 0;
		p2ScoreCount = 0;
		contentPane.resetBoard(); // Calls resetBoard to get the board to its empty initial state
	}
}
