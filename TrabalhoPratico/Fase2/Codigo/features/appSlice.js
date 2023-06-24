import { createSlice,createAsyncThunk } from '@reduxjs/toolkit';

export const fetchApp = createAsyncThunk('app/fetchApp', async () => {
  try {
    const response = await fetch('https://c5a2-193-137-92-29.eu.ngrok.io/app');
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error fetching app:', error);
    throw error;
  }
});

export const appSlice = createSlice({
  name: 'app',
  initialState: {
    app: {
      app_name: '',
      app_desc: '',
      app_landing_page_text: '',
    },
    socials: [],
    contacts: [],
    partners: [],
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchApp.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(fetchApp.fulfilled, (state, action) => {
        const payload = action.payload;

        state.status = 'succeeded';
        state.app.app_name = payload[0].app_name;
        state.app.app_desc = payload[0].app_desc;
        state.app.app_landing_page_text = payload[0].app_landing_page_text;
        state.socials = payload[0].socials[0];
        state.contacts = payload[0].contacts;
        state.partners = payload[0].partners[0];

      })
      .addCase(fetchApp.rejected, (state) => {
        state.status = 'failed';
      });
  },
});

export default appSlice.reducer;
