package com.example.FAKIR;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class activity_regster extends AppCompatActivity {
    EditText mFullName,mEmail,mContra,mCin,mPassword1,mPassword2;
    Button mSeregester;
    String FullName,Email,Contra,Cin,Password1,Password2,userID,fPassword;
    FirebaseAuth fAuth;
    TextView btlogin;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Go Full Screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        //Initialisation
                mFullName       = findViewById(R.id.fullname);
                mEmail          = findViewById(R.id.email1);
                mContra         = findViewById(R.id.contra);
                mCin            = findViewById(R.id.cin1);
                mPassword1      = findViewById(R.id.password1);
                mPassword2      = findViewById(R.id.password2);
                mSeregester = findViewById(R.id.seregester);
                btlogin =findViewById(R.id.btn_log);


                fAuth =FirebaseAuth.getInstance();
                fstore =FirebaseFirestore.getInstance();
                if (fAuth.getCurrentUser()!=null){
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();

        }

        mSeregester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = mEmail.getText().toString().trim();
                Password1 = mPassword1.getText().toString().trim();
                Password2 = mPassword2.getText().toString().trim();
                FullName = mFullName.getText().toString().trim();
                Cin = mCin.getText().toString().trim();
                Contra = mContra.getText().toString().trim();


                        if (TextUtils.isEmpty(Email)) {
                            mEmail.setError("Eamil is required.");
                        }

                        if (TextUtils.isEmpty(Password1)) {
                            mPassword1.setError("Enter your password.");
                        }

                        if (TextUtils.isEmpty(Password2)) {
                            mPassword2.setError("Enter your confirmation password");

                        }
                        if (mPassword1.length() < 6) {
                            mPassword1.setError(" password Must be >=6");
                            return;
                        }
                        if (mPassword2.length() < 6) {
                            mPassword2.setError(" password Must be >=6");
                            return;
                        }
                        if (!Password1.equals(Password2)){
                            Toast.makeText(activity_regster.this, "password dosnt matchs.", Toast.LENGTH_SHORT).show();}
                        else{
                    fAuth.createUserWithEmailAndPassword(Email,Password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task){
                            if (task.isSuccessful()){


                                Toast.makeText(activity_regster.this, "user craeat .", Toast.LENGTH_SHORT).show();
                                userID =fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference =fstore.collection("users").document(userID);
                                Map<String,Object>user =new HashMap<>();
                                user.put("fName",FullName);
                                user.put("email",Email);
                                user.put("cin",Cin);
                                user.put("contra",Contra);
                                user.put("passsword",Password1);

                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Tag","onSuccess: user Profile is created for" + userID);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Tag","OnFailure:"+e.toString());

                                    }
                                });


                                startActivity(new Intent(getApplicationContext(),activity_login.class));
                                finish();
                            }else {
                                Toast.makeText(activity_regster.this, "Error !"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }

                    });
                }

                }



        });



                      btlogin.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              startActivity(new Intent(getApplicationContext(),activity_login.class));
                              finish();
                          }
      });

 }
}
