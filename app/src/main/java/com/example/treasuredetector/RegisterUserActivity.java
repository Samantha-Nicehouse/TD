package com.example.treasuredetector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.treasuredetector.helper.DialogHelper;
import com.example.treasuredetector.helper.Helper;
import com.example.treasuredetector.repository.UserAuthRepository;
import com.example.treasuredetector.view_model.UserAuthViewModel;

public class RegisterUserActivity extends AppCompatActivity {

    EditText editTextName;
    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonRegister;

    UserAuthViewModel userAuthViewModel;
    DialogHelper dialogHelper;
    Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        dialogHelper = new DialogHelper(RegisterUserActivity.this);
        helper = new Helper(RegisterUserActivity.this);

        userAuthViewModel = new ViewModelProvider(this).get(UserAuthViewModel.class);
        userAuthViewModel.setCallbackRegister(new UserAuthRepository.CallbackRegister() {
            @Override
            public void registerSuccessful() {
                dialogHelper.dismissDialog();

                Toast.makeText(RegisterUserActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterUserActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void registerFailed(String msg) {
                dialogHelper.dismissDialog();

                Toast.makeText(RegisterUserActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {

        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!helper.isEmailValid(email)){
            Toast.makeText(this, "Please a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        dialogHelper.showDialog();
        userAuthViewModel.registerUser(email, password, name);
    }
}