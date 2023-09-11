package com.example.chat_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatWin extends AppCompatActivity {


    String reciverimg, reciverUid,reciverName,SenderUID;
    CircleImageView profile;
    TextView reciverNName;

    CardView sendbtn;
    EditText textmsg;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;

    public static String senderImg;
    public static String reciverIImg;


    String senderRoom,reciverRoom;
    RecyclerView mmessagesAdapter;

    ArrayList<msgModelclass> messagesArrayList;

    messagesAdapter messagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_win);

        getSupportActionBar().hide();

        reciverName = getIntent().getStringExtra("nameeee");
        reciverimg = getIntent().getStringExtra("reciverImg");
        reciverUid = getIntent().getStringExtra("uid");

        firebaseAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

         profile=findViewById(R.id.profileimgg);
         reciverNName=findViewById(R.id.recivername);

        Picasso.get().load(reciverimg).into(profile);
        reciverNName.setText(""+reciverName);

        mmessagesAdapter=findViewById(R.id.msgadpter);

        messagesArrayList=new ArrayList<>();

        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mmessagesAdapter.setLayoutManager(linearLayoutManager);
        messagesAdapter=new messagesAdapter(chatWin.this,messagesArrayList);
        mmessagesAdapter.setAdapter(messagesAdapter);

        SenderUID=firebaseAuth.getUid();
        senderRoom=SenderUID+reciverUid;
        reciverRoom=reciverRoom+SenderUID;





        DatabaseReference chatrefernce=database.getReference().child("chats").child(senderRoom).child("messages");

        chatrefernce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    msgModelclass messages=dataSnapshot.getValue(msgModelclass.class);
                    messagesArrayList.add(messages);
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               Toast.makeText(chatWin.this,"Failed to load message",Toast.LENGTH_SHORT).show();
            }
        });


        DatabaseReference reference=database.getReference().child("user").child(firebaseAuth.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg=snapshot.child("profilepic").getValue().toString();
                reciverIImg=reciverimg;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendbtn=findViewById(R.id.sendbtnn);
        textmsg=findViewById(R.id.textmsg);

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message= textmsg.getText().toString();
                if(message.isEmpty()){
                    Toast.makeText(chatWin.this,"Enter some Message",Toast.LENGTH_SHORT).show();
                }
                textmsg.setText("");
                Date date=new Date();
                msgModelclass messagess=new msgModelclass(message,SenderUID,date.getTime());

                database=FirebaseDatabase.getInstance();
                database.getReference().child("chats").child("senderRoom").child("messages").
                        push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats").child("reciverRoom").child("messages").
                                        push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                            }
                        });


            }
        });


    }
}