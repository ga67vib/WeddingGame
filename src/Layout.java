
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
import javax.imageio.ImageIO;
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
public class Layout {

    Main main;

    JFrame frame;
    myPanel panel;
    BufferStrategy bufferStrat;

    BufferedImage background;
    BufferedImage textBox;

    int windowSizeX = 1280;
    int windowSizeY = 720;

    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private Cursor cursor;

    public Layout(Main main) {
        this.main = main;
    }

    public void initCursor() {
        try {
            cursor = Toolkit.getDefaultToolkit().createCustomCursor(ImageIO.read(new File("res/Cursor.png")), new Point(16, 16), "CustomCursor");
        } catch (IOException e) {
            System.err.println("Couldn't load cursor!");
        }
    }

    public void setBackground(String path) {
        try {
            background = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("Couldn't load background!");
        }
    }

    private void playAudioFile(String path) {
        String soundFile = path;
        try {
            InputStream in = new FileInputStream(soundFile);

            // create an audiostream from the inputstream, turn into audiodata and then into continuous stream for looping
            AudioStream audioStream = new AudioStream(in);
            AudioData audiodata = audioStream.getData();
            ContinuousAudioDataStream loopMusic = new ContinuousAudioDataStream(audiodata);

            // play the audio clip with the audioplayer class
            AudioPlayer.player.start(loopMusic);

            // AudioPlayer.player.stop(audioStream);  // stops the audiostream sound
        } catch (Exception e) {
            System.err.println("Something with the audio failed; check filename & if file is really there: " + path);
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
        Graphics2D graphics = (Graphics2D) panel.getGraphics();

        panel.paintComponent(text, bufferStrat, background);

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
        decisions.add(new Decision("whoevencareslol", "res/test.txt"));
//muhahaha //fancy //shit - Koko 2016
        Internal internal = new Internal("Bertfred", "dope story", "res/background.png", "res/001Cry.wav", story, decisions);
        // String author, String title, String picturePath, String musicPath, ArrayList<String> story, ArrayList<Decision> decisions

        Main main = new Main();
        Layout layout = main.layout;
        layout.init();
        layout.display(internal);
    }

    public void display(Internal internal) {
        //TODO Andi
        //Hab einfach mal alle beschreibenden Kommentare hier rein gepasted

        //zeigt Bild, Text, Sound
        setBackground(internal.getPicturePath());  // what if we don't have an image? Load a default?
        // TODO: load sound here

        //Triggers on Button press nextStep(String path)  //path aus der entscheidung
        /**
         * > bild zeigen, klicken, textbox mit semitransparenz(auf bild), mehr
         * klicks, mehr text > Buttons am Ende erste zeigen > Skip Button, damit
         * die Entscheidungsbuttons erscheinen
         */
        paint(internal.getStory().get(0));

        boolean done = false;
        while (!done) {
            String textToDraw = panel.currentlyDrawnText;

            if (panel.clicked) {
                if (!panel.remains.equals("")) {
                    textToDraw = panel.remains;
                } else {

                    ArrayList<Decision> decisions = internal.getDecisions();
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
                    System.out.println("xxxxxxxxxx answer was selected!!!" + selectedAnswer);

                    DisplayEndDialogue();
                    main.nextStep(selectedAnswer);  // This never really returns, so uh.. that's kinda shitty, no?
                }

                panel.clicked = false;
            }

            paint(textToDraw);

            // dont remove this or things wont work (cuz busy waiting fucks with key presses?)
            try {
                Thread.sleep(30);
            } catch (Exception e) {
            }
        }
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
                    main.nextStep("Intro001.txt");
                } else if (selectedAnswer.equals("Ende")) {
                    main.endGame();
                }
                else if (selectedAnswer.equals("Quiz")) {
                    main.startQuiz();
                }
            }
            paint(" ");
        }
    }

    public void displayQuestion(String author, String title) {
        //TODO display question and after an answer display the solution, then return nothing
    }

    public void close() {
        //weiß nicht, ob man das braucht; halt Fenster schließen und so. Könnte aber auch überflüssig sein.
    }

}

class myPanel extends JPanel implements MouseMotionListener, MouseListener {

    Font questionFont = new Font("Consolas", Font.ITALIC, 30);
    BufferedImage textBox;
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
        Graphics2D graphics = (Graphics2D) bufferStrat.getDrawGraphics();
        super.paintComponent(graphics);

        paintBackgroundAndBoxes(graphics, background);

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
        for (QuestionBox questionbox : questionBoxes) {
            // draw highlighted box if mouse is hovering over it, otherwise draw regular box
            if (mouseInRect(questionbox.x, questionbox.y, questionbox.w, questionbox.h)) {
                graphics.drawImage(questionBoxHighlight, questionbox.x, questionbox.y, null);
            } else {
                graphics.drawImage(questionBox, questionbox.x, questionbox.y, null);
            }

            graphics.drawString(questions[questionbox.index], 325, questionbox.y + 45);
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
        graphics.drawImage(background, 0, 0, null);
        graphics.drawImage(textBox, 150, 50, null);
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
