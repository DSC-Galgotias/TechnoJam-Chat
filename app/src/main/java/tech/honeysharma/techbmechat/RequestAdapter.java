package tech.honeysharma.techbmechat;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import tech.honeysharma.techbmechat.Chat.Users;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    Context context;
    ArrayList<Users> mRequests;

    public RequestAdapter(Context context , ArrayList<Users> mRequests){
        this.context = context;
        this.mRequests = mRequests;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_list_item , viewGroup , false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.textView.setText(mRequests.get(i).getName());
    }

    @Override
    public int getItemCount() {
        Log.e("RequestAdapter" , "Size recieved in Adapter : " + mRequests.size());
        return mRequests.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txt);
        }
    }
}