package poli.edu.co.pomoapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
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
    private TextView textLabel;
    private TextView textHasAnAccount;
    private TextView textLogin;
    private CheckBox checkBox;
    private Button btnLogin;
    private TextView txtTermsConditions;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final String TAG = "Registration";
    private static final String TAG2 = "DB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connection Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        txtName = (EditText) findViewById(R.id.txtName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtTelephone = (EditText) findViewById(R.id.txtTelephone);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtTermsConditions = (TextView) findViewById(R.id.textView3);
        textLabel = (TextView) findViewById(R.id.txtLabel);
        textHasAnAccount = (TextView) findViewById(R.id.txtHasAnAccount);
        textLogin = (TextView) findViewById(R.id.txtLogin);

        textLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, (getResources().getDimension(R.dimen.txt_size_title) / getResources().getDisplayMetrics().density));
        textHasAnAccount.setTextSize(TypedValue.COMPLEX_UNIT_PX, (getResources().getDimension(R.dimen.txt_size_link) / getResources().getDisplayMetrics().density));
        textLogin.setTextSize(TypedValue.COMPLEX_UNIT_PX, (getResources().getDimension(R.dimen.txt_size_link) / getResources().getDisplayMetrics().density));
        txtTermsConditions.setText(Html.fromHtml(getString(R.string.terms_and_conditions)));
        ((TextView) txtTermsConditions.findViewById(R.id.textView3)).setMovementMethod(LinkMovementMethod.getInstance());
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    @Override
    public void onClick(View view) {
        userRegister();
    }

    /** Called when the user taps the Send button */
    public void loginNow(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void userRegister(){
        final DataBasicDTO dataBasicDTO;
        try{
            dataBasicDTO = validateRegister();
            this.createAccount(dataBasicDTO);
            Toast.makeText(this, String.valueOf(getResources().getString(R.string.successful_registration)), Toast.LENGTH_LONG).show();
        }catch (Exception exc){
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
    }

    private final DataBasicDTO validateRegister() throws Exception {

        final DataBasicDTO dataBasicDTO = new DataBasicDTO();

        String name = txtName.getText().toString().trim();
        String telephone = txtTelephone.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String confirmPassword = txtConfirmPassword.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            throw new Exception(String.valueOf(getResources().getString(R.string.error_name)));
        }

        if(TextUtils.isEmpty(email)){
            throw new Exception(String.valueOf(getResources().getString(R.string.error_email)));
        }

        if(TextUtils.isEmpty(telephone)){
            throw new Exception(String.valueOf(getResources().getString(R.string.error_telephone)));
        }

        if(TextUtils.isEmpty(password)){
            throw new Exception(String.valueOf(getResources().getString(R.string.error_pass_form)));
        }

        if(TextUtils.isEmpty(confirmPassword)){
            throw new Exception(String.valueOf(getResources().getString(R.string.error_pass_conf)));
        }

        if(TextUtils.equals(password, confirmPassword)==false) {
            throw new Exception(String.valueOf(getResources().getString(R.string.error_pass_not_match)));
        }

        if(!checkBox.isChecked()) {
            throw new Exception(String.valueOf(getResources().getString(R.string.error_accep_term)));
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
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        saveAccount(data);
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(RegistrationActivity.this, String.valueOf(getResources().getString(R.string.error_auth_failed)), Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });
    }

    private void saveAccount(@NonNull DataBasicDTO data){
        boolean sucess = false;
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
                        returnAct();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG2, "Error adding document", e);
                    }
                }).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                    }
                });
    }

    private void returnAct(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void reload() { }

    private void updateUI(FirebaseUser user) {

    }
}
