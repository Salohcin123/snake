package salohcin123.snake;

import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * DISCLAIMER
 * I am aware that there are many optimizations that can be made with a lot of this code and how it is structured, 
 * especially the Tile hierarchy. However, I only would like help with my threading issue outlined in the Reddit post.
 */

public class Run
{
	public static final boolean DEBUG_MODE = false;

	public final static int TILE_SIZE = 50; // DEFAULT: 50
	public final static int ROWS = 17; // DEFAULT: 17
	public final static int COLUMNS = 17; // DEFAULT: 17
	public static final int SCREEN_WIDTH = TILE_SIZE * COLUMNS;
	public static final int SCREEN_HEIGHT = TILE_SIZE * ROWS;

	private static Board board;

	public static void main(String[] args)
	{
		board = new Board(ROWS, COLUMNS);
		if (!DEBUG_MODE)
		{
			JFrame frame = new JFrame("Snake");
			JPanel contentPane = new JPanel();
			ScoreboardPanel scoreboardPanel = new ScoreboardPanel(board);
			/*
			 * The scoreboard is passed into the BoardPanel as part of the workaround for
			 * the threads being terminated. Ideally, the scoreboard would not need to be
			 * passed in, and would just keep having its thread run once it is started once.
			 */
			BoardPanel boardPanel = new BoardPanel(board, TILE_SIZE, SCREEN_WIDTH, SCREEN_HEIGHT, scoreboardPanel);
			buildFrame(frame, contentPane, scoreboardPanel, boardPanel);
		}
		else
		{
			loadConsoleGame();
		}
	}

	/**
	 * Sets up the frame and adds all the components to the content JPanel.
	 * 
	 * @param frame
	 * @param content
	 * @param scoreboard
	 * @param board
	 */
	public static void buildFrame(JFrame frame, JPanel content, ScoreboardPanel scoreboard, BoardPanel board)
	{
		ImageIcon img = new ImageIcon(BoardPanel.class.getResource("/res/snake_icon.png"));
		frame.setIconImage(img.getImage());

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setContentPane(content);
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		content.add(scoreboard);
		content.add(board);

		frame.pack();
		frame.setLocationRelativeTo(null);

		frame.setVisible(true);
	}

	/**
	 * Prints out the board in console. The board will increment when anything is
	 * entered. This was for early testing.
	 */
	public static void loadConsoleGame()
	{
		System.out.println(board);

		String userInput = "";
		Scanner console = new Scanner(System.in);
		while (userInput.indexOf("stop") == -1)
		{
			userInput = console.nextLine();
			board.increment();
			System.out.println(board);
		}
		console.close();
	}
}
