import React, { useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { joinRequest } from '../store/quizSlice'
import type { RootState, AppDispatch } from '../store'

export const JoinQuiz: React.FC = () => {
    const dispatch = useDispatch<AppDispatch>()
    const error = useSelector((state: RootState) => state.quiz.error)
    const [quizId, setQuizId] = useState('')
    const [name, setName] = useState('')
    const [localError, setLocalError] = useState<string | null>(null)

    const handleJoin = () => {
        if (!quizId.trim()) {
            setLocalError('Quiz ID is required')
            return
        }
        if (!name.trim()) {
            setLocalError('Name is required')
            return
        }
        setLocalError(null)
        dispatch(joinRequest({ quizId: quizId.trim(), name: name.trim() }))
    }

    return (
        <div style={styles.container}>
            <h1 style={styles.title}>Join a Quiz</h1>

            {localError && <div style={styles.alert}>{localError}</div>}
            {error && <div style={styles.alert}>{error}</div>}

            <div style={styles.formGroup}>
                <label htmlFor="quizId" style={styles.label}>Quiz ID</label>
                <input
                    id="quizId"
                    type="text"
                    value={quizId}
                    onChange={e => setQuizId(e.target.value)}
                    placeholder="e.g. quiz123"
                    style={styles.input}
                />
            </div>

            <div style={styles.formGroup}>
                <label htmlFor="name" style={styles.label}>Your Name</label>
                <input
                    id="name"
                    type="text"
                    value={name}
                    onChange={e => setName(e.target.value)}
                    placeholder="e.g. Alice"
                    style={styles.input}
                />
            </div>

            <button style={styles.button} onClick={handleJoin}>
                Join Quiz
            </button>
        </div>
    )
}

const styles: { [key: string]: React.CSSProperties } = {
    container: {
        maxWidth: 400,
        margin: '80px auto',
        padding: '32px',
        borderRadius: 12,
        backgroundColor: '#fff',
        boxShadow: '0 2px 20px rgba(0, 0, 0, 0.08)',
        fontFamily: 'Segoe UI, sans-serif',
    },
    title: {
        textAlign: 'center',
        fontSize: 24,
        marginBottom: 24,
        color: '#333',
    },
    formGroup: {
        marginBottom: 20,
    },
    label: {
        display: 'block',
        fontWeight: 600,
        marginBottom: 6,
        color: '#555',
    },
    input: {
        width: '100%',
        padding: '10px 12px',
        fontSize: 16,
        border: '1px solid #ccc',
        borderRadius: 6,
        outline: 'none',
        boxSizing: 'border-box',
    },
    alert: {
        backgroundColor: '#ffe6e6',
        color: '#b00020',
        border: '1px solid #f5c2c2',
        padding: '10px 12px',
        marginBottom: 16,
        borderRadius: 6,
        fontSize: 14,
    },
    button: {
        width: '100%',
        padding: 12,
        fontSize: 16,
        backgroundColor: '#007bff',
        color: '#fff',
        border: 'none',
        borderRadius: 6,
        cursor: 'pointer',
        fontWeight: 600,
        transition: 'background-color 0.2s ease',
    },
}
