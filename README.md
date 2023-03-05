# study_spring_database

JDBC 표준 인터페이스의 탄생
java.sql.Connection - 연결
java.sql.Statement - SQL을 담은 내용
java.sql.ResultSet - SQL 요청 응답

MyBatis - SQL Mapper

커넥션풀 / DriverManager / DataSource 
Transaction / DB Lock / 
체크예외 , 언체크예외

트랜잭션 AOP 적용 전체 흐름도

1. 프록시 호출 -> AOP프록시 -> 스프링 컨테이너를 통해 트랜잭션매니저 획득 -> transactionManager.getTransaction() -> 데이터소스 - 커넥션생성 -> con.setAutoCommit(false) -> 트랜잭션 동기화매니저에 커넥션보관  
2. AOP 프록시로 돌아와서 실제 서비스 호출 -> 비즈니스로직(Service) -> 데이터 접근 로직(repository) -> 트랜잭션 동기화매니저에서 이전에 보관한 커넥션 획득하여 이후 트랜잭션 수행
