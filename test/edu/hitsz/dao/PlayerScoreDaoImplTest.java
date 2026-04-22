package edu.hitsz.dao;

import edu.hitsz.application.PlayerScore;
import edu.hitsz.gameConfig.DifficultyLevel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerScoreDaoImplTest {

    private static final String SCORES_FILE = "file/scores.json";
    private static final String BACKUP_FILE = "file/scores_backup_test.json";

    private PlayerScoreDaoImpl dao;

    @BeforeEach
    void setUp() throws IOException {
        File dir = new File("file");
        if (!dir.exists()) dir.mkdirs();

        File real = new File(SCORES_FILE);
        if (real.exists()) {
            Files.copy(real.toPath(), new File(BACKUP_FILE).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            real.delete();
        }

        dao = new PlayerScoreDaoImpl();
    }

    @AfterEach
    void tearDown() throws IOException {
        File backup = new File(BACKUP_FILE);
        if (backup.exists()) {
            Files.copy(backup.toPath(), new File(SCORES_FILE).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            backup.delete();
        } else {
            new File(SCORES_FILE).delete();
        }
    }

    @Test
    void testFindAll_NoFile_ReturnsEmpty() {
        List<PlayerScore> result = dao.findAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testWriteAndRead_Roundtrip() {
        dao.writePlayerScore(new PlayerScore("Alice", 9999, DifficultyLevel.HARD));

        List<PlayerScore> result = dao.findAll();
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getPlayerName());
        assertEquals(9999, result.get(0).getScore());
        assertEquals(DifficultyLevel.HARD, result.get(0).getDifficulty());
        assertTrue(result.get(0).getPlayerId() > 0, "playerId should be auto-assigned");
    }

    @Test
    void testWritePlayerScore_AutoIncrementId() {
        dao.writePlayerScore(new PlayerScore("A", 100, DifficultyLevel.NORMAL));
        dao.writePlayerScore(new PlayerScore("B", 200, DifficultyLevel.NORMAL));
        dao.writePlayerScore(new PlayerScore("C", 300, DifficultyLevel.NORMAL));

        List<PlayerScore> all = dao.findAll();
        assertEquals(3, all.size());
        assertEquals(1, all.get(0).getPlayerId());
        assertEquals(2, all.get(1).getPlayerId());
        assertEquals(3, all.get(2).getPlayerId());
    }

    @Test
    void testFindByDifficulty_FiltersAndSortsDesc() {
        dao.writePlayerScore(new PlayerScore("A", 100, DifficultyLevel.NORMAL));
        dao.writePlayerScore(new PlayerScore("B", 500, DifficultyLevel.HARD));
        dao.writePlayerScore(new PlayerScore("C", 300, DifficultyLevel.NORMAL));
        dao.writePlayerScore(new PlayerScore("D", 700, DifficultyLevel.EXPERT));
        dao.writePlayerScore(new PlayerScore("E", 50,  DifficultyLevel.NORMAL));

        List<PlayerScore> normal = dao.findByDifficulty(DifficultyLevel.NORMAL);
        assertEquals(3, normal.size());
        assertEquals(300, normal.get(0).getScore());
        assertEquals(100, normal.get(1).getScore());
        assertEquals(50,  normal.get(2).getScore());

        List<PlayerScore> expert = dao.findByDifficulty(DifficultyLevel.EXPERT);
        assertEquals(1, expert.size());
        assertEquals("D", expert.get(0).getPlayerName());
    }

    @Test
    void testFindByPlayerId_ReturnsMatchOrNull() {
        dao.writePlayerScore(new PlayerScore("A", 100, DifficultyLevel.NORMAL));
        dao.writePlayerScore(new PlayerScore("B", 200, DifficultyLevel.HARD));

        PlayerScore found = dao.findByPlayerId(2);
        assertNotNull(found);
        assertEquals("B", found.getPlayerName());

        assertNull(dao.findByPlayerId(999));
    }

    @Test
    void testDeletePlayerScore_ById() {
        dao.writePlayerScore(new PlayerScore("A", 100, DifficultyLevel.NORMAL));
        dao.writePlayerScore(new PlayerScore("B", 200, DifficultyLevel.NORMAL));
        dao.writePlayerScore(new PlayerScore("C", 300, DifficultyLevel.NORMAL));

        dao.deletePlayerScore(2);

        List<PlayerScore> all = dao.findAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().noneMatch(p -> p.getPlayerName().equals("B")));
    }

    @Test
    void testDeleteByIndex_DeletesRankedRow() {
        dao.writePlayerScore(new PlayerScore("A", 100, DifficultyLevel.NORMAL));
        dao.writePlayerScore(new PlayerScore("B", 500, DifficultyLevel.NORMAL));
        dao.writePlayerScore(new PlayerScore("C", 300, DifficultyLevel.NORMAL));

        // 排序后 B(500)、C(300)、A(100)；删除索引 1 应当删除 C
        dao.deleteByIndex(DifficultyLevel.NORMAL, 1);

        List<PlayerScore> remaining = dao.findByDifficulty(DifficultyLevel.NORMAL);
        assertEquals(2, remaining.size());
        assertEquals("B", remaining.get(0).getPlayerName());
        assertEquals("A", remaining.get(1).getPlayerName());
    }

    @Test
    void testDeleteByIndex_OutOfRange_NoOp() {
        dao.writePlayerScore(new PlayerScore("A", 100, DifficultyLevel.NORMAL));

        dao.deleteByIndex(DifficultyLevel.NORMAL, 99);
        dao.deleteByIndex(DifficultyLevel.NORMAL, -1);

        assertEquals(1, dao.findAll().size());
    }

    @Test
    void testPlayerName_PreservesSpecialChars() {
        String name = "Zhang\"San, 张三\n\\slash";
        dao.writePlayerScore(new PlayerScore(name, 42, DifficultyLevel.HARD));

        List<PlayerScore> all = dao.findAll();
        assertEquals(1, all.size());
        assertEquals(name, all.get(0).getPlayerName());
    }
}
