package edu.gatech.seclass.sdpvocabquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


import java.io.Serializable;
import java.util.List;


public class displayQuizResult extends AppCompatActivity {

    ListView listView;
    private SQLiteDatabaseHandler db2;
    private TextView showQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_quiz_result);

        //TextView textViewToChange = (TextView) findViewById(R.id.textView2);
        //textViewToChange.setText("percentage");

        //TextView textViewToChange1 = (TextView) findViewById(R.id.textView3);
        //textViewToChange1.setText("date");

        //showQuiz = (TextView) findViewById(R.id.showQuiz);

        db2 = new SQLiteDatabaseHandler(this);

        List<Quiz> quizzes = db2.allQuizzes();

        //StringBuilder builder = new StringBuilder();
        // for (Quiz quiz: quizzes) {
        //builder.append(quiz).append("\n");
        //}
        //showQuiz.setText(builder.toString());

        listView = (ListView) findViewById(R.id.list);

        //String[] values = new String[] { "Quiz 1",
        //        "Quiz 2",
        //        "Quiz 3",
        //        "Quiz 4",
        //        "Quiz 5",
        //        "Quiz 6"
        //};
        ArrayList<String> values = new ArrayList<String>();
        for (Quiz quiz: quizzes) {
            String str = "Quiz: " + quiz.toString() + "\n" + "Percentage: " + "100" + "\n" + "Date Taken: " + "10/10/18";
            values.add(str);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listView.setAdapter(adapter);


        return;
    }

}
