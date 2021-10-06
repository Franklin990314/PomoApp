package poli.edu.co.pomoapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;

import poli.edu.co.pomoapp.dto.DataBasicDTO;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtName;
    private EditText txtTelephone;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtConfirmPassword;
    private CheckBox checkBox;
    private Button btnLogin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final String TAG = "Registration";
    private static final String TAG2 = "DB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
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
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    @Override
    public void onClick(View view) {
        //Invocamos al método:
        userRegister();
    }

    /** Called when the user taps the Send button */
    public void loginNow(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    //********************* Inicio de la Logica requerida para esta Actividad ****************************//

    private void userRegister(){
        final DataBasicDTO dataBasicDTO;
        try{
            dataBasicDTO = validateRegister();
            Log.d("tag", "Hola");
            this.createAccount(dataBasicDTO);

            Log.d("tag", "como que si se creo, no puede ser");
            Toast.makeText(this, "Registro Exitoso",Toast.LENGTH_LONG).show();
        }catch (Exception exc){
            Toast.makeText(this, exc.getMessage(),Toast.LENGTH_LONG).show();
            return;
        }


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

        if(TextUtils.isEmpty(email)){
            throw new Exception("Se debe ingresar un correo electronico");
        }

        if(TextUtils.isEmpty(telephone)){
            throw new Exception("Se debe ingresar el telefono");
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

    private void createAccount(@NonNull DataBasicDTO data) {
        mAuth.createUserWithEmailAndPassword(data.getEmail(),data.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            saveAccount(data);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void saveAccount(@NonNull DataBasicDTO data){
        Map<String, Object> user = new HashMap<>();
        user.put("nombre", data.getName());
        user.put("email", data.getEmail());
        user.put("telefono", data.getTelephone());

        db.collection("Usuarios")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG2, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG2, "Error adding document", e);
                    }
                });
    }

    private void reload() { }

    private void updateUI(FirebaseUser user) {

    }
}
