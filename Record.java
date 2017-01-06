import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

// Immutable.  Stores 3 strings referred to as entity, relation, and
// property. Each Record has a unique integer ID which is set on
// creation.  All records are made through the factory method
// Record.makeRecord(e,r,p).  Record which have some fields wild are
// created using Record.makeQuery(wild,e,r,p)
public class Record {
	private static int running_id = 0;
	private int id;
	private String entity;
	private String relation;
	private String property;
	private String wild;

	// Private Constructor
	private Record(String entity, String relation, String property) {
		this.id = nextId();
		this.wild = "";
		this.entity = entity;
		this.relation = relation;
		this.property = property;
	}
	
	
	// Private constructor with WILD
	private Record(String wild, String entity, String relation, String property) {
		this.id = nextId();
		this.wild = wild;
		this.entity = entity;
		this.relation = relation;
		this.property = property;
	}



	// Return the next ID that will be assigned to a Record on a call to
	// makeRecord() or makeQuery()
	public static int nextId() {
		return running_id++;
	}

	// Return a stringy representation of the record. Each string should
	// be RIGHT justified in a field of 8 characters with whitespace
	// padding the left. Java's String.format() is useful for padding
	// on the left.
	public String toString() {
		StringBuffer strToReturn = new StringBuffer();
		strToReturn.append(String.format("%8s %8s %8s ", entity, relation, property));
		return strToReturn.toString();
	}

	// Return true if this Record matches the parameter record r and
	// false otherwise. Two records match if all their fields match.
	// Two fields match if the fields are identical or at least one of
	// the fields is wild.
	public boolean matches(Record r) {
		if (entity().equals(r.entity()) || entityWild() || r.entityWild()) {
			if (relation().equals(r.relation()) || relationWild() || r.relationWild()) {
				if (property().equals(r.property()) || propertyWild() || r.propertyWild()) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	// Return this record's ID
	public int id() {
		return id;
	}

	// Accessor methods to access the 3 main fields of the record:
	// entity, relation, and property.
	public String entity() {
		return entity;
	}

	public String relation() {
		return relation;
	}

	public String property() {
		return property;
	}

	// Returns true/false based on whether the the three fields are
	// fixed or wild.
	public boolean entityWild() {
		return entity.equals(wild);
	}

	public boolean relationWild() {
		return relation.equals(wild);
	}

	public boolean propertyWild() {
		return property.equals(wild);
	}

	// Factory method to create a Record. No public constructor is
	// required.
	public static Record makeRecord(String entity, String relation,
			String property) {
		if (entity == null || relation == null || property == null)
			throw new IllegalArgumentException("One or More Arguments are NULL.\n");
		return new Record(entity, relation, property);
	}

	// Create a record that has some fields wild. Any field that is
	// equal to the first argument wild will be a wild card
	public static Record makeQuery(String wild, String entity, String relation,
			String property) {
		if (wild == null || entity == null || relation == null || property == null)
			throw new IllegalArgumentException("One or More Arguments are NULL.\n");
		return new Record(wild, entity, relation, property);
	}

	// Comparators that compare Records based on different orderings of
	// their fields. The names of the Comparators correspond to the
	// order in which they compare fields: ERPCompare compares Entity
	// (E), then Relation (R), then property (P). Likewise for
	// RPECompare and PER compare.
	public static final Comparator<Record> ERPCompare = new Comparator<Record>() {
		
		@Override
		public int compare(Record x, Record y) {
			// Compare Entity
			if ( x.entity().compareTo(y.entity()) == 0 ) {
				// Compare Relation
				if ( x.relation().compareTo(y.relation()) == 0 ) {
					// Compare Property
					if ( x.property().compareTo(y.property()) == 0 ) {
						return 0;
					} else {
						return x.property().compareTo(y.property());
					}
				} else {
					return x.relation().compareTo(y.relation());
				}
			} else {
				return x.entity().compareTo(y.entity());
			}
		}
	};

	public static final Comparator<Record> RPECompare = new Comparator<Record>() {
		
		@Override
		public int compare(Record x, Record y) {
			// Compare Relation
			if ( x.relation().compareTo(y.relation()) == 0 ) {
				// Compare Property
				if ( x.property().compareTo(y.property()) == 0 ) {
					// Compare Entity
					if ( x.entity().compareTo(y.entity()) == 0 ) {
						return 0;
					} else {
						return x.entity().compareTo(y.entity());
					}
				} else {
					return x.property().compareTo(y.property());
				}
			} else {
				return x.relation().compareTo(y.relation());
			}
		}
	};

	public static final Comparator<Record> PERCompare = new Comparator<Record>() {
		
		@Override
		public int compare(Record x, Record y) {
			// Compare Property
			if ( x.property().compareTo(y.property()) == 0 ) {
				// Compare Entity
				if ( x.entity().compareTo(y.entity()) == 0 ) {
					// Compare Relation
					if ( x.relation().compareTo(y.relation()) == 0 ) {
						return 0;
					} else {
						return x.relation().compareTo(y.relation());
					}
				} else {
					return x.entity().compareTo(y.entity());
				}
			} else {
				return x.property().compareTo(y.property());
			}
		}
	};
	
}