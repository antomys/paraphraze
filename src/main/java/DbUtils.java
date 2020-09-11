import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class DbUtils {
    private static Map<String, List<String>> synonyms = getSynonyms();

    public static Map<String, List<String>> getSynonyms() {
        if(synonyms != null){
            return synonyms;
        }
        Connection conn = null;
        Statement stmt = null;
        HashMap<String, List<String>> result = new HashMap<>();
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/synsets_ua.db");
            stmt = conn.createStatement();
            String sql = "select * from synset;";
            ResultSet resultSet = stmt.executeQuery(sql);
            ArrayList<Pair<Integer, String>> pairs = new ArrayList<>();
            while (resultSet.next()) {
                pairs.add(new Pair<>(resultSet.getInt("id"), resultSet.getString("mword")));
            }
            for (Pair<Integer, String> pair :pairs) {
                ArrayList<String> syns = new ArrayList<>();
                sql = "select word from syns where synset_id = " + pair.left;
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    syns.add(rs.getString("word"));
                }
                result.put(pair.rigth, syns);
            }
        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    conn.close();
            } catch (SQLException ignored) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return result;
    }

    public static void addText(String name, String text) {
        Scanner scanner = new Scanner(text);
        List<String> words = new ArrayList<>();
        while (scanner.hasNext()) {
            String s = scanner.next().toLowerCase();
            String finalS = s;
            Optional<Map.Entry<String, List<String>>> any = synonyms.entrySet().stream()
                    .filter(e -> e.getValue().contains(finalS))
                    .findAny();
            if (any.isPresent()) {
                s = any.get().getKey();
            }
            words.add(s);
        }

        Map<String, Integer> map = words.stream()
                .collect(Collectors.toMap(s -> s, s -> 1, Integer::sum));

        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/synsets_ua.db");
            stmt = conn.createStatement();
            String sql = "INSERT INTO text (text_name) VALUES ('" + name.replace("'", "") + "')";
            stmt.executeUpdate(sql);
            sql = "SELECT id FROM text WHERE text_name = '" + name.replace("'", "") + "'";
            ResultSet resultSet = stmt.executeQuery(sql);
            int id = resultSet.getInt("id");
            Statement finalStmt = stmt;
            map.entrySet().forEach(e -> {
                String sq = String.format("INSERT INTO wcount (text_id, word, count) VALUES (%d, '%s', %d)", id, e.getKey().replace("'", ""), e.getValue());
                try {
                    finalStmt.executeUpdate(sq);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            });
        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    conn.close();
            } catch (SQLException ignored) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
     //antomys method. Get text names
      public static Map<String, List<String>> getTextNames() {
         Connection conn = null;
         Statement stmt = null;
         HashMap<String, List<String>> result = new HashMap<>();
         try {
             conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/synsets_ua.db");
             stmt = conn.createStatement();
             String sql = "select * from text;";
             ResultSet resultSet = stmt.executeQuery(sql);
             ArrayList<Pair<Integer, String>> pairs = new ArrayList<>();
             while (resultSet.next()) {
                 pairs.add(new Pair<>(resultSet.getInt("id"), resultSet.getString("text_name")));
             }
             for (Pair<Integer, String> pair :pairs) {
                 ArrayList<String> text_names = new ArrayList<>();
                 sql = "select text_name from text where id = " + pair.left;
                 ResultSet rs = stmt.executeQuery(sql);
                 while (rs.next()) {
                     text_names.add(rs.getString("text_name"));
                 }
                 result.put(pair.rigth, text_names);
             }
         } catch (Exception se) {
             se.printStackTrace();
         } finally {
             try {
                 if (stmt != null)
                     conn.close();
             } catch (SQLException ignored) {
             }
             try {
                 if (conn != null)
                     conn.close();
             } catch (SQLException se) {
                 se.printStackTrace();
             }
         }
         return result;
     }
    public static Map<String, Map<String, Integer>> getText() {
        HashMap<String, Map<String, Integer>> map = new HashMap<>();
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/synsets_ua.db");
            stmt = conn.createStatement();
            String sql = "SELECT * FROM text";
            ResultSet resultSet = stmt.executeQuery(sql);
            ArrayList<Pair<String, Integer>> pairs = new ArrayList<>();
            while (resultSet.next()) {
                pairs.add(new Pair<>(resultSet.getString("text_name"), resultSet.getInt("id")));
            }

            Statement finalStmt1 = stmt;
            pairs.forEach(p -> {
                HashMap<String, Integer> wcount = new HashMap<>();
                String sq = "SELECT * FROM wcount WHERE text_id = " + p.rigth;
                try {
                    ResultSet resultSet1 = finalStmt1.executeQuery(sq);
                    while (resultSet1.next()) {
                        wcount.put(resultSet1.getString("word"), resultSet1.getInt("count"));
                    }
                    map.put(p.left, wcount);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    conn.close();
            } catch (SQLException ignored) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return map;
    }

    public static void removeText(String textName) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/synsets_ua.db");
            stmt = conn.createStatement();
            String sql = "SELECT * FROM text WHERE text_name = '" + textName + "'";
            ResultSet resultSet = stmt.executeQuery(sql);
            int id = resultSet.getInt("id");
            sql = "DELETE FROM wcount WHERE text_id = " + id;
            stmt.executeUpdate(sql);
            sql = "DELETE FROM text WHERE id = " + id;
            stmt.executeUpdate(sql);
        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    conn.close();
            } catch (SQLException ignored) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
