package com.kahoot.service

import com.kahoot.model.JoinRequest
import com.kahoot.model.JoinResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/quizzes")
class QuizController(private val service: QuizService) {
    @PostMapping("/{quizId}/join")
    fun join(
        @PathVariable quizId: String,
        @RequestBody(required = false) req: JoinRequest
    ): ResponseEntity<JoinResponse> {
        val pid = service.joinQuiz(quizId, req.participantName)
        return ResponseEntity.ok(JoinResponse(quizId, pid))
    }

}
