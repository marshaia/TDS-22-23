
import { StyleSheet,Text,View,Image,useColorScheme, } from 'react-native';

function TrailSection(props) {

    const colorScheme = useColorScheme()
    const isDark = colorScheme === 'dark'

    const {trail} = props

    return(
        <View style={[styles.trailCard, {backgroundColor: isDark ? '#55aaa3' : '#C2DCDA'}]}>
            <Text style={styles.trailName}>{trail.trail_name}</Text>
            <Text style={styles.trailDuration}>{trail.trail_duration} minutos</Text>
            {trail.trail_img &&
            <Image source={{ uri: trail.trail_img }} style={styles.trailImg}/>}
        </View>
    )
}

export default TrailSection

const styles = StyleSheet.create({
    trailCard: {
        marginHorizontal: 20,
        marginTop: 10,
        position: 'relative',
        height: 100,
        justifyContent: 'center',
    },
    trailImg: {
        height: 100,
        width: 100,
        position: 'absolute',
        right: 0,
    },
    trailName: {
        fontSize: 20,
        marginLeft: 10,
        fontWeight: 'bold'
    },
    trailDuration: {
        marginLeft: 10,
    }
  });