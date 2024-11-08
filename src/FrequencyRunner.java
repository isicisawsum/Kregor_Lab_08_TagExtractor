import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.nio.file.StandardOpenOption.CREATE;

public class FrequencyRunner {
    public static void main(String[] args) {
        FrequencyFrame frame = new FrequencyFrame();
        frame.setLocationRelativeTo(null);
        frame.getFile();  // Calling getFile on the frame instance

    }
}
