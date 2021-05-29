package salohcin123.snake;
import java.awt.*;

public class Tail extends Tile
{
    private int timer;
    
    public Tail(int timer)
    {
        super('T', new Color(0x00e5ff), false);
        this.timer = timer;
    }
    
    public boolean increment()
    {
        timer--;
        return timer <= 0;
    }
    
    public void ateFood()
    {
    	timer++;
    }
    
    public int getTimer()
    {
        return timer;
    }
}
