package my.rabbitmq.quickstart.common;

import com.rabbitmq.client.ConnectionFactory;

/**
 * Created by perl on 2020-02-15.
 */
public class ConnectionUtil {

    public static final ConnectionFactory getConnection() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("centos");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        return connectionFactory;
    }
}
