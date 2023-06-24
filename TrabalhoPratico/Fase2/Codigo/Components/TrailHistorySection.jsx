

import { useSelector} from 'react-redux';
import { StyleSheet,Text,View,Image, useColorScheme } from 'react-native';

import Icon from 'react-native-vector-icons/Ionicons';



function TrailHistorySection(props) {

    const colorScheme = useColorScheme()
    const isDark = colorScheme === 'dark'

    const {trailHistory} = props
    const trails = useSelector((state) => state.trails.trails);
    const trail = trails.find((trail) => trail.id === trailHistory.trail_id);

    if(trail)
        return(
            <View style={[styles.trailCard, {backgroundColor: isDark ? '#55aaa3' : '#C2DCDA'}]}>
                <Text style={styles.containter}>
                    <Text style={styles.trailName}>{trail.trail_name}</Text>
                    {trailHistory.state === "completed" && 
                        <Icon name="checkmark-done-outline" style={styles.done} size={30}></Icon>}
                    {trailHistory.state === "canceled" && 
                        <Icon name="close-circle-outline" style={styles.canceled} size={30}></Icon>}
                    {trailHistory.state === "onHold" && 
                        <Icon name="timer-outline" style={styles.onHold} size={30}></Icon>}
                </Text>
                <Text style={styles.trailDuration}>{trail.trail_duration} minutos</Text>
                {trail.trail_img &&
                    <Image source={{ uri: trail.trail_img }} style={styles.trailImg}/>}
            </View>
        )
    else{
        return(<View></View>)
    }
                    
}


export default TrailHistorySection


const styles = StyleSheet.create({
    trailCard: {
        marginHorizontal: 20,
        marginTop: 10,
        position: 'relative',
        height: 100,
        justifyContent: 'center',
        backgroundColor: "#C2DCDA",
    },
    trailImg: {
        height: 100,
        width: 100,
        position: 'absolute',
        right: 0,
    },
    trailName: {
        fontSize: 20,
        fontWeight: 'bold'
    },
    trailDuration: {
        marginLeft: 10,
    },
    done: {
        color: "#00BF63",
        position: "absolute",
        paddingLeft: 20,
    },
    canceled: {
        color: "red",
        position: "absolute",
        paddingLeft: 20,
    },
    onHold: {
        color: "yellow",
        position: "absolute",
        paddingLeft: 20,
    },
    containter: {
        marginLeft: 10,
    }
  });