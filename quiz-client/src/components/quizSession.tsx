import React from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { submitAnswer } from '../store/quizSlice'
import type { RootState, AppDispatch } from '../store'
import {QuestionSection} from "./questionSection";
import {Leaderboard} from "./leaderboard";

export const QuizSession: React.FC = () => {
    const dispatch = useDispatch<AppDispatch>()
    const question = useSelector((state: RootState) => state.quiz.question)
    const leaderboard = useSelector((state: RootState) => state.quiz.leaderboard)
    const participantName = useSelector((state: RootState) => state.quiz.participantName)
    const participantId = useSelector((state: RootState) => state.quiz.participantId)

    const handleSelect = (option: string) => {
        if (!question) return
        dispatch(submitAnswer({
            questionId: question.questionId,
            selectedOption: option
        }))
    }

    return (
        <div style={styles.container}>
            {question && (
                <QuestionSection
                    question={question}
                    participantName={participantName ?? ""}
                    onSelect={handleSelect}
                />
            )}
            <Leaderboard entries={leaderboard}
                            currentParticipantId={participantId ?? ""}/>
        </div>
    )
}

const styles = {
    container: {
        maxWidth: 600,
        margin: '60px auto',
        padding: 32,
        borderRadius: 12,
        backgroundColor: '#fff',
        boxShadow: '0 2px 20px rgba(0,0,0,0.08)',
        fontFamily: 'Segoe UI, sans-serif',
    },
}
