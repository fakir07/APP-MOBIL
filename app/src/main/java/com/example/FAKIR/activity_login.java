package com.example.FAKIR;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class activity_login extends AppCompatActivity {
    EditText mEmail,mPassword1;
    Button mloginBtn;
    FirebaseAuth fAuth;
    TextView tvCreateAccount,forgotTextLink;
    String Email,Password1;
    SharedPreferences prefs;
    Intent iHomePage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Go Full Screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //Check if user is logged in
        prefs = getSharedPreferences("user_details",MODE_PRIVATE);
        iHomePage = new Intent(getApplicationContext(),MainActivity.class);
        if (prefs.contains("email") && prefs.contains("pwd")){
            startActivity(iHomePage);
            this.finish();
        }
        mEmail=findViewById(R.id.email1);
        mPassword1=findViewById(R.id.password1);
        fAuth=FirebaseAuth.getInstance();
        mloginBtn=findViewById(R.id.btn_login);
        tvCreateAccount = findViewById(R.id.tv_create_account);
        forgotTextLink=findViewById(R.id.tv_forgot_pwd);



        mloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = mEmail.getText().toString().trim();
                Password1 = mPassword1.getText().toString().trim();

                if (TextUtils.isEmpty(Email)) {
                    mEmail.setError("Eamil is required.");
                }
                if (TextUtils.isEmpty(Password1)) {
                    mPassword1.setError("Password is required ");
                    return;
                }
                if (mPassword1.length() < 6) {
                    mPassword1.setError(" password Must be >=6");
                    return;
                }

                fAuth.signInWithEmailAndPassword(Email,Password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("email",Email);
                            editor.putString("pwd",Password1);
                            editor.commit();
                            Toast.makeText(activity_login.this, "logged in sucssfully.", Toast.LENGTH_SHORT).show();
                            startActivity(iHomePage);
                        } else {
                            Toast.makeText(activity_login.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });


        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_login.this, activity_regster.class));
                finish();
            }
        });


       forgotTextLink.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               final EditText restMail=new EditText(v.getContext());
               AlertDialog.Builder passwordResstDailog=new  AlertDialog.Builder(v.getContext());
               passwordResstDailog.setTitle("Reset Password");
               passwordResstDailog.setMessage("Enter Your Email To Received Link.");
               passwordResstDailog.setView(restMail);

               passwordResstDailog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                       // extract email and send link
                       String mail = restMail.getText().toString();
                       //control email
                       if (mail.isEmpty()){
                           Toast.makeText(activity_login.this, "Email is required!", Toast.LENGTH_SHORT).show();
                       }else {
                           fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   Toast.makeText(activity_login.this, "Reste Link Sent To Your Eamil ", Toast.LENGTH_SHORT).show();

                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Toast.makeText(activity_login.this, "Error ! Reset Link is Not Sent " + e.getMessage(), Toast.LENGTH_SHORT).show();
                               }

                           });
                       }
                   }
               });
               passwordResstDailog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                         //close the dailog
                   }
               });
               passwordResstDailog.create().show();



           }
       });





    }



    public void onBackPressed(){
        AlertDialog.Builder alertDialogBuilder =new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Confirm Exit..!!");

        alertDialogBuilder.setIcon(R.drawable.ic_exit);

        alertDialogBuilder.setMessage("Are You sure You want to exit ");

        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
           finish();

            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(activity_login.this, "you clicked on cancel ", Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();
    }
}
