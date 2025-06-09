import React, { useEffect, useState } from 'react'

interface QuestionSectionProps {
    question: {
        questionId: string
        text: string
        options: string[]
    }
    participantName: string
    onSelect: (option: string) => void
}

export const QuestionSection: React.FC<QuestionSectionProps> = ({
                                                                    question,
                                                                    participantName,
                                                                    onSelect,
                                                                }) => {
    const [secondsLeft, setSecondsLeft] = useState(10)
    const [selected, setSelected] = useState<string | null>(null)

    useEffect(() => {
        setSecondsLeft(10)
        setSelected(null)

        const timer = setInterval(() => {
            setSecondsLeft(prev => {
                if (prev <= 1) {
                    clearInterval(timer)
                    return 0
                }
                return prev - 1
            })
        }, 1000)

        return () => clearInterval(timer)
    }, [question.questionId])

    const handleClick = (option: string) => {
        if (selected || secondsLeft === 0) return
        setSelected(option)

        // Wait for fade-out animation before hiding interactions
        setTimeout(() => {
            onSelect(option)
        }, 300)
    }

    const isTimeUp = secondsLeft === 0 || selected !== null

    return (
        <div style={styles.container}>
            <div style={styles.header}>
                <h2 style={styles.question}>{question.text}</h2>
                <div style={styles.timer}>
                    ⏳ {isTimeUp && !selected ? 'Time up!' : `${secondsLeft}s`}
                </div>
            </div>

            {participantName && (
                <p style={styles.participant}>
                    <strong>You are:</strong> {participantName}
                </p>
            )}

            <div
                style={{
                    ...styles.options,
                    opacity: selected ? 0 : 1,
                    visibility: selected ? 'hidden' : 'visible',
                    transition: 'opacity 0.3s ease-out, visibility 0s linear 0.3s',
                }}
            >
                {question.options.map(opt => (
                    <button
                        key={opt}
                        onClick={() => handleClick(opt)}
                        disabled={isTimeUp}
                        style={{
                            ...styles.optionButton,
                            backgroundColor:
                                selected === opt ? '#d1e7dd' :
                                    isTimeUp ? '#eee' : '#f9f9f9',
                            transform: selected === opt ? 'scale(1.05)' : 'scale(1)',
                            transition: 'all 0.2s ease',
                            color: isTimeUp ? '#888' : '#000',
                            cursor: isTimeUp ? 'not-allowed' : 'pointer',
                        }}
                    >
                        {opt}
                    </button>
                ))}
            </div>
        </div>
    )
}

const styles: { [key: string]: React.CSSProperties } = {
    container: { marginBottom: 32 },
    header: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: 12,
    },
    question: { fontSize: 22, color: '#222' },
    timer: {
        fontSize: 16,
        fontWeight: 600,
        color: '#d9534f',
        minWidth: 80,
        textAlign: 'right',
    },
    participant: { fontSize: 16, marginBottom: 16, color: '#555' },
    options: {
        display: 'flex',
        flexDirection: 'column',
        gap: 12,
        minHeight: 160, // preserve space even if empty
    },
    optionButton: {
        padding: '12px 20px',
        fontSize: 16,
        borderRadius: 8,
        border: '1px solid #ccc',
        backgroundColor: '#f9f9f9',
    },
}
