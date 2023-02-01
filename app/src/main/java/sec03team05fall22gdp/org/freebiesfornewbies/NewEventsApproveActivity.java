package sec03team05fall22gdp.org.freebiesfornewbies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.parse.ParseUser;

public class NewEventsApproveActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private ImageView logoutBtn, ivMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_events_approve);

        progressDialog = new ProgressDialog(NewEventsApproveActivity.this);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        logoutBtn= findViewById(R.id.ivlogout);

        logoutBtn.setOnClickListener(v ->{
            progressDialog.show();
            // logging out of Parse
            ParseUser.logOutInBackground(e -> {
                progressDialog.dismiss();
                if (e == null)
                    showAlert("So, you're going...", "Ok...Bye-bye then");
            });
        });
        ivMenu = findViewById(R.id.ivMenuIcon);
        ivMenu.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
                int id = item.getItemId();

                Log.v("Inside:","onNavigationItemSelected");
                switch (id){
                    case R.id.nav_admin_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(NewEventsApproveActivity.this, "Admin Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NewEventsApproveActivity.this, AdminHomeActivity.class));
                        break;
                    case R.id.admin_nav_event_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(NewEventsApproveActivity.this, "Event Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NewEventsApproveActivity.this, NewEventsApproveActivity.class));
                        break;
                    case R.id.admin_nav_add_event:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(NewEventsApproveActivity.this, "Add Event is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NewEventsApproveActivity.this, NewEventsApproveActivity.class));
                        break;
                    case R.id.admin_nav_items_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(NewEventsApproveActivity.this, "Items Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NewEventsApproveActivity.this, NewEventsApproveActivity.class));
                        break;
                    case R.id.admin_nav_add_items:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(NewEventsApproveActivity.this, "Event Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NewEventsApproveActivity.this, NewEventsApproveActivity.class));
                        break;
                    case R.id.admin_nav_logout:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(NewEventsApproveActivity.this, "Logout is Clicked", Toast.LENGTH_SHORT).show();
                        progressDialog.show();
                        // logging out of Parse
                        ParseUser.logOutInBackground(e -> {
                            progressDialog.dismiss();
                            if (e == null)
                                showAlert("So, you're going...", "Ok...Bye-bye then");
                        });
                        break;
                    case R.id.admin_nav_share:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(NewEventsApproveActivity.this, "Share Link is Clicked", Toast.LENGTH_SHORT).show();break;
                    case R.id.admin_nav_contact:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(NewEventsApproveActivity.this, "Contact us is Clicked", Toast.LENGTH_SHORT).show();break;
                    default:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }return true;
            }
        });
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewEventsApproveActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(NewEventsApproveActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}