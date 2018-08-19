package kafka;

import hbase.HBaseDAO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import utils.PropertiesUtil;

import java.util.Arrays;

public class HBaseConsumer {

    public static void main(String[] args) {

        KafkaConsumer kafkaConsumer=new KafkaConsumer<String, String>(PropertiesUtil.properties);
        kafkaConsumer.subscribe(Arrays.asList(PropertiesUtil.getProperty("kafka.topics")));

        HBaseDAO hBaseDAO = new HBaseDAO();
        while (true){
            ConsumerRecords<String,String> records = kafkaConsumer.poll(100);
            //没有测试
            for (ConsumerRecord<String,String> cr: records) {
                String value = cr.value();//kafka读数据
                System.out.println(value);
                hBaseDAO.put(value);//写入HBase
            }
        }
    }
}
