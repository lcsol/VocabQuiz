package edu.gatech.seclass.sdpvocabquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class quizPortal extends AppCompatActivity {

    private Button add_quiz, remove_quiz, practice_quiz, quiz_statistics, logout_btn;
    private SQLiteDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_portal);

        add_quiz = (Button) findViewById(R.id.add_quiz);
        remove_quiz = (Button) findViewById(R.id.remove_quiz);
        practice_quiz = (Button) findViewById(R.id.practice_quiz);
        quiz_statistics = (Button) findViewById(R.id.quiz_statistics);
        logout_btn = (Button) findViewById(R.id.logout_btn);

        db = new SQLiteDatabaseHandler(this);
    }

    public void goToAddQuiz(View view) {

        Intent OpenQuizzes = new Intent(this, addQuiz.class);
        startActivity(OpenQuizzes);

    }

    public void goToRemoveQuizzes(View view) {

        Intent RemoveQuizzes = new Intent(this, removeQuiz.class);
        startActivity(RemoveQuizzes);

    }

    public void goToPractice(View view) {
        Intent practiceQuizzes = new Intent(this, practiceQuiz.class);
        startActivity(practiceQuizzes);
    }

    public void goToStats(View view) {
        Intent displayQuizResult = new Intent(this, viewQuizStats.class);
        startActivity(displayQuizResult);
    }

    public void logout(View view) {

        startActivity(new Intent(this, SDPVocabQuizActivity.class));
        finish();
    }
}
