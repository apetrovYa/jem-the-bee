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
package org.pepstock.jem.commands.docker;

import java.io.File;
import java.util.Properties;

import org.pepstock.jem.commands.util.NodeAttributes;
import org.pepstock.jem.commands.util.NodeProperties;
import org.pepstock.jem.node.configuration.ConfigurationException;


/**
 * Creates web app using the minimum arguments to use into Docker container run.
 * 
 * @author Andrea "Stock" Stocchero
 * @version 3.0
 */
public class StartUpWeb extends StartUp{
	
	private static final String COMMAND = "jem-web.sh";

	/* (non-Javadoc)
	 * @see org.pepstock.jem.commands.docker.StartUp#loadProperties(java.util.Properties)
	 */
	@Override
	void loadProperties(Properties props) throws ConfigurationException {
		// sets the mandatory values even if they are not necessary
		props.setProperty(NodeProperties.JEM_DB_DRIVER, this.getClass().getName());
		props.setProperty(NodeProperties.JEM_DB_URL, this.getClass().getName());
	}

	/* (non-Javadoc)
	 * @see org.pepstock.jem.commands.docker.StartUp#getCommand()
	 */
	@Override
	String getCommand() {
		return COMMAND;
	}

	/* (non-Javadoc)
	 * @see org.pepstock.jem.commands.docker.StartUp#hasConfigured()
	 */
	@Override
	boolean hasConfigured() {
		// checks if there is the persistent with ENVIROMENT 
		// already mounted
		File persistence = new File(JEM_GFS_FILE, "persistence");
		if (persistence.exists()){
			// if persistence exists
			File env = new File(persistence, getEnvironment());
			if (env.exists()){
				// if env exists
				File web = new File(env, NodeAttributes.TEMPLATE_WEB_DIRECTORY_NAME);
				if (web.exists()){
					// if web exists
					File warFile = new File(web, NodeAttributes.TEMPLATE_WAR_FILE_NAME);
					return warFile.exists();
				}
			}
		}
		return false;
	}

	/**
	 * Main method!  
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		StartUpWeb create = new StartUpWeb();
		System.exit(create.execute(args));
	}
	
}
