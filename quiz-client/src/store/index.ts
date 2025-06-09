import { configureStore } from '@reduxjs/toolkit';
import createSagaMiddleware from 'redux-saga';
import quizReducer from './quizSlice';
import rootSaga from "./saga";

// Create the saga middleware
const sagaMiddleware = createSagaMiddleware();

// Configure store
export const store = configureStore({
    reducer: {
        quiz: quizReducer,
    },
    middleware: getDefaultMiddleware =>
        getDefaultMiddleware({ thunk: false }).concat(sagaMiddleware),
});

// Run sagas
sagaMiddleware.run(rootSaga);

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;