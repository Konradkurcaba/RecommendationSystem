package pl.kurcaba.ProfileServer;

import java.util.Iterator;
import java.util.Queue;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import redis.clients.jedis.Jedis;

public class UpdaterCreator implements Runnable {

	Queue queue;
	
	public UpdaterCreator(Queue aQueue) {
		queue = aQueue;
	}
	
	@Override
	public void run() {
		while(true)
		{
			for(int i = queue.size();i<50;i++)
			{
				queue.add(createUpdater());
			}	
		}
	}
	
	private int queueSize()
	{
		Iterator it = queue.iterator();
		int size = 0;
		while(it.hasNext())
		{
			it.next();
			size++;
		}
		return size;
	}
	
	private UserProfileUpdater createUpdater()
	{
        Cluster cluster = null;
        cluster = Cluster.builder()                                                   
		        .addContactPoint("127.0.0.1")
		        .build();
		Session session = cluster.connect();
		Jedis jedis = new Jedis();
		UserProfileUpdater updater = new UserProfileUpdater(session, jedis);
		return updater;
	}
	
	
	
}
