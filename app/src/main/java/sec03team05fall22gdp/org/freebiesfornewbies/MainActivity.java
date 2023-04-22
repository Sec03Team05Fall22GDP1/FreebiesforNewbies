package sec03team05fall22gdp.org.freebiesfornewbies;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import androidx.appcompat.app.AlertDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText regFirstName, regLastName, regEmail, regUsername, regPhone, regPassword, regConfirmPassword;
    private EditText regDOB, regAddressLine, regCity, regState, regCountry, regZipcode;
    private Button registerBtn;
    private TextView regToLoginLink;
    private ProgressDialog progressDialog;

    String generestr;

    private Handler handler = new Handler();
    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            // logging out of Parse
            ParseUser.logOutInBackground(e -> {
                if (e == null){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    moveTaskToBack(true);
                }
            });
            Log.d("MyApp", "Performing operation after 2 minutes in background");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText emailEditText = findViewById(R.id.etEmail);
        String email = emailEditText.getText().toString();
        getSupportActionBar().hide();

        if (!email.matches("^[^@]+@(gmail|yahoo)\\.com$")) {
            // Invalid email address, show an error message
            emailEditText.setError("Please enter a valid email address");
        } else {
            Toast.makeText(this, "Please enter a valid email address with @gmail.com or @yahoo.com domain", Toast.LENGTH_SHORT).show();
        }

        Spinner spinner=findViewById(R.id.Item_genere_spinner);
        String[] genereOptions=new String[]{"Country","Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe", "Palestine"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(), R.layout.item_file,genereOptions);
        adapter.setDropDownViewResource(R.layout.item_file);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String genereval =adapterView.getItemAtPosition(i).toString();
                setGenereva(genereval);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });




        progressDialog = new ProgressDialog(MainActivity.this);
        regFirstName=findViewById(R.id.etFirstName);
        regLastName=findViewById(R.id.etLastName);
        regEmail=findViewById(R.id.etEmail);
        regUsername=findViewById(R.id.etUsername);
        regPhone=findViewById(R.id.etPhone);
        regPassword=findViewById(R.id.etPassword);
        registerBtn=findViewById(R.id.btnRegister);
        regToLoginLink = findViewById(R.id.tvRedirect);
        regConfirmPassword = findViewById(R.id.etConfirmPassword);
        regDOB=findViewById(R.id.etDOB);
        regAddressLine=findViewById(R.id.etAddressLine);
        regCity=findViewById(R.id.etCity);
        regState=findViewById(R.id.etState);

        regZipcode=findViewById(R.id.etZipcode);

        NewBie[] nUser = new NewBie[1];

        registerBtn.setOnClickListener(v -> {
            if (regPassword.getText().toString().equals(regConfirmPassword.getText().toString()) && !TextUtils.isEmpty(regUsername.getText().toString())) {
                String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
                String str = regPassword.getText().toString();
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(str);
                if(m.find()){
                    nUser[0] = new NewBie(regFirstName.getText().toString(), regLastName.getText().toString(), regEmail.getText().toString(), regPhone.getText().toString(), regDOB.getText().toString(), regAddressLine.getText().toString(), regCity.getText().toString(), regState.getText().toString(), generestr, regZipcode.getText().toString(), regUsername.getText().toString(), regPassword.getText().toString());
                    login(nUser[0]);
                }else {
                    regPassword.setError("Password must contain atleast:\n\t1 Capital Letter\n\t1 Small Letter\n\t1 Number\n\t1 Special character @$!%*?&");
                }


            }
            else{
                Toast.makeText(this, "Make sure that the values you entered are correct.", Toast.LENGTH_SHORT).show();
            }
        });

        regToLoginLink.setOnClickListener( v -> {
            startActivity(new Intent(this,LoginActivity.class));
        });

    }

    private void login(NewBie newUser) {
        progressDialog.show();
        ParseUser user = new ParseUser();
        // Set the user's username and password, which can be obtained by a forms
        user.setUsername(newUser.getUserName());
        user.setPassword(newUser.getPassWord());
        user.setEmail(newUser.geteMail());
        user.put("firstName",newUser.getFirstName());
        user.put("lastName",newUser.getLastName());
        user.put("phone",newUser.getPhoneNumber());
        user.put("dateOfBirth",newUser.getDateOfBirth());
        user.put("addressLine",newUser.getAddressLine());
        user.put("city",newUser.getCityName());
        user.put("state",newUser.getStateName());
        user.put("country",newUser.getCountryName());
        user.put("zipCode",newUser.getZipCode());

        user.signUpInBackground(e -> {
            progressDialog.dismiss();
            if (e == null) {
                showAlert("Successful Sign Up ! You are now logged in...\n", "Welcome " + user.getUsername() + " !");
            } else {
                ParseUser.logOut();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();


    }
    public void setGenereva(String val){
        generestr=val;
    }
}