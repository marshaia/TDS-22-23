import axios from 'axios';
import React, { useEffect, useState } from 'react';
import MapView, { Marker, Polyline } from 'react-native-maps'; // remove PROVIDER_GOOGLE import if not using Google Maps

import { StyleSheet, ActivityIndicator } from 'react-native';

function decodePolyline(polyline) {
  const points = [];
  let index = 0,
    latitude = 0,
    longitude = 0;

  while (index < polyline.length) {
    let shift = 0,
      result = 0,
      byte;

    do {
      byte = polyline.charCodeAt(index++) - 63;
      result |= (byte & 0x1f) << shift;
      shift += 5;
    } while (byte >= 0x20);

    const deltaLatitude = result & 1 ? ~(result >> 1) : result >> 1;
    latitude += deltaLatitude;

    shift = 0;
    result = 0;

    do {
      byte = polyline.charCodeAt(index++) - 63;
      result |= (byte & 0x1f) << shift;
      shift += 5;
    } while (byte >= 0x20);

    const deltaLongitude = result & 1 ? ~(result >> 1) : result >> 1;
    longitude += deltaLongitude;

    points.push({
      latitude: latitude / 1e5,
      longitude: longitude / 1e5,
    });
  }

  return points;
}

export default function MapComponent(props) {
  const urlPoly = props.urlPoly;
  const markerList = props.markerList;

  const [loading, setLoading] = useState(true);
  const [decodedCoordinates, setDecodedCoordinates] = useState([]);

  useEffect(() => {
    setLoading(true);

    axios
      .get(urlPoly)
      .then((res) => {
        const polyline = res.data.routes[0].overview_polyline.points;
        const decodedCoords = decodePolyline(polyline);
        setDecodedCoordinates(decodedCoords);
        setLoading(false);
      })
      .catch((err) => {
        setLoading(false);
        console.log(err);
        return null;
      });
  }, []);

  if (loading) {
    return <ActivityIndicator style={styles.indicator} size="large" />;
  }
  return (
    <MapView
      style={styles.map}
      initialRegion={{
        latitude: markerList[0].coordinates.latitude,
        longitude: markerList[0].coordinates.longitude,
        latitudeDelta: 0.0922,
        longitudeDelta: 0.0421,
      }}
    >
      {markerList.map((marker, index) => (
        <Marker key={index} coordinate={marker.coordinates} title={marker.title} />
      ))}
      {decodedCoordinates.length > 0 && (
        <Polyline
          coordinates={decodedCoordinates.map((coord) => ({
            latitude: coord.latitude,
            longitude: coord.longitude,
          }))}
          strokeColor="blue"
          strokeWidth={2.5}
        />
      )}
    </MapView>
  );
}

const styles = StyleSheet.create({
  map: {
    ...StyleSheet.absoluteFillObject,
  },
  indicator: {
    flex: 1,
    justifyContent: 'center',
  },
});
