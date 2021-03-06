/**
    JEM, the BEE - Job Entry Manager, the Batch Execution Environment
    Copyright (C) 2012-2015   Marco "Fuzzo" Cuccato
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
package org.pepstock.jem.node.resources.definition.fields;

import java.io.Serializable;

import org.pepstock.jem.node.resources.definition.AbstractField;
import org.pepstock.jem.node.resources.definition.ResourcePartDescriptor;

/**
 * This class represent a generic resource field. 
 * @author Marco "Fuzzo" Cuccato
 * @version 1.3
 */
public abstract class AbstractFieldDescriptor extends AbstractField implements Serializable, ResourcePartDescriptor {

	private static final long serialVersionUID = 1L;

	private boolean override = true;
	private boolean visible = true;

	/**
	 * Builds the field
	 * @param key the field key, identifier of the field
	 * @param label the field label, what the user see next to this field 
	 */
	public AbstractFieldDescriptor(String key, String label) {
		super.setKey(key);
		super.setLabel(label);
	}
	
	/**
	 * @return the override
	 */
	public boolean isOverride() {
		return override;
	}

	/**
	 * @param override the override to set
	 */
	public void setOverride(boolean override) {
		this.override = override;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	/* (non-Javadoc)
	 * @see org.pepstock.jem.node.resources.definition.AbstractField#toString()
	 */
	@Override
	public String toString() {
		return "AbstractFieldDescriptor [toString()=" + super.toString() + ", override=" + override + ", visible=" + visible + "]";
	}
}