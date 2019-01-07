package edu.gatech.seclass.sdpvocabquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class SDPVocabQuizActivity extends AppCompatActivity {

    private EditText username_text;
    private Button login_btn;
    private Button register_btn;

    private String username;
    private SQLiteDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdpvocab_quiz);

        username_text = (EditText) findViewById(R.id.username_text);
        login_btn = (Button) findViewById(R.id.login_btn);
        register_btn = (Button) findViewById(R.id.register_btn);

        db = new SQLiteDatabaseHandler(this);
    }

    // log into user account
    public void logIn(View view) {
        username = username_text.getText().toString();

        if (username.equals("")) username_text.setError("Username Can't be Empty!");

        else if (!db.checkUsername(username)) username_text.setError("Username Does Not Exist!");

        else {

            User.userName = username;

            Toast.makeText(this, "Successfully Logged In!", Toast.LENGTH_SHORT).show();

            Intent openPortal = new Intent(this, quizPortal.class);

            startActivity(openPortal);
        }
    }

    // go to register
    public void goToRegister(View view) {
        Intent register = new Intent(this, register.class);
        startActivity(register);
    }
}
