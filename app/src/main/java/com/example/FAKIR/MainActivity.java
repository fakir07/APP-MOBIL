package com.example.FAKIR;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.jetbrains.annotations.Nullable;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;
    Intent iLoginPage;
    TextView FullName,Contra,Cin,Email;
    FirebaseAuth fAuth;
    FirebaseFirestore firestore;
    String userId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Go Full Screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

               FullName=findViewById(R.id.textFullname);
               Contra=findViewById(R.id.textNcontra);
               Cin=findViewById(R.id.textCin);
               Email=findViewById(R.id.textEmail);

               fAuth=FirebaseAuth.getInstance();
               firestore=FirebaseFirestore.getInstance();

               userId=fAuth.getCurrentUser().getUid();
               final FirebaseUser user = fAuth.getCurrentUser();


   final DocumentReference documentReference=firestore.collection("users").document(userId);
       documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
           @Override
           public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
               FullName.setText(documentSnapshot.getString("fName"));
               Contra.setText(documentSnapshot.getString("contra"));
               Cin.setText(documentSnapshot.getString("cin"));
               Email.setText(documentSnapshot.getString("email"));
           }
       });


        //Check if user is logged in
               prefs = getSharedPreferences("user_details",MODE_PRIVATE);
               iLoginPage = new Intent(this,activity_login.class);
               if (!(prefs.contains("email") && prefs.contains("pwd"))){
                    startActivity(iLoginPage);
                    this.finish();
                }

            }


    public void Logout(View view){

            FirebaseAuth.getInstance().signOut();
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();
            startActivity(iLoginPage);
            finish();
        }
    }
