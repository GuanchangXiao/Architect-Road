package my.rabbitmq.quickstart.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import my.rabbitmq.quickstart.common.ConnectionUtil;

/**
 * Created by perl on 2020-02-15.
 */
public class Producer4FanoutExchange {

    public static void main(String[] args) throws Exception {

        //1 创建ConnectionFactory
        ConnectionFactory connectionFactory = ConnectionUtil.getConnection();

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        //4 声明
        String exchangeName = "test_fanout_exchange";
        //5 发送
        for(int i = 0; i < 10; i ++) {
            String msg = "Hello World RabbitMQ 4 FANOUT Exchange Message ...";
            channel.basicPublish(exchangeName, "", null , msg.getBytes());
        }
        channel.close();
        connection.close();
    }
}
