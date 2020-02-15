package my.rabbitmq.quickstart.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import my.rabbitmq.quickstart.common.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by perl on 2020-02-15.
 */
public class Procuder {

    public static void main(String[] args) throws IOException, TimeoutException {
        //1 创建ConnectionFactory
        ConnectionFactory connectionFactory = ConnectionUtil.getConnection();

        //2 通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        //3 通过connection创建一个Channel
        Channel channel = connection.createChannel();

        //4 通过Channel发送消息，这里我发送了5条消息哦
        for(int i=0; i < 5; i++){
            String msg = "Hello RabbitMQ!";
            String exchangeName = "";
            String routingKey = "test001";
            channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());
            System.out.println("生产消息：" + msg);
        }

        //6 关闭连接
        channel.close();
        connection.close();
    }
}
