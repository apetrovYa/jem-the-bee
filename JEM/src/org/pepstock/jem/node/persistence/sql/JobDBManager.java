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
package org.pepstock.jem.node.persistence.sql;

import org.pepstock.jem.Job;

/**
 * Manages all SQL statements towards the database to persist the job in INPUT,
 * OUTPUT and ROUTING queues.<br>
 * Actions are the same for all queues (and then table because there is a table
 * for queue)<br>
 * 
 * @author Andrea "Stock" Stocchero
 * @version 3.0
 * 
 */
public class JobDBManager extends AbstractDBManager<Job>{

	/**
	 * Creates a common DB manager for JOBS
	 * @param queueName hazelcast queuename
	 * @param sqlContainer SQL container
	 */
	public JobDBManager(String queueName, SQLContainer sqlContainer) {
		this(queueName, sqlContainer, false);
	}

	/**
	 * Creates a common DB manager for JOBS
	 * @param queueName hazelcast queuename
	 * @param sqlContainer SQL container
	 * @param canBeEvicted if teh map can be evited in HC
	 */
	public JobDBManager(String queueName, SQLContainer sqlContainer, boolean canBeEvicted) {
		super(queueName, sqlContainer, canBeEvicted);
	}

	/* (non-Javadoc)
	 * @see org.pepstock.jem.node.persistence.AbstractDBManager#getKey(java.lang.Object)
	 */
	@Override
	public String getKey(Job item) {
		return item.getId();
	}

}