import { useEffect, useState, useRef } from 'react'
import { View,Text,Image,StyleSheet,Button, } from 'react-native'

import Video from 'react-native-video';
import Sound from 'react-native-sound'


function AudioPlayer(audio) {
  let sound
  
  useEffect(() => {
    sound = new Sound(audio.media_file, Sound.MAIN_BUNDLE, (error) => {
      if (error) {
        console.log('Error loading sound', error);
      }
    });

    return () => {
      // Stop and release the sound when leaving the screen
      if (sound) {
        sound.stop();
        sound.release();
      }
    };
  }, []);

  return (      
    <View style={{flexDirection: 'row', justifyContent: 'space-evenly', marginTop:20}}>
      <Text style={styles.audio}>Audio</Text>
      <Button title="Play"  color="#00BF63" onPress={() => sound.play()} />
      <Button title="Pause" color="#00BF63" onPress={() => sound.pause()} />
      <Button title="Reset" color="#00BF63" onPress={() => sound.setCurrentTime(0)} />
    </View>
  );
}



function VideoPlayer(video) {
    const videoRef = useRef(video);
    const [paused, setPaused] = useState(true);
  
    const onPlayPause = () => {
      setPaused(!paused);
    };
  
    return (
      <View style={{ alignItems: 'center' , marginTop:20}}>
        <Video
          ref={videoRef}
          source={{ uri: video.media_file }}
          paused={paused}
          style={styles.video}
        />
  
        <Button title={paused ? 'Play' : 'Pause'} color="#00BF63" onPress={onPlayPause} />
      </View>
    );
  }





function getMediaTemplate (media) {

    switch (media.media_type) {
        case "I":
            return <Image style= {[styles.image, {marginTop:20}]} source={{ uri: media.media_file }}/>

        case "V":
            return VideoPlayer(media)

        case "R":
            return AudioPlayer(media)
          
        default: return <View/>
    }
}


function MediaList (props) {

    const {media} = props
    const template = getMediaTemplate (media)

    return(
        <View style={[styles.container, {marginBottom: 10}]}>
            {template}
        </View>
    )
}


const styles = StyleSheet.create({
    container: {
        borderRadius: 10,
    },

    image: {
        width: '100%',
        height: 250,
        resizeMode: 'contain'
    },
    video: {
      height: 200, 
      width: '100%'
    },
    audio: {
      fontWeight: 'bold', 
      alignSelf: 'center', 
      fontSize: 15
    }
})

export default MediaList;