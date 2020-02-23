package my.rabbitmq.quickstart.api.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import my.rabbitmq.quickstart.common.ConnectionUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by perl on 2020-02-15.
 */
public class Producer {

    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = ConnectionUtil.getConnection();

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String exchange = "test_dlx_exchange";
        String routingKey = "dlx.save";

        String msg = "Hello RabbitMQ DLX Message";

        /**
         * only when expired messages reach the head of a queue will they actually be discarded (or dead-lettered).
         * 只有当过期的消息到了队列的顶端（队首），才会被真正的丢弃或者进入死信队列.
         */
        for(int i = 1; i < 3; i++){
            Map<String, Object> headers = new HashMap<String, Object>();
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2)
                    .contentEncoding("UTF-8")
                    .expiration("" + 5000 * i)
                    .build();
            channel.basicPublish(exchange, routingKey, true, properties,
                    (msg + i).getBytes());
        }
        channel.close();
        connection.close();
    }
}
