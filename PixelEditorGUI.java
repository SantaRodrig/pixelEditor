import javax.swing.*;
//import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
//import javax.swing.JCheckBox;
//import javax.swing.JComponent;
import javax.swing.filechooser.*;

import java.awt.*;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

//import javax.image.IO;
import java.util.*;
import java.nio.file.*;
import java.io.*;

public class PixelEditorGUI extends JFrame 
{
    private Icon icon;

    private JPanel rightSide;
    private JPanel buttonPanel;
    private JPanel bottom;
    private JPanel currColor;
    private JPanel recentsLeftSide;

    private JLabel currColorText;
    private JLabel redLabel;
    private JLabel redValLabel;
    private JLabel greenLabel;
    private JLabel greenValLabel;
    private JLabel blueLabel;
    private JLabel blueValLabel;
    private JLabel recentLabel;

    private JSlider redSlider;
    private JSlider greenSlider;
    private JSlider blueSlider;

    private Queue<Color> lastFiveColors;

    private JButton saveButton;
    private JFileChooser fileChooser;
    //Make button, open up jfile chooser, pick where to save give it name, then call icons save bitmap 
    private JCheckBox checkBox; 
    private JDialog advancedWindow;
    private JTextField newRows;
    private JTextField newCols;
    private JButton advancedButton;

    private JButton upLoadButton;

    private int startX;
    private int startY;

    private ArrayList<JButton>colorsButton;

    private ArrayList<ArrayList<PixelButton>>allButtons;


    public PixelEditorGUI(int rows, int cols)
    {
        super("Pixel Editor");
        
        icon = new Icon(rows,cols);
        setLayout(new BorderLayout());

        //Making the left panel side to have the last five used colors
        recentsLeftSide = new JPanel();
        recentsLeftSide.setLayout(new GridLayout(7,1));

        recentLabel = new JLabel();
        recentLabel.setText("Recent Colors: ");

        //Making the right side to have the current color and
        //the queue of most recent colors
        rightSide = new JPanel();
        rightSide.setLayout(new GridLayout(2,1));

        //Want to make the panel that sets the Grid
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(rows, cols));

        //Want to make labels for each of the colors
        redLabel = new JLabel();
        redLabel.setText("Red: ");
        redValLabel = new JLabel();
        redValLabel.setText("255");
        

        greenLabel = new JLabel();
        greenLabel.setText("Green: ");
        greenValLabel = new JLabel();
        greenValLabel.setText("255");

        blueLabel = new JLabel();
        blueLabel.setText("Blue: ");
        blueValLabel = new JLabel();
        blueValLabel.setText("255");

        //Setting up the area for the contents under the gride of pixels
        bottom = new JPanel();
        bottom.setLayout(new GridLayout(4,3));
        
        //Set up red color slider
        final int minVal = 0;
        final int maxVal = 255;
        redSlider = new JSlider(JSlider.HORIZONTAL, minVal, maxVal, maxVal);
        
        //Set up green color slider
        greenSlider = new JSlider(JSlider.HORIZONTAL, minVal, maxVal, maxVal); 

        //Set up blue color slider 
        blueSlider = new JSlider(JSlider.HORIZONTAL, minVal, maxVal, maxVal);
        
        //Set up the right side
        currColorText = new JLabel();
        currColorText.setText("Current Color: ");

