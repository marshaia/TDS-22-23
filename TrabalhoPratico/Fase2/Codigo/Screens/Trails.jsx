import React from 'react';
import { useEffect } from 'react';
import { fetchTrails } from '../features/trailsSlice';
import { useSelector, useDispatch } from 'react-redux';
import { View, FlatList,TouchableOpacity, } from 'react-native';

import TrailSection from '../Components/TrailSection';

function Trails({navigation}) {
  const trails = useSelector((state) => state.trails.trails);
  const dispatch = useDispatch();

  useEffect(() => {
    if(trails.length == 0)
      dispatch(fetchTrails());
  }, []);

  function onPress(trailId){
    navigation.navigate('trail',{trailId})
  }

  function renderTrail({item}){
    return(
      <TouchableOpacity onPress={ () => onPress(item.id)}>
        <TrailSection trail={item} />
      </TouchableOpacity>
    )
  }

  return (
    <View>
      <View> 
        <FlatList
          data={trails}
          keyExtractor={trail => trail.id}
          renderItem={renderTrail}
        />
      </View>
    </View>
  );
}

export default Trails;

