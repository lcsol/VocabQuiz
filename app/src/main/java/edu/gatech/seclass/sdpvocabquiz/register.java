package edu.gatech.seclass.sdpvocabquiz;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class register extends AppCompatActivity {
    private EditText username_text, realName_text, email_text, major_text;
    private Spinner seniorityLevel_spinner;
    private Button register_btn, cancel_btn;

    private String username, realName, email, major, seniority;
    private List<String> seniorityList;
    private SQLiteDatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username_text = (EditText) findViewById(R.id.username_text);
        realName_text = (EditText) findViewById(R.id.realName_text);
        email_text = (EditText) findViewById(R.id.email_text);
        major_text = (EditText) findViewById(R.id.major_text);
        seniorityLevel_spinner = (Spinner) findViewById(R.id.seniorityLevel_spinner);
        register_btn = (Button) findViewById(R.id.register_btn);
        cancel_btn = (Button) findViewById(R.id.cancel_btn);

        db = new SQLiteDatabaseHandler(this);

        seniorityList = new ArrayList<>(Arrays.asList("Select your seniority level", "Freshman", "Sophomore", "Junior", "Senior", "Grad"));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, seniorityList) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView text = (TextView) view;
                if (position == 0) text.setTextColor(Color.GRAY); // set hint text color to gray
                else text.setTextColor(Color.BLACK);
                return view;
            }
        };

        seniorityLevel_spinner.setAdapter(adapter);
        seniorityLevel_spinner.setSelection(0); // set hint of spinner

        seniorityLevel_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                seniority = pos == 0 ? "" : seniorityLevel_spinner.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    // register new account
    public void register(View view) {
        username = username_text.getText().toString();
        realName = realName_text.getText().toString();
        email = email_text.getText().toString();
        major = major_text.getText().toString();

        if (validInput()) {
            User.userName = username;
            boolean inserted = db.addUser(username, realName, email, major, seniority);
            if (inserted) {
                Toast.makeText(this, "Successfully Registered!", Toast.LENGTH_SHORT).show();
                // go to quiz portal
                Intent goToPortal = new Intent(this, quizPortal.class);
                startActivity(goToPortal);
            }
            else Toast.makeText(this, "Register Failed!", Toast.LENGTH_SHORT).show();
        }


    }

    // go back to login
    public void goToLogin(View view) {
        Intent goToLogin = new Intent(this, SDPVocabQuizActivity.class);
        startActivity(goToLogin);
    }

    // check if input is valid
    private boolean validInput() {
        if (username.equals("")) {
            username_text.setError("Username can't be empty");
            return false;
        }
        if (db.checkUsername(username)) {
            username_text.setError("Username has been registered");
            return false;
        }
        if (realName.equals("")) {
            realName_text.setError("Real name can't be empty");
            return false;
        }
        if (email.equals("")) {
            email_text.setError("Email can't be empty");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_text.setError("Email address is invalid");
            return false;
        }
        if (major.equals("")) {
            major_text.setError("Major can't be empty");
            return false;
        }
        if (seniority.equals("")) {
            ((TextView) seniorityLevel_spinner.getSelectedView()).setError("Select your seniority level");
            return false;
        }
        return true;
    }

}
