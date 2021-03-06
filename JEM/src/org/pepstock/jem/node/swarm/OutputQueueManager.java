/**
    JEM, the BEE - Job Entry Manager, the Batch Execution Environment
    Copyright (C) 2012-2015  Simone "Busy" Businaro
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
package org.pepstock.jem.node.swarm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import org.pepstock.jem.Job;
import org.pepstock.jem.log.LogAppl;
import org.pepstock.jem.node.JobComparator;
import org.pepstock.jem.node.Main;
import org.pepstock.jem.node.NodeInfo;
import org.pepstock.jem.node.NodeMessage;
import org.pepstock.jem.node.OutputQueuePredicate;
import org.pepstock.jem.node.Status;
import org.pepstock.jem.node.hazelcast.ExecutorServices;
import org.pepstock.jem.node.hazelcast.Queues;
import org.pepstock.jem.node.persistence.DatabaseException;
import org.pepstock.jem.node.persistence.EvictionHelper;
import org.pepstock.jem.node.persistence.OutputMapManager;
import org.pepstock.jem.node.swarm.executors.RouterOut;
import org.pepstock.jem.util.filters.Filter;
import org.pepstock.jem.util.filters.FilterToken;
import org.pepstock.jem.util.filters.fields.JobFilterFields;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IMap;
import com.hazelcast.core.Member;
import com.hazelcast.map.listener.EntryAddedListener;

/**
 * Output queue manager listeners, whcih informs when a job is ended (put in OUTPTU map).
 * 
 * @author Simone "Busy" Businaro
 * @version 1.3
 * 
 */
public class OutputQueueManager implements EntryAddedListener<String, Job> {

	private JobComparator comparator = new JobComparator();

	private boolean notifyOutputEnded = true;

	/**
	 * Adds itself as listener to OUTPUT map
	 */
	public OutputQueueManager() {
		IMap<String, Job> routingQueue = Main.getHazelcast().getMap(Queues.OUTPUT_QUEUE);
		routingQueue.addEntryListener(this, true);
	}

	/* (non-Javadoc)
	 * @see com.hazelcast.core.EntryListener#entryAdded(com.hazelcast.core.EntryEvent)
	 */
	@Override
	public void entryAdded(EntryEvent<String, Job> event) {
		// if swarm is active
		if (Main.SWARM.getStatus().equals(Status.ACTIVE)) {
			Job job = event.getValue();
			// if it was a routed job notify the environment from where it was
			// routing that the job ended
			if (job.getRoutingInfo().getEnvironment() != null) {
				notifyEndedRoutedJob(job);
			}
		}		
	}

	/**
	 * This method will send back to the environment that execute the routing
	 * the routed jobs to notify their end.
	 */
	public synchronized void notifyEndedRoutedJobsByAvailableEnvironments() {
		// if swarm is active
		if (Main.SWARM.getStatus().equals(Status.ACTIVE)) {
			IMap<String, NodeInfo> nodesMap = Main.SWARM.getHazelcastInstance().getMap(Queues.SWARM_NODES_MAP);
			Collection<NodeInfo> nodes = nodesMap.values();
			// notify ended job only if nodes exist
			if (nodes != null && !nodes.isEmpty()) {
				// set of active environments
				Set<String> environments = new HashSet<String>();
				Iterator<NodeInfo> nodesiter = nodes.iterator();
				// loads all swarm nodes (of other environment)
				while (nodesiter.hasNext()) {
					String currEnv = nodesiter.next().getExecutionEnvironment().getEnvironment();
					// look only for environments different from local one
					if (!currEnv.equalsIgnoreCase(Main.EXECUTION_ENVIRONMENT.getEnvironment())){
						environments.add(currEnv);
					}
				}
				// gets output queue
				IMap<String, Job> outputQueue = Main.getHazelcast().getMap(Queues.OUTPUT_QUEUE);
				// creates the predicate 
				// to get all jobs submitted from another environment
				Collection<Job> jobs = null;
				OutputQueuePredicate oqp = new OutputQueuePredicate();
				oqp.setObject(environments);
				if (EvictionHelper.isEvicted(Queues.OUTPUT_QUEUE)){
					jobs = new ArrayList<Job>();
					for (String env : environments){
						Filter filter = new Filter();
						filter.add(new FilterToken(JobFilterFields.ENVIRONMENT.getName(), env));
						try {
							Collection<Job> tempJobs = OutputMapManager.getInstance().loadByFilter(filter);
							if (!tempJobs.isEmpty()){
								Iterator<Job> iter = tempJobs.iterator();
								while(iter.hasNext()){
									if (!oqp.apply(iter.next())){
										iter.remove();
									}
								}
								jobs.addAll(tempJobs);
							}
						} catch (DatabaseException e) {
							LogAppl.getInstance().emit(NodeMessage.JEMC295E, e);
						}
					}
				} else {
					// gets jobs
					jobs = outputQueue.values(oqp);
				}
				// sort jobs
				List<Job> queuedJobs = new ArrayList<Job>(jobs);
				Collections.sort(queuedJobs, comparator);
				// notifies the end of the job
				for (Job currJob : queuedJobs) {
					notifyEndedRoutedJob(currJob);
				}
			}
		}
	}

