package cmu.assignment.pool;

public class GuardShift {
	public int timeStart = -1;
	public int timeEnd = -1;
	public int timeCovered = -1;

	public GuardShift(int start, int end) {
		this.timeStart = start;
		this.timeEnd = end;
		this.timeCovered = end - start;
		if (timeCovered <= 0)
			throw new IllegalArgumentException(
					"End time cannot be less that start time. Start Time: " + start + ", End Time: " + end);
	}
}
