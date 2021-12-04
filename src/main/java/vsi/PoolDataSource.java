package vsi;

import vsi.exception.DriverNotFoundException;
import vsi.exception.NoAvailableConnectionsException;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

public class PoolDataSource implements DataSource {
    private final Queue<Connection> pools = new ConcurrentLinkedDeque<>();

    public PoolDataSource(PoolConfig config) throws SQLException {
        int size = config.getSize();

        if (config.getDriverName() != null) {
            try {
                Class.forName(config.getDriverName());
            } catch (ClassNotFoundException e) {
                throw new DriverNotFoundException();
            }
        }

        for (int i = 0; i < size; i++) {
            pools.add(DriverManager.getConnection(
                config.getUrl(),
                config.getUsername(),
                config.getPassword()
            ));
        }
    }

    public void enqueue(Connection connection) {
        pools.add(connection);
    }

    @Override
    public Connection getConnection() {
        if (pools.isEmpty()) {
            throw new NoAvailableConnectionsException();
        }

        return new ProxyConnection(pools.poll(), this);
    }

    @Override
    public Connection getConnection(String s, String s1) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setLogWriter(PrintWriter printWriter) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setLoginTimeout(int i) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public <T> T unwrap(Class<T> aClass) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public boolean isWrapperFor(Class<?> aClass) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
}
