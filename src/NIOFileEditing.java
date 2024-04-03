import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static java.nio.file.StandardOpenOption.CREATE;


/**
 *
 * @author pylemd
 * 
 * Use the thread safe NIO IO library to read/write a file
 */

public class NIOFileEditing {


    public static ArrayList<String> openFile() {

        ArrayList<String> list = new ArrayList<>();

        JFileChooser chooser = new JFileChooser();
        File selectedFile;
        
       try {
           // uses a fixed known path:
           //  Path file = Paths.get("c:\\My Documents\\data.txt");

           // use the toolkit to get the current working directory of the IDE
           // Not sure if the toolkit is thread safe...
           File workingDirectory = new File(System.getProperty("user.dir"));

           // Typically, we want the user to pick the file, so we use a file chooser
           // kind of ugly code to make the chooser work with NIO.
           // Because the chooser is part of Swing it should be thread safe.
           chooser.setCurrentDirectory(workingDirectory);
           // Using the chooser adds some complexity to the code.
           // we have to code the complete program within the conditional return of
           // the filechooser because the user can close it without picking a file

           if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
               selectedFile = chooser.getSelectedFile();

               Path file = selectedFile.toPath();
               String fileName = String.valueOf(file.getFileName());
               list.add(fileName);
               // Typical java pattern of inherited classes
               // we wrap a BufferedWriter around a lower level BufferedOutputStream
               InputStream in =
                       new BufferedInputStream(Files.newInputStream(file, CREATE));
               BufferedReader reader =
                       new BufferedReader(new InputStreamReader(in));

               // Finally we can read the file LOL!

               while (reader.ready()) {
                   list.add(reader.readLine());

               }
               reader.close(); // must close the file to seal it and flush buffer



           } else  // User closed the chooser without selecting a file
           {
               System.out.println("No file selected!!! ... exiting.\nRun the program again and select a file.");
           }
       }
       catch (FileNotFoundException e)
       {
           System.out.println("File not found!!!");
           e.printStackTrace();
       }
       catch (IOException e)
       {
           e.printStackTrace();
       }
       return list;
    }

    public static void writeFile(ArrayList<String> list, String fileName) {


        // Not entirely sure why this works but don't fix what's not broke I guess
        Path file = Paths.get(fileName + ".txt");

        try
        {
            // Typical java pattern of inherited classes
            // we wrap a BufferedWriter around a lower level BufferedOutputStream
            OutputStream out =
                    new BufferedOutputStream(Files.newOutputStream(file, CREATE));
            BufferedWriter writer =
                    new BufferedWriter(new OutputStreamWriter(out));

            // Finally can write the file LOL!

            for(String rec : list)
            {
                writer.write(rec, 0, rec.length());  // stupid syntax for write rec
                // 0 is where to start (1st char) the write
                // rec. length() is how many chars to write (all)
                writer.newLine();  // adds the new line

            }
            writer.close(); // must close the file to seal it and flush buffer
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    
}
