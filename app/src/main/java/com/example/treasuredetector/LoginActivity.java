package com.example.treasuredetector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treasuredetector.model.User;
import com.example.treasuredetector.view_model.UserViewModel;

import org.jetbrains.annotations.Async;

public class LoginActivity extends AppCompatActivity {


    EditText editTextUsername;
    EditText editTextPassword;
    TextView textViewRegisterNow;
    Button buttonLogin;

    UserViewModel userViewModel;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewRegisterNow = findViewById(R.id.textViewRegisterNow);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        textViewRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterUserActivity.class);
                startActivity(intent);
            }
        });
    }


    private void login() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (username.isEmpty()) {
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        //Since the sql query take longer time we are doing this operation in background thread
        new LoginTask().execute(username, password);

    }

    class LoginTask extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... strings) {

            String username = strings[0];
            String password = strings[1];
            return userViewModel.login(username, password);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);

            if (user == null) {
                Toast.makeText(LoginActivity.this, "Login failed, invalid email or password", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}