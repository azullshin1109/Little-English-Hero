package com.thaivantrung.littleenglishhero;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "LittleEnglishHero.db";
    public static final int DB_VERSION = 1;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // TABLE LESSON
        String createLesson = "CREATE TABLE Lesson(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "total INTEGER)";

        // TABLE VOCABULARY
        String createVocabulary = "CREATE TABLE Vocabulary(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "lessonId INTEGER," +
                "word TEXT," +
                "meaning TEXT," +
                "image TEXT)";

        db.execSQL(createLesson);
        db.execSQL(createVocabulary);

        insertData(db);
    }

    public ArrayList<VocabularyModel> getVocabularyByLesson(int lessonId){
        ArrayList<VocabularyModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Vocabulary WHERE lessonId = ?",
                new String[]{String.valueOf(lessonId)}
        );

        if(cursor.moveToFirst()){

            do{

                list.add(
                        new VocabularyModel(
                                cursor.getInt(0),
                                cursor.getInt(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4)
                        )
                );

            } while(cursor.moveToNext());

        }

        cursor.close();

        return list;

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void insertData(SQLiteDatabase db){

        // LESSON
        db.execSQL("INSERT INTO Lesson VALUES(null,'Animals',8)");
        db.execSQL("INSERT INTO Lesson VALUES(null,'Fruits',7)");
        db.execSQL("INSERT INTO Lesson VALUES(null,'Family',7)");
        db.execSQL("INSERT INTO Lesson VALUES(null,'Colors',7)");
        db.execSQL("INSERT INTO Lesson VALUES(null,'Numbers',10)");

        // ANIMALS
        db.execSQL("INSERT INTO Vocabulary VALUES(null,1,'Dog','Con chó','img_dog')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,1,'Cat','Con mèo','img_cat')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,1,'Tiger','Con hổ','img_tiger')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,1,'Chicken','Con gà','img_chicken')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,1,'Pig','Con heo','img_pig')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,1,'Lion','Sư tử','img_lion')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,1,'Duck','Con vịt','img_duck')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,1,'Cow','Con bò','img_cow')");

        // FRUITS
        db.execSQL("INSERT INTO Vocabulary VALUES(null,2,'Apple','Quả Táo','img_apple')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,2,'Banana','Quả Chuối','img_banana')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,2,'Mango','Quả Xoài','img_mango')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,2,'Orange','Quả Cam','img_orange')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,2,'Lemon','Quả Chanh','img_lemon')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,2,'Coconut','Quả Dừa','img_coconut')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,2,'Watermelon','Quả Dưa Hấu','img_watermelon')");

        // FAMILY
        db.execSQL("INSERT INTO Vocabulary VALUES(null,3,'Father','Ba','img_father')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,3,'Mother','Mẹ','img_mother')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,3,'Brother','Anh trai','img_brother')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,3,'Sister','Chị gái','img_sister')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,3,'Grandmother','Bà','img_grandmother')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,3,'Grandfather','Ông','img_grandfather')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,3,'Baby','Em bé','img_baby')");

        // COLORS
        db.execSQL("INSERT INTO Vocabulary VALUES(null,4,'Red','Màu đỏ','img_red')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,4,'Blue','Màu xanh dương','img_blue')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,4,'Yellow','Màu vàng','img_yellow')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,4,'Green','Màu xanh lá','img_green')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,4,'Pink','Màu hồng','img_pink')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,4,'Black','Màu đen','img_black')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,4,'White','Màu trắng','img_white')");

        // NUMBERS
        db.execSQL("INSERT INTO Vocabulary VALUES(null,5,'One','Số một','img_one')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,5,'Two','Số hai','img_two')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,5,'Three','Số ba','img_three')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,5,'Four','Số bốn','img_four')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,5,'Five','Số năm','img_five')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,5,'Six','Số sáu','img_six')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,5,'Seven','Số bảy','img_seven')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,5,'Eight','Số tám','img_eight')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,5,'Nine','Số chín','img_nine')");
        db.execSQL("INSERT INTO Vocabulary VALUES(null,5,'Ten','Số mười','img_ten')");


    }
}