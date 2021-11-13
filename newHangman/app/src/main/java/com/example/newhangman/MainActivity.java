package com.example.newhangman;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Java Keywords
    public static final String[] WORDS = {
            "ABSTRACT", "ASSERT", "BOOLEAN", "BREAK", "BYTE",
            "CASE", "CATCH", "CHAR", "CLASS", "CONST",
            "CONTINUE", "DEFAULT", "DOUBLE", "DO", "ELSE",
            "ENUM", "EXTENDS", "FALSE", "FINAL", "FINALLY",
            "FLOAT", "FOR", "GOTO", "IF", "IMPLEMENTS",
            "IMPORT", "INSTANCEOF", "INT", "INTERFACE",
            "LONG", "NATIVE", "NEW", "NULL", "PACKAGE",
            "PRIVATE", "PROTECTED", "PUBLIC", "RETURN",
            "SHORT", "STATIC", "STRICTFP", "SUPER", "SWITCH",
            "SYNCHRONIZED", "THIS", "THROW", "THROWS",
            "TRANSIENT", "TRUE", "TRY", "VOID", "VOLATILE", "WHILE"
    };
    public static final Random RANDOM = new Random();

    // Max errors before user lose
    public static final int MAX_ERRORS = 6;
    // Word to find
     String wordToFind;
    // Word found stored in a char array to show progression of user
    private char[] wordFound;
    private int nbErrors;
    // letters already entered by user
    private ArrayList < String > letters = new ArrayList < > ();
    private ImageView img;
    private TextView wordTv;
    private TextView wordToFindTv;
             TextView wordShow;
    StringBuilder mytext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.img);
        wordTv = findViewById(R.id.wordTv);
        wordToFindTv = findViewById(R.id.wordToFindTv);
        wordShow=(TextView) findViewById(R.id.showtext);
        mytext=new StringBuilder();
        newGame();

    }

    // Method returning randomly next word to find
    private String nextWordToFind() {
        return WORDS[RANDOM.nextInt(WORDS.length)];
    }

    public void newGameStart(View view) {
        newGame();
    }

        // Method for starting a new game
        public void newGame(){
            nbErrors = -1;
            letters.clear();

            wordToFind = nextWordToFind();
            // word found initialization
            wordFound = new char[wordToFind.length()];

            for (int i = 0; i < wordFound.length; i++) {
                wordFound[i] = '_';
            }

            updateImg(nbErrors);
            wordTv.setText(wordFoundContent());
            wordToFindTv.setText("");
           textPuzzal();
        }
    public void textPuzzal(){
        char[] strtxt=wordToFind.toCharArray();
            mytext.delete(0,mytext.length());
        char x;
        for (int i=0;i<strtxt.length;i++) {
            if ((i%2)==0){
                mytext.append("?");
            }
            else{
                x=strtxt[i];
                mytext.append(x+"");
            }
        }
        wordShow.setText(mytext);

    }

    // Method returning trus if word is found by user
    public boolean wordFound() {
        return wordToFind.contentEquals(new String(wordFound));
    }

    // Method updating the word found after user entered a character
    private void enter(String c) {
        // we update only if c has not already been entered
        if (!letters.contains(c)) {
            // we check if word to find contains c
            if (wordToFind.contains(c)) {
                // if so, we replace _ by the character c
                int index = wordToFind.indexOf(c);

                while (index >= 0) {
                    wordFound[index] = c.charAt(0);
                    index = wordToFind.indexOf(c, index + 1);
                }
            } else {
                // c not in the word => error
                nbErrors++;
                Toast.makeText(this, R.string.try_an_other, Toast.LENGTH_SHORT).show();
            }

            // c is now a letter entered
            letters.add(c);
        } else {
            Toast.makeText(this, R.string.letter_already_entered, Toast.LENGTH_SHORT).show();
        }
    }

    // Method returning the state of the word found by the user until by now
    private String wordFoundContent() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < wordFound.length; i++) {
            builder.append(wordFound[i]);

            if (i < wordFound.length - 1) {
                builder.append(" ");
            }
        }

        return builder.toString();
    }


    private void updateImg(int play) {
        int resImg = getResources().getIdentifier("hangman_" + play, "drawable",
                getPackageName());
        img.setImageResource(resImg);
    }


    public void touchLetter(View v) {
        if (nbErrors < MAX_ERRORS && !getString(R.string.you_win).equals(wordToFindTv.getText())) {
            String letter = ((Button) v).getText().toString();
            enter(letter);
            wordTv.setText(wordFoundContent());
            updateImg(nbErrors);

            // check if word is found
            if (wordFound()) {
                Toast.makeText(this, R.string.you_win, Toast.LENGTH_SHORT).
                        show();
                wordToFindTv.setText(R.string.you_win);
            } else {
                if (nbErrors >= MAX_ERRORS) {
                    Toast.makeText(this, R.string.you_lose, Toast.LENGTH_SHORT).show();
                    wordToFindTv.setText(getString(R.string.word_to_find).
                            replace("#word#", wordToFind));
//
                }
            }
        } else {
            Toast.makeText(this, R.string.game_is_ended, Toast.LENGTH_SHORT).show();
        }
    }
}