import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';

export const fetchTrails = createAsyncThunk('counter/fetchTrails', async () => {
  try {
    const response = await fetch('https://c5a2-193-137-92-29.eu.ngrok.io/trails',);
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error fetching trails:', error);
    throw error;
  }
});

function getPoints(trails){

  points = []

  trails.forEach(trail => {
    
    edges = trail.edges

    edges.forEach(edge => {

      edgeStart = edge.edge_start
      edgeEnd = edge.edge_end

      if(!points.some(point => point.id == edgeStart.id ))
        points.push(edgeStart)

      if(!points.some(point => point.id == edgeEnd.id ))
        points.push(edgeEnd)

    })
  });

  return points

}

export const trailsSlice = createSlice({
	name: 'trails',
	initialState: {
		trails: [],
		points: [],
	},
	reducers: {
		putTrailOnRunning: (state, action) => {
			const trail = state.trails.find((trail) => trail.id === action.payload.trail_id)
			trail.state = "running"
		},
		putTrailOnToStart: (state, action) => {
			const trail = state.trails.find((trail) => trail.id === action.payload.trail_id)
			trail.state = "toStart"
		},
	},
	extraReducers: builder => {
		builder
		.addCase(fetchTrails.pending, state => {
			state.status = 'loading';
		})
		.addCase(fetchTrails.fulfilled, (state, action) => {
			state.status = 'succeeded';
			state.trails = action.payload;
			state.points = getPoints(action.payload)
		})
		.addCase(fetchTrails.rejected, state => {
			state.status = 'failed';
		});
	},
});

export const { putTrailOnRunning, putTrailOnToStart } = trailsSlice.actions;
export default trailsSlice.reducer;
