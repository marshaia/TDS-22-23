import React from 'react';
import { useSelector } from 'react-redux';
import { View, FlatList,TouchableOpacity, } from 'react-native';

import PointHistorySection from '../Components/PointHistorySection';


function HistoryPoints({navigation}) {

    const points = useSelector((state) => state.trails.points);
    const pointsHistory = useSelector((state) => state.history.pins);

    function onPress(item){
      const pin = points.find((point) => point.id === item.pin_id);
      const trailID = item.id;
      navigation.navigate('pin', {pin, trailID})
    }

    function renderPoint({item}){
      return(
        <TouchableOpacity onPress={() => onPress(item)}>
          <PointHistorySection pointHistory={item} />
        </TouchableOpacity>
      )
    }

    return (
        <View>
        <View> 
            <FlatList
              data={pointsHistory}
              keyExtractor={point => point.id}
              renderItem={renderPoint}
            />
        </View>
        </View>
    );
}

export default HistoryPoints;

