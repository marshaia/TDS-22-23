package com.example.fase1.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fase1.Data.Contact;
import com.example.fase1.Repositories.ContactRepository;

import java.util.List;

public class ContactsViewModel extends AndroidViewModel {

    private ContactRepository contactRepository;

    private final LiveData<List<Contact>> allContacts;

    public ContactsViewModel(Application application) {
        super(application);
        contactRepository = new ContactRepository(application);
        allContacts = contactRepository.getAllContacts();
    }

    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }

}