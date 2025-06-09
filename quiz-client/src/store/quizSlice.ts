import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import {Question} from "../model/question";
import {LeaderboardEntry} from "../model/leaderboardEntry";

interface QuizState {
    quizId: string | null;
    participantName: string | null;
    participantId: string | null;
    question: Question | null;
    leaderboard: LeaderboardEntry[];
    error: string | null;
}

const initialState: QuizState = {
    quizId: null,
    participantName: null,
    participantId: null,
    question: null,
    leaderboard: [],
    error: null,
};

export const quizSlice = createSlice({
    name: 'quiz',
    initialState,
    reducers: {
        joinRequest(state, action: PayloadAction<{quizId: string; name: string}>) {
            state.error = null;
        },
        joinSuccess(state, action: PayloadAction<{quizId: string; participantId: string, name: string}>) {
            state.quizId = action.payload.quizId;
            state.participantId = action.payload.participantId;
            state.participantName = action.payload.name
        },
        joinFailure(state, action: PayloadAction<string>) {
            state.error = action.payload;
        },
        receiveQuestion(state, action: PayloadAction<Question>) {
            state.question = action.payload;
        },
        receiveLeaderboard(state, action: PayloadAction<LeaderboardEntry[]>) {
            state.leaderboard = action.payload;
        },
        submitAnswer(state, action: PayloadAction<{questionId: string; selectedOption: string}>) {
            state.error = null;
        },
        answerFailure(state, action: PayloadAction<string>) {
            state.error = action.payload;
        },
    },
});

export const {
    joinRequest,
    joinSuccess,
    joinFailure,
    receiveQuestion,
    receiveLeaderboard,
    submitAnswer,
    answerFailure,
} = quizSlice.actions;

export default quizSlice.reducer;