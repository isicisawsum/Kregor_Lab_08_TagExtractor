import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.nio.file.StandardOpenOption.CREATE;


public class FrequencyFrame extends JFrame{
    JPanel mainPnl;
    JPanel scrollArea;
    JPanel buttonArea;
    JScrollPane frequencyScrollLine;
    JLabel title;
    JTextArea frequencyScroll;
    JButton quit;
    JButton save;
    private static Map<String, Integer> frequency = new TreeMap<String, Integer>(); //this and words will auto-sort all of the words taken in
    private static Set<String> stopWords = readStopwords();
    private static LinkedList<String> words = new LinkedList<>();
    private static String totalText = "";

    public FrequencyFrame(){
        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());

        title = new JLabel("Tag Extractor!", JLabel.CENTER);
        title.setFont(new Font("Impact", Font.PLAIN, 36));
        mainPnl.add(title);

        createArea();
        mainPnl.add(scrollArea, BorderLayout.CENTER);

        createButtonArea();
        mainPnl.add(buttonArea, BorderLayout.SOUTH);

        add(mainPnl);
        setSize(900,950);
        setLocation(0,0);
        setTitle("Tag Extractor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void createArea(){
        scrollArea = new JPanel();
        scrollArea.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Tags:"));

        frequencyScroll = new JTextArea(50, 40);
        frequencyScroll.setEditable(false);  //makes the text area non-editable

        frequencyScrollLine = new JScrollPane(frequencyScroll);
        frequencyScrollLine.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  // Ensure the scrollbar always shows


        scrollArea.add(frequencyScrollLine);

    }
    private void createButtonArea(){
        buttonArea = new JPanel();
        buttonArea.setLayout(new GridLayout(1,2));

        save = new JButton();
        save.setText("SAVE");
        save.addActionListener((ActionEvent ae) -> save());
        mainPnl.add(save, BorderLayout.SOUTH);
        quit = new JButton();
        quit.setText("QUIT");
        quit.addActionListener((ActionEvent ae) -> System.exit(0));
        mainPnl.add(quit, BorderLayout.SOUTH);

        buttonArea.add(save);
        buttonArea.add(quit);
    }

    public void getFile(){
        JFileChooser chooser = new JFileChooser();
        File selectedFile;
        String rec = "";
        //String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

        String total = "";


        try {
            Path workingDirectory = new File(System.getProperty("user.dir")).toPath();

            chooser.setCurrentDirectory(workingDirectory.toFile());

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
                Path file = selectedFile.toPath();

                InputStream in =
                        new BufferedInputStream(Files.newInputStream(file, CREATE));
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(in));



                while (reader.ready()) {
                    rec = reader.readLine();
                    //System.out.println(rec);

                    rec = rec.toLowerCase();
                    rec = rec.replaceAll("[^a-z ]", "");

                    String[] lineWords = rec.split("\\s+");

                    for (String word : lineWords) {
                        if (!stopWords.contains(word)) {
                            words.add(word);
                            frequency.put(word, frequency.getOrDefault(word, 0) + 1); //adds 1 to the frequency every time it finds the word
                        }
                    }

                    /*for(String word: lineWords){
                        for(String stopWord: stopWords){
                            if (word.equals(stopWord)){
                                break;
                            }
                            else{
                                words.add(word);
                            }
                        }
                    }*/

                }

                reader.close(); // must close the file to seal it and flush buffer
                System.out.println("\n\nData file read!");


                for (Map.Entry<String, Integer> entry : frequency.entrySet()) {
                    totalText += entry.getKey() + ": " + entry.getValue() + "\n";
                }
                frequencyScroll.setText(totalText);

                //now opening the swing frame

            } else  // User closed the chooser without selecting a file
            {
                System.out.println("No file selected!!! ... exiting");
                System.exit(0);
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found!!!");
            e.printStackTrace(); //the file user entered was not found
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static Set<String> readStopwords(){ //reads the stop words file, returns a set and trims the space at the end
        Set<String> stopWords = new TreeSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("src/StopWords"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stopWords.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stopWords;
    }

    private void save(){ //handy code from Programming I
        try {
            JFileChooser chooser = new JFileChooser(); //making new file chooser
            int result = chooser.showSaveDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile(); //gets the selected file
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                oos.writeObject(totalText); //writes what is in the array table to a file

                oos.close();
                fos.close(); //closes FileOutputStream and ObjectObputStream

                System.out.println("Frequency saved to file successfully."); //telling user file sucessfully save
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
