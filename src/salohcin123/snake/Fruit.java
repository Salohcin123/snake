package salohcin123.snake;
import java.awt.Color;

public class Fruit extends Tile
{
    public Fruit()
    {
        super('F', new Color(0xff009b), false);
    }
    
    public String toString()
    {
        return "F";
    }
}
