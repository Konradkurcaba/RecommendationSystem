package pl.kurcaba.ProfileServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import redis.clients.jedis.Jedis;

public class UserProfileUpdater implements Runnable{

	private Socket userSocket;
	private Session cassSession;
	private Jedis jedis;
	
	public UserProfileUpdater(Session aCassSession,Jedis aJedis) {
		cassSession = aCassSession;
		jedis = aJedis;
	}
	
	
	@Override
	public void run() {
		try {
			BufferedReader sockerReader = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
			String userId = sockerReader.readLine();
			Map<String, String> values = readDataFromCass(userId);
			insertDataIntoRedis(userId,values);
			PrintWriter socketWriter = new PrintWriter(userSocket.getOutputStream());
			socketWriter.write(userId + " updated\n\r");
			socketWriter.flush();
			cassSession.close();
			jedis.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void insertSocket(Socket aSocket)
	{
		userSocket=aSocket;
	}
	
	private Map<String, String> readDataFromCass(String aUserId)
	{
		Map<String, String> values = new HashMap<String, String>();
                                      
		PreparedStatement prepared = cassSession.prepare("SELECT * from dataset.ratings WHERE userid=?");
		BoundStatement bound = prepared.bind(Integer.parseInt(aUserId)); 
		ResultSet rs = cassSession.execute(bound);
		
		for(Row row : rs)
		{
			values.put(String.valueOf(row.getInt("movieid")), row.getString("rating"));
		}
        return values;
	}
	
	private void insertDataIntoRedis(String aUserId,Map<String, String> aValues)
	{
        jedis.hmset(aUserId, aValues);
	}

}
