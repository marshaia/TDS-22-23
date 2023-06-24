import React from 'react'
import { useSelector } from 'react-redux';
import {ScrollView,View,Text,useColorScheme,StyleSheet,TouchableOpacity} from 'react-native'

import Icon from 'react-native-vector-icons/Ionicons';



export default function Profile({navigation}) {

    const colorScheme = useColorScheme()
    const isDark = colorScheme === 'dark'


    function goToHistoryTrails(){
        navigation.navigate('historyTrails')
    }

    function goToHistoryPoints(){
        navigation.navigate('historyPoints')
    }

    const user = useSelector((state) => state.user.user);
    const isPremium = useSelector((state) => state.user.isPremium);

    return (
        <ScrollView style={{backgroundColor: isDark ? '#303030' : '', paddingHorizontal: 20}}>
            <View style={[styles.container, {marginTop:20}]}>

                <TouchableOpacity style={[styles.miniContainer, {backgroundColor: '#00BF63', alignSelf: 'flex-end', flexDirection: 'row'}]}
                    onPress={() => {navigation.navigate("login")}} >
                    <Text style={{marginRight: 5}}>Log Out</Text>
                    <Icon name="log-out-outline" color={isDark ? 'white' : 'black'} size={20}/>
                </TouchableOpacity>


                <View style={{alignItems: 'center', marginTop: 20}}>
                    <Icon name="person" color="#00BF63" size={125}/> 
                
                    <Text style={styles.boldTitle}>{user.first_name} {user.last_name}</Text>
                    <Text style={styles.normalText}>@{user.username}</Text>
                </View>


                { user.email.length > 0 ? 
                <View style={{marginTop:20}}>
                    <Text style={styles.boldText}>Email</Text>
                    <View style={[styles.miniContainer,{backgroundColor: isDark ? '#555555' : '#DDDDDD'}]}>
                        <Text>{user.email}</Text>
                    </View>
                </View> : null }


                <View style={{marginTop:20}}>
                    <Text style={styles.boldText}>User Type</Text>
                    <View style={[styles.miniContainer,{backgroundColor: isDark ? '#555555' : '#DDDDDD'}]}>
                        <Text style={{color: isPremium ? '#00BF63' : 'black'}}>{isPremium ? 'Premium' : 'Standard'}</Text>
                    </View>
                </View>




                {isPremium ? 
                <View style={{marginTop:20}}>
                    <Text style={styles.boldText}>History</Text>

                    <TouchableOpacity style={[styles.miniContainer, {backgroundColor: isDark ? '#555555' : '#DDDDDD'}]}
                        onPress={() => goToHistoryTrails()} >
                        <View style={{flexDirection: 'row'}}>
                            <Icon name="map-outline" size={20} />
                            <Text style={{marginLeft: 5}}>Trails</Text>
                        </View>
                        <Icon name="arrow-forward" color="#00BF63" size={20}/>
                    </TouchableOpacity>



                    <TouchableOpacity style={[styles.miniContainer, {backgroundColor: isDark ? '#555555' : '#DDDDDD'}]}
                        onPress={() => goToHistoryPoints()} >
                        <View style={{flexDirection: 'row'}}>
                            <Icon name="location-outline" size={20} />
                            <Text style={{marginLeft: 5}}>Pins</Text>
                        </View>
                        <Icon name="arrow-forward" color="#00BF63" size={20}/>
                    </TouchableOpacity>
                 

                </View>
                : null }

            </View>
        </ScrollView>
    )
}

const styles = StyleSheet.create({

    normalText: {
        fontSize: 13,
        alignSelf:'center',
    },

    smallText: {
        fontSize: 13,
        alignSelf:'center',
    },

    boldText: {
        fontSize: 13,
        fontWeight: 'bold',
    },

    boldTitle: {
        fontSize: 19,
        textTransform: 'uppercase',
        alignSelf:'center',
        fontWeight: 'bold',
    },

    container: {
      margin: 10,
    },

    miniContainer: {
        padding: 10,
        borderRadius: 5,
        flexDirection:'row', 
        justifyContent: 'space-between', 
        marginTop:10,
    },
});