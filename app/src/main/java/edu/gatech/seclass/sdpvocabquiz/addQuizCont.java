package edu.gatech.seclass.sdpvocabquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class addQuizCont extends AppCompatActivity {

    private EditText numWords;
    private EditText listWords;
    private EditText wordDefs;
    private EditText incorrectDefs;
    private TextView test;
    private String name;
    private String description;
    private Button submit_btn;
    public int i;

    public SQLiteDatabaseHandler db2;
    private String[] getWords, getDefs, getIncDefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz_cont);
        numWords = (EditText) findViewById(R.id.numWords);
        listWords = (EditText) findViewById(R.id.listWords);
        wordDefs = (EditText) findViewById(R.id.wordDefs);
        incorrectDefs = (EditText) findViewById(R.id.incorrectDefs);

        Intent intent = getIntent();
        name = intent.getStringExtra(addQuiz.EXTRA_TEXT);
        description = intent.getStringExtra(addQuiz.EXTRA_TEXT2);

    }

    public void storeQuiz(View v) {


        db2 = new SQLiteDatabaseHandler(this);
        Quiz quiz = new Quiz(i, User.userName, name, description, listWords.getText().toString(), wordDefs.getText().toString(), incorrectDefs.getText().toString());


        if (validQuiz() && commasExist()) {
            db2.addQuiz(quiz);

            Intent goBackToQuizzes = new Intent(this, quizPortal.class);
            submit_btn = (Button) findViewById(R.id.submit_btn);
            startActivity(goBackToQuizzes);
        }

        else {

            Toast.makeText(this, "Invalid quiz!", Toast.LENGTH_SHORT).show();

        }

    }

    private boolean validQuiz() {

        getWords = listWords.getText().toString().split(",");
        int wordCount = getWords.length;

        getDefs = wordDefs.getText().toString().split(",");
        int defCount = getDefs.length;

        getIncDefs = incorrectDefs.getText().toString().split(",");
        int incDefsCount = getIncDefs.length;

        if (wordCount != Integer.parseInt(numWords.getText().toString())) {
            numWords.setError("Number of words set doesn't match number of words given!");
            return false;
        }
        if (defCount != Integer.parseInt(numWords.getText().toString())) {
            wordDefs.setError("Number of definitions doesn't match number of words given!");
            return false;
        }
        if (incDefsCount != wordCount * 3) {
            incorrectDefs.setError("Not enough incorrect definitions!");
            return false;
        }

        if (!commasExist()) {
            return false;
        }



        return true;
    }

    private boolean commasExist() {

        getWords = listWords.getText().toString().split(",");
        int wordCount = getWords.length;

        getDefs = wordDefs.getText().toString().split(",");
        int defCount = getDefs.length;

        getIncDefs = incorrectDefs.getText().toString().split(",");
        int incDefsCount = getIncDefs.length;

        String checkCommasForWords = listWords.getText().toString();
        int commaCount = 0;

        for (int i = 0; i < checkCommasForWords.length(); i++) {

            char c = checkCommasForWords.charAt(i);
            if (c == ',') {

                commaCount++;
            }

        }

        String checkCommasForDefs = wordDefs.getText().toString();

        int wordDefsCommaCount = 0;

        for (int i = 0; i < checkCommasForDefs.length(); i++) {

            char c = checkCommasForDefs.charAt(i);
            if (c == ',') {

                wordDefsCommaCount++;
            }

        }

        String checkCommasForIncDefs = incorrectDefs.getText().toString();

        int incDefsCommaCount = 0;

        for (int i = 0; i < checkCommasForIncDefs.length(); i++) {

            char c = checkCommasForIncDefs.charAt(i);
            if (c == ',') {

                incDefsCommaCount++;
            }

        }



        if (commaCount != wordCount-1) {
            listWords.setError("Add commas between your words!");
            return false;
        }

        if (wordDefsCommaCount != defCount-1) {

            wordDefs.setError("Add commas between your words!");
            return false;
        }

        if (incDefsCommaCount != incDefsCount-1) {
            incorrectDefs.setError("Add commas between your words!");
            return false;

        }

        return true;






    }


}
