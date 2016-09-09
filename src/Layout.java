
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

/**
 *
 * @author Andi
 */
//TODO: Get relative path to stories from internal or something, fix audio[play works, loop doesnt], get sb to fix umlauts during loading of files
public class Layout {

    Main main;

    JFrame frame;
    myPanel panel;
    BufferStrategy bufferStrat;

    Internal internal;
    String pathToStories = "res/Stories/";

    BufferedImage background;
    BufferedImage textBox;

    int windowSizeX = 1280;
    int windowSizeY = 720;

    AudioStream audiostream;

    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private Cursor cursor;

    public Layout(Main main) {
        this.main = main;

        init();
    }

    public void initCursor() {
        try {
            cursor = Toolkit.getDefaultToolkit().createCustomCursor(ImageIO.read(new File("res/Cursor.png")), new Point(16, 16), "CustomCursor");
        } catch (IOException e) {
            System.err.println("Couldn't load cursor!");
        }
    }

    public void setBackground(String path) {
        String defaultPath = "res/background.png";

        try {
            if (path.equals(" ") || path.equals("")) {
                try {
                    background = ImageIO.read(new File(defaultPath));
                } catch (Exception e52) {
                    System.err.println("Couldn't even load default background, RIP");
                }
            } else {
                background = ImageIO.read(new File(path));
            }

        } catch (IOException e) {
            System.err.println("Couldn't load background at " + path + "! Loading default background at " + defaultPath + " instead!");
            try {
                background = ImageIO.read(new File(defaultPath));
            } catch (Exception e2) {
                System.err.println("Couldn't even load default background, RIP");
            }
        }
    }

    private void stopMusic() {
        if (this.audiostream != null) {
            AudioPlayer.player.stop(this.audiostream);
        }
    }

    private void playAudioFile(String path) {
        stopMusic(); //stops previous music if it exists

        if (path.equals(" ") || path.equals("")) {
            return;
        }

        String soundFile = path;
        try {
            InputStream in = new FileInputStream(soundFile);

            // create an audiostream from the inputstream
            AudioStream audioStream = new AudioStream(in);

            // play the audio clip with the audioplayer class
            AudioPlayer.player.start(audioStream);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Something with the audio failed; check filename & if file is really there: " + path);
        }
    }

    private void loopAudioFile(String path) {
        stopMusic(); //stops previous music if it exists

        String soundFile = path;
        try {
            InputStream in = new FileInputStream(soundFile);

            // create an audiostream from the inputstream, turn into audiodata and then into continuous stream for looping
            AudioStream audioStream = new AudioStream(in);
            AudioData audiodata = audioStream.getData(); //hier bugts
            System.out.println("x");
            ContinuousAudioDataStream loopMusic = new ContinuousAudioDataStream(audiodata);
            System.out.println("x");

            // play the audio clip with the audioplayer class
            AudioPlayer.player.start(loopMusic);
            this.audiostream = audioStream;

            // AudioPlayer.player.stop(audioStream);  // stops the audiostream sound
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Something with the audio failed; check filename & if file is really there: " + path);

            try {
                InputStream in = new FileInputStream("res/doublebass.wav");
                AudioStream audioStream = new AudioStream(in);
                AudioData audiodata = audioStream.getData();
                ContinuousAudioDataStream loopMusic = new ContinuousAudioDataStream(audiodata);
                AudioPlayer.player.start(loopMusic);
                this.audiostream = audioStream;
            } catch (Exception e2) {
                System.err.println("Can't even load default background music :(");
            }
        }
    }

    public void init() {
        frame = new JFrame();
        frame.setSize(windowSizeX, windowSizeY);
        frame.setLocation(screenSize.width / 2 - frame.getWidth() / 2, screenSize.height / 2 - frame.getHeight() / 2);
        //frame.setUndecorated(true);

        try {
            textBox = ImageIO.read(new File("res/Textbox.png"));
        } catch (IOException e) {
            System.err.println("Couldn't load Textbox!");
        }

        setBackground("res/background.png");
        initCursor();
        if (cursor != null) {
            frame.setCursor(cursor);
        }

        panel = new myPanel();
        panel.setIgnoreRepaint(true);

        /* frame.addKeyListener(new PlayerController());
         frame.addMouseListener(MC);
         frame.addMouseMotionListener(MC);*/
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        frame.setVisible(true);

        frame.createBufferStrategy(2);
        bufferStrat = frame.getBufferStrategy();

        //playAudioFile("res/001Cry.wav");
    }

