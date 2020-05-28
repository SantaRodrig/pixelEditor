import javax.swing.JOptionPane;
public class Driver
{
       public static void main(String args[])
    {
        System.out.println("Pixel Editor");
        
        int rows = 0;
        int cols = 0;
        try
        {
            while(rows < 1 && cols < 1)
            {
                String r = JOptionPane.showInputDialog(null, "Enter the amount of rows: ");
                rows = Integer.parseInt(r);

                String c = JOptionPane.showInputDialog(null, "Enter the amount of colums: ");
                cols = Integer.parseInt(c);
            }
            PixelEditorGUI pf = new PixelEditorGUI(rows,cols);
        }
        catch(NumberFormatException notNum)
        {
            System.out.println("Input is not valid: "+ notNum );
        }
        
    }
}