package pl.kurcaba.ProfileServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Update;

import redis.clients.jedis.Jedis;

public class ProfileServer {
	
	private ServerSocket server;
	private ExecutorService executor;
	private Queue<UserProfileUpdater> updaterQueue = new ConcurrentLinkedQueue();
	
	
	public ProfileServer()
	{
		try {
			server = new ServerSocket(6666);
			executor = Executors.newCachedThreadPool();
			new Thread(new UpdaterCreator(updaterQueue)).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start() {

		while(true)
		{
			try {
			Socket socket = server.accept();
				UserProfileUpdater updater = updaterQueue.poll();
				updater.insertSocket(socket);
				executor.execute(updater);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
	
	
	
}
