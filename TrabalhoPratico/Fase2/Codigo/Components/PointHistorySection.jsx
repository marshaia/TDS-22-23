

import { useSelector} from 'react-redux';
import { StyleSheet,Text,View, useColorScheme } from 'react-native';

function PointHistorySection(props) {

    const colorScheme = useColorScheme()
    const isDark = colorScheme === 'dark'

    const {pointHistory} = props
    const points = useSelector((state) => state.trails.points);
    const point = points.find((point) => point.id === pointHistory.pin_id);

    if(point)
        return(
            <View style={[styles.pointCard, {backgroundColor: isDark ? '#55aaa3' : '#C2DCDA'}]}>
                <Text style={styles.pointName}>{point.pin_name}</Text>
                <Text style={styles.margin}>
                    <Text style={styles.bold}>Latitude: </Text>
                    {point.pin_lat}
                </Text>
                <Text style={styles.margin}>
                    <Text style={styles.bold}>Longitude: </Text>
                    {point.pin_lng}
                </Text>
            </View>
        )
    else{
        return(<View></View>)
    }
                    
}


export default PointHistorySection


const styles = StyleSheet.create({
    pointCard: {
        marginHorizontal: 20,
        marginTop: 10,
        position: 'relative',
        height: 100,
        justifyContent: 'center',
        backgroundColor: "#C2DCDA",
    },
    pointName: {
        fontSize: 20,
        fontWeight: 'bold',
        marginLeft: 10,
    },
    bold: {
        fontWeight: "bold",
    },
    margin: {
        marginLeft: 10,
    }
  });