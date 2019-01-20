package pl.kurcaba.ProfileServer;

import java.util.Iterator;
import java.util.Queue;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import redis.clients.jedis.Jedis;

public class UpdaterCreator implements Runnable {

	Queue queue;
	private static int maxQueueSize = 25;
	
	public UpdaterCreator(Queue aQueue) {
		queue = aQueue;
	}
	
	@Override
	public void run() {
		while(true)
		{
			for(int i = queue.size();i<maxQueueSize;i++)
			{
				queue.add(createUpdater());
			}	
		}
	}
	
	private UserProfileUpdater createUpdater()
	{
        Cluster cluster = null;
        cluster = Cluster.builder()                                                   
		        .addContactPoint("192.168.99.100")
		        .build();
		Session session = cluster.connect();
		Jedis jedis = new Jedis("192.168.99.100");
		UserProfileUpdater updater = new UserProfileUpdater(session, jedis);
		return updater;
	}
	
	
	
}
