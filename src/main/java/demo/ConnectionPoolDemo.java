package demo;

import lombok.SneakyThrows;
import vsi.PoolConfig;
import vsi.PoolDataSource;

import java.sql.*;

public class ConnectionPoolDemo {

    @SneakyThrows
    public static void main(String[] args) {
        PoolDataSource dataSource = new PoolDataSource(PoolConfig
            .builder()
            .url("jdbc:postgresql://localhost:5432/postgres")
            .username("postgres")
            .password("postgres")
            .build()
        );

        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            try (Connection connection = dataSource.getConnection()) {

                try (Statement statement = connection.createStatement()) {
                    ResultSet resultSet = statement.executeQuery("select m.*,\n" +
                        "       case\n" +
                        "         when mm.is_read is null\n" +
                        "           then false\n" +
                        "         else mm.is_read\n" +
                        "         end,\n" +
                        "       case\n" +
                        "         when mm.is_pinned is null\n" +
                        "           then false\n" +
                        "         else mm.is_pinned\n" +
                        "       end\n" +
                        "from messages m\n" +
                        "       left join messages_members mm on m.id = mm.message_id");

                    while (resultSet.next()) {
                        System.out.println(resultSet.getString("id"));
                    }
                }
            }
        }

        System.out.println((System.currentTimeMillis() - start));
    }


}
