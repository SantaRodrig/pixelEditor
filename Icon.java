import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Icon
{
    //made the private data memeber that will hold the pixel data 
    private ArrayList <ArrayList<Pixel>>bitMap;

    //Icon constructor if icon object was not given a width and length
    //default is 40x40
    public Icon()
    {
        bitMap = new ArrayList<ArrayList<Pixel>>();
        
        for(int rows =0; rows < 40; rows++)
        {
            bitMap.add(new ArrayList<Pixel>());
            for(int col = 0; col < 40; col++)
            {
                bitMap.get(rows).add(new Pixel());
            }
        }
        
    }

    //This constructor is used when a row and col is given when making the icon object in the driver
    public Icon(int row, int col)
    {
        bitMap = new ArrayList<ArrayList<Pixel>>();

        for(int r = 0; r < row; r++)
        {
            bitMap.add(new ArrayList<Pixel>());
            for(int c = 0; c < col; c++)
            {
                bitMap.get(r).add(new Pixel());
            }
        }
    }

    //this will the the pixel of a spot on the bitMap given 
    public void setPixel(int row, int col, int r, int g, int b)
    {
        //short circut evaluation that if one evaluates to true then it prints the error
        if(row > bitMap.size() || col > bitMap.get(0).size() || row < 0 || col < 0)
        {
            System.out.println("The size entered was not correct");
        }
        else
        {
            //set the colors at the specified row and col
            bitMap.get(row).get(col).setRed(r);
            bitMap.get(row).get(col).setGreen(g);
            bitMap.get(row).get(col).setBlue(b);
        }

    }

    //function that gets the pixel in row and col that was passed in 
    public Pixel getPixel(int row, int col) 
    {
        //checks to see if the row and col are valid 
        if(row > bitMap.size() || col > bitMap.get(0).size() || row < 0 || col < 0)
        {
            return null;
        }
        else
        {
            return bitMap.get(row).get(col);
        }
    }

    //function to give the hex version of the colors if the user wants to see it 
    public String toString()
    {
        String hex = "";
        for(int row = 0; row < bitMap.size(); row++)
        {
            for(int col = 0; col < bitMap.size(); col++)
            {
               hex += bitMap.get(row).get(col).printHex() + " ";
            }
            hex += "\n";
        }

        return hex;
    }

    //gets the padding depending on the width to make sure that the correct amount of bytes get added to fileSize
    private int getPadding()
    {
        int padding =0;
        int width = bitMap.get(0).size();

        if((width * 3)%4 == 1)
        {
            padding = 3;
        }
        else if((width * 3)%4 == 2)
        {
            padding = 2;
        }
        else if((width * 3)%4 == 3)
        {
            padding = 1;
        }


        return padding;
    }

    //The function to build the bitMap file 
    public void makeBitMapImage(String path)
    {
        //The start of setting up the bitMap
        ArrayList<Byte>bytes = new ArrayList<Byte>();

        //file Header of b and m casted to bytes and added to arrayList of bytes
        bytes.add((byte)'B');
        bytes.add((byte)'M');

        //Setting the fileSize for the header file 
        int fileSize = 54+((bitMap.get(0).size()+getPadding())*bitMap.size());
        bytes.add((byte)fileSize);
        bytes.add((byte)(fileSize>>8));
        bytes.add((byte)(fileSize>>16));
        bytes.add((byte)(fileSize>>24));

        bytes.add((byte)0);
        bytes.add((byte)0);

        bytes.add((byte)0);
        bytes.add((byte)0);

        bytes.add((byte)54);
        bytes.add((byte)0);
        bytes.add((byte)0);
        bytes.add((byte)0);


        //Start of the InfoHeader
        bytes.add((byte)40);
        bytes.add((byte)0);
        bytes.add((byte)0);
        bytes.add((byte)0); 

        int width = bitMap.get(0).size();
        bytes.add((byte)width);
        bytes.add((byte)(width>>8));
        bytes.add((byte)(width>>16));
        bytes.add((byte)(width>>24));

        int height = bitMap.size();
        bytes.add((byte)height);
        bytes.add((byte)(height>>8));
        bytes.add((byte)(height>>16));
        bytes.add((byte)(height>>24));

        bytes.add((byte)1);
        bytes.add((byte)0);

        bytes.add((byte)24);
        bytes.add((byte)0);

        bytes.add((byte)0);
        bytes.add((byte)0);
        bytes.add((byte)0);
        bytes.add((byte)0);

        int rawPixelDataSize = fileSize -54;
        bytes.add((byte)rawPixelDataSize);
        bytes.add((byte)(rawPixelDataSize>>8));
        bytes.add((byte)(rawPixelDataSize>>16));
        bytes.add((byte)(rawPixelDataSize>>24));

        //last 16 bytes
        bytes.add((byte)0);
        bytes.add((byte)0);
        bytes.add((byte)0);
        bytes.add((byte)0);
        
        bytes.add((byte)0);
        bytes.add((byte)0);
        bytes.add((byte)0);
        bytes.add((byte)0);

        bytes.add((byte)0);
        bytes.add((byte)0);
        bytes.add((byte)0);
        bytes.add((byte)0);

        bytes.add((byte)0);
        bytes.add((byte)0);
        bytes.add((byte)0);
        bytes.add((byte)0);
        
        //start of pixel array data 
        for(int row = bitMap.size()-1; row >= 0; row--)
        {
            for(int col = 0; col < bitMap.get(row).size(); col++)
            {
               bytes.add((byte)(bitMap.get(row).get(col).getBlue()));
               bytes.add((byte)(bitMap.get(row).get(col).getGreen()));
               bytes.add((byte)(bitMap.get(row).get(col).getRed()));
            }
            
            for(int i = 0; i < getPadding(); i++)
            {
                bytes.add((byte)0);
            }
        }

        //Writing to file and handles exceptions to make sure that that it can be done
        try
        {
            FileOutputStream file = new FileOutputStream(path);

            for(byte b : bytes)
            {
                try
                {
                    file.write(b);
                }
                catch(IOException innerFail)
                {
                    System.out.println("The file failed to be made ");
                }
            }
            try
            {
                file.close();
            }
            catch(IOException closeFail)
            {
                System.out.println("The file was not able to close ");
            }
        }
        catch(FileNotFoundException fail)
        {
            System.out.println("The file was not able to be written");
        }

    }
    

}