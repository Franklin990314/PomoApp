package poli.edu.co.pomoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText txtEmail;
    private Button btnResetPassword;
    private String email;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        btnResetPassword = (Button) findViewById(R.id.btnResetPassword);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = txtEmail.getText().toString();
                if (!TextUtils.isEmpty(email)) {
                    progressDialog.setMessage(String.valueOf(getResources().getString(R.string.reset_password_wait)));
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    resetPassword();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, String.valueOf(getResources().getString(R.string.error_email)), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void resetPassword() {
        mAuth.setLanguageCode(String.valueOf(getResources().getString(R.string.language_code)));
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    loginNow();
                    Toast.makeText(ResetPasswordActivity.this, String.valueOf(getResources().getString(R.string.reset_password_successful)), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, String.valueOf(getResources().getString(R.string.reset_password_unsuccessful)), Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    public void loginNow() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}