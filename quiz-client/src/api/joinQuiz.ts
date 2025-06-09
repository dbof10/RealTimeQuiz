import {JoinResponse} from "../model/joinResponse";

const API_BASE = 'http://localhost:8080';


export async function joinQuiz(
    quizId: string,
    name?: string
): Promise<JoinResponse> {
    const res = await fetch(
        `${API_BASE}/api/v1/quizzes/${encodeURIComponent(quizId)}/join`,
        {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({participantName: name}),
        }
    );

    if (!res.ok) {
        const text = await res.text();
        throw new Error(`joinQuiz failed: ${res.status} ${text}`);
    }

    return (await res.json()) as JoinResponse;
}
