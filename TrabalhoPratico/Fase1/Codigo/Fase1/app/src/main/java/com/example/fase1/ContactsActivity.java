package com.example.fase1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fase1.Adapters.ContactsAdapter;
import com.example.fase1.Data.Contact;
import com.example.fase1.ViewModel.ContactsViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsActivity extends BaseActivity {

    private ArrayList<Contact> contacts;
    private ContactsAdapter adapter;

    @BindView((R.id.recyclerViewContacts))
    RecyclerView view;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_contacts;
    }

    @Override
    protected int getNavBarItemSelected() {
        return R.id.navBar_contactos;
    }

    @Override
    protected void afterNavBar() {
        this.contacts = new ArrayList<>();

        setTitle("Contacts");
        ButterKnife.bind(this);

        adapter = new ContactsAdapter(contacts);
        view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        view.setAdapter(adapter);

        ContactsViewModel vm = new ViewModelProvider(this).get(ContactsViewModel.class);

        LiveData<List<Contact>> contacts = vm.getAllContacts();

        contacts.observe(this, contacts1 -> {
            ArrayList<Contact> contactsArrayList = new ArrayList<>(contacts1);
            adapter.setContacts(contactsArrayList);
            view.setAdapter(adapter);
        });

    }
}