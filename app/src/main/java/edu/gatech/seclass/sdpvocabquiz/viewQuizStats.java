package edu.gatech.seclass.sdpvocabquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class viewQuizStats extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private SQLiteDatabaseHandler db;
    private Button view_stats;
    private Spinner quizstats_selector;
    private String quizName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_quiz_stats);

        view_stats = (Button) findViewById(R.id.view_stats);
        db = new SQLiteDatabaseHandler(this);
        quizstats_selector = findViewById(R.id.quizstats_selector);
        quizstats_selector.setOnItemSelectedListener(this);
        updateViewQuizStat();

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void updateViewQuizStat(){
        try {
            List<String> quizzesNameList = db.getQuizzesNameList(User.userName);
            if (quizzesNameList.isEmpty()) {
                Toast.makeText(this, "No quizzed found", Toast.LENGTH_SHORT).show();
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, quizzesNameList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            quizstats_selector.setAdapter(dataAdapter);
        } catch (Exception e) {
        }
    }

    public void goToStatistics(View view) {

        try {
            quizName = (String) quizstats_selector.getSelectedItem().toString();
            if (quizName == null)
            {
                Toast.makeText(this, "Select a quiz!", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(this, quizStatistics.class);
                intent.putExtra("quizResult", quizName);
                startActivity(intent);
            }
        }catch (Exception e){

        }
    }
}

