package kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import utils.PropertiesUtil;

import java.util.Arrays;

public class HBaseConsumer {

    public static void main(String[] args) {

        KafkaConsumer kafkaConsumer=new KafkaConsumer<String, String>(PropertiesUtil.properties);
        kafkaConsumer.subscribe(Arrays.asList(PropertiesUtil.getProperty("kafka.topics")));
        while (true){
            ConsumerRecords<String,String> records = kafkaConsumer.poll(100);
            //没有测试
            for (ConsumerRecord cr: records) {
                System.out.println(cr.value());
            }
        }
    }
}
