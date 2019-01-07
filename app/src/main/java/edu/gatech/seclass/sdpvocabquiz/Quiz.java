package edu.gatech.seclass.sdpvocabquiz;

import java.io.Serializable;

public class Quiz implements Serializable {

    public int id;
    public String creator;
    public String quizName;
    public String quizDescription;
    public String getWords;
    public String wordDefinitions;
    public String incorrectDefinitions;


    public Quiz(String quizName) {
        this.quizName = quizName;
    }

    public Quiz(String quizName, String quizDescription, String getWords, String wordDefinitions, String incorrectDefinitions) {
        this.quizName = quizName;
        this.quizDescription = quizDescription;
        this.getWords = getWords;
        this.wordDefinitions = wordDefinitions;
        this.incorrectDefinitions = incorrectDefinitions;
    }

    public Quiz(int id, String creator, String quizName, String quizDescription, String getWords, String wordDefinitions, String incorrectDefinitions) {

        this.id = id;
        this.creator = creator;
        this.quizName = quizName;
        this.quizDescription = quizDescription;
        this.getWords = getWords;
        this.wordDefinitions = wordDefinitions;
        this.incorrectDefinitions = incorrectDefinitions;

    }

    public String getCreator() {

        return creator;
    }

    public String getQuizName() {

        return quizName;
    }

    public String getQuizDescription() {

        return quizDescription;
    }

    public String getWords() {

        return getWords;
    }

    public String wordDefinitions() {

        return wordDefinitions;
    }

    public String incorrectDefinitions() {

        return incorrectDefinitions;
    }

    public String toString() {
        return quizName;
    }
}
