import { createSlice } from '@reduxjs/toolkit';

export const userSlice = createSlice({
  name: 'user',
  initialState: { // TODO para tirar
		user: {},
		isPremium: true
    ,
    cookies: {
      token: "",
      session: "",
    }
  },
  reducers: {
    login: (state, action) => {
        state.cookies.token = action.payload.token
        state.cookies.session = action.payload.session
    },
    putUser: (state, action) => {
        state.user = action.payload
        state.isPremium = action.payload.user_type === 'Premium'
    }
  },
});

export const { login, putUser } = userSlice.actions;
export default userSlice.reducer;
