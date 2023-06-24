import { Text,View,StyleSheet,Linking,TouchableOpacity, } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

function ContactSection(props) {

    const {contact} = props

    function goToDial(){
        const dialNumber = `tel:${contact.contact_phone}`;
        Linking.openURL(dialNumber);
    }

    return(
        <View style={styles.contactSection}>
            <Text style={styles.contactName}>{contact.contact_name}</Text>
            <Text style={styles.contactMail}>
                <Text style={styles.bold}>Mail: </Text>
                {contact.contact_mail}
            </Text>
            <Text>
                <Text style={styles.bold}>Phone: </Text>
                {contact.contact_phone}
            </Text>
            <Text>
                <Text style={styles.urlLabel}>URL: </Text>
                <TouchableOpacity onPress={() => Linking.openURL(contact.contact_url)}>
                    <Text style={styles.url}>{contact.contact_url}</Text>
                </TouchableOpacity>
            </Text>

            <Icon name="call" style={styles.phone} size={30} onPress={goToDial}></Icon>
        </View>
    )
}

export default ContactSection


const styles = StyleSheet.create({
    contactSection: {
        marginHorizontal: 20,
        marginTop: 40,
        position: 'relative',
        height: 100,
        justifyContent: 'center',
    },
    contactName: {
        fontSize: 20,
        fontWeight: 'bold',
    },
    contactMail: {
        marginTop: 20,
    },
    bold: {
        fontWeight: 'bold'
    },
    phone: {
        position: 'absolute',
        right: 0,
        top: 0,
        color: "#00BF63",
    },
    url: {
        textDecorationLine: "underline",
    },
    urlLabel: {
        fontWeight: "bold",
    }
  });