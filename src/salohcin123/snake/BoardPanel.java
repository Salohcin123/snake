package salohcin123.snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel
{
	private final int DELAY = 80;
	private final Color LIGHT_COLOR = new Color(0xeeeeee);
	private final Color DARK_COLOR = new Color(0xe2e2e2);

	private int tileSize;
	private Board board;
	private Thread game;
	private volatile boolean gameRunning; // used to make sure the game is paused before the player presses a button
	private ArrayList<String> turns = new ArrayList<String>(); // used to queue the turns the player inputs
	private Image fruitImage; // the image of the fruit

	public BoardPanel(Board board, int tileSize, int screenWidth, int screenHeight, ScoreboardPanel scoreboardPanel)
	{
		this.board = board;
		this.tileSize = tileSize;

		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setFocusable(true);
		requestFocusInWindow();

		addKeyListener(new KeyListener()
		{

			@Override
			public void keyTyped(KeyEvent e)
			{
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
			}

			/*
			 * The turn method is called when one of the arrow keys are pressed. The board
			 * is reset after the enter key is pressed, which is used once the game is over.
			 */
			@Override
			public void keyPressed(KeyEvent e)
			{
				gameRunning = true;
				int keyCode = e.getKeyCode();
				switch (keyCode) {
				case KeyEvent.VK_UP:
					if (turns.size() < 5)
						turns.add("UP");
					break;
				case KeyEvent.VK_DOWN:
					if (turns.size() < 5)
						turns.add("DOWN");
					break;
				case KeyEvent.VK_LEFT:
					if (turns.size() < 5)
						turns.add("LEFT");
					break;
				case KeyEvent.VK_RIGHT:
					if (turns.size() < 5)
						turns.add("RIGHT");
					break;
				case KeyEvent.VK_ENTER:
					board.initializeBoard();
					repaint();
					gameRunning = false;
					startThread();
					scoreboardPanel.startThread(board.getHighScore());
					break;
				}
			}
		});

		startThread();

		ImageIcon fruitIcon = new ImageIcon(BoardPanel.class.getResource("/res/fruit.png"));
		fruitImage = fruitIcon.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		drawBoard(g);
	}

	/**
	 * This method is part of the workaround I created for the thread termination
	 * problem. It simply recreates the thread and starts it again to make the game
	 * run again after the player restarts.
	 * 
	 */
	public void startThread()
	{
		game = new Thread()
		{
			public void run()
			{
				while (true)
				{
					while (!board.gameOver())
					{
						if (gameRunning)
						{
							board.increment(); // increment has to go before repaint for instant fruit creation
							if (board.gameOver()) // needed to stop one more repaint after death
								return;
							repaint();
							if (!turns.isEmpty())
							{
								board.turn(turns.remove(0));
							}
							try
							{
								Thread.sleep(DELAY);
							} catch (InterruptedException e)
							{
								e.printStackTrace();
							}
						}
					}
				}
			}
		};
		game.start();
	}

	/**
	 * Draws the grid of squares, the snake, and the fruit.
	 * 
	 * @param g Graphics object from JPanel.
	 */
	public void drawBoard(Graphics g)
	{
		for (int i = 0; i < board.getRows(); i++)
		{
			for (int j = 0; j < board.getColumns(); j++)
			{
				if (board.getTile(i, j).isEmpty() || board.getTile(i, j).getID() == 'F')
				{
					if ((i + j) % 2 == 0)
						g.setColor(LIGHT_COLOR);
					else
						g.setColor(DARK_COLOR);
				}
				else
				{
					g.setColor(board.getTile(i, j).getColor());
				}
				g.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
				if (board.getTile(i, j).getID() == 'T')
					drawBorders(g, i, j);
				if (board.getTile(i, j).getID() == 'F')
					g.drawImage(fruitImage, j * tileSize, i * tileSize, this);
			}
		}
	}

	/**
	 * This method draws borders around the inputed square if the square is next to
	 * another part of the snake.
	 * 
	 * @param g JPanel graphics object.
	 * @param i The row index of the square.
	 * @param j The column index of the square.
	 */
	public void drawBorders(Graphics g, int i, int j) // TODO: this method's code can be massively optimized
	{
		g.setColor(Color.BLACK);
		Tile middle = board.getTile(i, j);
		int timer = ((Tail) middle).getTimer();
		if (middle.getID() == 'H')
		{
			g.drawRect(j * tileSize, i * tileSize, tileSize - 1, tileSize - 1);
		}
		else if (middle.getID() == 'T')
		{
			if (board.isValidSquare(i + 1, j) && board.getTile(i + 1, j).getID() == 'T'
					&& Math.abs(((Tail) (board.getTile(i + 1, j))).getTimer() - timer) > 1) // tile below
			{
				g.drawLine(j * tileSize, i * tileSize + tileSize - 1, j * tileSize + tileSize - 1, i * tileSize + tileSize - 1);
			}
			if (board.isValidSquare(i, j + 1) && board.getTile(i, j + 1).getID() == 'T'
					&& Math.abs(((Tail) (board.getTile(i, j + 1))).getTimer() - timer) > 1) // tile right
			{
				g.drawLine(j * tileSize + tileSize - 1, i * tileSize, j * tileSize + tileSize - 1, i * tileSize + tileSize - 1);
			}
		}
	}
}