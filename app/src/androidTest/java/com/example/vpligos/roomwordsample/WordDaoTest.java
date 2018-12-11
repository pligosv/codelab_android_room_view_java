package com.example.vpligos.roomwordsample;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class WordDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private WordDao wordDao;
    private WordRoomDatabase db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, WordRoomDatabase.class)
                //Allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();
        wordDao = db.wordDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertAndGetWord() throws Exception {
        Word word = new Word("word");
        wordDao.insert(word);
        List<Word> allWords = LiveDataTestUtil.getValue(wordDao.getAllWords());
        assertEquals(allWords.get(0).getWord(), word.getWord());
    }

    @Test
    public void getAllWords() throws Exception {
        Word word = new Word("aaa");
        wordDao.insert(word);
        Word word2 = new Word("bbb");
        wordDao.insert(word2);
        List<Word> allWords = LiveDataTestUtil.getValue(wordDao.getAllWords());
        assertEquals(allWords.get(0).getWord(), word.getWord());
        assertEquals(allWords.get(1).getWord(), word2.getWord());
    }

    @Test
    public void deleteAll() throws Exception {
        Word word = new Word("word");
        wordDao.insert(word);
        Word word2 = new Word("word2");
        wordDao.insert(word2);
        wordDao.deleteAll();
        List<Word> allWords = LiveDataTestUtil.getValue(wordDao.getAllWords());
        assertTrue(allWords.isEmpty());
    }
}
