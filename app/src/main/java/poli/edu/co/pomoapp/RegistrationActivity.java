package poli.edu.co.pomoapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import poli.edu.co.pomoapp.dto.DataBasicDTO;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtName;
    private EditText txtTelephone;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtConfirmPassword;
    private CheckBox checkBox;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Connection Firebase
        // TODO: realizar conexión a BD

        txtName = (EditText) findViewById(R.id.txtName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtTelephone = (EditText) findViewById(R.id.txtTelephone);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //Invocamos al método:
        userRegister();
    }

    //********************* Inicio de la Logica requerida para esta Actividad ****************************//

    private void userRegister(){

        final DataBasicDTO dataBasicDTO;
        try{
            dataBasicDTO = validateRegister();
        }catch (Exception exc){
            Toast.makeText(this, exc.getMessage(),Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this, "Registro Exitoso",Toast.LENGTH_LONG).show();

        // TODO: Guardar en BD
    }

    private final DataBasicDTO validateRegister() throws Exception {

        final DataBasicDTO dataBasicDTO = new DataBasicDTO();

        String name = txtName.getText().toString().trim();
        String telephone = txtTelephone.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String confirmPassword = txtConfirmPassword.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            throw new Exception("Se debe ingresar el nombre");
        }

        if(TextUtils.isEmpty(telephone)){
            throw new Exception("Se debe ingresar el telefono");
        }

        if(TextUtils.isEmpty(email)){
            throw new Exception("Se debe ingresar un correo electronico");
        }

        if(TextUtils.isEmpty(password)){
            throw new Exception("Se debe ingresar la contraseña");
        }


        if(TextUtils.isEmpty(confirmPassword)){
            throw new Exception("Se debe confirmar la contraseña");
        }

        if(TextUtils.equals(password, confirmPassword)==false) {
            throw new Exception("Las contraseñas no coinciden");
        }

        if(!checkBox.isChecked()) {
            throw new Exception("Debe aceptar los términos y condiciones");
        }

        dataBasicDTO.setName(name);
        dataBasicDTO.setTelephone(telephone);
        dataBasicDTO.setEmail(email);
        dataBasicDTO.setPassword(password);
        dataBasicDTO.setUser(email.split("@")[0]);

        return dataBasicDTO;
    }
}
