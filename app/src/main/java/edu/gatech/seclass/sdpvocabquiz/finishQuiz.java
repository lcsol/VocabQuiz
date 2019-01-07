package edu.gatech.seclass.sdpvocabquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class finishQuiz extends AppCompatActivity {
    private TextView percentage_Text;
    private Button done_btn;

    private double percentage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_quiz);

        percentage_Text = (TextView) findViewById(R.id.percentage_Text);
        done_btn = (Button) findViewById(R.id.done_btn);

        Intent result = getIntent();
        percentage = result.getDoubleExtra("percentage", percentage);

        percentage_Text.setText(percentage + " %");

    }

    // go back to quizPortal by clicking done_btn
    public void goToPortal(View view) {
        Intent goBackToMenu = new Intent(this, quizPortal.class);
        startActivity(goBackToMenu);
    }
}
