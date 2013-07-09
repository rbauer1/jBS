package base;

import java.io.IOException;

public interface AI {
	void attack();
	void printHits();
	String getName();
    public enum Statuses {
        SUBSCAN, SUNK, DEADSPACE, CR, BS, DES, SUB, PB, UNKNOWN
    }
}
