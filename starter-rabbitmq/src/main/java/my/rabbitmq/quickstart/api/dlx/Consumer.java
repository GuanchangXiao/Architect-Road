package my.rabbitmq.quickstart.api.dlx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import my.rabbitmq.quickstart.common.ConnectionUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by perl on 2020-02-15.
 */
public class Consumer {

    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = ConnectionUtil.getConnection();

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        // 这就是一个普通的交换机 和 队列 以及路由
        String exchangeName = "test_dlx_exchange";
        String routingKey = "dlx.#";
        String queueName = "test_dlx_queue";

        channel.exchangeDeclare(exchangeName, "topic", true, false, null);

        Map<String, Object> agruments = new HashMap<String, Object>();
        agruments.put("x-dead-letter-exchange", "dlx.exchange");
        //这个agruments属性，要设置到声明队列上
        channel.queueDeclare(queueName, true, false, false, agruments);
        channel.queueBind(queueName, exchangeName, routingKey);

        //要进行死信队列的声明:
        channel.exchangeDeclare("dlx.exchange", "topic", true, false, null);
        channel.queueDeclare("dlx.queue", true, false, false, null);
        channel.queueBind("dlx.queue", "dlx.exchange", "#");
        channel.basicConsume("dlx.queue", true, new MyConsumer(channel));

    }
}
