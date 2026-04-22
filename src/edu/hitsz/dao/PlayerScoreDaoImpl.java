package edu.hitsz.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.hitsz.application.PlayerScore;
import edu.hitsz.gameConfig.DifficultyLevel;

public class PlayerScoreDaoImpl implements PlayerScoreDao {

    private static final String SCORES_FILE = "file/scores.json";

    private List<PlayerScore> playerScores;

    public PlayerScoreDaoImpl() {
        this.playerScores = new ArrayList<>();
    }

    public PlayerScoreDaoImpl(List<PlayerScore> playerScores) {
        this.playerScores = playerScores;
    }

    @Override
    public List<PlayerScore> findAll() {
        playerScores.clear();
        File f = new File(SCORES_FILE);
        if (!f.exists()) {
            return playerScores;
        }
        try {
            String content = new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);
            playerScores.addAll(JsonCodec.parse(content));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playerScores;
    }

    @Override
    public List<PlayerScore> findByDifficulty(DifficultyLevel difficulty) {
        return findAll().stream()
                .filter(p -> p.getDifficulty() == difficulty)
                .sorted(Comparator.comparingInt(PlayerScore::getScore).reversed()
                        .thenComparingLong(PlayerScore::getTimestamp))
                .collect(Collectors.toList());
    }

