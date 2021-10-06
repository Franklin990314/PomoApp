package poli.edu.co.pomoapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import poli.edu.co.pomoapp.dto.CredentialDTO;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText textEmail;
    private EditText textPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Connection Firebase
        // TODO: realizar conexión a BD

        textEmail = (EditText) findViewById(R.id.txtEmail);
        textPassword = (EditText) findViewById(R.id.txtPassword);

        btnLogin = (Button) findViewById(R.id.buttonSignIn);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.userLogin();
    }

    /** Called when the user taps the Send button */
    public void registerNow(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    //********************* Inicio de la Logica requerida para esta Actividad ****************************//

    private void userLogin() {

        CredentialDTO credentialDTO;
        try{
            credentialDTO = validateLogger();
        }catch (Exception exc){
            Toast.makeText(this, exc.getMessage(),Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this, "Inicio de sesión Exitoso",Toast.LENGTH_LONG).show();

        // TODO: Validar credenciales en BD
    }

    public void nextActivity(String id){
        // TODO: next activity
    }

    public CredentialDTO validateLogger() throws Exception{
        CredentialDTO credentialDTO = new CredentialDTO();

        //Obtenemos el email y la contraseña desde las cajas de texto
        final String email = textEmail.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vacías
        if (TextUtils.isEmpty(email)) {//(precio.equals(""))
            throw new Exception("Se debe ingresar un email");
        }

        if (TextUtils.isEmpty(password)) {
            throw new Exception("Falta ingresar la contraseña");
        }

        credentialDTO.setEmail(email);
        credentialDTO.setPassword(password);

        return credentialDTO;
    }
}
