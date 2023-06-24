/* eslint-disable react/react-in-jsx-scope */
import { useState, useEffect } from 'react';
import { useDispatch } from 'react-redux';
import {
  View,
  TextInput,
  TouchableOpacity,
  KeyboardAvoidingView,
  useColorScheme,
  StyleSheet,
  Text,
  Image,
} from 'react-native';
import PropTypes from 'prop-types';
import { login } from '../features/userSlice';
import iconImage from '../images/icon.png';

let isLoggedIn = false;

function LoginScreen({ navigation }) {
  const colorScheme = useColorScheme();
  const isDark = colorScheme === 'dark';

  const [username, onChangeUser] = useState('');
  const [password, onChangePass] = useState('');

  useEffect(
    () =>
      navigation.addListener('beforeRemove', (e) => {
        if (!isLoggedIn) e.preventDefault();
      }),
    [navigation]
  );

  return (
    <KeyboardAvoidingView style={[styles.background, { backgroundColor: isDark ? '#303030' : '' }]}>
      <View style={styles.containerLogo}>
        <Image style={styles.imageLogo} source={iconImage} />
      </View>

      <View style={styles.containerName}>
        <Text style={styles.appName}>Braguia</Text>
        <Text style={styles.appDesc}>Explore the best of Braga</Text>
      </View>

      <LoginForm
        navigation={navigation}
        username={username}
        password={password}
        onChangeUser={onChangeUser}
        onChangePass={onChangePass}
      />
    </KeyboardAvoidingView>
  );
}

function LoginForm({ navigation, username, password, onChangeUser, onChangePass }) {
  const dispatch = useDispatch();

  function tryLogin() {
    const body = {
      username,
      email: '',
      password,
    };

    fetch('https://c5a2-193-137-92-29.eu.ngrok.io/login', {
      credentials: 'omit',
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(body),
    })
      .then((response) => {
        if (response.ok) {
          const responseHeaders = response.headers.map['set-cookie'];
          const csrfTokenMatch = responseHeaders.match(/csrftoken=([^;]+)/);
          const csrfToken = csrfTokenMatch ? csrfTokenMatch[1] : null;

          const sessionIdMatch = responseHeaders.match(/sessionid=([^;]+)/);
          const sessionId = sessionIdMatch ? sessionIdMatch[1] : null;

          const cookies = {
            token: csrfToken,
            session: sessionId,
          };

          dispatch(login(cookies));
          isLoggedIn = true;
          navigation.navigate('home');
        }
      })
      .catch((error) => {
        console.log(`The error was the following: ${error}`);
      });
  }

  return (
    <View style={styles.container}>
      <TextInput
        style={styles.input}
        autoCorrect={false}
        placeholder="Username"
        onChangeText={onChangeUser}
        value={username}
      />
      <TextInput
        secureTextEntry
        style={styles.input}
        autoCorrect={false}
        placeholder="Password"
        onChangeText={onChangePass}
        value={password}
      />
      <TouchableOpacity style={styles.btnSubmit} onPress={() => tryLogin()}>
        <Text style={styles.submitText}>Entrar</Text>
      </TouchableOpacity>
    </View>
  );
}

LoginForm.propTypes = {
  navigation: PropTypes.shape({
    navigate: PropTypes.func.isRequired,
    // Other properties of the navigation object
  }).isRequired,
  username: PropTypes.string.isRequired,
  password: PropTypes.string.isRequired,
  onChangeUser: PropTypes.func.isRequired,
  onChangePass: PropTypes.func.isRequired,
};

const styles = StyleSheet.create({
  background: {
    flex: 1,
    alignItems: 'center',
  },
  container: {
    flex: 1,
    width: '80%',
    marginTop: 50,
    alignItems: 'center',
  },
  containerInputs: {
    width: '80%',
    alignItems: 'center',
    justifyContent: 'center',
    paddingBottom: 50,
  },
  LoginText: {
    fontSize: 25,
    fontWeight: 'bold',
    paddingBottom: 15,
  },
  input: {
    width: '80%',
    marginBottom: 15,
    color: '#222',
    fontSize: 17,
    borderColor: 'grey',
    borderWidth: 1,
    borderRadius: 7,
    padding: 10,
    margin: 1,
  },
  btnSubmit: {
    backgroundColor: '#00BF63',
    width: '40%',
    height: 45,
    borderRadius: 7,
    alignItems: 'center',
    justifyContent: 'center',
  },
  submitText: {
    color: '#FFF',
    fontSize: 18,
    fontWeight: 'bold',
  },
  appName: {
    fontSize: 40,
    fontWeight: 'bold',
    alignSelf: 'center',
  },

  appDesc: {
    fontSize: 20,
    marginVertical: 5,
    fontWeight: 'bold',
    alignSelf: 'center',
  },
  imageLogo: {
    width: 200,
    height: 200,
    alignSelf: 'center',
    resizeMode: 'contain',
    margin: 10,
  },
  containerLogo: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 50,
  },
});
export default LoginScreen;
