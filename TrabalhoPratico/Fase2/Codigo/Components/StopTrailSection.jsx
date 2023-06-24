import { StyleSheet, Text, View } from 'react-native';
import React, { useEffect, useState } from 'react';

function switchVariableToVerify(variableToVerify, setVariableToVerify) {
  if (variableToVerify) {
    setVariableToVerify(false);
  } else {
    setVariableToVerify(true);
  }
}

function StopTrailSection({ pin, handleVariableVerify }) {
  const pValue = pin;

  const [variableToVerify, setVariableToVerify] = useState(false);

  useEffect(() => {
    const addVal = { pinName: pin.pin_name, pinId: pin.id };
    handleVariableVerify(variableToVerify, addVal);
  }, [variableToVerify]);

  const cardStyle = variableToVerify ? styles.card2 : styles.card1;

  return (
    <View
      style={[styles.trailCard, cardStyle]}
      onStartShouldSetResponder={() =>
        switchVariableToVerify(variableToVerify, setVariableToVerify)
      }
    >
      <View style={styles.TextView}>
        <Text style={styles.trailName}>{pValue.pin_name}</Text>
      </View>
    </View>
  );
}

export default StopTrailSection;

const styles = StyleSheet.create({
  trailCard: {
    marginHorizontal: 20,
    marginTop: 10,
    flexDirection: 'row',
    justifyContent: 'center',
    borderRadius: 10,
  },
  card2: {
    backgroundColor: `#C2DCDA`,
  },
  card1: {
    backgroundColor: `grey`,
  },
  TextView: {
    flex: 1,
    margin: 15,
  },
  trailImg: {
    height: 100,
    width: 100,
    right: 0,
  },
  trailName: {
    fontSize: 15,
    marginLeft: 10,
    fontWeight: 'bold',
  },
  trailText: {
    fontSize: 13,
  },
});
