package poli.edu.co.pomoapp.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import poli.edu.co.pomoapp.R;
import poli.edu.co.pomoapp.dto.DataBasicDTO;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private EditText txtName;
    private EditText txtTelephone;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtConfirmPassword;
    private TextView txtBasicDataLabel;
    private TextView txtPasswordLabel;
    private CheckBox checkBox;
    private Button btnSave;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private static final String TAG = "DB";
    private DataBasicDTO dataBasic;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        txtName = (EditText) root.findViewById(R.id.txtName);
        txtEmail = (EditText) root.findViewById(R.id.txtEmail);
        txtTelephone = (EditText) root.findViewById(R.id.txtTelephone);
        txtPassword = (EditText) root.findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText) root.findViewById(R.id.txtConfirmPassword);
        txtBasicDataLabel = (TextView) root.findViewById(R.id.txtBasicDataLabel);
        txtPasswordLabel = (TextView) root.findViewById(R.id.txtPasswordLabel);
        checkBox = (CheckBox) root.findViewById(R.id.checkBox);
        btnSave = (Button) root.findViewById(R.id.btnSave);

        txtName.setFocusable(false);
        txtName.setEnabled(false);
        txtName.setCursorVisible(false);
        txtName.setKeyListener(null);
        txtName.setBackgroundColor(Color.TRANSPARENT);

        txtPassword.setEnabled(false);
        txtConfirmPassword.setEnabled(false);

        txtBasicDataLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, (getResources().getDimension(R.dimen.txt_size_link) / getResources().getDisplayMetrics().density));
        txtPasswordLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, (getResources().getDimension(R.dimen.txt_size_link) / getResources().getDisplayMetrics().density));

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword(v);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userUpdate(root);
            }
        });

        return root;
    }

    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = db.collection("Usuarios").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        dataBasic = new DataBasicDTO();
                        dataBasic.setName(document.getData().get("nombre").toString());
                        dataBasic.setEmail(document.getData().get("email").toString());
                        dataBasic.setTelephone(document.getData().get("telefono").toString());
                        txtName.setText(dataBasic.getName());
                        txtEmail.setText(dataBasic.getEmail());
                        txtTelephone.setText(dataBasic.getTelephone());
                    } else {
                        Log.d(TAG, "No such document");
                        Toast.makeText(getContext(), String.valueOf(getResources().getString(R.string.menu_failure)), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    Toast.makeText(getContext(), String.valueOf(getResources().getString(R.string.menu_failure)), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void updatePassword(View view){
        if (checkBox.isChecked()) {
            txtPassword.setEnabled(true);
            txtConfirmPassword.setEnabled(true);
        } else {
            txtPassword.setEnabled(false);
            txtConfirmPassword.setEnabled(false);
        }
    }

    private void userUpdate(View root) {
        final DataBasicDTO dataBasic;
        try{
            dataBasic = validateUpdate();
        }catch (Exception exc){
            Toast.makeText(root.getContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
        updateDataUser(dataBasic);
    }

    private final DataBasicDTO validateUpdate() throws Exception {

        final DataBasicDTO dataUpdate = new DataBasicDTO();

        String email = txtEmail.getText().toString().trim();
        String telephone = txtTelephone.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String confirmPassword = txtConfirmPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            throw new Exception(String.valueOf(getResources().getString(R.string.error_email)));
        }

        if(TextUtils.isEmpty(telephone)){
            throw new Exception(String.valueOf(getResources().getString(R.string.error_telephone)));
        }

        if (checkBox.isChecked()) {
            if(TextUtils.isEmpty(password)){
                throw new Exception(String.valueOf(getResources().getString(R.string.error_new_pass_form)));
            }

            if(password.length() < 6) {
                throw new Exception(String.valueOf(getResources().getString(R.string.error_pass_invalid)));
            }

            if(TextUtils.isEmpty(confirmPassword)){
                throw new Exception(String.valueOf(getResources().getString(R.string.error_new_pass_conf)));
            }

            if(!TextUtils.equals(password, confirmPassword)) {
                throw new Exception(String.valueOf(getResources().getString(R.string.error_pass_not_match)));
            }
        }

        dataUpdate.setEmail(dataBasic.getEmail().equals(email) ? null : email);
        dataUpdate.setTelephone(dataBasic.getTelephone().equals(telephone) ? null : telephone);
        dataUpdate.setPassword(password);

        return dataUpdate;
    }

    public void updateDataUser(DataBasicDTO updateData) {
        if (!TextUtils.isEmpty(updateData.getEmail()) || !TextUtils.isEmpty(updateData.getPassword())) {
            if (!TextUtils.isEmpty(updateData.getEmail())) {
                currentUser.updateEmail(updateData.getEmail())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User email address updated.");
                                    updatePassword(updateData);
                                } else {
                                    Toast.makeText(getContext(), String.valueOf(getResources().getString(R.string.error_update_email)), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            } else {
                updatePassword(updateData);
            }
        } else {
            updateDataBasicUser(updateData);
        }
    }

    private void updatePassword(DataBasicDTO updateData) {
        if (!TextUtils.isEmpty(updateData.getPassword())) {
            currentUser.updatePassword(updateData.getPassword())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User password updated.");
                                updateDataBasicUser(updateData);
                            } else {
                                Toast.makeText(getContext(), String.valueOf(getResources().getString(R.string.error_update_pass)), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            updateDataBasicUser(updateData);
        }
    }

    private void updateDataBasicUser(DataBasicDTO updateData) {
        if (!TextUtils.isEmpty(updateData.getEmail()) || !TextUtils.isEmpty(updateData.getTelephone())) {
            CollectionReference usersRef = db.collection("Usuarios");
            Map<String, Object> data = new HashMap<>();
            if (!TextUtils.isEmpty(updateData.getEmail())) {
                data.put("email", updateData.getEmail());
            }
            if (!TextUtils.isEmpty(updateData.getTelephone())) {
                data.put("telefono", updateData.getTelephone());
            }
            usersRef.document(currentUser.getUid()).update(data);
            Toast.makeText(getContext(), String.valueOf(getResources().getString(R.string.update_profile)), Toast.LENGTH_LONG).show();
        } else if (!TextUtils.isEmpty(updateData.getPassword())) {
            Toast.makeText(getContext(), String.valueOf(getResources().getString(R.string.update_profile)), Toast.LENGTH_LONG).show();
        }
    }
}