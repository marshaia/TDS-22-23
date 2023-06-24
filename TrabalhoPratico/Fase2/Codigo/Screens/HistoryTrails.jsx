import React from 'react';
import { useEffect } from 'react';
import { fetchTrails } from '../features/trailsSlice';
import { useSelector, useDispatch } from 'react-redux';
import { View, FlatList,TouchableOpacity, } from 'react-native';

import TrailHistorySection from '../Components/TrailHistorySection';


function HistoryTrails({navigation}) {

    const trails = useSelector((state) => state.trails.trails);
    const trailsHistory = useSelector((state) => state.history.trails);

    const dispatch = useDispatch();

    useEffect(() => {
        if(trails.length === 0)
            dispatch(fetchTrails());
    }, []);

    function onPress(trailId){
      navigation.navigate('trail',{trailId}) // eliminar
    }

    function renderTrail({item}){
      return(
        <TouchableOpacity onPress={ () => onPress(item.trail_id)}>
          <TrailHistorySection trailHistory={item} />
        </TouchableOpacity>
      )
    }

    return (
        <View>
        <View> 
            <FlatList
            data={trailsHistory}
            keyExtractor={trail => trail.id}
            renderItem={renderTrail}
            />
        </View>
        </View>
    );
}

export default HistoryTrails;