        //The current color should have been saved to the panel as well
        currColor = new JPanel();
        currColor.setBackground(new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue()));
       
        redSlider.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent event)
            {
                currColor.setBackground(new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue()));
                redValLabel.setText(Integer.toString(redSlider.getValue()));
            }
        });

        greenSlider.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent event)
            {
                currColor.setBackground(new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue()));
                greenValLabel.setText(Integer.toString(greenSlider.getValue()));
            }
        });

        blueSlider.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent event)
            {
                currColor.setBackground(new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue()));
                blueValLabel.setText(Integer.toString(blueSlider.getValue()));
            }
        });


        recentsLeftSide.add(recentLabel);
        //ArrayList<JButton>colorsButton  = new ArrayList<JButton>();
        colorsButton = new ArrayList<JButton>();
        for(int i = 0; i < 5; i++)
        {
            JButton cButton = new JButton();
            cButton.setBackground(new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue()));
            colorsButton.add(cButton);
            cButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    redSlider.setValue(cButton.getBackground().getRed());
                    greenSlider.setValue(cButton.getBackground().getGreen());
                    blueSlider.setValue(cButton.getBackground().getBlue()); 
                }
            });
            recentsLeftSide.add(colorsButton.get(i));
        }
        
       
        advancedButton = new JButton("Commit settings");
        advancedButton.setBackground(new Color(255,255,255));
        advancedButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                advancedSetColors(rows, cols);
            }

        });

        newRows = new JTextField("How many Rows would you like edited at once" );
        newCols = new JTextField("How many Colums would you like edited at once");

        JPanel advancedSelector = new JPanel();
        advancedSelector.setLayout(new GridLayout(5,2));
        advancedSelector.add(new JLabel("Rows:"));
        advancedSelector.add(newRows);
        advancedSelector.add(new JLabel("Colums:"));
        advancedSelector.add(newCols);
        advancedSelector.add(advancedButton);

        upLoadButton = new JButton("Upload a BitMap File");
        upLoadButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                useBitMap();
            }
        });
        JPanel advancedUploader = new JPanel();
        advancedUploader.setLayout(new GridLayout(1,1));
        advancedUploader.add(upLoadButton);
        

        JTabbedPane settings = new JTabbedPane();
        settings.addTab("Advanced Pixel Editor", advancedSelector);
        settings.addTab("Upload BMP",advancedUploader);
        
        
        advancedWindow = new JDialog(PixelEditorGUI.this, "Advanced Settings");
        advancedWindow.add(settings);
        
        advancedWindow.setSize(400,400);
        advancedWindow.setLocation(400,400);
        advancedWindow.setVisible(false);

        //for holding the last 5 used colors use a queue
        lastFiveColors = new LinkedList<>();
        for(int i =0; i < 5; i++)
        {
            lastFiveColors.add(new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue()));
        }
        
        allButtons = new ArrayList<ArrayList<PixelButton>>();
        for(int i = 0; i < rows; i++)
        {
            allButtons.add(new ArrayList<PixelButton>());
            for(int j = 0; j<cols;j++)
            {
                //allButtons.get(i).add(new PixelButton());
                PixelButton b = new PixelButton(i, j);
                b.setBackground(new Color(icon.getPixel(i,j).getRed(),icon.getPixel(i,j).getGreen(),icon.getPixel(i,j).getBlue()));

                //Adding the buttons onto the arraylist of arraylist 
                allButtons.get(i).add(b);
                
                //Maybe this did it 
                //allButtons.get(i).get(j).setBackground(new Color(icon.getPixel(i,j).getRed(),icon.getPixel(i,j).getGreen(),icon.getPixel(i,j).getBlue()));

                //The actionlistener for when a button is pressed
                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        //if size of cursure is equal to one by one do this 
                        int x =((PixelButton) e.getSource()).getXCoord();
                        int y =((PixelButton) e.getSource()).getYCoord();

                        startX = x;
                        startY = y;
                        if(checkBox.isSelected()==true)
                        {
                            advancedDialogue();

                        }
                        else
                        {
                                                        
                            //In here I set the buttons to the what sliders are at the moment
                            icon.setPixel(x,y,redSlider.getValue(),greenSlider.getValue(),blueSlider.getValue()); 
                            b.setBackground(new Color(icon.getPixel(x,y).getRed(),icon.getPixel(x,y).getGreen(),icon.getPixel(x,y).getBlue()));
                            
                            boolean colorThere = false;
                            //making check to see if colors of sliders the last five colors
                            for(Color c: lastFiveColors)
                            {
                                if(c.getRed() == redSlider.getValue() && c.getGreen() == greenSlider.getValue() && c.getBlue() == blueSlider.getValue())
                                {
                                    colorThere = true;
                                }
                            }

                            if(colorThere != true)
                            {
                                lastFiveColors.remove();
                                lastFiveColors.add(new Color(icon.getPixel(x,y).getRed(),icon.getPixel(x,y).getGreen(),icon.getPixel(x,y).getBlue()));
                            }

                            for(int i=0; i<5;i++)
                            {
                                colorsButton.get(i).setBackground((Color)lastFiveColors.toArray()[i]);
                            }


                            System.out.println("Pixel Button clicked at " + x + " " + y);

                            //else change the color 0x0 
                            //use loop to have the bottons x+i y+j
                        }

                        
                        

                    }
                });

                buttonPanel.add(b);
                buttonPanel.add(bottom);
            }
        }

                //Set the saveButton to have text and changed the color
                saveButton = new JButton("Save BitMap");
                saveButton.setBackground(new Color(255,255,255));
                //Now will have the action listener to save it when user wants to
                saveButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        fileChooser = new JFileChooser();
                        fileChooser.setDialogTitle("Specify a file to save");
        
                        int userSelection = fileChooser.showOpenDialog(PixelEditorGUI.this);
        
                        if(userSelection == JFileChooser.APPROVE_OPTION)
                        {
                            File fileToSave = fileChooser.getSelectedFile();
                            String file = fileToSave.getAbsolutePath();
                            icon.makeBitMapImage(file);
                        }
                    }
        
                });


        checkBox = new JCheckBox("Advanced settngs", false);
        recentsLeftSide.add(checkBox);
        
        bottom.add(redLabel);
        bottom.add(redValLabel);
        bottom.add(redSlider);

        bottom.add(greenLabel);
        bottom.add(greenValLabel);
        bottom.add(greenSlider);

        bottom.add(blueLabel);
        bottom.add(blueValLabel);
        bottom.add(blueSlider);

        bottom.add(saveButton);

        rightSide.add(currColorText);
        rightSide.add(currColor);
        


        add(buttonPanel,BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
        add(rightSide, BorderLayout.EAST);
        add(recentsLeftSide, BorderLayout.WEST);
        

        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(200, 200);
        setVisible(true);
    }

    public void advancedDialogue()
    {
        advancedWindow.setVisible(true);
        
    }

    public void advancedSetColors(int rows, int cols)
    {
       
        //get where to start (the row and col that was originally pressed)

        int nRow = Integer.parseInt(newRows.getText());
        int nCol = Integer.parseInt(newCols.getText());


        if(startX + nRow >= rows)
        {
            nRow = rows - startX;
        }
        if(startY + nCol >= cols)
        {
            nCol = cols - startY;
        }

        //start i and j were the cordinates of the jButton of were to start 
        for(int i = startX; i<startX+nRow;i++)
        {
            for(int j = startY; j < startY+nCol; j++)
            {  
                allButtons.get(i).get(j).setBackground(new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue()));
                icon.setPixel(i,j,redSlider.getValue(),greenSlider.getValue(),blueSlider.getValue());
            }
        }

        boolean colorThere = false;
        //making check to see if colors of sliders the last five colors
        for(Color c: lastFiveColors)
        {
            if(c.getRed() == redSlider.getValue() && c.getGreen() == greenSlider.getValue() && c.getBlue() == blueSlider.getValue())
            {
                colorThere = true;
            }
        }

        if(colorThere != true)
        {
            lastFiveColors.remove();
            lastFiveColors.add(new Color(redSlider.getValue(),greenSlider.getValue(),blueSlider.getValue()));
        }

        for(int i=0; i<5;i++)
        {
            colorsButton.get(i).setBackground((Color)lastFiveColors.toArray()[i]);
        }

    }

    public void useBitMap()
    {
        int height; 
        int width;

        JFileChooser loadFileChooser = new JFileChooser(".");
        loadFileChooser.setDialogTitle("Select bitMap to load ");


        int userSelection = loadFileChooser.showOpenDialog(PixelEditorGUI.this);

        if(userSelection == JFileChooser.APPROVE_OPTION)
        {
            File fileToLoad = loadFileChooser.getSelectedFile();
            
            try
            {
                Path path = Paths.get(fileToLoad.getAbsolutePath());
                byte [] data = Files.readAllBytes(path);
                
                width = ((int)data[18])+((int)data[19]<<8)+((int)data[20]<<16)+((int)data[21]<<24);
                height= ((int)data[22])+((int)data[23]<<8)+((int)data[24]<<16)+((int)data[24]<<24);

                int rowSize= width*3+getPadding(width);
                int startByte = 54;
                int currRow = height-1; 
                int currCol = 0;
                int rowByteCounter=0;

                //System.out.println("Data length: "+data.length);
                for(int i = startByte; i < data.length; i++)
                {
                    
                    if( rowByteCounter < width*3)
                    {
                        int tempB = (data[i] & 0xFF);
                        i++;
                        rowByteCounter++;
                        int tempG = (data[i] & 0xFF);
                        i++;
                        rowByteCounter++;
                        int tempR = (data[i] & 0xFF);

                        if(startX+currRow < allButtons.size()&&startY+currCol < allButtons.get(0).size())
                        {
                            allButtons.get(startX+currRow).get(startY+currCol).setBackground(new Color(tempR,tempG,tempB));
                            //System.out.println("i: "+i);
                            //System.out.println("CurrRow: "+currRow);
                            //System.out.println("CurrCol: "+currCol);
                            icon.setPixel(startX+currRow, startY+currCol,tempR, tempG, tempB);
                            currCol++;
                        }

                    }
                    rowByteCounter++;
                    if(rowByteCounter==rowSize)
                    {
                        currRow--;
                        currCol=0;
                        rowByteCounter=0;
                    }
                   

                }
            }
            catch(IOException readFail)
            {
                System.out.println("The file had a problem being read");
            }
        }
    }

    private int getPadding(int width)
    {
        int padding =0;

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

}
