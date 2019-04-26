package com.example.restaurantlocator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantlocator.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
//import com.example.restaurant2eat.User;

//import android.os.Bundle;
//import android.widget.ImageView;
//import android.widget.TextView;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputName;
    private  EditText inputPhone;
    private  EditText inputPassword;
    private Button SignUpButton;
    private TextView textViewLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    String name, phone, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mDatabase = (DatabaseReference) FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        SignUpButton =findViewById(R.id.buttonSignUp);
        inputName = findViewById(R.id.InputName);
        inputPhone = findViewById(R.id.InputPhone);
        inputPassword = findViewById(R.id.InputPassword);
        textViewLogin =  findViewById(R.id.textViewLogin);
        progressDialog = new ProgressDialog(this);


        SignUpButton.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);



    }

    private void registerUser() {
        name = inputName.getText().toString().trim();
        phone = inputPhone.getText().toString().trim();
        password = inputPassword.getText().toString().trim();


        if (name.isEmpty())
        {
            inputName.setError("Name is required");
            inputName.requestFocus();

        }


        if (phone.isEmpty())
        {
            inputPhone.setError("Email is required");
            inputPhone.requestFocus();

        }
        if (phone.length() < 10)
        {
            inputPhone.setError("Phone Number Must be exactly 10 digit");
            inputPhone.requestFocus();
        }

       /* if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Please enter a valid Email");
            inputEmail.requestFocus();

        }*/


        if (password.isEmpty()) {
            inputPassword.setError("Password is required");
            inputPassword.requestFocus();

        }

        if (password.length() < 6) {
            inputPassword.setError("Minimum length of password should be 6");
            inputPassword.requestFocus();

        }

        progressDialog.setMessage("Registering User");
        progressDialog.show();
        ValidatephoneNumber(name, phone, password);
    }

/*        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                   //sendUserData();
                    finish();
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        inputEmail.setError("This email is already registered");
                        inputEmail.requestFocus();
                        progressDialog.dismiss();
                        return;

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    Toast.makeText(SignUpActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }


            }
        });
    }
    private void writeNewUser(String userId, String email, String username) {
       final DatabaseReference RootRef;
*//*        RootRef = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> userdataMap = new HashMap<>();
        userdataMap.put("email", email);
        userdataMap.put("name", name);
        RootRef.child("Users").child(userId).setValue(userdataMap);*//*
        User user = new User(username, email);
        mDatabase.child("Users").child(userId).setValue(user);

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null) {
            onAuthSuccess(firebaseAuth.getCurrentUser());
            finish();
            startActivity(new Intent(this, LoginActivity.class));

        }
    }*/




      /*  private void sendUserData(){
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
                User user = new User(name, email);
                myRef.setValue(user);
            }*/


    private void ValidatephoneNumber(final String name, final String phone, final String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("name", name);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignUpActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(SignUpActivity.this, "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }




            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }






    @Override
    public void onClick(View view)
    {
        if(view == SignUpButton )
        {
            registerUser();

        }
        if(view == textViewLogin){
            finish();
            startActivity(new Intent(this,LoginActivity.class));//loginActivity

    }
    }
}
