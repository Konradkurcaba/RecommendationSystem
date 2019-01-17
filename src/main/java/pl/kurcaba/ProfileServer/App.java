package pl.kurcaba.ProfileServer;

import java.util.HashMap;
import java.util.Map;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import Client.Client;
import redis.clients.jedis.Jedis;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main( String[] args )
    {
		ProfileServer profileServer = new ProfileServer();
		profileServer.start();
    }
}
