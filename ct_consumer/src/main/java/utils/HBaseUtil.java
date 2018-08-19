package utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.TreeSet;

public class HBaseUtil {

    /**
     * 判断表是否存在
     *
     * @param conf      HBaseConfiguration
     * @param tableName
     * @return
     */
    public static boolean isExistTable(Configuration conf, String tableName) throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();
        boolean result = admin.tableExists(TableName.valueOf(tableName));
        admin.close();
        return result;
    }

    /**
     * 初始化命名空间
     *
     * @param conf
     * @param namespace
     * @throws IOException
     */
    public static void initNamespace(Configuration conf, String namespace) throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();

        NamespaceDescriptor nd = NamespaceDescriptor.create(namespace)
                .addConfiguration("CREATE_TIME", String.valueOf(System.currentTimeMillis()))
                .addConfiguration("AUTHOR", "JinJi")
                .build();

        admin.createNamespace(nd);
        admin.close();
        connection.close();

    }


    /**
     * 创建表：协处理器
     *
     * @param conf
     * @param tableName
     * @param columnFamily
     * @throws IOException
     */
    public static void createTable(Configuration conf, String tableName, int regions, String... columnFamily) throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();

        if (isExistTable(conf, tableName)) {
            return;
        }

        HTableDescriptor htd = new HTableDescriptor(TableName.valueOf(tableName));
        for (String cf : columnFamily) {
            htd.addFamily(new HColumnDescriptor(cf));
        }
//        htd.addCoprocessor("CalleeWriteObserver");
        admin.createTable(htd,getSplitKeys(regions));

        admin.close();
        connection.close();;
    }

    /**
     * @param regions 分区
     * @return
     */
    private static byte[][] getSplitKeys(int regions) {
        //定义一个存放分区键的数组
        String[] keys = new String[regions];
        //目前推算，region个数不会超过2位数，所以region分区键格式化为两位数字所代表的字符串
        DecimalFormat df = new DecimalFormat("00");
        for (int i = 0; i < regions; i++) {
            keys[i] = df.format(i) + "|";
        }


        byte[][] splitKeys = new byte[regions][];
        //生成byte[][]类型的分区键的时候，一定要保证分区键是有序的
        TreeSet<byte[]> treeSet = new TreeSet<>(Bytes.BYTES_COMPARATOR);
        for (int i = 0; i < regions; i++) {
            treeSet.add(Bytes.toBytes(keys[i]));
        }

        Iterator<byte[]> iterator = treeSet.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            byte[] next = iterator.next();
            splitKeys[index++] = next;
        }
        return splitKeys;
    }

    /**
     * 生成rowkey
     * 格式：regionCode_call1_buildTime_call2_flag_duration
     *
     * @return
     */
    public static String genRowkey(String regionCode, String call1, String buildTime, String call2, String flag, String duration) {
        StringBuffer sb = new StringBuffer();
        sb.append(regionCode + "_")
                .append(call1 + "_")
                .append(buildTime + "_")
                .append(call2 + "_")
                .append(flag + "_")
                .append(duration);
        return sb.toString();
    }

    public static String genRegionCode(String call1, String buildTime, int regions) {
        int length = call1.length();
        //取出手机后4位号码
        String lastPhone = call1.substring(length - 4);
        //取出年月
        String ym = buildTime
                .replaceAll("-", "")
                .replaceAll(":", "")
                .replaceAll(" ", "")
                .substring(0, 6);
        //离散操作1
        Integer x = Integer.valueOf(lastPhone) ^ Integer.valueOf(ym);
        int a = 10;
        int b = 20;
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
        //离散操作2
        int y = x.hashCode();
        //生成分区号
        int regionCode = y % regions;
        //格式化分区号
        DecimalFormat df = new DecimalFormat("00");
        return  df.format(regionCode);
    }

}

