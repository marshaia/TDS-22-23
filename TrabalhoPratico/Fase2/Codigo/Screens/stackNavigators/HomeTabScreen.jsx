import React from 'react';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import Icon from 'react-native-vector-icons/Ionicons';
import { useColorScheme } from 'react-native';


import HomePage from '../Home'
import TrailsStackScreen from './TrailsStackScreen';
import ProfileStackScreen from './ProfileStackScreen';
import Contacts from '../Contacts';

const home = "Home"
const trails = "Trails"
const profile = "Profile"
const contacts = "Emergency"


const Tab = createBottomTabNavigator()


function HomeTabScreen() {

  const colorScheme = useColorScheme()
  const isDark = colorScheme === 'dark'

  return (
      <Tab.Navigator 
        initialRouteName={home}
        screenOptions={ ({route}) => ({
          tabBarIcon: ({focused, color, size}) => {
            let iconName;
            let routeName = route.name;

            if (routeName === home) {
              iconName = focused ? 'home' : 'home-outline'
            } else if (routeName === trails) {
              iconName = focused ? 'map' : 'map-outline'
            } else if (routeName === profile) {
              iconName = focused ? 'person' : 'person-outline'
            } else if (routeName === contacts) {
              iconName = focused ? 'call' : 'call-outline'
            }

            return <Icon name={iconName} size={size} color={color}/>
          },

          tabBarActiveTintColor: '#00BF63',
          tabBarInactiveTintColor: isDark ? 'white' : 'grey',
          tabBarLabelStyle : { paddingBottom: 5, fontSize: 10},
          tabBarStyle: { backgroundColor: isDark ? '#303030' : 'white',height:55 }
        })}>

        <Tab.Screen name={home} component={HomePage} options={{ headerShown: false }}/>
        <Tab.Screen name={trails} component={TrailsStackScreen} options={{ headerShown: false }}/>
        <Tab.Screen name={profile} component={ProfileStackScreen} options={{ headerShown: false }}/>
        <Tab.Screen name={contacts} component={Contacts} options={{ headerShown: false }}/>
      
      </Tab.Navigator>
  );
}

export default HomeTabScreen;
