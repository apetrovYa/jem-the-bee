/**
    JEM, the BEE - Job Entry Manager, the Batch Execution Environment
    Copyright (C) 2012-2015   Andrea "Stock" Stocchero
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.pepstock.jem.node.persistence.mongo;

import java.net.UnknownHostException;

import org.bson.Document;
import org.pepstock.jem.node.persistence.DatabaseException;
import org.pepstock.jem.util.TimeUtils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Singleton which contains the MONGO client to connect MONGO daemon to perform persistent operations.
 * 
 * @author Andrea "Stock" Stocchero
 * @version 3.0
 */
public final class DBManager {
	
	/**
	 * Default timeout to check if the connection is good
	 */
	public static final int SERVER_SELECTION_TIMEOUT = 5* (int)TimeUtils.SECOND;
	
	private static DBManager INSTANCE = null;
	
	MongoClientURI configuration = null;

	private MongoClient client = null;
	
	/**
	 * Used the MONGO configuration to create the MONGO client
	 * @param configuration configuration read from JEM configuration
	 * @throws UnknownHostException if any network errors occurs
	 */
	private DBManager(MongoClientURI configuration) throws UnknownHostException {
		// saves configuration
		this.configuration = configuration;
		// creates client
		this.client = new MongoClient(configuration);
	}
	
	/**
	 * Creates an instance ONLY if is not previously created.
	 * @param configuration configuration read from JEM configuration
	 * @return a DB instance
	 * @throws UnknownHostException if any network errors occurs
	 * @throws DatabaseException 
	 */
	public static synchronized DBManager createInstance(MongoClientURI configuration) throws UnknownHostException, DatabaseException{
		if (INSTANCE == null){
			INSTANCE = new DBManager(configuration);
			INSTANCE.init();
		}
		return INSTANCE;
	}
	
	/**
	 * Returns the instance 
	 * @return the instance
	 */
	public static DBManager getInstance(){
		return INSTANCE;
	}
	
	/**
	 * Initializes the client, checking if the connection is ok running a ping command.
	 * This is mandatory because Mongo JAVA client doesn't return any exception if the credentials are not ok. 
	 * @throws DatabaseException if credentials are not ok.
	 */
	private void init() throws DatabaseException{
		try {
			// checks the status of cedetials
			client.getDatabase(configuration.getDatabase()).runCommand(new Document("ping", 1));
		} catch (Exception e) {
			throw new DatabaseException(e);
		}
	}
	
	/**
	 * Returns the MONGO client
	 * @return the MONGO client
	 */
	public MongoClient getClient(){
		return client;
	}
	
	/**
	 * Returns the MONGO collection by name
	 * @param collection collection name 
	 * @return MONGO collection 
	 */
	public MongoCollection<Document> getCollection(String collection){
		MongoDatabase db = client.getDatabase(configuration.getDatabase());
		return db.getCollection(collection);
	}
}
