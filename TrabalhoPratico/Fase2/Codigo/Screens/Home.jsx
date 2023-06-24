import React, { useEffect } from 'react';
import { useSelector,useDispatch } from 'react-redux';
import {ScrollView,StyleSheet,Text,View,Image,Linking,TouchableOpacity,useColorScheme,} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import { putUser } from '../features/userSlice';
import { fetchApp } from '../features/appSlice';


function getUser (session, token) {

    const dispatch = useDispatch();

    fetch('https://c5a2-193-137-92-29.eu.ngrok.io/user', {
        method: 'GET',
        headers : {
            'Content-Type': 'application/json',
            'Cookie' : `csrftoken=${  token  };sessionid=${  session}`,
        }})
    .then(response => response.json())
    .then(user => {  
        dispatch(putUser(user));
    })
}


function HomePage() {

    const colorScheme = useColorScheme()
    const isDark = colorScheme === 'dark'
    const dispatch = useDispatch()

    const cookies = useSelector((state) => state.user.cookies);
    getUser(cookies.session, cookies.token)

    const app = useSelector((state) => state.app.app);
    const partners = useSelector((state) => state.app.partners);
    const socials = useSelector((state) => state.app.socials);

    useEffect(() => {
        if(app.app_name === "")
            dispatch(fetchApp())
    },[])

    if (Object.keys(app).length === 0) {
        return (
        <View> 
            <Text>
                Loading App info...
            </Text>
        </View>)
    }


    return (
        <ScrollView style={{backgroundColor: isDark ? '#303030' : ''}}>
            <View style={styles.containerLogo}>
                <Image style={styles.imageLogo} source={require('../images/icon.png')} />
            </View> 

            <View style={styles.containerName}>
                <Text style={styles.appName}>{app.app_name}</Text>
                <Text style={styles.appDesc}>{app.app_desc}</Text>
            </View>
          

            <View style={styles.container}>
                
                <Text style={styles.normalText}>{app.app_landing_page_text}</Text>
                
                <View style={[styles.containerMaps, styles.greyContainer, {backgroundColor: isDark ? '#555555' : '#DDDDDD'}]}>
                    <Image style={styles.imageMaps} source={require('../images/maps_icon.png')} />
                    <Text style={styles.normalText}>NOTE: This app needs Google Maps in order to work properly, so if you don't have it, please install it first!</Text>
                </View>
                

                <View style={styles.separator} />

                <Text style={styles.boldText}>Partners</Text>

                    <View style={styles.flex}>
                    <Image style={[styles.imageUminho, styles.roundedBorders]} source={require('../images/uminho.png')} />

                    <View style={{alignSelf: 'center', marginHorizontal: 15}}> 
                        <Text style={{ fontSize: 16, alignSelf:'center', fontWeight: 'bold'}}>
                            {partners.partner_name}
                        </Text>

                        <TouchableOpacity onPress={() => Linking.openURL(partners.partner_url)}>
                            <Text style={{fontSize: 13, alignSelf:'center',textDecorationLine: 'underline'}}>{partners.partner_url}</Text>
                        </TouchableOpacity>
                        
                        <Text style={styles.smallText}>{partners.partner_phone}</Text>
                        <Text style={styles.smallText}>{partners.partner_mail}</Text>
                    </View>
                    </View>



                    <View style={styles.separator} />

                    <View>
                        <Text style={styles.boldText}>About us</Text>

                        <View style={styles.flex}>
                            <Icon name="logo-facebook" color={isDark ? "#5180dd" : "#4267B2"} size={22} style={{marginRight:10}}/>
                            <TouchableOpacity onPress={() => Linking.openURL(socials.social_url)}>
                                <Text style={{textDecorationLine: 'underline'}}>
                                    {socials.social_name}
                                </Text>
                            </TouchableOpacity>
                        </View>
                    </View>

            </View>
        </ScrollView>
    );
}	


const styles = StyleSheet.create({

    appName: {
        fontSize: 30,
        fontWeight: 'bold',
    },

    appDesc: {
        fontSize: 20,
        marginVertical: 5,
        fontWeight: 'bold',
    },

    normalText: {
        fontSize: 13,
        alignSelf:'center',
    },

    smallText: {
        fontSize: 13,
        alignSelf:'center',
    },

    boldText: {
        fontSize: 18,
        textTransform: 'uppercase',
        alignSelf:'center',
        fontWeight: 'bold',
        color: '#00BF63',
    },

    boldTitle: {
        fontWeight: 'bold',
        fontSize: 19,
    },

    container: {
      flex:1,
      margin: 10,
      marginTop: 10,
    },
    
    greyContainer: {
        flex: 1,
        margin: 10,
        borderRadius: 10,
        padding: 20,
    },

    flexSpaceBetween: {
        flexDirection: 'row',
        margin: 10,
        justifyContent: 'space-between',
    },

    flex: {
        flexDirection: 'row',
        margin: 10,
        justifyContent: 'center'
    },

    containerLogo: {
        alignItems:'center',
        justifyContent: 'center',
    },

    containerName: {
        alignItems:'center',
    },

    containerMaps: {
        flexDirection: 'row',
        marginTop: 25,
        paddingHorizontal: 50,
        alignItems:'center',
        justifyContent: 'center',
    },

    imageLogo: {
        width: 100,
        height: 100,
        alignSelf: 'center',
        resizeMode: 'contain',
        margin: 10,
    },

    imageMaps: {
        width: 50,
        height: 50,
        alignSelf: 'center',
        resizeMode: 'contain',
        marginRight: 10,
    },

    roundedBorders: {
        borderRadius: 10,
        overflow: 'hidden',
    },

    imageUminho: {
        width: 70,
        height: 70,
        alignSelf: 'center',
        resizeMode: 'contain',
        marginHorizontal: 20,
    },

    separator: {
        height: 1,
        backgroundColor: 'gray',
        marginVertical: 15,
    },
});

export default HomePage;
