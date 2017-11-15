package com.bestwaiting.canal.client.clientXML;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.Message;
import com.bestwaiting.canal.client.entity.CanalRowChange;
import com.bestwaiting.canal.client.process.BaseProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class CanalConsumer {

    private static final Logger logger = LoggerFactory.getLogger(CanalConsumer.class);

    //���ݴ�����keyΪ������valueΪ��Ӧ�Ĵ�����
    private Map<String, BaseProcess> processor;

    public static volatile boolean running = true;

    private String destination;

    private String zkServers;


    public void init() {

        // ��������
        logger.error("--------CanalConsumer destination:" + destination + " start------------");
        Thread thread = new Thread(new Runnable() {

            public void run() {

                CanalConnector connector = CanalConnectors.newClusterConnector(zkServers, destination, null, null);
                int batchSize = 1000;
                try {
                    connector.connect();
                    connector.subscribe();
                    while (running) {

                        Message message = connector.getWithoutAck(batchSize); // ��ȡָ������������
                        long batchId = message.getId();
                        int size = message.getEntries().size();

                        if (batchId == -1 || size == 0) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            boolean res = parseData(message.getEntries(), destination);
                            if (res) {
                                connector.ack(batchId);
                            } else {
                                connector.rollback(batchId);
                            }
                        }

                    }
                } finally {
                    connector.disconnect();
                }
            }
        });
        thread.start();
    }

    /**
     * ���ݴ���
     *
     * @param entrys
     * @param destination2
     * @return
     */
    private boolean parseData(List<Entry> entrys, String destination2) {

        for (Entry entry : entrys) {

            if (EntryType.TRANSACTIONBEGIN.equals(entry.getEntryType()) || EntryType.TRANSACTIONEND.equals(entry.getEntryType())) {
                continue;
            }

            RowChange rowChage = null;
            try {
                rowChage = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e);
            }

            EventType eventType = rowChage.getEventType();
            String schemaName = entry.getHeader().getSchemaName();
            String tableName = entry.getHeader().getTableName();
            /*1. ������ݱ����־*/
            if (logger.isErrorEnabled()) {

                if (eventType == EventType.INSERT || eventType == EventType.DELETE || eventType == EventType.UPDATE) {

                    logger.error("================> binlog[" + entry.getHeader().getLogfileName() + ":" + entry.getHeader().getLogfileOffset() + "] , "
                            + "name[" + schemaName + ":" + tableName + "] , eventType : " + eventType);
                    for (RowData rowData : rowChage.getRowDatasList()) {
                        if (eventType == EventType.DELETE) {
                            printColumn(rowData.getBeforeColumnsList());
                        } else if (eventType == EventType.INSERT) {
                            printColumn(rowData.getAfterColumnsList());
                        } else if (eventType == EventType.UPDATE) {
                            logger.error("-------> before");
                            printColumn(rowData.getBeforeColumnsList());
                            logger.error("-------> after");
                            printColumn(rowData.getAfterColumnsList());
                        }
                    }
                }
            }
			/*2. ���ݴ���*/
            if (EventType.INSERT.equals(eventType) || EventType.DELETE.equals(eventType) || EventType.UPDATE.equals(eventType)) {
                //����CanalRowChange����
                CanalRowChange rowChange = bulidCanalRowChange(schemaName, tableName, eventType, rowChage.getRowDatasList());
				/*���ݱ�����ȡ���ݴ�����*/
                if (processor.containsKey(tableName.toLowerCase())) {

                    boolean res = false;

                    //�����¼����͵�����Ӧ�����ݴ�����
                    if (EventType.UPDATE.equals(eventType)) {
                        res = processor.get(tableName).processUpdate(rowChange);
                    } else if (EventType.INSERT.equals(eventType)) {
                        res = processor.get(tableName).processInsert(rowChange);
                    } else if (EventType.DELETE.equals(eventType)) {
                        res = processor.get(tableName).processDelete(rowChange);
                    }

                    if (!res) {
                        logger.error("================> binlog[" + entry.getHeader().getLogfileName() + ":" + entry.getHeader().getLogfileOffset() + "] , "
                                + "name[" + schemaName + ":" + tableName + "] , eventType : " + eventType);
                        return res;
                    }
                }
            }
        }
        return true;
    }

    private CanalRowChange bulidCanalRowChange(String schemaName, String tableName, EventType eventType, List<RowData> rowData) {
        CanalRowChange change = new CanalRowChange();

        change.setSchemaName(schemaName);
        change.setEventType(eventType);
        change.setRowData(rowData);
        change.setTableName(tableName);
        return change;
    }

    private void printColumn(List<Column> columns) {
        for (Column column : columns) {
            logger.error(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }

    protected void stop() {
        if (!running) {
            return;
        }
        running = false;
    }

    public Map<String, BaseProcess> getProcessor() {
        return processor;
    }

    public void setProcessor(Map<String, BaseProcess> processor) {
        this.processor = processor;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getZkServers() {
        return zkServers;
    }

    public void setZkServers(String zkServers) {
        this.zkServers = zkServers;
    }
}
