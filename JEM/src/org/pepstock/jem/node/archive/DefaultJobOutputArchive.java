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
package org.pepstock.jem.node.archive;

import org.pepstock.jem.Job;
import org.pepstock.jem.log.JemException;

/**
 * @author Andrea "Stock" Stocchero
 * @version 1.0	
 *
 */
public class DefaultJobOutputArchive implements JobOutputArchive {


	/* (non-Javadoc)
	 * @see org.pepstock.jem.node.archive.JobOutputArchive#archive(org.pepstock.jem.Job, byte[])
	 */
	@Override
	public boolean archive(Job job, byte[] zipOutputContent) throws JemException {
		return true;
	}

}