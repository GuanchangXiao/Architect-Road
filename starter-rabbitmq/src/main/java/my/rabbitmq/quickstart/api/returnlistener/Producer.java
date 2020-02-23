package my.rabbitmq.quickstart.api.returnlistener;

import com.rabbitmq.client.*;
import my.rabbitmq.quickstart.common.ConnectionUtil;

import java.io.IOException;

/**
 * Created by perl on 2020-02-15.
 */
public class Producer {

    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = ConnectionUtil.getConnection();

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String exchange = "test_return_exchange";
        String routingKey = "return.save";
        String routingKeyError = "abc.save";

        String msg = "Hello RabbitMQ Return Message";

        // 这个监听器就是监听return消息的，用于处理不可路由的情况
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange,
                                     String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 这是broker把消息原路返回后提供的一些响应参数，供生产端去做后续的处理
                System.err.println("---------handle  return----------");
                System.err.println("replyCode: " + replyCode);
                System.err.println("replyText: " + replyText);
                System.err.println("exchange: " + exchange);
                System.err.println("routingKey: " + routingKey);
                System.err.println("properties: " + properties);
                System.err.println("body: " + new String(body));
            }
        });
        channel.basicPublish(exchange, routingKeyError, true, null, msg.getBytes());
        //channel.basicPublish(exchange, routingKeyError, true, null, msg.getBytes());
    }
}
