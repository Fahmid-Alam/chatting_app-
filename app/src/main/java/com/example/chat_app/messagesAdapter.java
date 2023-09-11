package com.example.chat_app;

import static com.example.chat_app.chatWin.reciverIImg;
import static com.example.chat_app.chatWin.senderImg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class messagesAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<msgModelclass> messagesAdapterArrayList;
    int ITEM_SEND=1;
    int ITEM_RECIVE=2;

    public messagesAdapter(Context context, ArrayList<msgModelclass> messagesAdapterArrayList) {
        this.context = context;
        this.messagesAdapterArrayList = messagesAdapterArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SEND){
            View view= LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);
            return new senderViewHolder(view);
        }
        else{
            View view=LayoutInflater.from(context).inflate(R.layout.reciver_layout,parent,false);
            return new reciverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        msgModelclass messages=messagesAdapterArrayList.get(position);
        if(holder.getClass()==senderViewHolder.class){
            senderViewHolder viewHolder=(senderViewHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());

            Picasso.get().load(senderImg).into(viewHolder.circleImageView);
        }
        else{
            reciverViewHolder viewHolder=(reciverViewHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());
            Picasso.get().load(reciverIImg).into(viewHolder.circleImageView);

        }

    }

    @Override
    public int getItemCount() {
        return messagesAdapterArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        msgModelclass messages=messagesAdapterArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId()))
            return ITEM_SEND;
        else
            return ITEM_RECIVE;

    }

    class senderViewHolder extends RecyclerView.ViewHolder{
      CircleImageView circleImageView;
      TextView msgtxt;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.profilerggg);
            msgtxt=itemView.findViewById(R.id.msgsendertyp);
        }
    }

    class reciverViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView msgtxt;
        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.pro);
            msgtxt=itemView.findViewById(R.id.recivertextset);

        }
    }

}
