package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC - DriverManager 사용
 */
@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values (?,?)";

        Connection con = null;
        PreparedStatement pstmt = null;


        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate(); // db에 영향받은 로우 수 만큼 리턴함
            return member;
        } catch (SQLException e) {
            log.info("db error", e);
            throw e;
        }finally {

//            pstmt.close(); //여기에서 Exception 발생시 아래 close 코드가 실행되지않을 수 있다, 그렇기에 아래 close 메서드를 따로 만들어야한다.
//            con.close();
            close(con,pstmt,null);
        }

    }

    public Member findById(String memberId) throws SQLException {
        String sql="select * from member where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();
            if(rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }

        }catch (SQLException e) {
            log.info("db error", e);
            throw e;
        }finally {
            close(con,pstmt,rs);
        }

    }

    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id = ?";


        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();// db에 영향받은 로우 수 만큼 리턴함
            log.info("resultSize={}",resultSize);
        } catch (SQLException e) {
            log.info("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id = ?";


        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();// db에 영향받은 로우 수 만큼 리턴함
        } catch (SQLException e) {
            log.info("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }



    private void close(Connection con , Statement stmt , ResultSet rs) {
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("db error", e);
            }
        }

        if(stmt != null) {
            try {
                stmt.close(); //여기에서 Exception 발생시 아래 close 코드가 실행되지않을 수 있다.
            } catch (SQLException e) {
                log.info("db error", e);
            }
        }

        if(con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("db error", e);
            }
        }

    }

    private static Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
