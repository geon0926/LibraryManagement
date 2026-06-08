import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;
import static org.junit.jupiter.api.Assertions.*;

class Injectiontest {
    public User loadUser(String id, String pw) {
        //String sql = "SELECT * FROM users WHERE user_id = ? AND password = ?";
        String sql = "SELECT * FROM users WHERE user_id = ? AND password = ?";
//        인젝션 공격에 취약점이 발견되어서 plcaeholer 파라미터로 바꿈
        //System.out.println(sql);

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.setString(2, pw);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // 반환 타입이 User로 바뀌었으므로 이제 에러 없이 정상 작동합니다.
                    return new User(
                            rs.getString("user_id"),
                            rs.getString("password"),
                            rs.getString("type")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[오류] 로그인 조회 실패: " + e.getMessage());
        }
        return null; // 일치하는 사용자가 없을 때
    }

    private Connection getConnection() {
    }
}