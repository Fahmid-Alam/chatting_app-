package com.example.chat_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class registration extends AppCompatActivity {

    TextView loginbut;
    EditText rg_username, rg_email , rg_password, rg_repassword;
    Button rg_signup;

    CircleImageView rg_profileImg;

    FirebaseAuth auth;

    Uri imageUri;
    String imageuri;

    String emailPattern = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";    // email pattern for checking validity of email

    FirebaseDatabase database;
    FirebaseStorage storage=FirebaseStorage.getInstance();

    //String imageuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();

        loginbut = findViewById(R.id.loginbut);
        rg_username = findViewById(R.id.rgusername);
        rg_email = findViewById(R.id.rgemail);
        rg_password = findViewById(R.id.rgpassword);
        rg_repassword = findViewById(R.id.rgrepassword);

        rg_profileImg = findViewById(R.id.profilerg0);
        rg_signup = findViewById(R.id.signupbutton);

        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        auth=FirebaseAuth.getInstance();


        loginbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(registration.this, login.class);
                startActivity(i);
                finish();
            }
        });

        rg_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namee=rg_username.getText().toString();
                String emaill=rg_email.getText().toString();
                String Password=rg_password.getText().toString();
                String cPassword=rg_repassword.getText().toString();
                String status="Hey I'm using Anytime Messenger";

                if(TextUtils.isEmpty(namee) || TextUtils.isEmpty(emaill) ||
                        TextUtils.isEmpty(Password) || TextUtils.isEmpty(cPassword)){
                    Toast.makeText(registration.this,"Please Fill Completely & Correctly",Toast.LENGTH_LONG).show();
                }
                else if(!emaill.matches(emailPattern)){
                    rg_email.setError("Invalid Email");
                }
                else if(Password.length()<6){
                    rg_password.setError("Password too small");
                }
                else if(!Password.matches(cPassword)){
                    rg_password.setError("Both password doesnt match");
                }
                else{

                    auth.createUserWithEmailAndPassword(emaill,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){

                           String id=task.getResult().getUser().getUid();
                           DatabaseReference reference=database.getReference().child("user").child(id);
                           StorageReference storageReference=storage.getReference().child("Upload").child(id);

                           if(imageUri!=null){
                               storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                   @Override
                                   public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                  if(task.isSuccessful()){
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            imageuri=uri.toString();
                                            Users users=new Users(id,namee,emaill,Password,imageuri,status);
                                       reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               if(task.isSuccessful()){
                                                   Intent i=new Intent(registration.this,login.class);
                                                   startActivity(i);
                                                   finish();
                                               }
                                               else{
                                                   Toast.makeText(registration.this,"Error in Creating User",Toast.LENGTH_SHORT).show();
                                               }
                                           }
                                       }) ;

                                        }
                                    });
                                  }
                                   }
                               });
                           }
                           else{

                               String status="Hey I'm using Anytime Messenger";
                               imageuri="https://firebasestorage.googleapis.com/v0/b/chat-app-31c16.appspot.com/o/man.png?alt=media&token=4043b52d-c2da-45c2-8d39-0ac37215e4dd";
                               Users users=new Users(id,namee,emaill,Password,imageuri,status);
                               reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if(task.isSuccessful()){
                                           Intent i=new Intent(registration.this,login.class);
                                           startActivity(i);
                                           finish();
                                       }
                                       else{
                                           Toast.makeText(registration.this,"Error in Creating User",Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               }) ;

                           }

                       }
                       else{
                           Toast.makeText(registration.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                       }
                        }
                    });
                }

            }
        });

        rg_profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"PLease Select Picture"),10);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(data!=null){
                imageUri=data.getData();
                rg_profileImg.setImageURI(imageUri);
            }
        }
    }
}