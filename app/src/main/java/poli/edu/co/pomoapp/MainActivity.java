package poli.edu.co.pomoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView textEmail;
    private TextView textName;
    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_task, R.id.nav_report, R.id.nav_profile, R.id.nav_pomodoro)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
        this.getCustomerInfo();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void reload() { }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_log_out) {
            mAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    private void getCustomerInfo() {
        View headerLayout = navigationView.getHeaderView(0);
        textEmail = (TextView) headerLayout.findViewById(R.id.txtEmail);
        textName = (TextView) headerLayout.findViewById(R.id.txtName);

        textName.setTextSize(TypedValue.COMPLEX_UNIT_PX, (getResources().getDimension(R.dimen.txt_size_link) / getResources().getDisplayMetrics().density));

        textEmail.setText(currentUser.getEmail());
        // TODO: Crear lógica con consulta a BD
        String name = "";
        if (currentUser.getEmail().equals("joas@gmail.com")){
            name = "jesus octavio";
        } else if (currentUser.getEmail().equals("djlenon25@gmail.com")){
            name = "leleal";
        } else if (currentUser.getEmail().equals("leal88-@hotmasil.com")){
            name = "leonardo leaal";
        } else if (currentUser.getEmail().equals("d4n6l4ck@gmail.com")){
            name = "Alexo";
        } else if (currentUser.getEmail().equals("nicolasherrang98@gmail.com")){
            name = "Frank Nicolas Herran Garzón";
        }
        textName.setText(name);
    }
}