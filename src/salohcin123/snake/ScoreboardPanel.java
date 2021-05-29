/*
 * TODO: The scoreboard does not update again once the game is completed and restarted.
 */
package salohcin123.snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ScoreboardPanel extends JPanel
{
	private final Color BACKGROUND_COLOR = new Color(0xa6a6a6);
	private final Color TEXT_COLOR = new Color(0xffffff);
	private Board board;
	private Thread update;
	private int highScore;

	public ScoreboardPanel(Board board)
	{
		this.board = board;
		setPreferredSize(new Dimension(Run.SCREEN_WIDTH, Run.TILE_SIZE * 2));
		setBackground(BACKGROUND_COLOR);
		startThread(board.getHighScore());
	}

	/**
	 * This method is part of the workaround I created for the thread termination
	 * problem. It simply recreates the thread and starts it again to make the
	 * scoreboard update after the player starts the game again.
	 * 
	 * @param highScore
	 */
	public void startThread(int highScore)
	{
		this.highScore = highScore;
		update = new Thread()
		{
			public void run()
			{
				while (true)
				{
					if (!board.gameOver())
					{
						repaint();
					}
				}
			}
		};
		update.start();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		drawScore(g);
		drawHighScore(g);
	}

	/**
	 * Draws the score in the top left.
	 * 
	 * @param g Graphics object from JPanel.
	 */
	public void drawScore(Graphics g)
	{
		Font scoreFont = new Font("Helvetica", Font.BOLD, Run.TILE_SIZE);
		g.setFont(scoreFont);
		FontMetrics fontMetrics = g.getFontMetrics(scoreFont);
		int realScoreHeight = (int) scoreFont.createGlyphVector(fontMetrics.getFontRenderContext(), board.getLength() - 3 + "").getVisualBounds()
				.getHeight();
		g.setColor(TEXT_COLOR);
		g.drawString(board.getLength() - 3 + "", Run.TILE_SIZE / 2, getHeight() / 2 + realScoreHeight / 2);
	}

	/**
	 * Draws the high score in the top right.
	 * 
	 * @param g Graphics object from JPanel.
	 */
	public void drawHighScore(Graphics g)
	{
		Font scoreFont = new Font("Helvetica", Font.BOLD, Run.TILE_SIZE);
		g.setFont(scoreFont);
		FontMetrics fontMetrics = g.getFontMetrics(scoreFont);
		String highScoreString = "High Score: " + highScore;
		int realScoreHeight = (int) scoreFont.createGlyphVector(fontMetrics.getFontRenderContext(), board.getLength() - 3 + "").getVisualBounds()
				.getHeight();
		g.setColor(TEXT_COLOR);
		g.drawString(highScoreString + "", getWidth() - fontMetrics.stringWidth(highScoreString) - Run.TILE_SIZE / 2,
				getHeight() / 2 + realScoreHeight / 2);
	}
}