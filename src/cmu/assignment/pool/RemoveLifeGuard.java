package cmu.assignment.pool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RemoveLifeGuard {
	private ArrayList<GuardShift> listShifts = null;
	private int supersededIntervalCount = 0;
	private int totalCoverage = 0;

	public RemoveLifeGuard(ArrayList<GuardShift> listShifts) {
		this.listShifts = listShifts;
	}

	public int maxCoverageAfterRemoveOneLifeGuard() {

		ShiftStartTimeComparator startTimeComparator = new ShiftStartTimeComparator();

		// sort the list
		Collections.sort(this.listShifts, startTimeComparator);

		calcSupersededIntervalsAndTotalCoverage();

		// if there is at least one superseded interval, then we can safely remove
		// one life guard with out affecting the total coverage time
		if (this.supersededIntervalCount > 0) {
			return this.totalCoverage;
		}

		// find the life guard whose removal has minimal impact on total coverage
		int maxCoverageAfterRemoval = findLifeGaurdWithMinimalImpact();

		return maxCoverageAfterRemoval;
	}

	// return max coverage after removal of lifeguard with minimal impact
	private int findLifeGaurdWithMinimalImpact() {
		int size = this.listShifts.size();
		// keep track of max coverage seen so far, after removal of one life guard
		int maxCoverageSeenSoFar = -1;

		for (int counter = 0; counter < size; counter++) {
			int leftOverlap = 0, rightOverlap = 0;
			int currShiftStartTime = listShifts.get(counter).timeStart;
			int currShiftEndTime = listShifts.get(counter).timeEnd;
			int currShiftCoverage = listShifts.get(counter).timeCovered;

			if (counter == 0) {
				// first shift in the list will not have left over laps
				leftOverlap = 0;
			} else {
				int prevShiftStartTime = listShifts.get(counter-1).timeStart;
				int prevShiftEndTime = listShifts.get(counter-1).timeEnd; 
				leftOverlap = calcLeftOverlap(prevShiftStartTime, prevShiftEndTime, currShiftStartTime, currShiftEndTime);
			}

			if (counter == size - 1) {
				// LAST shift in the list will not have RIGHT over laps
				rightOverlap = 0;
			} else {
				int nextShiftStartTime = listShifts.get(counter+1).timeStart;
				int nextShiftEndTime = listShifts.get(counter+1).timeEnd;
				rightOverlap = calcRightOverlap(currShiftStartTime, currShiftEndTime, nextShiftStartTime, nextShiftEndTime);
			}

			
			//unique coverage of units provided by current shift
			int uniqueCoverageCurrShift = currShiftCoverage - (leftOverlap + rightOverlap);
			int coverage = totalCoverage - uniqueCoverageCurrShift;
			
			if (coverage > maxCoverageSeenSoFar)
				maxCoverageSeenSoFar = coverage;
		}

		return maxCoverageSeenSoFar;
	}

	private void calcSupersededIntervalsAndTotalCoverage() {

		int size = this.listShifts.size();
		int timeWindowStart = this.listShifts.get(0).timeStart;
		int timeWindowEnd = this.listShifts.get(0).timeEnd;

		for (int counter = 1; counter < size; counter++) {
			int currTimeStart = this.listShifts.get(counter).timeStart;
			int currTimeEnd = this.listShifts.get(counter).timeEnd;

			// time window is subset (lies within) the current shift time
			if (checkSupersededShift(timeWindowStart, timeWindowEnd, currTimeStart, currTimeEnd)) {
				this.supersededIntervalCount = +1;
				timeWindowEnd = currTimeEnd;
				continue;
			}

			// time window (overlaps) with current shift time
			if (checkIntersection(timeWindowStart, timeWindowEnd, currTimeStart, currTimeEnd)) {
				timeWindowEnd = currTimeEnd;
				continue;
			}

			// current shift time does not intersect with time window
			totalCoverage = totalCoverage + (timeWindowEnd - timeWindowStart);
			timeWindowStart = currTimeStart;
			timeWindowEnd = currTimeEnd;
		}
		totalCoverage = totalCoverage + (timeWindowEnd - timeWindowStart);
	}

	private boolean checkIntersection(int timeWindowStart, int timeWindowEnd, int currTimeStart, int currTimeEnd) {
		if (currTimeStart <= timeWindowEnd) {
			return true;
		}
		return false;
	}

	// check if given time window is subset of current start and end time
	private boolean checkSupersededShift(int timeWindowStart, int timeWindowEnd, int currTimeStart, int currTimeEnd) {

		if ((timeWindowStart >= currTimeStart) && (timeWindowEnd <= currTimeEnd)) {
			return true;
		}
		return false;
	}

	private int calcLeftOverlap(int leftStartTime, int leftEndTime, int currStartTime, int currEndTime) {
		if (leftEndTime <= currStartTime) {
			return 0;
		}
		return leftEndTime - currStartTime;
	}

	private int calcRightOverlap(int currStartTime, int currEndTime, int rightStartTime, int rightEndTime) {
		if (rightStartTime >= currEndTime) {
			return 0;
		} 
		
		return currEndTime - rightStartTime;
	}

	private class ShiftStartTimeComparator implements Comparator<GuardShift> {

		@Override
		public int compare(GuardShift s1, GuardShift s2) {

			if (s1.timeStart == s2.timeStart) {
				// both shifts are exactly same
				if (s1.timeEnd == s2.timeEnd) {
					return 0;
				}
				// s1 ends before s2; s1 is smaller
				else if (s1.timeEnd < s2.timeEnd) {
					return -1;
				}
				// s1 ends after s2; s1 is bigger
				else
					return 1;
			}

			// s1 starts before s2; s1 is smaller
			if (s1.timeStart < s2.timeStart) {
				return -1;
			}

			// s1 starts after s2; s1 is bigger
			return 1;
		}

	}
}
