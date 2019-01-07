package edu.gatech.seclass.sdpvocabquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class quizStatistics extends AppCompatActivity {
    private TextView firstScore_text, firstPercentage_text, highestScore_text, highestPercentage_text,
    firstThree_text, firstThreeName_text;

    private String quizName;

    private SQLiteDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_statistics);

        db = new SQLiteDatabaseHandler(this);

        firstScore_text = (TextView) findViewById(R.id.firstScore_text);
        firstPercentage_text = (TextView) findViewById(R.id.firstPercentage_text);
        highestScore_text = (TextView) findViewById(R.id.highestScore_text);
        highestPercentage_text = (TextView) findViewById(R.id.highestPercentage_text);
        firstThree_text = (TextView) findViewById(R.id.firstThree_text);
        firstThreeName_text = (TextView) findViewById(R.id.firstThreeName_text);



            Intent intent = getIntent();
            quizName = (String) intent.getSerializableExtra("quizResult");

            try {
                quizResult first = db.getFirstScore(quizName);
                String firstPercentage = String.valueOf(first.getPercentage());
                String firstDate = first.getFinishTime();
                if (!firstPercentage.equals("")) {
                    firstPercentage_text.setText(firstPercentage + " at " + firstDate);
                }
                else
                {
                    firstPercentage_text.setText("N/A");
                }
            }catch(Exception e) {
                firstPercentage_text.setText("N/A");
            }

            try {

                quizResult highest = db.getHighestScore(quizName);
                String highestPercentage = String.valueOf(highest.getPercentage());
                String highestDate = highest.getFinishTime();

                if (!highestPercentage.equals("")) {
                    highestPercentage_text.setText(highestPercentage + " at " + highestDate);
                } else {
                    highestPercentage_text.setText("N/A");
                }

            }catch(Exception e) {
                highestPercentage_text.setText("N/A");
            }

            try {
                TreeSet<String> set = db.getFirstThree(quizName);
                String firstThreeName = "";

                if (!set.isEmpty()) {
                    for (String str : set) firstThreeName += str + ", ";
                }

                int firstThreeNameLength = firstThreeName.length();
                if (firstThreeNameLength > 0) {
                    firstThreeName = firstThreeName.substring(0, firstThreeName.length() - 1);
                } else {
                    firstThreeName = "N/A";
                }
                firstThreeName_text.setText(firstThreeName);

            }catch (Exception e){
                firstThreeName_text.setText("N/A");
            }

    }

}
