import { StyleSheet, Text, View, Image, useColorScheme } from 'react-native';

function getMediaFile(pinmedia) {
  let mediaFile = '';
  for (const media of pinmedia) {
    if (media.media_type == 'I') {
      mediaFile = media.media_file;
      break;
    }
  }
  return mediaFile;
}

function goToPin(navigation, pin, trailID) {
  navigation.navigate('pin', {pin, trailID});
}

function PinListSection({ pin, trailID, navigation }) {
  const colorScheme = useColorScheme();
  const isDark = colorScheme === 'dark';

  const pValue = pin;
  const media = getMediaFile(pValue.media);

  return (
    <View
      style={[styles.trailCard, { backgroundColor: isDark ? '#55aaa3' : '#C2DCDA' }]}
      onStartShouldSetResponder={() => goToPin(navigation, pValue, trailID)}
    >
      <View style={styles.TextView}>
        <Text style={styles.trailName}>{pValue.pin_name}</Text>
        <Text numberOfLines={3} ellipsizeMode="tail" style={styles.trailText}>
          {pValue.pin_desc}
        </Text>
      </View>
      <View>
        {media !== '' ? (
          <Image source={{ uri: media }} style={styles.trailImg} />
        ) : (
          <Image source={require('../images/icon.png')} style={styles.trailImg} />
        )}
      </View>
    </View>
  );
}

export default PinListSection;

const styles = StyleSheet.create({
  trailCard: {
    marginHorizontal: 20,
    marginTop: 10,
    flexDirection: 'row',
    height: 100,
    backgroundColor: `#C2DCDA`,
    justifyContent: 'center',
  },
  TextView: {
    flex: 1,
  },
  trailImg: {
    height: 100,
    width: 100,
    right: 0,
  },
  trailName: {
    fontSize: 15,
    marginLeft: 10,
    marginTop: 5,
    fontWeight: 'bold',
  },
  trailText: {
    fontSize: 13,
    margin: 10,
  },
});
