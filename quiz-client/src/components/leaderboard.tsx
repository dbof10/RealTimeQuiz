import React, { useEffect, useRef, useState } from 'react'

interface Entry {
    participantId: string
    name: string
    rank: number
    score: number
}

interface LeaderboardProps {
    entries: Entry[]
    currentParticipantId: string
}

export const Leaderboard: React.FC<LeaderboardProps> = ({ entries, currentParticipantId }) => {
    const [prevRanks, setPrevRanks] = useState<{ [id: string]: number }>({})
    const [movers, setMovers] = useState<{ [id: string]: 'up' | 'down' | null }>({})
    const [showFireworks, setShowFireworks] = useState(false)

    useEffect(() => {
        const newMovers: { [id: string]: 'up' | 'down' | null } = {}

        entries.forEach(entry => {
            const prev = prevRanks[entry.participantId]
            if (prev !== undefined && prev !== entry.rank) {
                newMovers[entry.participantId] = entry.rank < prev ? 'up' : 'down'
            } else {
                newMovers[entry.participantId] = null
            }
        })

        setMovers(newMovers)

        // Trigger fireworks ONLY if current user became #1
        const youNowTop1 = entries.find(e => e.participantId === currentParticipantId && e.rank === 1)
        const youWereNotTop1 = prevRanks[currentParticipantId] !== 1

        if (youNowTop1 && youWereNotTop1) {
            setShowFireworks(true)
            setTimeout(() => setShowFireworks(false), 1200)
        }

        const newRanks: { [id: string]: number } = {}
        entries.forEach(e => {
            newRanks[e.participantId] = e.rank
        })
        setPrevRanks(newRanks)
    }, [entries, currentParticipantId])

    return (
        <div style={styles.container}>
            <h3 style={styles.title}>🏁 Live Leaderboard</h3>
            <ol style={styles.list}>
                {entries.map(entry => {
                    const move = movers[entry.participantId]
                    const isFirst = entry.rank === 1
                    const isYou = entry.participantId === currentParticipantId
                    const podiumIcon = getPodiumIcon(entry.rank)
                    const moveIcon = move === 'up' ? ' ⏫' : move === 'down' ? ' ⏬' : ''

                    return (
                        <li
                            key={entry.participantId}
                            style={{
                                ...styles.item,
                                ...(isYou ? styles.youRow : {}),
                                ...(move === 'up' ? styles.moveUp : {}),
                                ...(move === 'down' ? styles.moveDown : {}),
                                ...(isFirst ? styles.firstPlace : {}),
                            }}
                        >
              <span style={styles.rank}>
                {podiumIcon || `#${entry.rank}`}
              </span>
                            <span style={styles.name}>
                {entry.name || entry.participantId}
                                {isYou && <span style={styles.youBadge}>you</span>}
              </span>
                            <span style={styles.score}>
                {entry.score}{moveIcon}
              </span>
                        </li>
                    )
                })}
            </ol>

            {showFireworks && <FireworksOverlay />}
        </div>
    )
}

const getPodiumIcon = (rank: number) => {
    switch (rank) {
        case 1:
            return '🥇'
        case 2:
            return '🥈'
        case 3:
            return '🥉'
        default:
            return null
    }
}

const FireworksOverlay: React.FC = () => (
    <div style={styles.fireworksContainer}>
        <span style={styles.firework}>🎆</span>
        <span style={{ ...styles.firework, left: '60%' }}>🎇</span>
        <span style={{ ...styles.firework, left: '40%' }}>✨</span>
    </div>
)

const styles: { [key: string]: React.CSSProperties } = {
    container: {
        position: 'relative',
    },
    title: {
        fontSize: 20,
        marginBottom: 12,
        color: '#333',
    },
    list: {
        listStyle: 'none',
        padding: 0,
        margin: 0,
    },
    item: {
        fontSize: 16,
        padding: '10px 12px',
        marginBottom: 6,
        borderRadius: 8,
        backgroundColor: '#f8f9fa',
        border: '1px solid #eee',
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        transition: 'transform 0.3s ease, background-color 0.4s ease',
    },
    rank: {
        fontWeight: 600,
        color: '#007bff',
        minWidth: 36,
    },
    name: {
        fontWeight: 500,
        flex: 1,
    },
    score: {
        fontFamily: 'monospace',
        fontSize: 14,
        color: '#444',
    },
    moveUp: {
        backgroundColor: '#d1e7dd',
        transform: 'translateY(-4px)',
    },
    moveDown: {
        backgroundColor: '#f8d7da',
        transform: 'translateY(4px)',
    },
    firstPlace: {
        border: '2px solid gold',
        boxShadow: '0 0 12px rgba(255, 215, 0, 0.6)',
    },
    youRow: {
        backgroundColor: '#e0f7fa',
        borderColor: '#00acc1',
    },
    youBadge: {
        fontSize: 12,
        marginLeft: 8,
        padding: '2px 6px',
        backgroundColor: '#00acc1',
        color: '#fff',
        borderRadius: 10,
        textTransform: 'uppercase',
    },
    fireworksContainer: {
        position: 'absolute',
        top: -20,
        left: 0,
        right: 0,
        textAlign: 'center',
        pointerEvents: 'none',
        animation: 'fadeOut 1s ease-out forwards',
    },
    firework: {
        fontSize: 28,
        animation: 'pop 0.6s ease',
        position: 'relative',
        left: '50%',
        transform: 'translateX(-50%)',
        margin: '0 8px',
    },
}
