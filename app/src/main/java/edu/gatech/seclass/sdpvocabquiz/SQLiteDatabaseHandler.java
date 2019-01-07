package edu.gatech.seclass.sdpvocabquiz;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.TreeSet;

import android.content.ContentValues;
import android.util.Log;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6;

    private static final String DATABASE_NAME = "SDPVocabQuizDB";

    // User table
    private static final String TABLE_USER = "user";
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_REALNAME = "real_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MAJOR = "major";
    private static final String KEY_SENIORITY_LEVEL = "seniority_level";

    // Quiz table
    private static final String TABLE_QUIZ = "quizzes";
    private static final String KEY_QUIZ_ID = "id";
    private static final String KEY_CREATOR = "creator";
    private static final String KEY_QUIZ_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_WORDS = "words";
    private static final String KEY_DEFINITIONS = "definitions";
    private static final String KEY_INCDEFINITIONS = "incorrect_definitions";

    // Quiz result table
    private static final String TABLE_QUIZ_RESULT = "quiz_result";
    private static final String KEY_RESULT_ID = "id";
    private static final String KEY_RESULT_QUIZ_NAME = "quiz_name";
    private static final String KEY_PLAYER_NAME = "player_name";
    private static final String KEY_PERCENTAGE = "percentage";
    private static final String KEY_FINISH_TIME = "finish_time";

    // user table create statement
    private static final String CREATE_USER = "CREATE TABLE " + TABLE_USER + " (" + KEY_USER_ID + " INTEGER PRIMARY KEY, " + KEY_USERNAME
            + " TEXT, " + KEY_REALNAME + " TEXT, " + KEY_EMAIL + " TEXT, " + KEY_MAJOR + " TEXT, " + KEY_SENIORITY_LEVEL + " TEXT " + ")";

    // quiz table create statement
    private static final String CREATE_QUIZ = "CREATE TABLE " + TABLE_QUIZ + " (" + KEY_QUIZ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_CREATOR + " TEXT, " + KEY_QUIZ_NAME + " TEXT, " + KEY_DESCRIPTION + " TEXT, " + KEY_WORDS + " TEXT, " +
            KEY_DEFINITIONS + " TEXT, " + KEY_INCDEFINITIONS + " TEXT" + ")";

    // quiz result table statement
    private static final String CREATE_QUIZ_RESULT = "CREATE TABLE " + TABLE_QUIZ_RESULT + " (" + KEY_RESULT_ID + " INTEGER PRIMARY KEY, "
            + KEY_RESULT_QUIZ_NAME + " TEXT, " + KEY_PLAYER_NAME + " TEXT, " + KEY_PERCENTAGE + " TEXT, " + KEY_FINISH_TIME + " TEXT" + ")";

    private static final String[] COLUMNS = {KEY_QUIZ_ID, KEY_CREATOR, KEY_QUIZ_NAME, KEY_DESCRIPTION, KEY_WORDS, KEY_DEFINITIONS, KEY_INCDEFINITIONS};

    private static final String[] RESULT_COLUMNS = {KEY_RESULT_QUIZ_NAME, KEY_PLAYER_NAME, KEY_PERCENTAGE, KEY_FINISH_TIME};




    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_QUIZ);
        db.execSQL(CREATE_QUIZ_RESULT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_RESULT);

        this.onCreate(db);

    }
    // ===== user table ===== //

    // store user info
    public boolean addUser(String username, String realName, String email, String major, String seniority) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_USERNAME, username);
        values.put(KEY_REALNAME, realName);
        values.put(KEY_EMAIL, email);
        values.put(KEY_MAJOR, major);
        values.put(KEY_SENIORITY_LEVEL, seniority);

        long newRowId = db.insert(TABLE_USER, null, values);
        return newRowId != -1;
    }

    public void removeStudent(String username) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, KEY_USERNAME + " = ?", new String[] {username});
        db.close();
    }

    public List<User> allStudent() {

        List<User> users = new LinkedList<>();
        String query = "SELECT * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        User user = null;

        if (cursor.moveToFirst()) {

            do {

                user = new User(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4));
                users.add(user);


            } while (cursor.moveToNext());

        }
        return users;

    }

    // check if input username exist
    public boolean checkUsername(String input) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[]{KEY_USERNAME}, KEY_USERNAME + " = ?", new String[]{input},
                null, null, null, null);
        return cursor.getCount() > 0;
    }

    public boolean checkQuizName(String input) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_QUIZ, new String[]{KEY_QUIZ_NAME}, KEY_QUIZ_NAME + " = ?", new String[]{input},
                null, null, null, null);
        return cursor.getCount() > 0;

    }

    // ===== quiz table ==== //

    public void addQuiz(Quiz quiz) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues getValues = new ContentValues();
        getValues.put(KEY_CREATOR, quiz.creator);
        getValues.put(KEY_QUIZ_NAME, quiz.quizName);
        getValues.put(KEY_DESCRIPTION, quiz.quizDescription);
        getValues.put(KEY_WORDS, quiz.getWords);
        getValues.put(KEY_DEFINITIONS, quiz.wordDefinitions);
        getValues.put(KEY_INCDEFINITIONS, quiz.incorrectDefinitions);
        db.insert(TABLE_QUIZ, null, getValues);
        db.close();

    }

    public void deleteQuiz(String quizName) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_QUIZ, KEY_QUIZ_NAME + " = ?", new String[] {quizName});
        db.close();
    }

    // Get Quizzes list that were created by Student
    public List<String> getQuizzesCreatedByUser(String userName) {

        SQLiteDatabase db = this.getWritableDatabase();
        List<String> quizzesCreatedByUser = new ArrayList<>();
        System.out.print(userName);
        Cursor cursor;
        String quizName;

        try {
            cursor = db.query(TABLE_QUIZ, new String[]{KEY_CREATOR, KEY_QUIZ_NAME},
                    KEY_CREATOR + " = ?", new String[]{userName},
                    null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    quizName = cursor.getString(cursor.getColumnIndex(KEY_QUIZ_NAME));
                    quizzesCreatedByUser.add(quizName);

                } while (cursor.moveToNext());
            }

            cursor.close();

        }catch (SQLiteException e)
        {
            System.out.print("Retrieve quizzes created by user failed");
        }

        return quizzesCreatedByUser;

    }

    // Get Quizzes list that were created by Student
    public List<String> getQuizzesCreatedByOthers(String userName) {

        SQLiteDatabase db = this.getWritableDatabase();
        List<String> quizListCreatedByOthers = new ArrayList<>();
        System.out.print(userName);
        Cursor cursor;
        String quizName;

        try {
            cursor = db.query(TABLE_QUIZ, new String[]{KEY_CREATOR, KEY_QUIZ_NAME},
                    KEY_CREATOR + " != ?", new String[]{userName},
                    null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    quizName = cursor.getString(cursor.getColumnIndex(KEY_QUIZ_NAME));
                    quizListCreatedByOthers.add(quizName);

                } while (cursor.moveToNext());
            }

            cursor.close();

        }catch (SQLiteException e)
        {
            System.out.print("Retrieve quizzes created by others failed");
        }

        return quizListCreatedByOthers;

    }

    // Get All Quizzes Name List
    public List<String> getQuizzesNameList(String userName) {

        SQLiteDatabase db = this.getWritableDatabase();
        List<String> quizzesNameList = new ArrayList<>();
        Cursor cursor;
        String quizName;

        try {

            // get quizzed that were practiced by user
            cursor = db.query(TABLE_QUIZ_RESULT, new String[]{ KEY_RESULT_QUIZ_NAME, KEY_PLAYER_NAME, KEY_FINISH_TIME},
                    KEY_PLAYER_NAME + " =?", new String[] {userName},KEY_RESULT_QUIZ_NAME, null, KEY_FINISH_TIME + " DESC", null);

            if (cursor.moveToFirst()) {
                do {
                    quizName = cursor.getString(cursor.getColumnIndex(KEY_RESULT_QUIZ_NAME));
                    quizzesNameList.add(quizName);

                } while (cursor.moveToNext());
            }

            cursor.close();

            // get quizzes that were practiced by others
            cursor = db.query(TABLE_QUIZ_RESULT, new String[]{ KEY_RESULT_QUIZ_NAME, KEY_PLAYER_NAME, KEY_FINISH_TIME},
                    KEY_PLAYER_NAME + " !=?", new String[] {userName},KEY_RESULT_QUIZ_NAME, null, KEY_FINISH_TIME + " DESC", null);

            if (cursor.moveToFirst()) {
                do {
                    quizName = cursor.getString(cursor.getColumnIndex(KEY_RESULT_QUIZ_NAME));
                    quizzesNameList.add(quizName);

                } while (cursor.moveToNext());
            }

            cursor.close();

        }catch (SQLiteException e)
        {
            System.out.print("Retrieve quizzes name list failed");
        }

        return quizzesNameList;

    }



    public Quiz getQuiz(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_QUIZ, COLUMNS, KEY_QUIZ_ID + " = ?",
                new String[] {String.valueOf(id)},
                null,
                null,
                null,
                null

                );

        if (cursor != null) {

            cursor.moveToFirst();
        }

        Quiz quiz = new Quiz(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6));
        return quiz;

    }

    public Quiz getQuizByName(String quizName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_QUIZ, COLUMNS, KEY_QUIZ_NAME + " = ?",
                new String[] {quizName},
                null,
                null,
                null,
                null

        );

        if (cursor != null) {

            cursor.moveToFirst();
        }

        Quiz quiz = new Quiz(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6));
        return quiz;

    }

    public List<Quiz> allQuizzes() {

        List<Quiz> quizzes = new LinkedList<>();
        String query = "SELECT * FROM " + TABLE_QUIZ;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        Quiz quiz = null;

        if (cursor.moveToFirst()) {

            do {

                quiz = new Quiz(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6));
                quizzes.add(quiz);


            } while (cursor.moveToNext());

        }
        return quizzes;

    }

    public int updateQuiz(Quiz quiz) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues getValues = new ContentValues();

        getValues.put(KEY_QUIZ_NAME, quiz.quizName);
        getValues.put(KEY_CREATOR, quiz.creator);

        getValues.put(KEY_DESCRIPTION, quiz.quizDescription);
        getValues.put(KEY_WORDS, quiz.getWords);
        getValues.put(KEY_DEFINITIONS, quiz.wordDefinitions);
        getValues.put(KEY_INCDEFINITIONS, quiz.incorrectDefinitions);

        int i = db.update(TABLE_QUIZ, getValues, KEY_QUIZ_ID + " = ?",
                new String[] {String.valueOf(quiz.id)});

        db.close();
        return i;
    }


    // ===== quiz result table ===== //

    // store quiz result
    public long storeQuizResult(quizResult result) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues getValues = new ContentValues();

        getValues.put(KEY_RESULT_QUIZ_NAME, result.getQuizName());
        getValues.put(KEY_PLAYER_NAME, result.getPlayerName());
        getValues.put(KEY_PERCENTAGE, result.getPercentage());
        getValues.put(KEY_FINISH_TIME, result.getFinishTime());

        return db.insert(TABLE_QUIZ_RESULT, null, getValues);
    }

    // get a quiz result
    public quizResult getQuizResult(int resultId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_QUIZ_RESULT, null, KEY_RESULT_ID + " = ? ",
                new String[] {String.valueOf(resultId)}, null,null, null);

        if (cursor != null) cursor.moveToFirst();

        quizResult result = new quizResult(cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getDouble(3),
                cursor.getString(4));

        return result;
    }

    public List<quizResult> getAllQuizResults(String userName) {


        SQLiteDatabase db = this.getReadableDatabase();
        List<quizResult> allQuizResults = new ArrayList<>();

        System.out.print(userName);
        Cursor cursor;
        quizResult quiz_result = null;

        try {
            cursor = db.query(TABLE_QUIZ_RESULT, new String[]{KEY_PLAYER_NAME, KEY_RESULT_QUIZ_NAME},
                    KEY_PLAYER_NAME + " = ?", new String[]{userName},
                    null, null, null, null);


            if (cursor.moveToFirst()) {
                do {
                    quiz_result = new quizResult(cursor.getString(cursor.getColumnIndex(KEY_RESULT_QUIZ_NAME)),
                            cursor.getString(cursor.getColumnIndex(KEY_PLAYER_NAME)),
                            cursor.getDouble(cursor.getColumnIndex(KEY_PERCENTAGE)),
                            cursor.getString(cursor.getColumnIndex(KEY_FINISH_TIME)));
                    allQuizResults.add(quiz_result);
                } while (cursor.moveToNext());

            }
            cursor.close();

        }catch (SQLiteException e)
        {
            System.out.print("Retrieve quiz results failed");

        }

        return allQuizResults;

    }

    public List<String> getQuizResultsQuizNames(String userName) {

        SQLiteDatabase db = this.getWritableDatabase();
        List<String> quizResultNames = new ArrayList<>();
        System.out.print(userName);
        Cursor cursor;
        String quizName;

        try {
            cursor = db.query(TABLE_QUIZ_RESULT, new String[]{KEY_PLAYER_NAME, KEY_RESULT_QUIZ_NAME},
                    KEY_PLAYER_NAME + " = ?", new String[]{userName},
                    null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    quizName = cursor.getString(cursor.getColumnIndex(KEY_RESULT_QUIZ_NAME));
                    quizResultNames.add(quizName);

                } while (cursor.moveToNext());
            }

            cursor.close();

        }catch (SQLiteException e)
        {
            System.out.print("Retrieve quizzes created by user failed");
        }

        return quizResultNames;

    }

    // delete a quiz result
    public void deleteQuizResult(String quizName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUIZ_RESULT, KEY_RESULT_QUIZ_NAME + " = ?", new String[] {quizName});
        db.close();
    }

    // get first score of user
    public quizResult getFirstScore(String quizName) {
        quizResult res = null;

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.query(TABLE_QUIZ_RESULT, RESULT_COLUMNS, KEY_RESULT_QUIZ_NAME + " = ?" + " AND " + KEY_PLAYER_NAME + " = ? ", new String[]{quizName, User.userName},
                    null, null, KEY_FINISH_TIME + " DESC", null);

            if (cursor != null && cursor.moveToFirst()) {
                res = new quizResult(cursor.getString(cursor.getColumnIndex(KEY_RESULT_QUIZ_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_PLAYER_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_PERCENTAGE)),
                        cursor.getString(cursor.getColumnIndex(KEY_FINISH_TIME)));
            }
        }catch (Exception e){


        }
        return res;
    }

    // get highest score of user
    public quizResult getHighestScore(String quizName) {
        quizResult res = null;

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.query(TABLE_QUIZ_RESULT, RESULT_COLUMNS, KEY_RESULT_QUIZ_NAME + " = ?" + " AND " + KEY_PLAYER_NAME + " = ? ", new String[]{quizName, User.userName},
                    null, null, KEY_PERCENTAGE + " DESC", null);

            if (cursor != null && cursor.moveToFirst()) {
                res = new quizResult(cursor.getString(cursor.getColumnIndex(KEY_RESULT_QUIZ_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_PLAYER_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_PERCENTAGE)),
                        cursor.getString(cursor.getColumnIndex(KEY_FINISH_TIME)));
            }
        }catch (Exception e){

        }

        return res;
    }

    // get first three name get 100%
    public TreeSet<String> getFirstThree(String quizName) {

        TreeSet<String> results = new TreeSet<>();

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.query(TABLE_QUIZ_RESULT, RESULT_COLUMNS, KEY_RESULT_QUIZ_NAME + " = ?" + " AND " + KEY_PERCENTAGE + " = ?", new String[]{quizName, "100.0"},
                    null, null, KEY_FINISH_TIME + " DESC", "3");


            if (cursor != null && cursor.moveToFirst()) {
                do {

                    results.add(cursor.getString(cursor.getColumnIndex(KEY_PLAYER_NAME)));

                } while (cursor.moveToNext());
            }
        }catch (Exception e){

        }
        return results;
    }

}
