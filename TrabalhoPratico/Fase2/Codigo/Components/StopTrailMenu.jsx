import React, { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { StyleSheet, Text, View, TouchableOpacity } from 'react-native';
import PropTypes from 'prop-types';
import StopTrailSection from './StopTrailSection';
import { addPin, finishTrail } from '../features/historySlice';
import { putTrailOnToStart } from '../features/trailsSlice';
/* global alert */

function alertConditionAux(pinList, minutesDifference, verifiedPins, pinsNamesCompleted) {
  const verifiedPinsNames = verifiedPins.map((pin) => pin.pinName);

  if (
    (pinsNamesCompleted.includes(pinList[pinList.length - 1].pin_name) ||
      verifiedPinsNames.includes(pinList[pinList.length - 1].pin_name)) &&
    pinsNamesCompleted.length + verifiedPins.length >= pinList.length / 2
  ) {
    const alertMessage = `Congratulations, you visited ${
      pinsNamesCompleted.length + verifiedPins.length
    } places\nIn ${minutesDifference} minutes`;
    alert(`Trail concluded\n${alertMessage}`);
  } else {
    const alertMessage = `Before canceling, you visited ${
      pinsNamesCompleted.length + verifiedPins.length
    } places\nIn ${minutesDifference} minutes`;
    alert(`Trail canceled\n${alertMessage}`);
  }
}

function addToHistory(
  verifiedPins,
  trailID,
  dispatch,
  setStopMenu,
  trail,
  pinList,
  minutesDifference,
  pinsNamesCompleted
) {
  verifiedPins.map((pin) => {
    dispatch(
      addPin({
        pin_id: pin.pinId,
        pin_name: pin.pinName,
        trail_id: trailID,
      })
    );

    return null; // Add a return statement here
  });
  alertConditionAux(pinList, minutesDifference, verifiedPins, pinsNamesCompleted);
  // alert(`Trail concluded`);

  setStopMenu(false);
  dispatch(
    finishTrail({
      trail_id: trailID,
    })
  );

  dispatch(
    putTrailOnToStart({
      trail_id: trailID,
    })
  );
}

function StopTrailMenu({ pinList, trailID, setStopMenu }) {
  const [verifiedPins, setVerifiedPins] = useState([]);
  const dispatch = useDispatch();

  const handleVariableVerify = (variableToVerify, pinNameId) => {
    if (variableToVerify) {
      setVerifiedPins((prevPins) => [...prevPins, pinNameId]);
    } else {
      setVerifiedPins((prevPins) => prevPins.filter((pin) => pin.pinName !== pinNameId.pinName));
    }
  };

  const trailsHistory = useSelector((state) => state.history.trails);

  const trail = trailsHistory.find(
    (trailH) => trailH.trail_id === trailID && trailH.state === 'onHold'
  );

  const pinsCompleted = trail.pins_done;
  const pinsNamesCompleted = pinsCompleted.map((pin) => pin.pin_name);

  const newDate = new Date().toISOString();
  const startDate = new Date(trail.start_date);

  const timeDifference = new Date(newDate).getTime() - startDate.getTime();
  const minutesDifference = Math.floor(timeDifference / 60000);

  const filteredPins = pinList.filter((pin) => !pinsNamesCompleted.includes(pin.pin_name));

  const list = filteredPins.map((pin) => (
    <StopTrailSection key={pin.id} pin={pin} handleVariableVerify={handleVariableVerify} />
  ));
  return (
    <View style={styles.container}>
      <Text style={styles.message}>In case you forgot to add:</Text>
      {list}
      <TouchableOpacity
        style={styles.btnSubmit}
        onPress={() =>
          addToHistory(
            verifiedPins,
            trailID,
            dispatch,
            setStopMenu,
            trail,
            pinList,
            minutesDifference,
            pinsNamesCompleted
          )
        }
      >
        <Text>Add to History</Text>
      </TouchableOpacity>
    </View>
  );
}

StopTrailMenu.propTypes = {
  pinList: PropTypes.arrayOf(
    PropTypes.shape({
      // Define the shape of each element in the array
      // Example: pinName: PropTypes.string.isRequired,
    })
  ).isRequired,
  trailID: PropTypes.number.isRequired,
  setStopMenu: PropTypes.func.isRequired,
};

export default StopTrailMenu;

const styles = StyleSheet.create({
  container: {
    margin: 10,
    marginHorizontal: 20,
    width: '100%',
    backgroundColor: `white`,
    alignItems: 'center',
  },

  message: {
    fontSize: 15,
    marginLeft: 10,
    marginTop: 5,
    fontWeight: 'bold',
  },
  btnSubmit: {
    backgroundColor: '#00BF63',
    width: '30%',
    height: 45,
    borderRadius: 7,
    alignItems: 'center',
    justifyContent: 'center',
    margin: 20,
    marginBottom: 10,
  },
});
