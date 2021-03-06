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
package org.pepstock.jem.gwt.client.panels.administration.nodesconfig.commands;

import org.pepstock.jem.NodeInfoBean;
import org.pepstock.jem.gwt.client.commons.Loading;
import org.pepstock.jem.gwt.client.commons.ServiceAsyncCallback;
import org.pepstock.jem.gwt.client.commons.Toast;
import org.pepstock.jem.gwt.client.panels.administration.nodesconfig.CommandExecutor;
import org.pepstock.jem.gwt.client.panels.administration.nodesconfig.ResultPanel;
import org.pepstock.jem.gwt.client.services.Services;
import org.pepstock.jem.log.MessageLevel;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;

/**
 * @author Andrea "Stock" Stocchero
 *
 */
public class LogCommand extends CommandExecutor {
	
	private static final int LOG = 0;
	
	private static final String LABEL = "Get Log";
	
	private static final String TITLE = "Last log records";


	/**
	 * 
	 */
	public LogCommand() {
		setIndex(LOG);
		setTitle(TITLE);
		setLabel(LABEL);
	}


	/* (non-Javadoc)
	 * @see org.pepstock.jem.gwt.client.panels.administration.nodesconfig.CommandExecutor#execute(org.pepstock.jem.gwt.client.panels.administration.nodesconfig.ResultPanel)
	 */
    @Override
    public void execute(final NodeInfoBean node, final ResultPanel resultPanel) {
    	Loading.startProcessing();
	    Scheduler scheduler = Scheduler.get();
	    scheduler.scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				Services.NODES_MANAGER.log(node, new LogAsyncCallback(resultPanel));
			}
	    });
    }
    
    private static class LogAsyncCallback extends ServiceAsyncCallback<String> {
    	
    	private final ResultPanel resultPanel;
    	
    	public LogAsyncCallback(final ResultPanel resultPanel) {
    		this.resultPanel = resultPanel;
    	}
    	
		@Override
		public void onJemFailure(Throwable caught) {
			new Toast(MessageLevel.ERROR, caught.getMessage(), "Log Command error!").show();
		}

		@Override
		public void onJemSuccess(String result) {
			// sets content to a panel to show it
			if (result != null){
				resultPanel.setResult(result);
			} else {
				new Toast(MessageLevel.WARNING, "Log Command result is null. <br>Please have a look to JEM node log", "Log Command warning!").show();
			}
		}
		
		@Override
        public void onJemExecuted() {
			Loading.stopProcessing();
        }
    }
    
}