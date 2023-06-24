import React from 'react';
import Icon from 'react-native-vector-icons/Ionicons'
import { useDispatch,useSelector } from 'react-redux';
import { ScrollView } from 'react-native-gesture-handler'
import {View,Text,Image,Button,useColorScheme,StyleSheet,TouchableOpacity, Alert, } from 'react-native'
import { addPin } from "../features/historySlice"


function getMediaFile(pinmedia){
    let mediaFile = ""
    for(const media of pinmedia){
        if (media.media_type === "I"){
            mediaFile = media.media_file
            break;
        }
    }
    return mediaFile
}


function getBulletsPoints(points) {
    const result = [
      <Text key="properties" style={[styles.boldText, { marginTop: 20}]}>
        Properties:
      </Text>,
    ];
    for (const point of points) {
      result.push(
        <Text key={point.attrib} style={styles.normalText}>
          {`\u2022`} {point.attrib}
        </Text>
      );
    }
  
    return result;
  }
  
function addToHistory(pinId, pinName, trailId,dispatch){

    dispatch(addPin({
        pin_id: pinId,
        pin_name: pinName,
        trail_id: trailId,
    }))

    Alert.alert('Success', 'Point of Interest added to History!', [
        {text: 'OK', onPress: () => console.log('OK Pressed')},
    ]);
  
}


export default function PinPage({route,navigation}) {

    const colorScheme = useColorScheme()
    const isDark = colorScheme === 'dark'
    const dispatch = useDispatch();


    const pin = route.params.pin;
    const trailID = route.params.trailID;

    let media = ""
    if (pin.media) media = getMediaFile(pin.media)

    const isPremium = useSelector((state) => state.user.isPremium);

    const hasBulletPoints = pin.rel_pin.length > 0
    let bulletPoints 
    if (hasBulletPoints) bulletPoints = getBulletsPoints(pin.rel_pin)

    
    return (
        <ScrollView style={{backgroundColor: isDark ? '#303030' : '', paddingHorizontal: 20}}>
            <View style={{marginBottom: 20}}>
                <View style={{flexDirection: 'row', justifyContent: 'space-between', marginTop: 20}}>
                    { media != "" ? 
                    <Image source={{ uri: media }} style={[styles.image, {width: isPremium && pin.media.length > 0  ? '75%' : '100%'}]}/> 
                    : 
                    <Image style={[styles.image, {width: isPremium && pin.media.length > 0 ? '75%' : '100%'}]} source={require('../images/braga_thumbnail.jpg')}/>
                    }


                    { isPremium && pin.media.length > 0 ?
                        <View>
                            <TouchableOpacity style={[styles.miniContainer, {backgroundColor: isDark ? '#555555' : '#DDDDDD', alignItems: 'center'}]}
                                onPress={() => { navigation.navigate("media", pin.media) }} >
                                <Text>View</Text>
                                <Text>Media</Text>
                                <Icon name="images-outline" color="black" size={20}/>
                            </TouchableOpacity> 
                        </View> : null }

                </View>
                

                <Text style={styles.pinName}>{pin.pin_name}</Text>


                <View style={[styles.greyContainer, {backgroundColor: isDark ? '#555555' : '#DDDDDD',marginTop: 15}]}>
                    <Text style={styles.normalText}>
                        <Text style={styles.boldText}>Description: </Text>
                        {pin.pin_desc}
                    </Text>

                    
                    { hasBulletPoints ? bulletPoints : null} 
                </View>


                { isPremium ? 
                <View style={{marginTop: 20}}>
                    <Button onPress={() => addToHistory(pin.id,pin.pin_name,trailID,dispatch)} // para alterar aqui o id
                    title="MARK AS VISITED" color="#00BF63" 
                    />
                </View>
                : null}

            </View>
        </ScrollView>
    )
}



const styles = StyleSheet.create({

    image: {
        height: 200,
        borderRadius: 10,
        overflow: 'hidden',
    },

    normalText: {
        fontSize: 13,
    },

    boldText: {
        fontSize: 13,
        fontWeight: 'bold',
    },

    pinName: {
        marginTop: 20,
        fontSize: 20,
        fontWeight: 'bold',
    },

    greyContainer: {
        padding: 10,
        borderRadius: 5,
    },

    miniContainer: {
        padding: 10,
        borderRadius: 5,
    },
})