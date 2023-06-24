import PropTypes from 'prop-types';
import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import Geolocation from 'react-native-geolocation-service';
import ReactNativeForegroundService from '@supersami/rn-foreground-service';
import {
  ScrollView,
  View,
  StyleSheet,
  Text,
  Image,
  Dimensions,
  TouchableOpacity,
  Linking,
  useColorScheme,
  PermissionsAndroid,
  Platform,
} from 'react-native';
import { addTrail } from '../features/historySlice';
import { fetchTrails, putTrailOnRunning } from '../features/trailsSlice';

import StopTrailMenu from '../Components/StopTrailMenu';
import PinListSection from '../Components/PinListSection';
import MapComponent from '../Components/MapComponent';
/* global alert */

const windowHeight = Dimensions.get('window').height;
const imageHeight = windowHeight * 0.4;

function listPins(edges) {
  const pinList = [];
  edges.forEach((edge, index, array) => {
    if (index === array.length - 1) {
      pinList.push(edge.edge_start);
      pinList.push(edge.edge_end);
    } else {
      pinList.push(edge.edge_start);
    }
  });

  return pinList;
}

function redirectMap(pinList, location) {
  const origin = `${location.latitude},${location.longitude}`;
  const tempList = pinList;
  const destination = tempList.pop().pin_name;

  const waypointsString = tempList.map((pin) => `${pin.pin_lat},${pin.pin_lng}`).join('|');

  const url = `https://www.google.com/maps/dir/?api=1&origin=${origin}&destination=${destination}&waypoints=${waypointsString}`;
  const apiKey = 'AIzaSyA7xqDjkmGSkExeSTub6G_HgCjDuchjV7I';
  const originPoly = `${pinList[0].pin_lat},${pinList[0].pin_lng}`;

  if (pinList.length > 2) {
    const urlPoly = `https://maps.googleapis.com/maps/api/directions/json?origin=${originPoly}&destination=${destination}&waypoints=${waypointsString}&key=${apiKey}`;
    const urls = { urlRedirect: url, urlPoly };
    return urls;
  }
  const urlPoly = `https://maps.googleapis.com/maps/api/directions/json?origin=${originPoly}&destination=${destination}&key=${apiKey}`;
  const urls = { urlRedirect: url, urlPoly };
  return urls;
}

function pinToMarker(pinList) {
  const markerList = [];

  pinList.map((pin) => {
    const markerObj = {
      title: pin.pin_name,
      coordinates: { latitude: pin.pin_lat, longitude: pin.pin_lng },
    };
    markerList.push(markerObj);
    return null;
  });

  return markerList;
}

async function requestLocationPermission() {
  let result = null;
  if (Platform.OS === 'ios') {
    Geolocation.setRNConfiguration({
      authorizationLevel: 'whenInUse',
    });

    Geolocation.requestAuthorization();
    // IOS permission request does not offer a callback :/
    result = null;
  }
  if (Platform.OS === 'android') {
    try {
      const fineLocationGranted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION
      );

      if (fineLocationGranted === PermissionsAndroid.RESULTS.GRANTED) {
        let backgroundLocationGranted = true;
        if (Platform.Version >= 29) {
          backgroundLocationGranted = await PermissionsAndroid.request(
            PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION
          );
        }

        if (backgroundLocationGranted === PermissionsAndroid.RESULTS.GRANTED) {
          result = true;
        } else {
          result = false;
        }
      } else {
        result = false;
      }
    } catch (err) {
      console.warn(err.message);
      result = false;
    }
  }
  return result;
}

async function getCurrentPosition(setLocation) {
  const hasLocationPermission = await requestLocationPermission();

  if (hasLocationPermission === false) {
    setLocation(null);
    return;
  }

  Geolocation.getCurrentPosition(
    (position) => {
      setLocation(position.coords);
    },
    (error) => {
      console.log(error);
      setLocation(null);
    },
    { enableHighAccuracy: true, timeout: 20000, maximumAge: 10000 }
  );
}

