package com.kahoot.repository

import com.kahoot.model.Question

interface QuestionRepository {
    fun getAll(): List<Question>
}
