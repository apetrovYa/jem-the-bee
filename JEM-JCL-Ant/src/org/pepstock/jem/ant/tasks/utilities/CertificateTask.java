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
package org.pepstock.jem.ant.tasks.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.BuildException;
import org.pepstock.jem.ant.tasks.utilities.certificate.Delete;
import org.pepstock.jem.ant.tasks.utilities.certificate.Import;
import org.pepstock.jem.log.JemException;
import org.pepstock.jem.log.LogAppl;
import org.pepstock.jem.log.MessageException;
import org.pepstock.jem.node.tasks.jndi.ContextUtils;

/**
 * Is a utility (both a task ANT and a main program) that is able to manage
 * (add, remove, get and update) common resources.<br>
 * Needs a <code>COMMAND</code> data description which must contain all
 * commands.<br>
 * 
 * @author Andrea "Stock" Stocchero
 * @version 2.3
 * 
 */
public class CertificateTask extends AntUtilTask {

	private static final String DATA_DESCRIPTION_NAME = "COMMAND";

	private static final String COMMAND_SEPARATOR = ";";

	/**
	 * Empty constructor
	 */
	public CertificateTask() {
	}

	/**
	 * Sets itself as main program and calls <code>execute</code> method of
	 * superclass (StepJava) to prepare the RESOURCES data description.
	 * 
	 * @throws BuildException
	 *             occurs if an error occurs
	 */
	@Override
	public void execute() throws BuildException {
		super.setClassname(CertificateTask.class.getName());
		super.execute();
	}

	/**
	 * Main program, called by StepJava class. It reads the InputStream defined
	 * as :<br>
	 * <ul>
	 * <li>COMMAND data description with all define Nodes commands</li>
	 * </ul>
	 * <br>
	 * Before to start commands, checks all command syntax
	 * 
	 * @param args
	 *            RMI port necessary to execute command in the cluster
	 * @throws ParseException 
	 * @throws NamingException 
	 * @throws IOException 
	 * @throws JemException 
	 * @throws Exception
	 *             if COMMAND data description doesn't exists, if an error
	 *             occurs during the command parsing
	 */
	public static void main(String[] args) throws ParseException, NamingException, IOException, JemException {
		// new initial context to access by JNDI to COMMAND DataDescription
		InitialContext ic = ContextUtils.getContext();

		// gets inputstream
		Object filein = (Object) ic.lookup(DATA_DESCRIPTION_NAME);

		// reads content of inout stream
		StringBuilder recordsSB = read((InputStream) filein);
		// trims result to see if is empty
		String records = recordsSB.toString().trim();
		if (records.length() > 0) {
			// list with all gdgs because it checks command syntax before
			// starting creation
			List<SubCommand> list = new LinkedList<SubCommand>();

			// splits with command separator ";"
			String[] commands = records.toString().split(COMMAND_SEPARATOR);
			for (int i = 0; i < commands.length; i++) {
				// removes all useless blanks leaving a single blank
				String[] s = StringUtils.split(commands[i], " ");
				String commandLine = StringUtils.join(s, ' ');
				// logs which command is parsing
				LogAppl.getInstance().emit(AntUtilMessage.JEMZ025I, commandLine);
				SubCommand command = null;
				if (StringUtils.startsWith(commandLine, Import.COMMAND_KEYWORD)) {
					command = new Import(commandLine);
				} else if (StringUtils.startsWith(commandLine,
						Delete.COMMAND_KEYWORD)) {
					command = new Delete(commandLine);
				} else {
					throw new MessageException(AntUtilMessage.JEMZ026E, "CERTIFICATE",	commandLine);
				}
				// adds to list
				list.add(command);
			}
			// compute all valid commands
			for (SubCommand cmd : list) {
				cmd.execute();
			}
		}
	}
}