    public void paint(String text) {
        paint(text, true);
    }

    public void paint(String text, boolean showBoxes) {
        Graphics2D graphics = (Graphics2D) panel.getGraphics();

        panel.paintComponent(text + " ", bufferStrat, background, showBoxes);

        try {
            Thread.sleep(30);
        } catch (Exception e) {
        }
    }

    public void run(String text) {
        int i = 0;
        while (true) {
            paint(text);

            try {
                Thread.sleep(30);
            } catch (Exception e) {
            }

            if (panel.clicked && i == 0) {
                text = "hey this is a new text lol";
                panel.clicked = false;

                i++;
            }
            if (panel.clicked && i == 1) {
                String[] newQuestions = {"this is a test question", "So is this", "yep, testing"};
                panel.setQuestions(newQuestions);
                i++;
            }

            if (!panel.selectedAnswer.equals("")) {
                System.out.println(panel.selectedAnswer);
                panel.cleanupQuestions();
            }

            if (!panel.remains.equals("")) {
                text = panel.remains;
            }
        }
    }

    // get main method that calls display with 2 internals to test
    public static void main(String[] args) {
        ArrayList<String> story = new ArrayList<>();
        story.add("this is the first part of the storythis is the first part of "
                + "the storythis is the first part of the storythis is the first "
                + "first part of the storythis is the first part of the storyt                + \"part of the storythis is the first part of the storythis is the \"\n"
                + "his is "
                + "the first part of the storythis is the first part of the storythis is"
                + " the first part of the storythis is the first part of the storythis is "
                + "the first part of the storythis is the first part of the storythis is the fir"
                + "st part of the storythis is the first part of the storythis is the first part"
                + " of the storythis is the first part of the storythis is the first part of the sto"
                + "part of the storythis is the first part of the storythis is the "
                + "first part of the storythis is the first part of the storythis is "
                + "the first part of the storythis is the first part of the storythis is"
                + " the first part of the storythis is the first part of the storythis is "
                + "the first part of the storythis is the first part of the storythis is the fir"
                + "st part of the storythis is the first part of the storythis is the first part"
                + " of the storythis is the first part of the storythis is the first part of the sto"
                + "rythis is the first part of the story");
        story.add("This be te segund bart :DDD");
        ArrayList<Decision> decisions = new ArrayList<>();
        decisions.add(new Decision("Pick me pls I'm the next story!!", "res/test.txt"));
//muhahaha //fancy //shit - Koko 2016
        Internal internal = new Internal("Bertfred", "dope story", "res/background.png", "res/doublebass.wav", story, decisions);
        // String author, String title, String picturePath, String musicPath, ArrayList<String> story, ArrayList<Decision> decisions

        Main main = new Main();
        Layout layout = main.layout;
        // layout.init();

        layout.displayQuestion("Leo", "Test");
        layout.displayQuestion("Johannes", "Test2");

        layout.display(internal);
    }

