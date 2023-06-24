import React from 'react';
import { createStackNavigator } from '@react-navigation/stack';
import TrailPage from '../Trail';
import PinPage from '../Pin';
import PinListSection from '../../Components/PinListSection';
import PinMedia from '../PinMedia';
import Trails from '../Trails';


const trailStack = createStackNavigator()

export default function TrailsStackScreen() {    

    return (
      <trailStack.Navigator>
        <trailStack.Screen name="trails" component={Trails} options={{ headerShown: false }} />
        <trailStack.Screen name="trail" component={TrailPage} options={{ headerShown: false }} />        
        <trailStack.Screen name="pin" component={PinPage} options={{ headerShown: false }} />
        <trailStack.Screen name="pinList" component={PinListSection} options={{ headerShown: false }} />
        <trailStack.Screen name="media" component={PinMedia} options={{ headerShown: false }} />
      </trailStack.Navigator>
    );
}

