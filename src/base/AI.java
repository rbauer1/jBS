package base;


public interface AI {
	void attack();
	void printHits();
	String getName();
	boolean hasProbabilities();
	int[][][][] getProbabilities();
	int findHighestProbabilityPublic();
    public enum Statuses {
        SUBSCAN, SUNK, DEADSPACE, CR, BS, DES, SUB, PB, UNKNOWN
    }
}
