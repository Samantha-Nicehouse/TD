package com.example.treasuredetector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.treasuredetector.model.User;
import com.example.treasuredetector.view_model.UserViewModel;

public class RegisterUserActivity extends AppCompatActivity {

    EditText editTextName;
    EditText editTextUsername;
    EditText editTextPassword;
    Button buttonRegister;

    UserViewModel userViewModel;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        editTextName = findViewById(R.id.editTextName);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


    }

    private void registerUser() {

        String name = editTextName.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(name.isEmpty()){
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(username.isEmpty()){
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.isEmpty()){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        long id = userViewModel.registerUser(new User(username, password, name));

        if(id == -1){
            Toast.makeText(this, "Registration failed username "+username+" already used, try different username", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegisterUserActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }


}