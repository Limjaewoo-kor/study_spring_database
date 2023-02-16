package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBCTemplate 사용
 */
@Slf4j
public class MemberRepositoryV5 implements MemberRepository{

    private final JdbcTemplate template;

    public MemberRepositoryV5(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member(member_id, money) values (?,?)";
        int update = template.update(sql, member.getMemberId(), member.getMoney());
        return member;

//        Connection con = null;
//        PreparedStatement pstmt = null;
//
//
//        try {
//            con = getConnection();
//            pstmt = con.prepareStatement(sql);
//            pstmt.setString(1, member.getMemberId());
//            pstmt.setInt(2, member.getMoney());
//            pstmt.executeUpdate(); // db에 영향받은 로우 수 만큼 리턴함
//            return member;
//        } catch (SQLException e) {
//            throw exTranslator.translate("save",sql,e);
////            throw new MyDbException(e);
//        }finally {
//
////            pstmt.close(); //여기에서 Exception 발생시 아래 close 코드가 실행되지않을 수 있다, 그렇기에 아래 close 메서드를 따로 만들어야한다.
////            con.close();
//            close(con,pstmt,null);
//        }

    }

    @Override
    public Member findById(String memberId) {
        String sql="select * from member where member_id=?";
        Member member = template.queryForObject(sql, memberRowMapper(), memberId);
        return member;

//        Connection con = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//
//        try{
//            con = getConnection();
//            pstmt = con.prepareStatement(sql);
//            pstmt.setString(1, memberId);
//
//            rs = pstmt.executeQuery();
//            if(rs.next()) {
//                Member member = new Member();
//                member.setMemberId(rs.getString("member_id"));
//                member.setMoney(rs.getInt("money"));
//                return member;
//            } else {
//                throw new NoSuchElementException("member not found memberId=" + memberId);
//            }
//
//        }catch (SQLException e) {
//            throw exTranslator.translate("FindById",sql,e);
////            throw new MyDbException(e);
//        }finally {
//            close(con,pstmt,rs);
//        }

    }

    @Override
    public void update(String memberId, int money){
        String sql = "update member set money=? where member_id = ?";
        int update = template.update(sql, money, memberId);


//        Connection con = null;
//        PreparedStatement pstmt = null;
//
//        try {
//            con = getConnection();
//            pstmt = con.prepareStatement(sql);
//            pstmt.setInt(1, money);
//            pstmt.setString(2, memberId);
//            int resultSize = pstmt.executeUpdate();// db에 영향받은 로우 수 만큼 리턴함
//            log.info("resultSize={}",resultSize);
//        } catch (SQLException e) {
//            throw exTranslator.translate("update",sql,e);
////            throw new MyDbException(e);
//        } finally {
//            close(con, pstmt, null);
//        }
    }


    @Override
    public void delete(String memberId) {
        String sql = "delete from member where member_id = ?";
        template.update(sql, memberId);

//
//        Connection con = null;
//        PreparedStatement pstmt = null;
//
//        try {
//            con = getConnection();
//            pstmt = con.prepareStatement(sql);
//            pstmt.setString(1, memberId);
//            pstmt.executeUpdate();// db에 영향받은 로우 수 만큼 리턴함
//        } catch (SQLException e) {
//            throw exTranslator.translate("delete",sql,e);
////            throw new MyDbException(e);
//        } finally {
//            close(con, pstmt, null);
//        }
    }

    @Override
    public void deleteAll()  {
        String sql = "delete from member";
        template.update(sql);


//        Connection con = null;
//        PreparedStatement pstmt = null;
//
//        try {
//            con = getConnection();
//            pstmt = con.prepareStatement(sql);
//            pstmt.executeUpdate();// db에 영향받은 로우 수 만큼 리턴함
//        } catch (SQLException e) {
//            throw exTranslator.translate("deleteAll",sql,e);
////            throw new MyDbException(e);
//        } finally {
//            close(con, pstmt, null);
//        }
    }





//    private void close(Connection con , Statement stmt , ResultSet rs) {
//        JdbcUtils.closeResultSet(rs);
//        JdbcUtils.closeStatement(stmt);
//        //주의!  트랜잭션 동기화를 사용하려면 DataSourceUtils를 사용해야 한다.
////        JdbcUtils.closeConnection(con);
//        DataSourceUtils.releaseConnection(con, dataSource);
//    }
//
//
//    private Connection getConnection() throws SQLException {
//        //주의! 트랜잭션 동기화를 사용하려면 DataSourceUtils를 사용해야 한다.
//        //        Connection con = dataSource.getConnection();
//        Connection con = DataSourceUtils.getConnection(dataSource);
//        log.info("get connection={}, class={}",con,con.getClass());
//        return con;
//    }


    private RowMapper<Member> memberRowMapper() {
        return (rs,rowNum) -> {
            Member member = new Member();
            member.setMemberId(rs.getString("member_id"));
            member.setMoney(rs.getInt("money"));
            return member;
        };
    }

}
