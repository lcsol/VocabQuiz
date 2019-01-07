package edu.gatech.seclass.sdpvocabquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class removeQuiz extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Button cancel_btn;
    private Button remove_btn;
    private Spinner removeQuiz_spinner;
    private SQLiteDatabaseHandler db2;
    private List<String> quizList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_quiz);

        db2 = new SQLiteDatabaseHandler(this);

        removeQuiz_spinner = findViewById(R.id.remove_spinner);
        removeQuiz_spinner.setOnItemSelectedListener(this);

        updateRemoveQuizSpinner();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }



    public void updateRemoveQuizSpinner() {

        try {
            quizList = db2.getQuizzesCreatedByUser(User.userName);
            if (quizList.isEmpty()) {
                Toast.makeText(this, "No quizzes created by current user", Toast.LENGTH_SHORT).show();
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, quizList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            removeQuiz_spinner.setAdapter(dataAdapter);
        }catch (Exception e)
        {}
    }

    public void goToPortal(View view) {

        Intent goBackToMenu = new Intent(this, quizPortal.class);
        cancel_btn = (Button) findViewById(R.id.cancel_btn);
        startActivity(goBackToMenu);
    }

    public void removeQuiz(View view) {

        try {
            String removeQuizString = removeQuiz_spinner.getSelectedItem().toString();

            if (removeQuizString.isEmpty()) {
                Toast.makeText(this, "Select a quiz from list!", Toast.LENGTH_SHORT).show();
            } else {
                remove_btn = (Button) findViewById(R.id.remove_btn);
                db2.deleteQuiz(removeQuizString);
                db2.deleteQuizResult(removeQuizString);
                updateRemoveQuizSpinner();
            }
        }catch (Exception e)
        {
            Toast.makeText(this, "Select a quiz from list!", Toast.LENGTH_SHORT).show();
        }

    }

}
