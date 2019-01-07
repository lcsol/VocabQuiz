package edu.gatech.seclass.sdpvocabquiz;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class addQuiz extends AppCompatActivity {

    public static final String EXTRA_TEXT="edu.gatech.seclass.sdpvocabquiz.EXTRA_TEXT";
    public static final String EXTRA_TEXT2="edu.gatech.seclass.sdpvocabquiz.EXTRA_TEXT2";
    private SQLiteDatabaseHandler db;
    public String name, description;



    private Button next_btn;
    private Button cancel_btn;
    private EditText enter_quizname;
    private EditText enter_shortdescrip;
    private EditText test;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz);
        enter_quizname = (EditText) findViewById(R.id.enter_quizname);
        enter_shortdescrip = (EditText) findViewById(R.id.enter_shortdescrip);
        db = new SQLiteDatabaseHandler(this);



    }

    public void sendQuizInfo(View view) {

        String name = enter_quizname.getText().toString();
        String description = enter_shortdescrip.getText().toString();

        if (validInput()) {


            Intent intent = new Intent(this, addQuizCont.class);
            intent.putExtra(EXTRA_TEXT, name);
            intent.putExtra(EXTRA_TEXT2, description);
            startActivity(intent);
        }

        else {

            Toast.makeText(this, "Invalid quiz!", Toast.LENGTH_SHORT).show();

        }


    }


    public void goToPortal(View view) {

        Intent goBackToMenu = new Intent(this, quizPortal.class);
        cancel_btn = (Button) findViewById(R.id.cancel_btn);
        startActivity(goBackToMenu);

    }
    private boolean validInput() {

        String name = enter_quizname.getText().toString();
        String description = enter_shortdescrip.getText().toString();

        if (name.equals("")) {
            enter_quizname.setError("Quiz name cannot be empty");
            return false;
        }
        if (db.checkQuizName(name)) {
            enter_quizname.setError("Quiz name already exists");
            return false;
        }
        if (description.equals("")) {
            enter_shortdescrip.setError("Quiz descrption cannot be empty");
            return false;
        }

        return true;
    }
/*
    public class Quiz {


        public String quizName = enter_quizname.getText().toString();
        public String quizDescription = enter_shortdescrip.getText().toString();
        public String[] words = new String[10];
        public String[] wordDefinitions = new String[10];
        public String[] wrongDefinitions = new String[30];

        //public Quiz(String name, String description, String [] words, String [] wordDefinitions, String [] wrongDefs) {





    }*/

}
