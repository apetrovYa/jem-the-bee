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

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.pepstock.jem.node.hazelcast.Queues;
import org.pepstock.jem.node.resources.Resource;

/**
 * Map manager based on MONGO for common resources.
 * 
 * @author Andrea "Stock" Stocchero
 * @version 3.0
 */
public class CommonResourcesMongoManager extends AbstractMongoManager<Resource> {
	
	private static final String FIELD_KEY = "name";

	/**
	 * Creates the object setting queue and field key of JSON
	 */
	public CommonResourcesMongoManager() {
		super(Queues.COMMON_RESOURCES_MAP, FIELD_KEY);
	}
	
	/* (non-Javadoc)
	 * @see org.pepstock.jem.node.persistence.DataBaseManager#getKey(java.lang.Object)
	 */
	@Override
	public String getKey(Resource item) {
		return item.getName();
	}

	/* (non-Javadoc)
	 * @see org.pepstock.jem.node.persistence.mongo.AbstractMongoManager#createObject(org.codehaus.jackson.map.ObjectMapper, java.lang.String)
	 */
	@Override
	public Resource createObject(ObjectMapper mapper, String objFound) throws IOException {
		return mapper.readValue(objFound, Resource.class);
	}
	
}
