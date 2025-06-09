import { call, put, takeLatest, fork, take, cancel, select } from 'redux-saga/effects';
import { eventChannel } from 'redux-saga';
import {
    joinRequest,
    joinSuccess,
    joinFailure,
    receiveQuestion,
    receiveLeaderboard,
    submitAnswer,
    answerFailure,
} from './quizSlice';
import {Question} from "../model/question";
import {LeaderboardEntry} from "../model/leaderboardEntry";
import {joinQuiz} from "../api/joinQuiz";
import {RootState} from "./index";

function createWebSocketChannel(ws: WebSocket) {
    return eventChannel<any>(emitter => {
        ws.onmessage = event => {
            const msg = JSON.parse(event.data);
            emitter(msg);
        };
        ws.onerror = err => emitter(new ErrorEvent('error', { error: err }));
        return () => ws.close();
    });
}

function* handleJoin(
    action: ReturnType<typeof joinRequest>
): Generator<any, void, any> {
    try {
        const { quizId, name } = action.payload;
        const res: { quizId: string; participantId: string } = yield call(joinQuiz, quizId, name);

        yield put(joinSuccess({...res, name}));

        const ws: WebSocket = new WebSocket(
            `ws://localhost:8090/ws/quiz?quizId=${res.quizId}&participantId=${res.participantId}`
        );
        (window as any).quizWebSocket = ws;

        const channel = yield call(createWebSocketChannel, ws);

        const listenTask = yield fork(function* (): Generator<any, void, any> {
            while (true) {
                const msg = yield take(channel);
                if (msg.type === 'new_question') {
                    yield put(receiveQuestion(msg.payload as Question));
                }
                if (
                    msg.type === 'leaderboard_data' ||
                    msg.type === 'leaderboard_changed'
                ) {
                    yield put(receiveLeaderboard(msg.payload as LeaderboardEntry[]));
                }
            }
        });

        yield take('quiz/end');
        yield cancel(listenTask);
    } catch (err: any) {
        yield put(joinFailure(err.message));
    }
}

function* handleSubmitAnswer(
    action: ReturnType<typeof submitAnswer>
): Generator<any, void, any> {
    try {
        const { questionId, selectedOption } = action.payload;

        const participantId: string | null = yield select((state: RootState) => state.quiz.participantId);

        if (!participantId) {
            console.error('No participantId in state! Cannot send answer_submission.');
            return;
        }

        // 2) build and log the message
        const msg = {
            type: 'answer_submission',
            payload: {
                participantId,
                questionId,
                selectedOption,
                answeredAt: new Date().toISOString(),
            },
        };
        console.debug('WS TX →', msg);

        const ws: WebSocket = (window as any).quizWebSocket;
        ws.send(JSON.stringify(msg));
    } catch (err: any) {
        yield put(answerFailure(err.message));
    }
}


export default function* rootSaga(): Generator<any, void, any> {
    yield takeLatest(joinRequest.type, handleJoin);
    yield takeLatest(submitAnswer.type, handleSubmitAnswer);
}
