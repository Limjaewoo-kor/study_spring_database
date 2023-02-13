package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC - ConnectionParam
 */
@Slf4j
public class MemberRepositoryV2 {

    private final DataSource dataSource;

    public MemberRepositoryV2(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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

    public Member findById(Connection con, String memberId) throws SQLException {
        String sql="select * from member where member_id=?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
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
            close1(pstmt,rs);
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


    public void update(Connection con, String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id = ?";


        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();// db에 영향받은 로우 수 만큼 리턴함
            log.info("resultSize={}",resultSize);
        } catch (SQLException e) {
            log.info("db error", e);
            throw e;
        } finally {
            close1( pstmt, null);
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

    public void deleteAll() throws SQLException {
        String sql = "delete from member";


        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();// db에 영향받은 로우 수 만큼 리턴함
        } catch (SQLException e) {
            log.info("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }





    private void close(Connection con , Statement stmt , ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }

    private void close1( Statement stmt , ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
    }

    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get connection={}, class={}",con,con.getClass());
        return con;
    }
}