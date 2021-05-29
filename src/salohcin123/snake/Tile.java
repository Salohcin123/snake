package salohcin123.snake;
import java.awt.*;

public class Tile
{
	private char id;
    private Color color;
    private boolean isEmpty;
    
    public Tile(char id, Color color, boolean isEmpty)
    {
        this.id = id;
        this.color = color;
        this.isEmpty = isEmpty;
    }
    
    public char getID()
    {
        return id;
    }
    
    public Color getColor()
    {
        return color;
    }
    
    public boolean isEmpty()
    {
        return isEmpty;
    }
}
