package edu.gatech.seclass.sdpvocabquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class currentPractice extends AppCompatActivity {

    private EditText quizName;
    private EditText quizWord;
    private Button choice1;
    private Button choice2;
    private Button choice3;
    private Button choice4;
    private Button confirm_btn;
    private Button cancel_btn;

    private Quiz currentQuiz;
    private int number; // number of words in current quiz
    private List<String> words; // list of words
    private List<String> definitions; // list of definitions
    private Map<String, String> word_def; // key: word. value: definition
    private List<String> incorrectDefinitions; // list of incorrect definitions
    private String word; // random word for current quiz
    private String correctAnswer;
    private String userSelectedAnswer;
    private List<String> choices; // four choices
    private int correctChoice; // number of correct choice
    private double percentage;

    private SQLiteDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_practice);

        quizName = (EditText) findViewById(R.id.quizName);
        quizWord = (EditText) findViewById(R.id.quizWord);
        choice1 = (Button) findViewById(R.id.choice1);
        choice2 = (Button) findViewById(R.id.choice2);
        choice3 = (Button) findViewById(R.id.choice3);
        choice4 = (Button) findViewById(R.id.choice4);
        confirm_btn = (Button) findViewById(R.id.confirm_btn);
        cancel_btn = (Button) findViewById(R.id.cancel_btn);
        correctChoice = 0;
        percentage = 0;

        db = new SQLiteDatabaseHandler(this);

        Intent curPractice = getIntent();
        currentQuiz = (Quiz) curPractice.getSerializableExtra("selectedQuiz");

        quizName.setText(currentQuiz.getQuizName()); // display current quiz name

        words = new ArrayList<>(Arrays.asList(currentQuiz.getWords().split(",")));
        definitions = new ArrayList<>(Arrays.asList(currentQuiz.wordDefinitions().split(",")));
        incorrectDefinitions = new ArrayList<>(Arrays.asList(currentQuiz.incorrectDefinitions().split(",")));
        number = definitions.size();

        word_def = new HashMap<>();
        for (int i = 0; i < number; i++) {
            word_def.put(words.get(i), definitions.get(i));
        }

        setRandomWord();
        choices = getDefinitionList();
        updateQuestion();

        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSelectedAnswer = choice1.getText().toString();
                choice1.setSelected(true);
                choice2.setSelected(false);
                choice3.setSelected(false);
                choice4.setSelected(false);
            }
        });

        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSelectedAnswer = choice2.getText().toString();
                choice2.setSelected(true);
                choice1.setSelected(false);
                choice3.setSelected(false);
                choice4.setSelected(false);
            }
        });

        choice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSelectedAnswer = choice3.getText().toString();
                choice3.setSelected(true);
                choice1.setSelected(false);
                choice2.setSelected(false);
                choice4.setSelected(false);
            }
        });

        choice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSelectedAnswer = choice4.getText().toString();
                choice4.setSelected(true);
                choice1.setSelected(false);
                choice2.setSelected(false);
                choice3.setSelected(false);
            }
        });
    }

    // get a random word and it's definition
    private void setRandomWord() {
        int size = words.size();
        int id = new Random().nextInt(size);
        word = words.get(id);
        correctAnswer = word_def.get(word);
        words.remove(id);
    }

    // get four definitions including correct definition
    private List<String> getDefinitionList() {
        List<String> res = new ArrayList<>();

        List<String> definitionCollection = new ArrayList<>();
        definitionCollection.addAll(definitions);
        definitionCollection.addAll(incorrectDefinitions);

        Collections.shuffle(definitionCollection);

        for (int i = 0; i < 3; i++) {
            String cur = definitionCollection.get(i);
            if (!cur.equals(correctAnswer)) res.add(cur);
        }
        if (res.size() < 3) res.add(definitionCollection.get(3));
        res.add(correctAnswer);
        Collections.shuffle(res);

        return res;
    }

    // question for a new word
    private void updateQuestion() {
        quizWord.setText(word);

        choice1.setText(choices.get(0));
        choice2.setText(choices.get(1));
        choice3.setText(choices.get(2));
        choice4.setText(choices.get(3));

        choice1.setSelected(false);
        choice2.setSelected(false);
        choice3.setSelected(false);
        choice4.setSelected(false);

        userSelectedAnswer = null;
    }

    // record percentage and go to next question by clicking confirm_btn
    public void confirmSelect(View view) {
        if (userSelectedAnswer == null) {
            Toast.makeText(this, "Select your answer!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userSelectedAnswer.equals(correctAnswer)) {
            correctChoice++;
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
        }

        if (words.isEmpty()) {
            double temp = (correctChoice * 100) / number;
            percentage = Math.round(temp * 100) / 100; // round to two digits

            // store quiz result
            quizResult result = new quizResult(currentQuiz.getQuizName(), User.userName, percentage, getTime());
            db.storeQuizResult(result);

            Intent showResult = new Intent(this, finishQuiz.class);
            showResult.putExtra("percentage", percentage);
            showResult.putExtra("quiz_name", quizName.toString());
            startActivity(showResult);
            return;
        }

        setRandomWord();
        choices = getDefinitionList();
        updateQuestion();
    }

    // go back to practiceQuiz by clicking cancel_btn
    public void goToPracticeQuiz(View view) {
        Intent goBackToSelectQuiz = new Intent(this, practiceQuiz.class);
        startActivity(goBackToSelectQuiz);
    }

    // get date and time when quiz finished
    private String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy, HH:mm a");
        return dateFormat.format(Calendar.getInstance().getTime());
    }
}
