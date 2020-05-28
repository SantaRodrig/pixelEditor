import javax.swing.JButton;

public class PixelButton extends JButton
{
    private int xCoord;
    private int yCoord;

    public PixelButton( int x, int y)
    {
        //super(text);
        setXCoord(x);
        setYCoord(y);
    }

    public int getXCoord()
    {
        return xCoord;
    }

    public void setXCoord(int x)
    {
        if(x >= 0)
        {
            xCoord = x;
        }
        else
        {
            xCoord = 0;
        }
    }

    public int getYCoord()
    {
        return yCoord;
    }

    public void setYCoord(int y)
    {
        if(y>=0)
        {
            yCoord = y;
        }
        else
        {
            yCoord =0;
        }
    }

}