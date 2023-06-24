import React from 'react';
import { Provider } from 'react-redux'
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';

import store from './store/store'
import LoginScreen from './Screens/Login';
import HomeTabScreen from './Screens/stackNavigators/HomeTabScreen';


const loginStack = createStackNavigator()

function App() {

  return (
    <Provider store={store}>
      <NavigationContainer>
        <loginStack.Navigator>
          <loginStack.Screen name="login" component={LoginScreen} options={{ headerShown: false }} />             
          <loginStack.Screen name="home" component={HomeTabScreen} options={{ headerShown: false }} />
        </loginStack.Navigator>
      </NavigationContainer>
    </Provider>
  );
}

export default App;
