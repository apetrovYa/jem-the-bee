package org.pepstock.jem.gwt.client.panels.jobs.commons;

import java.util.Date;

import org.pepstock.jem.Job;

/**
 * Utility class for comparing and sorting Jobs
 * @author Marco "Fuzzo" Cuccato
 */
public final class JobFieldsComparator {

	private JobFieldsComparator() {
	}

	/**
	 * Sort jobs by user
	 * @param o1
	 * @param o2
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object 
	 */
	public static int sortByUser(Job o1, Job o2) {
		String userO1= (o1.isUserSurrogated()) ? o1.getJcl().getUser() : o1.getUser() ;
		String userO2= (o2.isUserSurrogated()) ? o2.getJcl().getUser() : o2.getUser() ;
		return userO1.compareTo(userO2);
	}
	
	/**
	 * Sort jobs by route status
	 * @param o1
	 * @param o2
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object
	 */
	public static int sortByRouteStatus(Job o1, Job o2) {
		int diff;
		Boolean committed1 = o1.getRoutingInfo().isRoutingCommitted();
		Boolean committed2 = o2.getRoutingInfo().isRoutingCommitted();
		// could be true, false or NULL
		if (committed1 != null && committed2 != null) {
			diff = committed1.compareTo(committed2);
		} else if (committed1 != null && committed2 == null) {
			diff = 1;
		} else if (committed1 == null && committed2 != null) {
			diff = -1;
		} else {
			diff = 0;
		}
		return diff;
	}
	
	/**
	 * Sort jobs by routed time
	 * @param o1
	 * @param o2
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object
	 */
	public static int sortByRoutedTime(Job o1, Job o2) {
		int diff;
		Date routedTime1 = o1.getRoutingInfo().getRoutedTime();
		Date routedTime2 = o2.getRoutingInfo().getRoutedTime();
		Boolean routed1 = routedTime1 != null;
		Boolean routed2 = routedTime2 != null;
		if (routed1 && routed2) {
			diff = routedTime1.compareTo(routedTime2);
		} else if (routed1 && !routed2) {
			diff = 1;
		} else if (!routed1 && routed2) {
			diff = -1;
		} else {
			diff = 0;
		}
		return diff;
	}

	
}
