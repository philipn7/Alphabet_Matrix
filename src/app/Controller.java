package app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    private Label labelKeyPressed;
    @FXML
    private Label labelRowCol;
    @FXML
    private TextArea textMessage;
    @FXML
    private SplitPane root;
    @FXML
    private Canvas canvas;

    String message = "_";

    private int row = -1;
    private int col = -1;
    private Boolean isGetLetterMode = true;
    private int prevRow = -1;
    private int prevCol = -1;
    Boolean isBackSpace = false;

    final char[][] alphabet =   {
                                    { 'a', 'b', 'c', 'd', 'e'},
                                    { 'f', 'g', 'h', 'i', 'j'},
                                    { 'k', 'l', 'm', 'n', 'o'},
                                    { 'p', 'q', 'r', 's', 't'},
                                    { 'u', 'v', 'w', 'x', 'y'},
                                };

    @Override
    public void initialize(URL location, ResourceBundle resources){
        labelKeyPressed.setFocusTraversable(true);
        labelRowCol.setFocusTraversable(true);
        textMessage.setFocusTraversable(true);
        root.setOnKeyReleased(this::handle);

        //center labels
        labelKeyPressed.setAlignment(Pos.CENTER);
        labelRowCol.setAlignment(Pos.CENTER);
        draw();
    }

    private void appendToMessage(char c){
        this.message = message.replaceAll("_", "");
        this.message += c;
        this.message += '_';
        textMessage.setText(message);
    }

    private void removeLastLetter(){
        this.message = message.replaceAll("_", "");
        this.message = message.substring(0, message.length() - 1);
        this.message += '_';
        textMessage.setText(message);
    }

    private void getLetter(KeyCode input){
        isGetLetterMode = true;
        isBackSpace = false;
        if(row == -1 && col == -1){
            labelKeyPressed.setText("Enter the row");
            if(input != KeyCode.BACK_SPACE){
                row = keycodeToInt(input);
                updateRowCol();
                //System.out.println("Row set to " + row);
            } else {
                isBackSpace = true;
                removeLastLetter();
            }

        }else if(row != -1 && col == -1){
            labelKeyPressed.setText("Enter the col");
            if(input != KeyCode.BACK_SPACE){
                col = keycodeToInt(input);
                updateRowCol();
                //System.out.println("Col set to " + col);

                // print letter
                appendToMessage(alphabet[row-1][col-1]);
                prevRow = row;
                prevCol = col;
                resetRowCol();
            }else{
                isBackSpace = true;
                resetRowCol();
            }
        }else {
            System.out.println("ERROR: Unable to get row and col.");
        }

        // Scroll text area to bottom.
        textMessage.setScrollTop(Double.MAX_VALUE);
    }

    private void resetRowCol(){
        //reset row and col
        row = -1;
        col = -1;
        labelRowCol.setText("[row,col]");
        isGetLetterMode = false;
    }

    private int keycodeToInt(KeyCode input){
        int number = -1;
        switch (input) {
            case DIGIT1:
                number = 1;
                break;
            case DIGIT2:
                number = 2;
                break;
            case DIGIT3:
                number = 3;
                break;
            case DIGIT4:
                number = 4;
                break;
            case DIGIT5:
                number = 5;
                break;
        }
        return number;
    }

    private void updateRowCol(){
        String rowStr = String.valueOf(row);
        String colStr = String.valueOf(col);
        if (row == -1) { rowStr = "row"; }
        if (col == -1) { colStr = "col"; }
        labelRowCol.setText("[ " + rowStr + ", " + colStr + " ]");
    }

    private void clearScreen(){
        message = "_";
        textMessage.setText(message);
    }

    private void handle(KeyEvent keyEvent){
        // Print key pressed.
        labelKeyPressed.setText("[Key " + keyEvent.getCode().getName() + " is pressed.]");

        switch (keyEvent.getCode()) {
            case DIGIT1:
                getLetter(KeyCode.DIGIT1);
                break;
            case DIGIT2:
                getLetter(KeyCode.DIGIT2);
                break;
            case DIGIT3:
                getLetter(KeyCode.DIGIT3);
                break;
            case DIGIT4:
                getLetter(KeyCode.DIGIT4);
                break;
            case DIGIT5:
                getLetter(KeyCode.DIGIT5);
                break;
            case BACK_SPACE:
                getLetter(KeyCode.BACK_SPACE);
                break;
            case C:
                clearScreen();
                break;
            case SPACE:
                // Add space if NOT in get letter mode
                if (isGetLetterMode == false){
                    appendToMessage(' ');
                }else{
                    resetRowCol();
                    appendToMessage(' ');
                }
                break;
        }
        draw();
    }

    void draw(){
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        Color color = Color.LAVENDER;
        Boolean muted = false;
        Boolean highlight = false;

        for (int i = 0; i < 5; i++) {
            switch (i) {
                case 0:
                    color = Color.AZURE;
                    break;
                case 1:
                    color = Color.BISQUE;
                    break;
                case 2:
                    color = Color.HONEYDEW;
                    break;
                case 3:
                    color = Color.LIGHTGOLDENRODYELLOW;
                    break;
                case 4:
                    color = Color.LAVENDERBLUSH;
                    break;
            }

            // highlight current row.
            muted = false;
            if(row != -1){
                muted = true;
                if (row == i+1){
                    muted = false;
                }
            }

            if(row == -1 && col == -1 && prevRow != -1 && i+1 == prevRow && isBackSpace == false){
                highlight = true;
            } else {
                highlight = false;
            }

            drawRow(g, 10 + i*150, color, i+1, muted, highlight);
        }
    }

    void drawRow(GraphicsContext g, int yPos, Color color, int row, Boolean muted, Boolean highlight){
        g.setFill(color);
        int xPos = 0;
        for (int i = 0; i < 5; i++) {
            //Highlight previous letter
            if(highlight && i+1 == prevCol){
                g.setFill(Color.LIGHTPINK);
            }else{
                g.setFill(color);
            }
            xPos = (i*100) + (i*10);
            g.fillRect(xPos, yPos, 100, 100);
        }

        Color textColor = Color.BLACK;
        g.setFill(textColor);
        if (muted){
            color = color.darker();
            //color = color.darker();
            g.setFill(color);
        }

        g.setFont(new Font("System", 140));
        xPos = 0;
        for (int i = 0; i < 5; i++) {
            xPos = (i*100) + (i*10);
            g.fillText((Character.toString(alphabet[row-1][i]).toUpperCase()), xPos, yPos+100, 100);
        }
    }

}