async function getLocationSimple() {
  const hasLocationPermission = await requestLocationPermission();
  if (hasLocationPermission) {
    Geolocation.getCurrentPosition(
      (position) => {
        console.log(`COORDS ARE : ${position.coords.latitude} , ${position.coords.longitude}`);
      },
      (error) => {
        console.log(error);
      },
      { enableHighAccuracy: true, timeout: 20000, maximumAge: 10000 }
    );
  }
}
const log = () => getLocationSimple();

const startService = () => {
  ReactNativeForegroundService.start({
    id: 1244,
    title: 'Foreground Service',
    message: 'We are live World',
    icon: '../images/icon.png',
    button: true,
    buttonText: 'Button',
    buttonOnPress: 'cray',
    setOnlyAlertOnce: true,
    color: '#000000',
    progress: {
      max: 100,
      curr: 50,
    },
  });
};

function stopEverything(setStopMenu, howManyRunning) {
  if (howManyRunning === 2) {
    ReactNativeForegroundService.stopAll();
    setStopMenu(true);
  } else if (howManyRunning === 1) {
    alert("There's already  a trail Running");
  } else {
    alert('Start the trail first');
  }
}

function startEverything(url, trail_id, pinList, dispatch, howManyRunning) {
  if (howManyRunning === 1) {
    alert("There's a trail already running");
  } else if (howManyRunning === 2) {
    alert('This trail is already running');
  } else {
    startService();

    const lastPin = pinList[pinList.length - 1];

    dispatch(
      addTrail({
        trail_id,
        nr_pins: pinList.length,
        last_pin_id: lastPin.id,
        last_pin_name: lastPin.pin_name,
      })
    );
    dispatch(
      putTrailOnRunning({
        trail_id,
      })
    );
    Linking.openURL(url);
  }
}

function setStopButtonStyleColor(state) {
  if (!state) return styles.colorDeactive;
  if (state === 'running') return styles.colorActive;
  if (state === 'toStart') return styles.colorDeactive;
  return null;
}

function setStartButtonStyleColor(state) {
  if (!state) return styles.colorActive;
  if (state === 'running') return styles.colorDeactive;
  if (state === 'toStart') return styles.colorActive;
  return null;
}

function checkOtherTrailsRunning(trail_id, trails) {
  const running = trails.filter((trail) => trail.state && trail.state === 'running');

  if (running.length === 0) return 0;
  if (running && running[0].id === trail_id) return 2;

  return 1;
}

