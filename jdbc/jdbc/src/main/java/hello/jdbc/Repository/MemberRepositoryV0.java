package hello.jdbc.Repository;


import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

@Slf4j

// JDBC(DriverManager)를 사용하여 DB와 데이터를 주고 받게 할 것이다.
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException {

        String sql = "insert into member(member_id,money) values (?,?)";
        Connection connection = null;
        PreparedStatement pstmt = null; // Statement Class를 상속받고 있으며, Statement는 완벽한 sql문이여야 하는 반면, preparedStatement는 ?와 같이 매개변수를 설정가능!


        try {

            connection = getConncetion();
            pstmt = connection.prepareStatement(sql);
            // SQL문의 매개변수(?)에 각각 데이터를 바인딩해준다.
            pstmt.setString(1,member.getMemberId()); // 1번째 매개변수(?)
            pstmt.setInt(2,member.getMoney()); // 2번째 매개변수(?)
            pstmt.executeUpdate();
            // 만약, 여기서 close(...) 작업을 해주게 되면, pstmt가 실행 중 예외가 터지면, 이 부분이 실행이 되지 않고, 연결이 계속 유지되어서 리소스 누수가 발생을 한다.
            // close()작업은 반드시 finally에서 해주자.
            return member;

        } catch (SQLException e) {

            log.info("db error",e);
             throw e;

        }
        finally {

           // pstmt.close(); // 여기서 Exception이 발생을 하면, 아래의 코드 connection.close()가 실행이 되지 않는다.
           // connection.close(); // 그래서 close()를 작성하였다.
            close(connection,pstmt,null);

        }

    }

    private void close(Connection con, Statement stmt, ResultSet resultSet){

        if(resultSet != null){

            try {

                resultSet.close();

            } catch (SQLException e) {

                throw new RuntimeException(e);
            }

        }

        // 리소스를 정리할 때는 항상 역순으로 정리! ( preparedStatement -> connection )
        if(stmt != null){

            try {
                stmt.close(); // 만약 여기서 예외가 발생을 하도, catch에서 그걸 잡기 때문에, con.close()에는 영향을 주지 않는다.

            } catch (SQLException e) {

                log.info("error",e);

            }
        }

        if(con != null) {

            try {

                con.close();

            } catch (SQLException e) {

                log.info("error",e);

            }
        }

    }

    public Member findById(String memberId) throws SQLException {

        String sql = "select * from member where member_Id=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;


        try {

            con = DBConnectionUtil.getConncetion();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,memberId);

            rs = pstmt.executeQuery(); // Update : insert, delete update문, Query : select문
            if(rs.next()){

                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;

            }
            else{

                throw new NoSuchElementException("Member not found member_Id=" + memberId);

            }


        } catch (SQLException e)
        {
            log.error("error",e);
            return null;
        }

        finally{

            close(con,pstmt,rs);
        }

    }

    public void update(String memberId, int money) throws SQLException {


        String sql = "update member set money = ? where member_Id = ?";
        Connection con = null;
        PreparedStatement ps = null;



        try {

            con = DBConnectionUtil.getConncetion();
            ps = con.prepareStatement(sql);

            ps.setInt(1, money);
            ps.setString(2,memberId);

            int size = ps.executeUpdate();
            log.info("size = {}",size);

        } catch (SQLException e) {

            log.info("error",e);

        }
        finally {

          close(con,ps,null);

        }

    }

    public void delete(String memberId) throws SQLException {


        String sql = "delete from member where member_Id = ?";
        Connection con = null;
        PreparedStatement ps = null;

        con = DBConnectionUtil.getConncetion();
        ps = con.prepareStatement(sql);
        ps.setString(1,memberId);
        int size = ps.executeUpdate();

        log.info("size =",size);

        close(con,ps,null);


    }


    private Connection getConncetion() {

        return DBConnectionUtil.getConncetion();

    }

}
