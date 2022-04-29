package cc.dodder.dhtserver.netty.schedule;

import cc.dodder.dhtserver.netty.DHTServer;
import cc.dodder.dhtserver.netty.handler.DHTServerHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/***
 * 定时检测本地节点数并自动加入 DHT 网络
 *
 * @author Mr.Xu
 * @date 2019-02-16 22:04
 **/
@Slf4j
@Component
public class AutoJoinDHT {

	@Autowired
	private DHTServer dhtServer;
	@Autowired
	private DHTServerHandler handler;

	@Scheduled(fixedDelay = 60 * 1000, initialDelay = 10 * 1000)
	public void doJob() {
		if (handler.NODES_QUEUE.isEmpty()) {
			log.info("The number of local DHT nodes is 0 and automatically rejoins the DHT network...");
			handler.joinDHT();
		}
		//dhtServer.saveBloomFilter();
	}
}