function TrailPage({ route, navigation }) {
  const colorScheme = useColorScheme();
  const isDark = colorScheme === 'dark';

  const { trailId } = route.params;

  const [location, setLocation] = useState(null);
  const [locationSimple, setLocationSimple] = useState(null);
  const [stopMenu, setStopMenu] = useState(false);
  const trails = useSelector((state) => state.trails.trails);

  const isPremiumm = useSelector((state) => state.user.isPremium);

  const howManyRunning = checkOtherTrailsRunning(trailId, trails);

  const dispatch = useDispatch();

  useEffect(() => {
    if (trails.length === 0) dispatch(fetchTrails());

    getCurrentPosition(setLocation);

    ReactNativeForegroundService.add_task(log, {
      delay: 10000,
      onLoop: true,
      taskId: 'taskid',
    });
  }, []);

  if (trails.length === 0) {
    return <Text style={styles.loadingMessage}>Loading trail...</Text>;
  }
  const trail = trails.find((trailfind) => trailfind.id === trailId);
  const { edges } = trail;

  const pinList = listPins(edges);
  const newlist = listPins(edges);

  const pinsShow = pinList.map((pin) => (
    <PinListSection key={pin.id} pin={pin} trailID={trailId} navigation={navigation} />
  ));

  const markerList = pinToMarker(pinList);

  let mapContainer = <Text>Loading....</Text>;

  let urls = '';
  if (location) {
    urls = redirectMap(pinList, location);

    if (isPremiumm) mapContainer = <MapComponent markerList={markerList} urlPoly={urls.urlPoly} />;
  }

  const stopButtonStyle = setStopButtonStyleColor(trail.state);
  const startButtonStyle = setStartButtonStyleColor(trail.state);

  return (
    <ScrollView
      contentContainerStyle={[styles.containerScreen, { backgroundColor: isDark ? '#303030' : '',}]}
    >
      {isPremiumm && (
        <View style={styles.containerDurDif}>
          <TouchableOpacity
            onPress={() =>
              startEverything(urls.urlRedirect, trailId, newlist, dispatch, howManyRunning)
            }
            style={[styles.btnSubmit, startButtonStyle]}
          >
            <Text>Start</Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={[styles.btnSubmit, stopButtonStyle]}
            onPress={() => stopEverything(setStopMenu, howManyRunning)}
          >
            <Text>Stop</Text>
          </TouchableOpacity>
        </View>
      )}
      {stopMenu && <StopTrailMenu pinList={newlist} trailID={trail.id} setStopMenu={setStopMenu} />}

      <Image style={styles.thumbnail} source={{ uri: trail.trail_img }} />
      <Text style={styles.titleText}>{trail.trail_name}</Text>
      <View style={styles.containerDurDif}>
        <View style={styles.durDifView}>
          <Text style={styles.durDifViewTitle}>Duration(min)</Text>
          <Text>{trail.trail_duration}</Text>
        </View>
        <View style={styles.durDifView}>
          <Text style={styles.durDifViewTitle}>Difficulty(min)</Text>
          <Text>{trail.trail_difficulty}</Text>
        </View>
      </View>
      <View style={{alignSelf: 'flex-start'}}>
        <Text style={styles.descTitle}>Trail Description:</Text>
        <Text style={styles.descText}>{trail.trail_desc}</Text>
      </View>
      {isPremiumm && <View style={styles.mapContainer}>{mapContainer}</View>}

      <View style={styles.listPins}>{pinsShow}</View>
    </ScrollView>
  );
}

TrailPage.propTypes = {
  route: PropTypes.shape({
    params: PropTypes.object,
  }).isRequired,
  navigation: PropTypes.shape({
    navigate: PropTypes.func,
  }).isRequired,
};

const styles = StyleSheet.create({
  containerScreen: {
    alignItems: 'center',
  },
  colorActive: {
    backgroundColor: '#00BF63',
  },
  colorDeactive: {
    backgroundColor: 'grey',
  },
  listPins: {
    width: '95%',
    margin: 20,
    marginBottom: 10,
  },
  btnSubmit: {
    width: '30%',
    height: 45,
    borderRadius: 7,
    alignItems: 'center',
    justifyContent: 'center',
    margin: 20,
    marginBottom: 10,
  },
  containerDurDif: {
    flexDirection: 'row',
  },
  durDifView: {
    alignItems: 'center',
    margin: 20,
  },
  durDifViewTitle: {
    fontWeight: 'bold',
  },
  descTitle: {
    fontWeight: 'bold',
    paddingTop: 5,
    paddingHorizontal: 10,
  },
  descText: {
    paddingTop: 5,
    paddingHorizontal: 10,
    textAlign: 'justify',
  },
  titleText: {
    fontSize: 25,
    fontWeight: '700',
  },
  loadingMessage: {
    justifyContent: 'center',
  },
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },
  thumbnail: {
    marginTop: 20,
    width: '100%',
    height: imageHeight,
    resizeMode: 'contain',
  },
  mapContainer: {
    height: 400,
    width: '90%',
    justifyContent: 'flex-end',
    alignItems: 'center',
    margin: 10,
  },
  map: {
    ...StyleSheet.absoluteFillObject,
  },
});

export default TrailPage;
