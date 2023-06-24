import React from 'react'
import {
    StyleSheet,
    TouchableOpacity,
    View,
    Text,
    useColorScheme,
    PermissionsAndroid, 
    ActivityIndicator
} from 'react-native'
import { ScrollView } from 'react-native-gesture-handler'
import Icon from 'react-native-vector-icons/Ionicons';
import RNFS from 'react-native-fs';
import MediaListSection from '../Components/MediaListSection'


let downloadingMedia = false
  

const downloadFile = (file) => {

    const fileUrl = file.media_file
    const split = fileUrl.split("/")
    const len = split.length
    const fileName = split[len-1]
    const destinationPath = `${RNFS.DocumentDirectoryPath}/${fileName}`;

    RNFS.downloadFile({
      fromUrl: fileUrl,
      toFile: destinationPath,
    })
    .promise.then((res) => {
        console.log(`${fileName  } downloaded! `, res);
    })
    .catch((err) => {
        console.log(`Error downloading ${  fileName  }: `, err);
    });
};



function downloadMedia(medias) {

    downloadingMedia = true

    medias.forEach(media => {
        downloadFile(media)
    });
    
    downloadingMedia = false
}


const requestStoragePermission = async (medias) => {
    try {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE,
        {
          title: 'Storage Permission',
          message: 'App needs access to your device storage to download files.',
          buttonNeutral: 'Ask Me Later',
          buttonNegative: 'Cancel',
          buttonPositive: 'OK',
        }
      );
    
      if (granted === PermissionsAndroid.RESULTS.GRANTED) {
        console.log('Downloading Media');
        downloadMedia(medias)
      } else {
        console.log('Storage permission denied');
      }
    } catch (err) {
      console.warn('Error requesting storage permission:', err);
    }
};


function clickedDownload(medias) {
    requestStoragePermission(medias);
}




function PinMedia({route}) {
    const colorScheme = useColorScheme();
    const isDark = colorScheme === 'dark';
    
    const medias = route.params;

    const showMedias = medias.map((media) => <MediaListSection key={media.id} media={media} />);

    return(
        <ScrollView style={{backgroundColor: isDark ? '#303030' : '', paddingHorizontal: 20}}>
            <View style={{marginTop: 20}}>
                {showMedias}
            </View>
            <TouchableOpacity style={[styles.miniContainer, {backgroundColor: isDark ? '#555555' : '#DDDDDD', marginTop:20}]}
                onPress={() => {clickedDownload(medias)}} >
                    { downloadingMedia ? 
                    <ActivityIndicator color="#00BF63"/>

                    : 
                    <View style={{flexDirection:'row', justifyContent: 'center'}}>
                        <Icon name="download-outline" color="#00BF63" size={20}/>
                        <Text style={{marginLeft: 5}}>Download All Media</Text>
                    </View> }

            </TouchableOpacity>
        </ScrollView>
    )
}


const styles = StyleSheet.create({
    miniContainer: {
        padding: 10,
        borderRadius: 5,
    },
})

export default PinMedia;