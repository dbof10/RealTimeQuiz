// src/App.tsx
import React from 'react'
import { useSelector } from 'react-redux'
import type { RootState } from './store'
import {JoinQuiz} from "./components/joinQuiz";
import {QuizSession} from "./components/quizSession";

const App: React.FC = () => {
  const quizId = useSelector((state: RootState) => state.quiz.quizId)
  const participantId = useSelector((state: RootState) => state.quiz.participantId)

  return (
      <div className="app-container">
        {!quizId || !participantId ? <JoinQuiz /> : <QuizSession />}
      </div>
  )
}

export default App
