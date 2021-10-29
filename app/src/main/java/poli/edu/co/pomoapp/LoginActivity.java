package poli.edu.co.pomoapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import poli.edu.co.pomoapp.dto.CredentialDTO;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText textEmail;
    private EditText textPassword;
    private TextView textLabelLogin;
    private TextView textWelcome;
    private TextView textRememberPassword;
    private Button btnLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        btnLogin = (Button) findViewById(R.id.buttonSignIn);
        textLabelLogin = (TextView) findViewById(R.id.txtLabelLogin);
        textWelcome = (TextView) findViewById(R.id.txtWelcome);
        textRememberPassword = (TextView) findViewById(R.id.txtRememberPassword);
        textEmail = (EditText) findViewById(R.id.txtEmail);
        textPassword = (EditText) findViewById(R.id.txtPassword);

        textLabelLogin.setTextSize(TypedValue.COMPLEX_UNIT_PX, (getResources().getDimension(R.dimen.txt_size_title) / getResources().getDisplayMetrics().density));
        textWelcome.setTextSize(TypedValue.COMPLEX_UNIT_PX, (getResources().getDimension(R.dimen.txt_size_link) / getResources().getDisplayMetrics().density));
        textRememberPassword.setTextSize(TypedValue.COMPLEX_UNIT_PX, (getResources().getDimension(R.dimen.txt_size_link) / getResources().getDisplayMetrics().density));
        btnLogin.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void reload() { }

    @Override
    public void onClick(View view) {
        this.userLogin();
    }

    public void registerNow(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void resetPassword(View view) {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    private void userLogin() {
        CredentialDTO credentialDTO;
        try{
            credentialDTO = validateLogger();
            mAuth.signInWithEmailAndPassword(credentialDTO.getEmail(),credentialDTO.getPassword())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                Toast.makeText(LoginActivity.this, String.valueOf(getResources().getString(R.string.successful_login)), Toast.LENGTH_LONG).show();
                                nextActivity();
                            } else {
                                Toast.makeText(LoginActivity.this, String.valueOf(getResources().getString(R.string.incorrect_credentials)), Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
    }


    public void nextActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public CredentialDTO validateLogger() throws Exception{
        CredentialDTO credentialDTO = new CredentialDTO();

        // Obtenemos el email y la contraseña desde las cajas de texto
        final String email = textEmail.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        // Verificamos que las cajas de texto no esten vacías
        if (TextUtils.isEmpty(email)) {
            throw new Exception(String.valueOf(getResources().getString(R.string.error_email)));
        }

        if (TextUtils.isEmpty(password)) {
            throw new Exception(String.valueOf(getResources().getString(R.string.error_password)));
        }

        credentialDTO.setEmail(email);
        credentialDTO.setPassword(password);

        return credentialDTO;
    }

    private void updateUI(FirebaseUser user) {

    }
}
