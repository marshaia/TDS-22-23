package com.example.fase1.Adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fase1.Data.Contact;
import com.example.fase1.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>{

    private ArrayList<Contact> contacts;

    public ContactsAdapter(ArrayList<Contact> contacts){
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contactcard,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String contact = Integer.toString(contacts.get(position).getContact_phone());

        holder.name.setText(contacts.get(position).getContact_name());
        holder.contact.setText(contact);
        holder.url.setText(contacts.get(position).getContact_url());
        holder.mail.setText(contacts.get(position).getContact_mail());
        holder.description.setText(contacts.get(position).getContact_desc());

        holder.phoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+contact));
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return contacts.size();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView((R.id.contactName))
        TextView name;
        @BindView((R.id.contactNumberValue))
        TextView contact;
        @BindView((R.id.contactURLvalue))
        TextView url;
        @BindView((R.id.contactMailvalue))
        TextView mail;
        @BindView((R.id.contactDescriptionvalue))
        TextView description;
        @BindView((R.id.callIcon))
        ImageView phoneIcon;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

}