    public void display(Internal internal) {
        this.internal = internal;
        panel.clicked = false;
        //TODO Andi
        //Hab einfach mal alle beschreibenden Kommentare hier rein gepasted

        //zeigt Bild, Text, Sound
        setBackground(internal.getPicturePath());  // Loading default if image path is invalid
        playAudioFile(internal.getMusicPath()); // plays vivaldi as default if path invalid
        // TODO: load sound here

        //Triggers on Button press nextStep(String path)  //path aus der entscheidung
        /**
         * > bild zeigen, klicken, textbox mit semitransparenz(auf bild), mehr
         * klicks, mehr text > Buttons am Ende erste zeigen > Skip Button, damit
         * die Entscheidungsbuttons erscheinen
         */
        //display image only until click
        while (true) {
            paint(" ", false);

            if (panel.clicked) {
                panel.clicked = false;
                break;
            }

            try {
                Thread.sleep(30);
            } catch (Exception e) {
            }
        }

        while (true) {

            for (int x = 0; x < internal.getStory().size(); x++) {
                paint(internal.getStory().get(x));

                boolean done = false;
                while (!done) {
                    String textToDraw = panel.currentlyDrawnText;

                    if (panel.clicked) {
                        if (!panel.remains.equals("")) {
                            textToDraw = panel.remains;
                        } else if (x == internal.getStory().size() - 1) {
                            ArrayList<Decision> decisions = internal.getDecisions();
                            if (decisions.isEmpty()) {
                                return; // reached the end of the story
                            }

                            String[] questions = new String[decisions.size()];

                            int i = 0;
                            for (Decision decision : decisions) {
                                questions[i] = decision.getDescription();
                                i++;
                            }
                            panel.setQuestions(questions);
                            //call paint component or something 
                        }

                        if (!panel.selectedAnswer.equals("")) {
                            String selectedAnswer = panel.selectedAnswer;
                            panel.cleanupQuestions();

                            //DisplayEndDialogue();
                            main.nextStep(this.pathToStories + getPathToAnswer(selectedAnswer).trim());
                            return;  // we reach this return at the end of the story
                        }

                        panel.clicked = false;
                        done = true;
                    }

                    paint(textToDraw);

                    // dont remove this or things wont work (cuz busy waiting fucks with key presses?)
                    try {
                        Thread.sleep(30);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    public String getPathToAnswer(String answer) {
        for (Decision decision : this.internal.getDecisions()) {
            if (answer.equals(decision.getDescription())) {
                return decision.getDestinationPath();
            }
        }

        System.err.println("Path for answer '" + answer + "' doesn't exist!");
        return "";
    }

    public void DisplayEndDialogue() {
        //TODO Andi
        //pop-up, select either:
        //neustart -> m.nextStep(Intro001.txt)
        //ende -> m.endgame()
        //quiz -> m.startQuiz()

        // TODO: maybe reset panel state first?
        String[] questions = {"Neustart", "Ende", "Quiz"};
        panel.setQuestions(questions);

        boolean done = false;
        while (!done) {
            if (!panel.selectedAnswer.equals("")) {
                String selectedAnswer = panel.selectedAnswer;
                panel.cleanupQuestions();

                if (selectedAnswer.equals("Neustart")) {
                    main.nextStep(Main.storyPath + "Intro01.txt");
                    done=true;
                } else if (selectedAnswer.equals("Ende")) {
                    main.endGame();
                } else if (selectedAnswer.equals("Quiz")) {
                    main.startQuiz();
                    done = true;
                }
            }
            paint(" ");
        }
    }

    public String getRandomElem(String[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    public boolean isInArray(String elem, String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                return false;
            }

            if (array[i].equals(elem)) {
                return true;
            }
        }

        return false;
    }

    public void displayQuestion(String author, String title) {
        //TODO: Select random names from list (except author), then add author. Can't display all names at once.

        String[] possibleAuthors = {"Leo", "Conny", "Dunkle Baronin", "Sonja/Johannes", "Maxi", "Jakob", "Andi", "Linda", "Koko", "Julia"};
        int[] randomAuthors = new int[4];
        String[] selectedAuthors = new String[5];

        for (int i = 0; i < randomAuthors.length; i++) {
            String rndAuth = getRandomElem(possibleAuthors);

            //keep selecting random authors until there's no duplicates and it's not the real author
            while (rndAuth.equals(author) || isInArray(rndAuth, selectedAuthors)) {
                rndAuth = getRandomElem(possibleAuthors);
            }
            selectedAuthors[i] = rndAuth;
        }

        // determine position of real author randomly; move one random position author to pos5 and then real author to their position
        int rnd = new Random().nextInt(selectedAuthors.length - 1);
        selectedAuthors[selectedAuthors.length - 1] = selectedAuthors[rnd];
        selectedAuthors[rnd] = author;

        String textToPaint = "Von wem ist die Geschichte mit Titel " + title + "?";

        while (true) {
            paint(textToPaint);

            if (panel.clicked) {
                panel.clicked = false;
                break;
            }

            try {
                Thread.sleep(30);
            } catch (Exception e) {
            }
        }

        panel.setQuestions(selectedAuthors);

        boolean done = false;
        while (!done) {
            if (!panel.selectedAnswer.equals("")) {
                String selectedAnswer = panel.selectedAnswer;
                panel.cleanupQuestions();

                if (selectedAnswer.equals(author)) {
                    textToPaint = ("Richtig! Die Geschichte war von " + author);
                } else {
                    textToPaint = ("Falsch! Die Geschichte war von " + author);
                }

                done = true;
            }
            paint(textToPaint);
            panel.clicked = false;
        }

        while (!panel.clicked) {
            paint(textToPaint);

            try {
                Thread.sleep(30);
            } catch (Exception e) {
            }
        }

    }

    public void close() {
        //weiß nicht, ob man das braucht; halt Fenster schließen und so. Könnte aber auch überflüssig sein.
    }

}

class myPanel extends JPanel implements MouseMotionListener, MouseListener {

    Font questionFont = new Font("Consolas", Font.PLAIN, 30);
    BufferedImage textBox;
    BufferedImage behindQuestionOverlay;
    BufferedImage questionBox;
    BufferedImage questionBoxHighlight;

    String[] questions = new String[0];
    ArrayList<QuestionBox> questionBoxes = new ArrayList<>();
    String selectedAnswer = "";
    String remains = "";

    String currentlyDrawnText = "";

    public boolean clicked = false;

    private static final Hashtable<TextAttribute, Object> map = new Hashtable<>();

    static {
        map.put(TextAttribute.FAMILY, "Consolas");
        map.put(TextAttribute.SIZE, new Float(30.0));
    }

    public myPanel() {
        super();
        super.setBackground(Color.BLACK);

        try {
            textBox = ImageIO.read(new File("res/textBox.png"));
            questionBox = ImageIO.read(new File("res/questionBox.png"));
            questionBoxHighlight = ImageIO.read(new File("res/questionBoxHighlight.png"));
            behindQuestionOverlay = ImageIO.read(new File("res/behindQuestionOverlay.png"));
        } catch (IOException e) {
            System.err.println("Couldn't load boxes!");
        }

        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }

    private final int drawPosX = 200;
    private final int breakWidth = 1080 - drawPosX;
    private final int startDrawPosY = 70;
    private final int breakHeight = 620;

    public int currentMouseX = 0;
    public int currentMouseY = 0;

    public void paintComponent(String text, BufferStrategy bufferStrat, BufferedImage background) {
        paintComponent(text, bufferStrat, background, true);
    }

    public void paintComponent(String text, BufferStrategy bufferStrat, BufferedImage background, boolean paintBoxes) {
        Graphics2D graphics = (Graphics2D) bufferStrat.getDrawGraphics();
        super.paintComponent(graphics);

        paintBackgroundAndBoxes(graphics, background, paintBoxes);

        configureFont(graphics);
        //AttributedString textAtt = new AttributedString(text, map);
        remains = drawFormattedStringAndReturnRemains(text, graphics);

        //String[] questions = {"this is a test question", "So is this", "yep, testing"};
        drawQuestions(questions, graphics);
        graphics.dispose();
        bufferStrat.show();
    }

    public void setQuestions(String[] questions) {
        this.questions = questions;

        int drawY = 100;
        int drawX = 265;

        int boxW = 750;
        int boxH = 72;

        int index = 0;

        for (String question : questions) {
            this.questionBoxes.add(new QuestionBox(drawX, drawY, boxW, boxH, index));
            drawY += 75;
            index++;
        }
    }

    public void cleanupQuestions() {
        this.questions = new String[0];
        this.questionBoxes = new ArrayList<>();
        this.selectedAnswer = "";
    }

    private void drawQuestions(String[] questions, Graphics2D graphics) {
        if (!questionBoxes.isEmpty()) {
            graphics.drawImage(behindQuestionOverlay, 0, 0, null);
        }

        for (QuestionBox questionbox : questionBoxes) {
            // draw highlighted box if mouse is hovering over it, otherwise draw regular box
            if (mouseInRect(questionbox.x, questionbox.y, questionbox.w, questionbox.h)) {
                graphics.drawImage(questionBoxHighlight, questionbox.x, questionbox.y, null);
            } else {
                graphics.drawImage(questionBox, questionbox.x, questionbox.y, null);
            }

            String textToDraw = questions[questionbox.index];

            int textSize = getTextSize(textToDraw, graphics);
            configureFont(graphics); //sets font back to 30

            int fontSize = 25;
            while (textSize > 1080 - 325) {
                Font newFont = new Font("Consolas", Font.PLAIN, fontSize);
                graphics.setFont(newFont);
                graphics.setRenderingHint(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

                textSize = getTextSize(textToDraw, graphics, newFont);
                fontSize -= 3;

                if (fontSize < 5) {
                    System.exit(0);
                    break;
                }
            }

            graphics.drawString(textToDraw, 275, questionbox.y + 45);
        }
    }

    public boolean mouseInRect(int x, int y, int w, int h) {
        return (currentMouseX > x && currentMouseX < x + w && currentMouseY > y && currentMouseY < y + h);
    }

    public boolean mouseInRect(QuestionBox qbox) {
        return mouseInRect(qbox.x, qbox.y, qbox.w, qbox.h);
    }

    private String drawFormattedStringAndReturnRemains(String stringText, Graphics2D graphics) {
        currentlyDrawnText = stringText;

        AttributedString text = new AttributedString(stringText, map);

        AttributedCharacterIterator paragraph = text.getIterator();
        int paragraphStart = paragraph.getBeginIndex();
        int paragraphEnd = paragraph.getEndIndex();
        FontRenderContext frc = graphics.getFontRenderContext();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);

        float drawPosY = startDrawPosY;

        // Set position to the index of the first
        // character in the paragraph.
        lineMeasurer.setPosition(paragraphStart);

        // Get lines from until the entire paragraph
        // has been displayed.
        while (lineMeasurer.getPosition() < paragraphEnd) {

            TextLayout layout = lineMeasurer.nextLayout(breakWidth);

            // Move y-coordinate by the ascent of the layout
            drawPosY += layout.getAscent();

            // Draw the TextLayout at (drawPosX,drawPosY).
            // System.out.println(drawPosX + " / " + drawPosY);
            layout.draw(graphics, drawPosX, drawPosY);

            // Move y-coordinate in preparation for next
            // layout.
            drawPosY += layout.getDescent() + layout.getLeading();

            if (drawPosY > breakHeight) {
                String remainingText = stringText.substring(lineMeasurer.getPosition());
                //  System.out.println(remainingText);
                return remainingText;
            }
        }
        return "";
    }

    private void paintBackgroundAndBoxes(Graphics2D graphics, BufferedImage background) {
        paintBackgroundAndBoxes(graphics, background, true);
    }

    private void paintBackgroundAndBoxes(Graphics2D graphics, BufferedImage background, boolean paintBoxes) {
        graphics.drawImage(background, 0, 0, 1280, 720, null);
        if (paintBoxes) {
            graphics.drawImage(textBox, 150, 50, null);
        }
    }

    private void configureFont(Graphics2D graphics) {
        graphics.setFont(questionFont);
        graphics.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
    }

    private int getTextSize(String text, Graphics2D graphics) {
        FontMetrics metrics = graphics.getFontMetrics(questionFont);

        return metrics.stringWidth(text) + 2;
    }

    private int getTextSize(String text, Graphics2D graphics, Font font) {
        FontMetrics metrics = graphics.getFontMetrics(font);

        return metrics.stringWidth(text) + 2;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        currentMouseX = e.getX();
        currentMouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentMouseX = e.getX();
        currentMouseY = e.getY();
    }

    public void mouseReleased(MouseEvent e) {
        if (questions.length > 0) {
            for (QuestionBox questBox : questionBoxes) {
                if (mouseInRect(questBox)) {
                    selectedAnswer = questions[questBox.index];
                    //System.out.println(questions[questBox.index]);
                    clicked = true;
                }
            }
        } else {
            clicked = true;
        }
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }
}

class QuestionBox {

    int x, y, w, h, index;

    public QuestionBox(int x, int y, int w, int h, int index) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.index = index;
    }
}
