package com.example.treasuredetector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treasuredetector.helper.DialogHelper;
import com.example.treasuredetector.helper.Helper;
import com.example.treasuredetector.view_model.UserAuthViewModel;

public class LoginActivity extends AppCompatActivity {


    EditText editTextEmail;
    EditText editTextPassword;
    TextView textViewRegisterNow;
    Button buttonLogin;

    DialogHelper dialogHelper;
    Helper helper;

    UserAuthViewModel userAuthViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dialogHelper = new DialogHelper(LoginActivity.this);
        helper = new Helper(LoginActivity.this);
        userAuthViewModel = new ViewModelProvider(this).get(UserAuthViewModel.class);

        userAuthViewModel.getUserMutableLiveData().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            dialogHelper.dismissDialog();
        });

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewRegisterNow = findViewById(R.id.textViewRegisterNow);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(v -> login());

        textViewRegisterNow.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterUserActivity.class);
            startActivity(intent);
        });

        helper.isLocationPermissionGranted(LoginActivity.this);
    }

    private void login() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (helper.isEmailInvalid(email)) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        dialogHelper.showDialog();
        userAuthViewModel.loginUser(email, password);
    }

}