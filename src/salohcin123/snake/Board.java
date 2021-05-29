package salohcin123.snake;

/*
 * TODO: Fix that you can't turn immediately upon start
 */

public class Board
{
	private Tile[][] squares;
	private boolean gameOver;
	private int columns;
	private int rows;
	private int length;
	private int vx;
	private int vy;
	private int headRow;
	private int headCol;
	private int highScore;

	public Board() // TODO: can you call the other constructor in the default constructor? for
					// example new Board(17, 17);
	{
		rows = 17;
		columns = 17;
		initializeBoard();
	}

	/**
	 * Creates a new board with the specified number of rows and columns.
	 * 
	 * @param rows
	 * @param columns
	 */
	public Board(int rows, int columns)
	{
		this.rows = rows;
		this.columns = columns;
		initializeBoard();
	}

	/**
	 * Sets up the initial conditions of the board, positions the snake and first
	 * fruit, and resets the snake's velocity and length.
	 */
	public void initializeBoard()
	{
		getHighScore(); // saves the high score
		squares = new Tile[rows][columns];
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				setTile(i, j, new EmptyTile());
			}
		}
		for (int i = 1; i <= 3; i++)
		{
			setTile((rows - 1) / 2, i, new Tail(i));
		}
		headRow = (rows - 1) / 2;
		headCol = 4;
		setTile(headRow, 4, new Head());
		setTile(headRow, columns - 4, new Fruit());
		length = 3;
		gameOver = false;
		vx = 1;
		vy = 0;
	}

	/**
	 * Decreases all the tail counters by one and moves the head to it's next
	 * location.
	 */
	public void increment()
	{
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				if (getTile(i, j).getID() == 'T')
				{
					if (((Tail) (getTile(i, j))).increment())
					{
						clearTile(i, j);
					}
				}
			}
		}
		if (isValidMove(headRow, headCol, vx, vy))
		{
			Head head = (Head) getTile(headRow, headCol);
			setTile(headRow, headCol, new Tail(length));
			headRow = headRow + vy;
			headCol = headCol + vx;
			boolean nextTileIsFood = getTile(headRow, headCol).getID() == 'F';
			setTile(headRow, headCol, head);

			if (nextTileIsFood)
			{
				length++;
				for (int i = 0; i < rows; i++)
				{
					for (int j = 0; j < columns; j++)
					{
						if (getTile(i, j).getID() == 'T')
							((Tail) getTile(i, j)).ateFood();
					}
				}
				createNewFruit();
			}
		}
		else
			gameOver = true;
	}

	public void createNewFruit()
	{
		int randRow = (int) (Math.random() * rows);
		int randCol = (int) (Math.random() * columns);

		while (!getTile(randRow, randCol).isEmpty())
		{
			randRow = (int) (Math.random() * rows);
			randCol = (int) (Math.random() * columns);
		}

		setTile(randRow, randCol, new Fruit());
	}

	public boolean isValidMove(int headRow, int headCol, int vx, int vy)
	{
		if (!isValidSquare(headRow + vy, headCol + vx))
			return false;
		if (getTile(headRow + vy, headCol + vx).getID() == 'T')
			return false;
		return true;
	}

	public boolean isValidSquare(int row, int col)
	{
		if (row < 0 || row >= rows || col < 0 || col >= columns)
			return false;
		return true;
	}

	/**
	 * Changes the velocity variables for the snake to match the given direction.
	 * 
	 * @param direction The direction the snake should move in. Valid inputs are:
	 *                  "RIGHT", "LEFT", "UP", or "DOWN". If an invalid direction is
	 *                  inputed, the snake turns right.
	 */
	public void turn(String direction) // direction is either RIGHT, LEFT, UP, or DOWN
	{
		int initialvx = vx;
		int initialvy = vy;

		switch (direction) {
		case "RIGHT":
			vx = 1;
			vy = 0;
			break;
		case "LEFT":
			vx = -1;
			vy = 0;
			break;
		case "UP":
			vy = -1;
			vx = 0;
			break;
		case "DOWN":
			vy = 1;
			vx = 0;
			break;
		default:
			vx = 1;
			vy = 0;
		}
		/*
		 * This does not change the snake's direction if the input is not valid. An
		 * invalid input is one that is the same as the snake's current direction or one
		 * that is opposite to the snake's current direction.
		 */
		if (vx + initialvx == 0 || vy + initialvy == 0 || vx + initialvx == 2 || vy + initialvy == 2)
		{
			vx = initialvx;
			vy = initialvy;
		}
	}

	/**
	 * Returns the number of rows the board has.
	 * 
	 * @return Number of rows.
	 */
	public int getRows()
	{
		return rows;
	}

	/**
	 * Returns the number of columns the board has.
	 * 
	 * @return Number of columns.
	 */
	public int getColumns()
	{
		return columns;
	}

	/**
	 * Changes the tile at a specific location to the inputed tile.
	 * 
	 * @param row  The row index of the tile.
	 * @param col  The column index of the tile.
	 * @param tile The tile to which the tile will be changed.
	 */
	public void setTile(int row, int col, Tile tile)
	{
		squares[row][col] = tile;
	}

	/**
	 * Returns the tile at a specific row and column index.
	 * 
	 * @param row The row index of the tile.
	 * @param col The column index of the tile.
	 * @return The tile object.
	 */
	public Tile getTile(int row, int col)
	{
		return squares[row][col];
	}

	/**
	 * Sets the tile at a given row and column index to be an EmptyTile object.
	 * 
	 * @param row The row index of the tile.
	 * @param col The column index of the tile.
	 */
	public void clearTile(int row, int col)
	{
		squares[row][col] = new EmptyTile();
	}

	/**
	 * Returns whether or not the game is over.
	 * 
	 * @return The value of the gameOver variable.
	 */
	public boolean gameOver()
	{
		return gameOver;
	}

	/**
	 * Returns the current length of the snake.
	 * 
	 * @return The length of the snake.
	 */
	public int getLength()
	{
		return length;
	}

	/**
	 * Returns the highest value of either the snake's current length minus three or
	 * the previous high score.
	 * 
	 * @return
	 */
	public int getHighScore()
	{
		highScore = Math.max(length - 3, highScore);
		return highScore;
	}

	/**
	 * Used for printing out the board when in debug mode in a console-only game.
	 */
	public String toString()
	{
		String output = "";
		for (int i = 0; i < 17; i++)
		{
			for (int j = 0; j < 17; j++)
			{
				Tile currentTile = getTile(i, j);
				String printed = currentTile.getID() == 'T' ? ((Tail) (currentTile)).getTimer() + "" : currentTile.getID() + "";
				output += printed + " ";
			}
			output += "\n";
		}
		return output;
	}
}
