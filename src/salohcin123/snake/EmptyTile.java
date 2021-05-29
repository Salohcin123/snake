package salohcin123.snake;
import java.awt.*;

public class EmptyTile extends Tile
{
    public EmptyTile()
    {
        super('-', Color.WHITE, true);
    }
    
    public String toString()
    {
        return "-";
    }
}