    @Override
    public PlayerScore findByPlayerId(int playerId) {
        for (PlayerScore p : findAll()) {
            if (p.getPlayerId() == playerId) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void writePlayerScore(PlayerScore playerScore) {
        findAll();
        if (playerScore.getPlayerId() <= 0) {
            int maxId = 0;
            for (PlayerScore p : playerScores) {
                maxId = Math.max(maxId, p.getPlayerId());
            }
            playerScore.setPlayerId(maxId + 1);
        }
        if (playerScore.getTimestamp() == 0) {
            playerScore.setTimestamp(System.currentTimeMillis());
        }
        playerScores.add(playerScore);
        persist();
    }

    @Override
    public void deletePlayerScore(int playerId) {
        findAll();
        playerScores.removeIf(p -> p.getPlayerId() == playerId);
        persist();
    }

    @Override
    public void deleteByIndex(DifficultyLevel difficulty, int rankIndex) {
        List<PlayerScore> ranked = findByDifficulty(difficulty);
        if (rankIndex < 0 || rankIndex >= ranked.size()) {
            return;
        }
        int targetId = ranked.get(rankIndex).getPlayerId();
        playerScores.removeIf(p -> p.getPlayerId() == targetId);
        persist();
    }

    public List<PlayerScore> getPlayerScores() {
        return playerScores;
    }

    public void addPlayerScore(PlayerScore playerScore) {
        this.playerScores.add(playerScore);
    }

    private void persist() {
        File dir = new File("file");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try (BufferedWriter w = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(SCORES_FILE, false), StandardCharsets.UTF_8))) {
            w.write(JsonCodec.write(playerScores));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------------- JSON
    // 简易 JSON 编解码：只处理本项目使用的数组 + 扁平对象，无需引入三方库
    private static final class JsonCodec {

        static String write(List<PlayerScore> list) {
            StringBuilder sb = new StringBuilder();
            sb.append("[\n");
            for (int i = 0; i < list.size(); i++) {
                PlayerScore p = list.get(i);
                sb.append("  {");
                sb.append("\"playerId\":").append(p.getPlayerId()).append(',');
                sb.append("\"playerName\":\"").append(escape(p.getPlayerName())).append("\",");
                sb.append("\"score\":").append(p.getScore()).append(',');
                sb.append("\"difficulty\":\"").append(p.getDifficulty().name()).append("\",");
                sb.append("\"timestamp\":").append(p.getTimestamp());
                sb.append('}');
                if (i < list.size() - 1) {
                    sb.append(',');
                }
                sb.append('\n');
            }
            sb.append(']');
            return sb.toString();
        }

        static List<PlayerScore> parse(String text) {
            List<PlayerScore> out = new ArrayList<>();
            if (text == null) {
                return out;
            }
            Cursor c = new Cursor(text);
            c.skipWs();
            if (c.peek() != '[') {
                return out;
            }
            c.next();
            c.skipWs();
            while (c.peek() != ']' && c.peek() != 0) {
                c.skipWs();
                if (c.peek() == '{') {
                    PlayerScore ps = parseObject(c);
                    if (ps != null) {
                        out.add(ps);
                    }
                }
                c.skipWs();
                if (c.peek() == ',') {
                    c.next();
                }
                c.skipWs();
            }
            return out;
        }

        private static PlayerScore parseObject(Cursor c) {
            c.next(); // {
            int playerId = 0;
            String playerName = "";
            int score = 0;
            DifficultyLevel difficulty = DifficultyLevel.NORMAL;
            long timestamp = 0L;
            c.skipWs();
            while (c.peek() != '}' && c.peek() != 0) {
                c.skipWs();
                String key = c.readString();
                c.skipWs();
                if (c.peek() == ':') {
                    c.next();
                }
                c.skipWs();
                switch (key) {
                    case "playerId":
                        playerId = (int) c.readNumber();
                        break;
                    case "playerName":
                        playerName = c.readString();
                        break;
                    case "score":
                        score = (int) c.readNumber();
                        break;
                    case "difficulty":
                        String d = c.readString();
                        try {
                            difficulty = DifficultyLevel.valueOf(d);
                        } catch (IllegalArgumentException ignored) {
                        }
                        break;
                    case "timestamp":
                        timestamp = c.readNumber();
                        break;
                    default:
                        c.skipValue();
                        break;
                }
                c.skipWs();
                if (c.peek() == ',') {
                    c.next();
                }
                c.skipWs();
            }
            if (c.peek() == '}') {
                c.next();
            }
            return new PlayerScore(playerId, playerName, score, difficulty, timestamp);
        }

        private static String escape(String s) {
            if (s == null) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                char ch = s.charAt(i);
                switch (ch) {
                    case '"':  sb.append("\\\""); break;
                    case '\\': sb.append("\\\\"); break;
                    case '\n': sb.append("\\n");  break;
                    case '\r': sb.append("\\r");  break;
                    case '\t': sb.append("\\t");  break;
                    default:
                        if (ch < 0x20) {
                            sb.append(String.format("\\u%04x", (int) ch));
                        } else {
                            sb.append(ch);
                        }
                }
            }
            return sb.toString();
        }

        private static final class Cursor {
            final String s;
            int i;

            Cursor(String s) {
                this.s = s;
                this.i = 0;
            }

            char peek() {
                return i < s.length() ? s.charAt(i) : 0;
            }

            char next() {
                return i < s.length() ? s.charAt(i++) : 0;
            }

            void skipWs() {
                while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
                    i++;
                }
            }

            String readString() {
                if (peek() != '"') {
                    return "";
                }
                next();
                StringBuilder sb = new StringBuilder();
                while (i < s.length()) {
                    char ch = s.charAt(i++);
                    if (ch == '"') {
                        break;
                    }
                    if (ch == '\\' && i < s.length()) {
                        char e = s.charAt(i++);
                        switch (e) {
                            case '"':  sb.append('"'); break;
                            case '\\': sb.append('\\'); break;
                            case '/':  sb.append('/'); break;
                            case 'n':  sb.append('\n'); break;
                            case 'r':  sb.append('\r'); break;
                            case 't':  sb.append('\t'); break;
                            case 'b':  sb.append('\b'); break;
                            case 'f':  sb.append('\f'); break;
                            case 'u':
                                if (i + 4 <= s.length()) {
                                    sb.append((char) Integer.parseInt(s.substring(i, i + 4), 16));
                                    i += 4;
                                }
                                break;
                            default:   sb.append(e);
                        }
                    } else {
                        sb.append(ch);
                    }
                }
                return sb.toString();
            }

            long readNumber() {
                int start = i;
                if (peek() == '-') {
                    i++;
                }
                while (i < s.length() && (Character.isDigit(s.charAt(i)) || s.charAt(i) == '.')) {
                    i++;
                }
                try {
                    return (long) Double.parseDouble(s.substring(start, i));
                } catch (NumberFormatException e) {
                    return 0L;
                }
            }

            void skipValue() {
                skipWs();
                char c = peek();
                if (c == '"') {
                    readString();
                } else if (c == '{' || c == '[') {
                    char open = c;
                    char close = c == '{' ? '}' : ']';
                    int depth = 0;
                    do {
                        char n = next();
                        if (n == open) depth++;
                        else if (n == close) depth--;
                        else if (n == 0) break;
                    } while (depth > 0);
                } else {
                    while (i < s.length() && ",}]".indexOf(s.charAt(i)) == -1
                            && !Character.isWhitespace(s.charAt(i))) {
                        i++;
                    }
                }
            }
        }
    }
}
