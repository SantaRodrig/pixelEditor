public class Pixel
{
    //the rgbs are just set to being 0 at first 
    private int rgb = 0b00000000000000000000000000000000;

    //Default constructor makes it so it is just set to white
    public Pixel()
    {
        setRed(255);
        setGreen(255);
        setBlue(255);
    }

    //The pixels r g b are being made sure to have correct values or they will just be set to while 
    public Pixel(int r, int g, int b)
    {
        if((r <= 255 && r >=0) && (g <= 255 && g >= 0) && (b <= 255 && b >= 0))
        {
            setRed(r);
            setGreen(g);
            setBlue(b);
        }
    }

    //Sets the red values 
    public void setRed(int r)
    {
        if(r <= 255 && r >= 0)
        {
            //             |blank ||   red || green||blue  |   
            int redMask = 0b00000000000000001111111111111111;
            int copyRGB = rgb & redMask;
            rgb = copyRGB | (r<<16);
        }
    }

    public int getRed()
    {
        int onlyRed = 0b00000000111111110000000000000000;
        int red = rgb & onlyRed;
        red = red >>16;
        return red;
    }

    public void setGreen(int g)
    {
        if(g <= 255 && g >= 0)
        {
            //              |blank   ||   red|| green||blue  |
            int greenMask = 0b00000000111111110000000011111111;   
            int copyRGB = rgb & greenMask;
            rgb = copyRGB | (g<<8);
        }
    }

    public int getGreen()
    { 
        //                |blank ||   red || green||blue  | 
        int onlyGreen = 0b00000000000000001111111100000000;
        int green = rgb & onlyGreen;
        green = green>>8;
        return green;

    }

    public void setBlue(int b)
    {
        if(b <= 255 && b >= 0)
        {
            int blueMask = 0b00000000111111111111111100000000;
            int copyRGB = rgb & blueMask;
            rgb = copyRGB | (b);
        }
    }

    public int getBlue()
    {
        //               |blank ||   red|| green||blue  | 
        int onlyBlue = 0b00000000000000000000000011111111;
        int blue = rgb & onlyBlue;
        return blue;        
    }

    /*public int getRGB()
    {
        return rgb;
    }*/

    //return the string version of each of the pixels in hex 
    public String printHex()
    {
       String redS="";
       if(getRed() < 16)
       {
           redS = redS.concat("0");
           redS = redS + Integer.toHexString(getRed()); 
       }
       else
       {
            redS = Integer.toHexString(getRed());
       }

       String greenS ="";
       if(getGreen() < 16)
       {
           greenS = greenS.concat("0");
           greenS = greenS + Integer.toHexString(getGreen());
       }
       else
       {
            greenS = Integer.toHexString(getGreen());
       }

       String blueS = "";
       if(getBlue() < 16)
       {
           blueS = blueS.concat("0");
           blueS = blueS + Integer.toHexString(getBlue());
       }
       else
       {
            blueS = Integer.toHexString(getBlue());
       }

       String hexString = "#" + redS.toUpperCase() + greenS.toUpperCase() + blueS.toUpperCase();

       //System.out.println(hexString);
       return hexString;
    }

}