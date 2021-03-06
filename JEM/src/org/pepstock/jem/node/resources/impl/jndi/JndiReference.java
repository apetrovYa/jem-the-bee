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
package org.pepstock.jem.node.resources.impl.jndi;

import javax.naming.Reference;

/**
 * Sets constants for JNDI for Datasource oject. It uses Apache DBCP classes.<br>
 * It uses <code>org.apache.commons.dbcp.BasicDataSourceFactory</code> to create a datasource
 * to use inside the java programs.
 * 
 * @author Andrea "Stock" Stocchero
 * @version 1.0	
 *
 */
public class JndiReference extends Reference {

	private static final long serialVersionUID = 1L;

	/**
	 * Apache DBCP JNDI factory
	 */
	public static final String JNDI_FACTORY = JndiFactory.class.getName();
	
	/**
	 * Is DataSource object created when requested
	 */
	public static final String JNDI_OBJECT = ContextWrapper.class.getName();
	
	/**
	 * Creates a JNDI reference for JDBC purposes
	 */
	public JndiReference() {
		super(JNDI_OBJECT, JNDI_FACTORY, null);
	}

}