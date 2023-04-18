package com.example;


import com.rabbitmq.client.*;
 
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Hello world!
 * mvn clean package -DskipTests  -Denforcer.skip=true
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("Hello World!");
        ConnectionFactory factory = new ConnectionFactory();
        //配置rabbitMQ的连接信息
        // factory.setHost("192.168.100.210");
        factory.setHost("192.168.145.23");
        factory.setPort(5672);
        factory.setUsername("mymq");
        factory.setPassword("mq123..");
        //建立到代理服务器到连接
        Connection conn = factory.newConnection();
        //获得信道
        final Channel channel = conn.createChannel();
        //声明队列
        channel.queueDeclare("myQueue", true, false, false, null);
        //消费消息
        boolean autoAck = true;
        String consumerTag = "";
        //接收消息
        //参数1 队列名称
        //参数2 是否自动确认消息 true表示自动确认 false表示手动确认
        //参数3 为消息标签 用来区分不同的消费者这里暂时为""
        // 参数4 消费者回调方法用于编写处理消息的具体代码（例如打印或将消息写入数据库）
        //注意：使用了basicConsume方法以后，会启动一个线程持续的监听队列，如果队列中有信息的数据进入则会自动接收消息
        //因此不能关闭连接和通道对象
        channel.basicConsume("myQueue", autoAck, consumerTag, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                //获取消息数据
                String bodyStr = new String(body, "UTF-8");
                System.out.println("消费者--  "+bodyStr);
            }
        });
        // channel.close();
        // conn.close();
    }
}
