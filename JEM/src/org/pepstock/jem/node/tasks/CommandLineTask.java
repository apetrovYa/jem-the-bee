/**
    JEM, the BEE - Job Entry Manager, the Batch Execution Environment
    Copyright (C) 2012-2015  Andrea "Stock" Stocchero
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
package org.pepstock.jem.node.tasks;

import java.util.HashMap;
import java.util.Map;

import org.pepstock.jem.node.tasks.shell.JavaCommand;

/**
 * Instances of this class encapsulate the execution of an external process,
 * program or shell script.<br>
 * This task starts and external process using command line arguments,
 * environment variables, and a list of input and/or output files to use or
 * generated by the external process.<br>
 * This task also captures the standard and error output (i.e. equivalent to
 * System.out and System.err) of the external process.
 */
public abstract class CommandLineTask extends Task {
	
	/**
	 * Explicit serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The environment variables to set.
	 */
	private Map<String, String> env = new HashMap<String, String>();
	/**
	 * The directory to start the command in.
	 */
	private String startDir = null;
	
	private JavaCommand command = null;
	
	/**
	 * Default constructor.
	 */
	public CommandLineTask() {
		super();
	}

	/**
	 * @return the command
	 */
	public JavaCommand getCommand() {
		return command;
	}



	/**
	 * @param command the command to set
	 */
	public void setCommand(JavaCommand command) {
		this.command = command;
	}



	/**
	 * Get the environment variables to set.
	 * 
	 * @return a map of variable names to their corresponding values.
	 */
	public Map<String, String> getEnv() {
		return env;
	}

	/**
	 * Get the environment variables to set.
	 * 
	 * @param env a map of variable names to their corresponding values.
	 */
	public void setEnv(Map<String, String> env) {
		this.env = env;
	}

	/**
	 * Get the directory to start the command in.
	 * 
	 * @return the start directory as a string.
	 */
	public String getStartDir() {
		return startDir;
	}

	/**
	 * Set the directory to start the command in.
	 * 
	 * @param startDir the start directory as a string.
	 */
	public void setStartDir(String startDir) {
		this.startDir = startDir;
	}

	
}