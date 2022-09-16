package sec03team05fall22gdp.org.freebiesfornewbies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText et_EmailId, et_Password;
    Button btn_Register;
    TextView tv_SignIn;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        et_EmailId = findViewById(R.id.etEmail);
        et_Password = findViewById(R.id.etPassword);
        btn_Register = findViewById(R.id.btnRegister);
        tv_SignIn = findViewById(R.id.tvRedirect);

        btn_Register.setOnClickListener(v -> {
            String email = et_EmailId.getText().toString();
            String pwd = et_Password.getText().toString();
            if(email.isEmpty() && pwd.isEmpty()){
                Toast.makeText(MainActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
            }
            else if(email.isEmpty()){
                et_EmailId.setError("Please enter email id");
                et_EmailId.requestFocus();
            }
            else  if(pwd.isEmpty()){
                et_Password.setError("Please enter your et_Password");
                et_Password.requestFocus();
            }
            else  if(!(email.isEmpty() && pwd.isEmpty())){
                mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"SignUp Unsuccessful, Please Try Again",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            startActivity(new Intent(MainActivity.this,HomeActivity.class));
                        }
                    }
                });
            }
            else{
                Toast.makeText(MainActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

            }
        });

        tv_SignIn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
        });
    }
}