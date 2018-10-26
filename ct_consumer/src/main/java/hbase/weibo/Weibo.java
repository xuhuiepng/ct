package hbase.weibo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class Weibo {
    //HBase配置对象
    private Configuration conf = HBaseConfiguration.create();
    //创建命名空间
    private static final byte[] NS_WEIBO = Bytes.toBytes("ns_weibo");
    private static final byte[] TABLE_CONTENT = Bytes.toBytes("ns_weibo:content");
    private static final byte[] TABLE_RELATION = Bytes.toBytes("ns_weibo:relation");
    private static final byte[] TABLE_INBOX = Bytes.toBytes("ns_weibo:inbox");

    //创建命名空间
    private void initNamespace() throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();
        NamespaceDescriptor ns_weibo = NamespaceDescriptor
                .create("ns_weibo")
                .addConfiguration("creator","xuhuipeng")
                .addConfiguration("create_time",String.valueOf(System.currentTimeMillis()))
                .build();
        admin.createNamespace(ns_weibo);
        admin.close();
        connection.close();

    }

    private void initTableContent() throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();
        HTableDescriptor contentTableDescriptor = new HTableDescriptor(TableName.valueOf(TABLE_CONTENT));
        HColumnDescriptor infoColumnDescriptor = new HColumnDescriptor("info");
        infoColumnDescriptor.setBlockCacheEnabled(true);
        infoColumnDescriptor.setBlocksize(2*1024*1024);
        infoColumnDescriptor.setMinVersions(1);
        infoColumnDescriptor.setMaxVersions(1);

        contentTableDescriptor.addFamily(infoColumnDescriptor);

        admin.createTable(contentTableDescriptor);
        admin.close();
        connection.close();
    }

    /**
     * 表名：ns_weibo:relation
     * 列族名：attends，fans
     * 列名：用户id
     * value：用户id
     * rowkey：当前操作人的用户id
     * versions:1
     * @throws IOException
     */
    private void initTableRelation() throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();
        //创建用户关系表描述器
        HTableDescriptor relationTableDescriptor = new HTableDescriptor(TableName.valueOf(TABLE_RELATION));

        //创建attends列描述器
        HColumnDescriptor attendsColumnDescriptor = new HColumnDescriptor("attends");
        //设置块缓存
        attendsColumnDescriptor.setBlockCacheEnabled(true);
        //设置块缓存大小 2M
        attendsColumnDescriptor.setBlocksize(2 * 1024 * 1024);
        //设置版本
        attendsColumnDescriptor.setMinVersions(1);
        attendsColumnDescriptor.setMaxVersions(1);

        //创建fans列描述器
        HColumnDescriptor fansColumnDescriptor = new HColumnDescriptor("fans");
        //设置块缓存
        fansColumnDescriptor.setBlockCacheEnabled(true);
        //设置块缓存大小 2M
        fansColumnDescriptor.setBlocksize(2 * 1024 * 1024);
        //设置版本
        fansColumnDescriptor.setMinVersions(1);
        fansColumnDescriptor.setMaxVersions(1);

        //将两个列描述器添加到表描述器中
        relationTableDescriptor.addFamily(attendsColumnDescriptor);
        relationTableDescriptor.addFamily(fansColumnDescriptor);

        //创建表
        admin.createTable(relationTableDescriptor);
        admin.close();
        connection.close();
    }

    /**
     * 表名：ns_weibo:inbox
     * 列族：info
     * 列：当前用户所关注的人的用户id
     * value：微博rowkey
     * rowkey：用户id
     * versions:100
     * @throws IOException
     */
    private void initTableInbox() throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();

        HTableDescriptor inboxTableDescriptor = new HTableDescriptor(TableName.valueOf(TABLE_INBOX));
        HColumnDescriptor infoColumnDescriptor = new HColumnDescriptor("info");
        //设置块缓存
        infoColumnDescriptor.setBlockCacheEnabled(true);
        //设置块缓存大小 2M
        infoColumnDescriptor.setBlocksize(2 * 1024 * 1024);
        //设置版本
        infoColumnDescriptor.setMinVersions(100);
        infoColumnDescriptor.setMaxVersions(100);

        inboxTableDescriptor.addFamily(infoColumnDescriptor);
        admin.createTable(inboxTableDescriptor);
        admin.close();
        connection.close();
    }

    /**
     * 发布微博
     * a、向微博内容表中添加刚发布的内容，多了一个微博rowkey
     * b、向发布微博人的粉丝的收件箱表中，添加该微博rowkey
     * @throws IOException
     */


}
