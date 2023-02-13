package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 파라미터 연동, 풀 고려한 종료
 */
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;



    public void accountTrandsfer(String fromId,String toId, int money) throws SQLException {

        Connection con = dataSource.getConnection();

        try{
            con.setAutoCommit(false);

            //비즈니스 로직
            bizLogic(con, fromId, toId, money);
            log.info("commit");
            con.commit();// 성공시 커밋
        }catch (Exception e){
            log.info("rollback");
            con.rollback(); //실패시 롤백
            throw new IllegalStateException(e);
        }finally {
            release(con);
        }



    }

    private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(con,fromId,fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(con,toId, toMember.getMoney() + money);
    }

    private static void release(Connection con) {
        if(con !=null){
            try{
                con.setAutoCommit(true); //풀에 돌아가도 false상태가 유지되기에 true로 원복후에 돌려준다.
                con.close();
            }catch (Exception e){
                log.info("error",e);
            }
        }
    }

    private static void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
