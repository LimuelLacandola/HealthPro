package com.example.healthprolatestversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextInputLayout usernameregister, passwordregister, fullnameregister, bloodtyperegister, addressregister;
    TextInputEditText contactregister;
    Spinner genderSpinner, bloodTypeSpinner;
    TextView registeredtxt;
    Button registerbtn;
    FirebaseAuth mAuth;
    CheckBox termsCheckBox;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        usernameregister = findViewById(R.id.usernameregister);
        passwordregister = findViewById(R.id.passwordregister);
        fullnameregister = findViewById(R.id.fullnameregister);
        genderSpinner = findViewById(R.id.genderSpinner);
        bloodTypeSpinner = findViewById(R.id.bloodTypeSpinner);
        addressregister = findViewById(R.id.addressregister);
        contactregister = findViewById(R.id.contactregister);
        registerbtn = findViewById(R.id.registerbtn);
        registeredtxt = findViewById(R.id.registeredtxt);
        bloodTypeSpinner = findViewById(R.id.bloodTypeSpinner);
        termsCheckBox = findViewById(R.id.termsCheckBox);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this, R.array.genders_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);
        genderSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> bloodTypeAdapter = ArrayAdapter.createFromResource(this, R.array.blood_types_array, android.R.layout.simple_spinner_item);
        bloodTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodTypeSpinner.setAdapter(bloodTypeAdapter);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        registeredtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (termsCheckBox.isChecked()) {
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("users");

                    String email = usernameregister.getEditText().getText().toString();
                    String password = passwordregister.getEditText().getText().toString();
                    String fullname = fullnameregister.getEditText().getText().toString();
                    String gender = genderSpinner.getSelectedItem().toString();
                    String bloodtype = bloodTypeSpinner.getSelectedItem().toString();
                    String address = addressregister.getEditText().getText().toString();
                    String contact = contactregister.getText().toString();

                    UserHelperClass helperClass = new UserHelperClass(email, password, fullname, gender, bloodtype, address, contact);
                    // call register function
                    register(email, password, helperClass);


                } else {
                    Toast.makeText(Register.this, "Please agree to the terms and conditions", Toast.LENGTH_SHORT).show();
                }
            }
        });

        termsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                registerbtn.setEnabled(isChecked);
            }
        });

        TextView termsAndConditionsTextView = findViewById(R.id.TCtxt);
        termsAndConditionsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTermsAndConditionsDialog();
            }
        });
    }

    private void showTermsAndConditionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Terms and Conditions")
                .setMessage("HealthPro Mobile Application Terms and Conditions\n" +
                        "\n" +
                        "Please read these Terms and Conditions (\"Terms\") carefully before using the HealthPro mobile application (\"App\"). By using the App, you agree to be bound by these Terms. If you do not agree with any part of these Terms, do not use the App.\n" +
                        "\n" +
                        "1. Privacy and Data Collection: We may collect and process your full name, blood type, location, walking movement, and emergency mobile number. We handle your personal information in accordance with our Privacy Policy.\n" +
                        "\n" +
                        "2. App Usage: You may use the App for personal, non-commercial purposes. Do not modify, distribute, sell, or misuse the App.\n" +
                        "\n" +
                        "3. User Responsibilities: You are responsible for maintaining the confidentiality of your account information and providing accurate data.\n" +
                        "\n" +
                        "4. Use of Data: We may collect aggregated and anonymized data for statistical and improvement purposes. We may engage third-party service providers to assist in improving the App.\n" +
                        "\n" +
                        "5. Health Information Disclaimer: The App is for informational purposes only and not a substitute for professional medical advice. Consult a qualified healthcare provider before making healthcare decisions.\n" +
                        "\n" +
                        "6. Disclaimer of Warranties: The App is provided \"as is\" without warranties. We do not guarantee accuracy or uninterrupted operation.\n" +
                        "\n" +
                        "7. Limitation of Liability: We are not liable for any damages arising from the use or inability to use the App.\n" +
                        "\n" +
                        "8. Indemnification: You agree to indemnify us against any claims or damages related to your use of the App.\n" +
                        "\n" +
                        "9. Modifications to the Terms: We may update these Terms at any time. Continued use of the App implies acceptance of the changes.\n" +
                        "\n")
                .setPositiveButton("I agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .setIcon(R.drawable.handshake)
                .show();
    }


    public void register(String email, String password, UserHelperClass helperClass) {
        // Perform validation checks for each field
        if (email.isEmpty()) {
            usernameregister.setError("Email is required");
            return;
        } else {
            usernameregister.setError(null);
        }

        if (password.isEmpty()) {
            passwordregister.setError("Password is required");
            return;
        } else {
            passwordregister.setError(null);
        }

        if (helperClass.getFullname().isEmpty()) {
            fullnameregister.setError("Full Name is required");
            return;
        } else {
            fullnameregister.setError(null);
        }

        if (helperClass.getAddress().isEmpty()) {
            addressregister.setError("Address is required");
            return;
        } else {
            addressregister.setError(null);
        }

        if (contactregister.getText().toString().isEmpty()) {
            contactregister.setError("Contact is required");
            return;
        } else {
            contactregister.setError(null);
        }

        // Proceed with the registration process
        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            if (result != null && result.getSignInMethods() != null && result.getSignInMethods().size() > 0) {
                                // Email already exists
                                Toast.makeText(Register.this, "The email is already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                // Email doesn't exist, proceed with registration
                                mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Registration success
                                                    Log.d("Register", "createUserWithEmail:success");
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    String uid = user.getUid();

                                                    // Send email verification
                                                    sendEmailVerification(user);

                                                    // Get the selected country code from the CountryCodePicker
                                                    CountryCodePicker countryCodePicker = findViewById(R.id.countryCodePicker);
                                                    String countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();

                                                    // Get the contact number from the TextInputLayout
                                                    String contactNumber = contactregister.getText().toString();

                                                    // Concatenate the country code and contact number
                                                    String contact = countryCode + contactNumber;

                                                    // Set the updated contact value in the UserHelperClass
                                                    helperClass.setContact(contact);

                                                    // Save user data to the Firebase Realtime Database
                                                    DatabaseReference userRef = reference.child(uid);
                                                    userRef.setValue(helperClass);

                                                    Intent intent = new Intent(Register.this, Login.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    // Registration failed
                                                    Log.w("Register", "createUserWithEmail:failure", task.getException());
                                                    Toast.makeText(Register.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            // Error occurred while checking email existence
                            Log.w("Register", "fetchSignInMethodsForEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Failed to register. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Verification email sent. Please check your email.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("Register", "sendEmailVerification:failure", task.getException());
                            Toast.makeText(Register.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String gender = parent.getItemAtPosition(position).toString();
        // You can perform additional actions based on the selected gender if needed
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finish();
    }
}