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
package org.pepstock.jem.junit.test.antutils;


/**
 * 
 * @author Simone "Busy" Businaro
 * @version 1.4
 */
public class ScriptTask extends AntTestCase {

	/**
	 * Test the script linux task
	 * 
	 * @throws Exception
	 */
	public void testScriptLinux() throws Exception {
		if (!System.getProperty("os.name").startsWith("Windows")) {
			assertEquals(submit("script/TEST_ANTUTILS_SCRIPT_LINUX.xml"), 0);
			assertEquals(submit("script/TEST_ANTUTILS_SCRIPT_ADVANCED_LINUX.xml"), 0);
		}
	}

	/**
	 * Test the script windows task
	 * 
	 * @throws Exception
	 */
	public void testScriptWindows() throws Exception {
		if (System.getProperty("os.name").startsWith("Windows")) {
			assertEquals(submit("script/TEST_ANTUTILS_SCRIPT_WINDOWS.xml"), 0);
		}
	}
}
