package hello.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import hello.jdbc.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static hello.jdbc.connection.ConnectionConst.*;

class MemberRepositoryV1Test {

   MemberRepositoryV1 repository;

    @BeforeEach
    void beforeEach() throws Exception {
        //기본 DriverManager - 항상 새로운 커넥션 획득
//         DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        //커넥션 풀링: HikariProxyConnection -> JdbcConnection
         HikariDataSource dataSource = new HikariDataSource();
         dataSource.setJdbcUrl(URL);
         dataSource.setUsername(USERNAME);
         dataSource.setPassword(PASSWORD);
         repository = new MemberRepositoryV1(dataSource);
    }


    @Test
    void crud() throws SQLException {

        Member memberV0 = new Member("memberV0", 10000);
        repository.save(memberV0);

        Member findMember = repository.findById(memberV0.getMemberId());
        Assertions.assertThat(findMember).isEqualTo(memberV0);

        repository.update(memberV0.getMemberId(),20000);
        Member updateMember = repository.findById(memberV0.getMemberId());
        Assertions.assertThat(updateMember.getMoney()).isEqualTo(20000);

        repository.delete(memberV0.getMemberId());
        Assertions.assertThatThrownBy(() -> repository.findById(memberV0.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);



    }
}