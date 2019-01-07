package edu.gatech.seclass.sdpvocabquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class practiceQuiz extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Spinner selectQuiz_spinner;
    private Button practice_btn;
    private Button cancel_btn;
    private Quiz selectedQuiz; // selected quiz for practicing

    private SQLiteDatabaseHandler db2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_quiz);

        practice_btn = (Button) findViewById(R.id.practice_btn);

        cancel_btn = (Button) findViewById(R.id.cancel_btn);

        db2 = new SQLiteDatabaseHandler(this);

        selectQuiz_spinner = findViewById(R.id.selectQuiz_spinner);

        selectQuiz_spinner.setOnItemSelectedListener(this);

        try {
            List<String> quizList = db2.getQuizzesCreatedByOthers(User.userName);
            if (quizList.isEmpty()) {
                Toast.makeText(this, "No quizzes created by others", Toast.LENGTH_SHORT).show();
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, quizList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selectQuiz_spinner.setAdapter(dataAdapter);
        } catch (Exception e) {
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public void startQuiz(View view) {
        try {
            String selectedQuizString = selectQuiz_spinner.getSelectedItem().toString();
            selectedQuiz = db2.getQuizByName(selectedQuizString);

            if (selectedQuiz == null)
                Toast.makeText(this, "Select your quiz!", Toast.LENGTH_SHORT).show();
            else {

                Intent curPractice = new Intent(this, currentPractice.class);
                curPractice.putExtra("selectedQuiz", selectedQuiz);
                startActivity(curPractice);
            }
        }catch (Exception e)
        {
            Toast.makeText(this, "Select a quiz from list!", Toast.LENGTH_SHORT).show();
        }
    }

    // go back to quizPortal by clicking cancel_btn
    public void goToPortal(View view) {
        Intent goBackToMenu = new Intent(this, quizPortal.class);
        startActivity(goBackToMenu);
    }

}
