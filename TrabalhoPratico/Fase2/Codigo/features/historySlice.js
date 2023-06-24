import { createSlice } from '@reduxjs/toolkit';

export const trailsSlice = createSlice({
  name: 'history',
  initialState: {
    trail_id_count: 1,
	pin_id_count: 1,
    trails: [/*{
        id: 0,
        trail_id: 1,
		nrPins,
		last_pin_name,
		last_pin_id,
        state: "completed",
		pins_done: ["Sameiro"],
    }*/],
    pins: [/*{
        id: 0,
        pin_id: 1,
    }*/],
  },
  reducers: {
    addTrail: (state, action) => {

		const newTrailHistory = {
			id: state.trail_id_count,
			trail_id: action.payload.trail_id,
			nrPins: action.payload.nr_pins,
			last_pin_id: action.payload.last_pin_id,
			last_pin_name: action.payload.last_pin_name,
			state: "onHold",
			pins_done: [],
			start_date: new Date().toISOString(),
		}

		state.trail_id_count = state.trail_id_count + 1
		state.trails.push(newTrailHistory)
    },
	finishTrail: (state, action) => {


		let trailHistoryOnHold = state.trails.find((trail) => 
			(trail.trail_id === action.payload.trail_id && trail.state === "onHold"));
		


		const nrPinsCompleted = trailHistoryOnHold.pins_done.length
		const nrPins = trailHistoryOnHold.nrPins


		const lastPinIsCompleted = trailHistoryOnHold.pins_done.some(item => item.pin_name == trailHistoryOnHold.last_pin_name);
		trailHistoryOnHold.pins_done = []


		if( nrPinsCompleted >= nrPins/2 && lastPinIsCompleted)
			trailHistoryOnHold.state = "completed"
		else 
			trailHistoryOnHold.state = "canceled"

	},
    addPin: (state, action) => {

		const newPointHistory = {
			id: state.pin_id_count,
			pin_id: action.payload.pin_id,
		}

        state.pins.push(newPointHistory);

		const trailHistoryOnHold = state.trails.find((trail) => 
			(trail.trail_id === action.payload.trail_id && trail.state == "onHold"));


		if(trailHistoryOnHold){

			const newPinCompleted = {
				pin_name: action.payload.pin_name,
				pin_id: action.payload.pin_id,
			}

			trailHistoryOnHold.pins_done.push(newPinCompleted)

		}

		state.pin_id_count = state.pin_id_count + 1

    },
  },
});

export const { addTrail, cancelTrail,finishTrail, addPin } = trailsSlice.actions;
export default trailsSlice.reducer;
