
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Maxi
 */
public class TXTReader {

    static BufferedReader br;

    public static Internal readTXT(String path) throws FileNotFoundException, IOException {

        br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "ISO-8859-1"));

        String line;
        line = br.readLine();
        if (line.equals("Anmerkungen: \"")) {
            line = br.readLine();
        }
        while (!line.endsWith("\"")) {
            line = br.readLine();
        }
        line = br.readLine();
        if (!line.startsWith("Autor: ")) {
            throw new RuntimeException("Format Error bei Autor");
        }
        String author = line.substring(7);
        System.out.println("Autor: " + author);

        line = br.readLine();
        if (!line.startsWith("Titel: ")) {
            throw new RuntimeException("Format Error bei Titel");
        }
        String title = line.substring(7);
        System.out.println("title: " + title);

        line = br.readLine();
        if (!line.startsWith("Bild: ")) {
            throw new RuntimeException("Format Error bei Bild");
        }
        String picturePath = line.substring(6);
        System.out.println("picturePath: " + picturePath);

        line = br.readLine();
        if (!line.startsWith("Musik: ")) {
            throw new RuntimeException("Format Error bei musik");
        }
        String musicPath = line.substring(7);
        System.out.println("musicPath: " + musicPath);

        ArrayList<String> story = new ArrayList();
        line = br.readLine();
        if (!line.equals("@")) {
            throw new RuntimeException("Format Error bei story");
        }
        line = br.readLine();
        while (!line.equals("@")) {
            if (!line.equals("")) {
                story.add(line);
                System.out.println("story: " + line);
            }
            line = br.readLine();
        }

        ArrayList<Decision> decisions = new ArrayList();
        line = br.readLine();
        while (line != null) {
            if (!line.startsWith("E")) {
                throw new RuntimeException("Format Error bei decision");
            }
            line = line.substring(4);
            String[] temp = line.split(">");
            try {
                System.out.println("decisions: " + temp[0] + " ; " + temp[1]);
                decisions.add(new Decision(temp[0], temp[1]));
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new RuntimeException("(Jakob; bekannt) ArrayIndexOutOfBoundsException bei decisions; ist höchst wahrscheinlich eine entscheidung bei der einträge fehlen");
            }
            line = br.readLine();
        }

        return new Internal(author, title, picturePath, musicPath, story, decisions);
    }

    /*
    public static void main(String[] args) throws FileNotFoundException, IOException {
        readTXT("c:/Users/Jakob/Dropbox/Alex und Bö Hochzeit/Maxi/smT.txt");
    }
     */
}
