import {configureStore} from '@reduxjs/toolkit';
import trailsReducer from '../features/trailsSlice';
import appReducer from '../features/appSlice';
import historyReducer from '../features/historySlice';
import userReducer from '../features/userSlice';

export default configureStore({
  reducer: {
    trails: trailsReducer,
    app: appReducer,
    history: historyReducer,
    user: userReducer,
  },
});
