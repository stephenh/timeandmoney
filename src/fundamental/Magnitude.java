/**
 *
 * This code is provided under the following licence: 
 * MIT Licence
 * timeandmoney.sourceforge.net
 * Copyright 2004, Domain Language, Inc.
 */

package fundamental;


public interface Magnitude extends Comparable {
	boolean lt(Magnitude other);
	boolean gt(Magnitude other);
	boolean le(Magnitude other);
	boolean ge(Magnitude other);
}
