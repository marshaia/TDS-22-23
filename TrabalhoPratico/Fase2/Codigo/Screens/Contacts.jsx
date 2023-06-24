import React from 'react';
import { View, FlatList } from 'react-native';
import { useSelector, useDispatch } from 'react-redux';
import { useEffect } from 'react';
import { fetchApp } from '../features/appSlice';


import ContactSection from '../Components/ContactSection';

function Contacts() {

  const contacts = useSelector((state) => state.app.contacts);
  const dispatch = useDispatch();

  useEffect(() => {
    if(contacts.length === 0)
        dispatch(fetchApp());
  }, []);

  function renderContact({item}){
    return(
        <ContactSection contact={item} />
    )
  }

  return (
    <View>
      <View> 
        <FlatList
          data={contacts}
          keyExtractor={contact => contact.contact_phone}
          renderItem={renderContact}
        />
      </View>
    </View>
  );
}

export default Contacts;