	/**
	 * Notify the end of a single job
	 * 
	 * @param currJob job ended and submitted from another environment
	 */
	private synchronized void notifyEndedRoutedJob(Job currJob) {
		// if swarm is active
		if (Main.SWARM.getStatus().equals(Status.ACTIVE)) {
			// sets status that is working
			setNotifyOutputEnded(false);
			IMap<String, Job> outputQueue = Main.getHazelcast().getMap(Queues.OUTPUT_QUEUE);
			IMap<String, NodeInfo> nodesMap = Main.SWARM.getHazelcastInstance().getMap(Queues.SWARM_NODES_MAP);
			// lock the entry of the job
			try {
				// gets job by job id
				outputQueue.lock(currJob.getId());
				Job job = outputQueue.get(currJob.getId());
				// if job is still pending to be notified
				if (job != null && job.getRoutingInfo().isOutputCommitted() == null) {
					// gets member to reply the end of the job
					MapSwarmNodePredicate mnp = new MapSwarmNodePredicate();
					mnp.setObject(job.getRoutingInfo().getEnvironment());
					Member member = MapSwarmNodesManager.getMember(nodesMap.values(mnp));
					// check if member is still available otherwise do
					// nothing
					if (member != null) {
						LogAppl.getInstance().emit(SwarmNodeMessage.JEMO006I, job);
						// executes a distrbuted task to notified to the member
						// previously extracted that
						// the job is ended
						IExecutorService executorService = Main.SWARM.getHazelcastInstance().getExecutorService(ExecutorServices.SWARM);
						// start 2 phase commit
						// setting the status to false to the job and saving it again
						job.getRoutingInfo().setOutputCommitted(false);
						outputQueue.put(job.getId(), job);
						Future<Boolean> task = executorService.submitToMember(new RouterOut(job), member);
						if (task.get()) {
							// now output is committed
							// and set the status
							job.getRoutingInfo().setOutputCommitted(true);
							// and save again
							outputQueue.put(job.getId(), job);
							LogAppl.getInstance().emit(SwarmNodeMessage.JEMO007I, job);
						}
					}
				}
			} catch (Exception e) {
				LogAppl.getInstance().emit(SwarmNodeMessage.JEMO005E, currJob, e);
			} finally {
				// always unlock output queue
				if (outputQueue != null) {
					outputQueue.unlock(currJob.getId());
				}
			}
			// reset notification status
			setNotifyOutputEnded(true);
		}
	}

	/**
	 * @return the notifyOutputEnded used to see if the OutputQueueManager is
	 *         still working
	 */
	public boolean isNotifyOutputEnded() {
		return notifyOutputEnded;
	}

	/**
	 * @param notifyOutputEnded the notifyOutputEnded to set
	 */
	private void setNotifyOutputEnded(boolean notifyOutputEnded) {
		this.notifyOutputEnded = notifyOutputEnded;
	}
}