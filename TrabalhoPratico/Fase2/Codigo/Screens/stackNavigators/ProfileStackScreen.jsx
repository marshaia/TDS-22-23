import React from 'react';
import { createStackNavigator } from '@react-navigation/stack';
import Profile from '../Profile';
import HistoryTrails from "../HistoryTrails"
import HistoryPoints from "../HistoryPoints"
import PinPage from '../Pin';
import TrailPage from '../Trail';

const profileStack = createStackNavigator()

export default function ProfileStackScreen() {

    return (
      <profileStack.Navigator>
        <profileStack.Screen name="profile" component={Profile} options={{ headerShown: false }} />
        <profileStack.Screen name="historyTrails" component={HistoryTrails} options={{ headerShown: false }} />
        <profileStack.Screen name="trail" component={TrailPage} options={{ headerShown: false }} />
        <profileStack.Screen name="historyPoints" component={HistoryPoints} options={{ headerShown: false }} />
        <profileStack.Screen name="pin" component={PinPage} options={{ headerShown: false }} />
      </profileStack.Navigator>
    );
}
