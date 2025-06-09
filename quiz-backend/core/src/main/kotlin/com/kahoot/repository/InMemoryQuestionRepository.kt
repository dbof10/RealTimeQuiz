// src/main/kotlin/com/kahoot/repository/InMemoryQuestionRepository.kt
package com.kahoot.repository

import com.kahoot.model.Question
import org.springframework.stereotype.Repository

@Repository
class InMemoryQuestionRepository : QuestionRepository {
    override fun getAll(): List<Question> = listOf(
        Question("q1", "What is 2+2?", listOf("3", "4", "5"), "4"),
        Question("q2", "What's the capital of France?", listOf("Berlin", "Paris", "Rome"), "Paris"),
        Question("q3", "Pick a color", listOf("Red", "Green", "Blue"), "Red"),
        Question("q4", "Which planet is known as the Red Planet?", listOf("Earth", "Mars", "Jupiter"), "Mars"),
        Question("q5", "What is the boiling point of water?", listOf("90°C", "100°C", "110°C"), "100°C"),
        Question("q6", "Which language is used for Android development?", listOf("Swift", "Kotlin", "Python"), "Kotlin"),
        Question("q7", "What is the largest mammal?", listOf("Elephant", "Blue Whale", "Giraffe"), "Blue Whale"),
        Question("q8", "Who wrote 'Hamlet'?", listOf("Shakespeare", "Dickens", "Hemingway"), "Shakespeare"),
        Question("q9", "Which gas do plants absorb?", listOf("Oxygen", "Carbon Dioxide", "Nitrogen"), "Carbon Dioxide"),
        Question("q10", "What is the square root of 16?", listOf("4", "5", "6"), "4"),
        Question("q11", "What is H2O?", listOf("Oxygen", "Hydrogen", "Water"), "Water"),
        Question("q12", "Which animal barks?", listOf("Cat", "Dog", "Horse"), "Dog"),
        Question("q13", "What is the capital of Japan?", listOf("Seoul", "Tokyo", "Beijing"), "Tokyo"),
        Question("q14", "Which organ pumps blood?", listOf("Lungs", "Heart", "Liver"), "Heart"),
        Question("q15", "How many continents are there?", listOf("5", "6", "7"), "7"),
        Question("q16", "What's 5 x 3?", listOf("15", "10", "8"), "15"),
        Question("q17", "Who painted the Mona Lisa?", listOf("Van Gogh", "Da Vinci", "Picasso"), "Da Vinci"),
        Question("q18", "What is the freezing point of water?", listOf("0°C", "5°C", "10°C"), "0°C"),
        Question("q19", "Which shape has 3 sides?", listOf("Square", "Circle", "Triangle"), "Triangle"),
        Question("q20", "Which language runs in a browser?", listOf("Java", "C++", "JavaScript"), "JavaScript"),
        Question("q21", "Which is a programming language?", listOf("HTML", "CSS", "Python"), "Python"),
        Question("q22", "How many legs do spiders have?", listOf("6", "8", "10"), "8"),
        Question("q23", "Which month has 28 days?", listOf("February", "April", "All of them"), "All of them"),
        Question("q24", "What color do you get by mixing red and white?", listOf("Pink", "Purple", "Brown"), "Pink"),
        Question("q25", "Which number is even?", listOf("3", "5", "4"), "4"),
        Question("q26", "Where does the sun rise?", listOf("West", "East", "North"), "East"),
        Question("q27", "What is the opposite of 'cold'?", listOf("Warm", "Hot", "Both"), "Both"),
        Question("q28", "What comes after 'C'?", listOf("B", "D", "E"), "D"),
        Question("q29", "Which animal has a long neck?", listOf("Elephant", "Giraffe", "Kangaroo"), "Giraffe"),
        Question("q30", "How many minutes in an hour?", listOf("60", "100", "120"), "60"),
        Question("q31", "What is 10 divided by 2?", listOf("4", "5", "6"), "5"),
        Question("q32", "Which planet do we live on?", listOf("Mars", "Earth", "Venus"), "Earth"),
        Question("q33", "How many colors in a rainbow?", listOf("5", "6", "7"), "7"),
        Question("q34", "Which sense uses the nose?", listOf("Sight", "Smell", "Taste"), "Smell"),
        Question("q35", "What's the first day of the week?", listOf("Sunday", "Monday", "Saturday"), "Monday"),
        Question("q36", "How many days in a leap year?", listOf("365", "366", "367"), "366"),
        Question("q37", "Which fruit is yellow?", listOf("Apple", "Banana", "Grape"), "Banana"),
        Question("q38", "Which animal says 'meow'?", listOf("Dog", "Cat", "Cow"), "Cat"),
        Question("q39", "What is the main gas in air?", listOf("Oxygen", "Nitrogen", "Hydrogen"), "Nitrogen"),
        Question("q40", "What shape is a wheel?", listOf("Square", "Oval", "Circle"), "Circle"),
        Question("q41", "Which number is a prime?", listOf("4", "5", "6"), "5"),
        Question("q42", "Which is not a vegetable?", listOf("Carrot", "Potato", "Apple"), "Apple"),
        Question("q43", "What is 12 + 8?", listOf("20", "21", "22"), "20"),
        Question("q44", "What color is the sky?", listOf("Green", "Blue", "Orange"), "Blue"),
        Question("q45", "How many letters in 'Kotlin'?", listOf("6", "7", "8"), "6"),
        Question("q46", "What is the largest ocean?", listOf("Atlantic", "Indian", "Pacific"), "Pacific"),
        Question("q47", "What is a baby dog called?", listOf("Kitten", "Puppy", "Cub"), "Puppy"),
        Question("q48", "How many hours in a day?", listOf("24", "12", "48"), "24"),
        Question("q49", "What season comes after winter?", listOf("Summer", "Spring", "Autumn"), "Spring"),
        Question("q50", "Which continent is Egypt in?", listOf("Asia", "Africa", "Europe"), "Africa")
    )
}

