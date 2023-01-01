package hello.jdbc.connection;


import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;
import static java.sql.Connection.*;

@Slf4j
public class DBConnectionUtil {

    public static Connection getConncetion ()  {

        try {
            // Spring 프로젝트 생성 시, 가지고 온 Dependency(라이브러리)에서 jdbd의 구현체인 connection driver(h2 connection driver)을 찾아서 반환을 해준다.
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("get Connection ={} , class={}",connection,connection.getClass());
            return connection;

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }

    }

}
