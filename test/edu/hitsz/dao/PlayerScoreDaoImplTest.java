package edu.hitsz.dao;

import edu.hitsz.application.Mode;
import edu.hitsz.application.PlayerScore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerScoreDaoImplTest {

    private static final String SCORES_FILE = "file/scores.txt";
    private static final String BACKUP_FILE = "file/scores_backup_test.txt";

    private PlayerScoreDaoImpl dao;

    // ------------------------------------------------------------------ setup

    @BeforeEach
    void setUp() throws IOException {
        File dir = new File("file");
        if (!dir.exists()) dir.mkdirs();

        // Back up the real scores file so tests never corrupt it
        File real = new File(SCORES_FILE);
        if (real.exists()) {
            Files.copy(real.toPath(),
                       new File(BACKUP_FILE).toPath(),
                       StandardCopyOption.REPLACE_EXISTING);
        }

        // Start every test with an empty file
        new FileWriter(SCORES_FILE, false).close();

        dao = new PlayerScoreDaoImpl();
    }

    @AfterEach
    void tearDown() throws IOException {
        File backup = new File(BACKUP_FILE);
        if (backup.exists()) {
            Files.copy(backup.toPath(),
                       new File(SCORES_FILE).toPath(),
                       StandardCopyOption.REPLACE_EXISTING);
            backup.delete();
        } else {
            new File(SCORES_FILE).delete();
        }
    }

    // -------------------------------------------------- getPlayerScores / add

    @Test
    void testGetPlayerScores_InitiallyEmpty() {
        assertTrue(dao.getPlayerScores().isEmpty(),
                   "New DAO should have an empty in-memory list");
    }

    @Test
    void testAddPlayerScore_StoresInMemory() {
        PlayerScore ps = new PlayerScore(1, 100, Mode.Easy);
        dao.addPlayerScore(ps);

        List<PlayerScore> scores = dao.getPlayerScores();
        assertEquals(1, scores.size());
        assertEquals(1,         scores.get(0).getPlayerId());
        assertEquals(100,       scores.get(0).getScore());
        assertEquals(Mode.Easy, scores.get(0).getMode());
    }

    @Test
    void testConstructorWithList_UsesProvidedList() {
        ArrayList<PlayerScore> initial = new ArrayList<>();
        initial.add(new PlayerScore(5, 500, Mode.Medium));

        PlayerScoreDaoImpl daoWithList = new PlayerScoreDaoImpl(initial);

        List<PlayerScore> scores = daoWithList.getPlayerScores();
        assertEquals(1, scores.size());
        assertEquals(5,           scores.get(0).getPlayerId());
        assertEquals(500,         scores.get(0).getScore());
        assertEquals(Mode.Medium, scores.get(0).getMode());
    }

    // ------------------------------------------------------- writePlayerScore

    @Test
    void testWritePlayerScore_ProducesCorrectFileLine() throws IOException {
        dao.writePlayerScore(new PlayerScore(1, 100, Mode.Easy));

        try (BufferedReader reader = new BufferedReader(new FileReader(SCORES_FILE))) {
            assertEquals("1,100,Easy", reader.readLine());
            assertNull(reader.readLine(), "File should contain exactly one line");
        }
    }

    @Test
    void testWritePlayerScore_AppendsMultipleEntries() throws IOException {
        dao.writePlayerScore(new PlayerScore(1, 100, Mode.Easy));
        dao.writePlayerScore(new PlayerScore(2, 200, Mode.Hard));

        try (BufferedReader reader = new BufferedReader(new FileReader(SCORES_FILE))) {
            assertEquals("1,100,Easy", reader.readLine());
            assertEquals("2,200,Hard", reader.readLine());
            assertNull(reader.readLine());
        }
    }

    // ---------------------------------------------------------------- findAll

    @Test
    void testFindAll_EmptyFile_ReturnsEmptyList() {
        List<PlayerScore> result = dao.findAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAll_ParsesAllModes() {
        dao.writePlayerScore(new PlayerScore(1, 100, Mode.Easy));
        dao.writePlayerScore(new PlayerScore(2, 200, Mode.Medium));
        dao.writePlayerScore(new PlayerScore(3, 300, Mode.Hard));

        List<PlayerScore> result = dao.findAll();

        assertEquals(3, result.size());

        assertEquals(1,         result.get(0).getPlayerId());
        assertEquals(100,       result.get(0).getScore());
        assertEquals(Mode.Easy, result.get(0).getMode());

        assertEquals(2,           result.get(1).getPlayerId());
        assertEquals(200,         result.get(1).getScore());
        assertEquals(Mode.Medium, result.get(1).getMode());

        assertEquals(3,         result.get(2).getPlayerId());
        assertEquals(300,       result.get(2).getScore());
        assertEquals(Mode.Hard, result.get(2).getMode());
    }

    @Test
    void testFindAll_ClearsStaleInMemoryData() {
        // Put stale data into the in-memory list
        dao.addPlayerScore(new PlayerScore(99, 999, Mode.Hard));

        // findAll must discard it and reload from the (empty) file
        List<PlayerScore> result = dao.findAll();
        assertTrue(result.isEmpty(),
                   "findAll should clear in-memory list before reloading");
    }

    // ------------------------------------------------ write + read round trip

    @Test
    void testWriteAndRead_Roundtrip() {
        dao.writePlayerScore(new PlayerScore(42, 9999, Mode.Hard));

        List<PlayerScore> result = dao.findAll();

        assertEquals(1, result.size());
        assertEquals(42,        result.get(0).getPlayerId());
        assertEquals(9999,      result.get(0).getScore());
        assertEquals(Mode.Hard, result.get(0).getMode());
    }

    @Test
    void testWriteAndRead_LargeScore() {
        dao.writePlayerScore(new PlayerScore(7, Integer.MAX_VALUE, Mode.Medium));

        List<PlayerScore> result = dao.findAll();

        assertEquals(1, result.size());
        assertEquals(Integer.MAX_VALUE, result.get(0).getScore());
    }
}
