import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

// Three-column database that supports query, add, and remove in
// logarithmic time.
public class TripleStore {
	TreeSet<Record> tripleStore_ERP;
	TreeSet<Record> tripleStore_RPE;
	TreeSet<Record> tripleStore_PER;
	String wild;
	// Create an empty TripleStore. Initializes storage trees
	public TripleStore() {
		tripleStore_ERP = new TreeSet<>(Record.ERPCompare);
		tripleStore_RPE = new TreeSet<>(Record.RPECompare);
		tripleStore_PER = new TreeSet<>(Record.PERCompare);
		wild = "*";
	}

	// Access the current wild card string for this TripleStore which
	// may be used to match multiple records during a query() or
	// remove() calll
	public String getWild() {
		return wild;
	}

	// Set the current wild card string for this TripleStore
	public void setWild(String w) {
		wild = w;
	}

	// Ensure that a record is present in the TripleStore by adding it
	// if necessary. Returns true if the addition is made, false if the
	// Record was not added because it was a duplicate of an existing
	// entry. A Record with any fields may be added to the TripleStore
	// including a Record with fields that are equal to the
	// TripleStore's current wild card. Throws an
	// IllegalArgumentException if any argument is null.
	//
	// Target Complexity: O(log N)
	// N: number of records in the TripleStore
	public boolean add(String entity, String relation, String property) {
		if (entity == null || relation == null || property == null)
			throw new IllegalArgumentException("One or More Arguments are NULL.\n");
		
		Record newRecord = Record.makeRecord(entity, relation, property);
		if (tripleStore_ERP.contains(newRecord))
			return false;
		else {
			tripleStore_ERP.add(newRecord);
			tripleStore_RPE.add(newRecord);
			tripleStore_PER.add(newRecord);
			return true;
		}
	}

	// Return a List of the Records that match the given query. If no
	// Records match, the returned list should be empty. If a String
	// matching the TripleStore's current wild card is used for one of
	// the fields of the query, multiple Records may be returned in the
	// match. An appropriate tree must be selected and searched
	// correctly in order to meet the target complexity. Throws an
	// IllegalArgumentException if any argument is null.
	//
	// TARGET COMPLEXITY: O(K + log N)
	// K: the number of matching records
	// N: the number of records in the triplestore.
	public List<Record> query(String entity, String relation, String property) {
		List<Record> queryResult = new ArrayList<Record>();
		if (entity == null || relation == null || property == null)
			throw new IllegalArgumentException("One or More Arguments are NULL.\n");
		
		Record queryRecord = Record.makeQuery(getWild(), entity, relation, property);
		
		if ( entity.equals(getWild()) && relation.equals(getWild()) && property.equals(getWild()) ) {
			// All wild. Query("", "*","*","*")
			queryResult.addAll(tripleStore_ERP);
			
		} else if ( !entity.equals(getWild()) && relation.equals(getWild()) && property.equals(getWild()) ) {
			// All wild But Entity. Query("", "Entity","*","*")
			Record recordMatch = null;
			Iterator<Record> iterator = tripleStore_ERP.iterator();
			while (iterator.hasNext()){
				recordMatch = iterator.next();
				if (queryRecord.matches(recordMatch))
					break;
			}
			Iterator<Record> iterator_E = tripleStore_ERP.tailSet(recordMatch, true).iterator();
			while (iterator_E.hasNext()){
				Record record = iterator_E.next();
				if (queryRecord.matches(record))
					queryResult.add(record);
			}
			
		} else if ( entity.equals(getWild()) && !relation.equals(getWild()) && property.equals(getWild()) ) {
			// All wild But Relation. Query("", "*","Relation","*")
			Record recordMatch = null;
			Iterator<Record> iterator = tripleStore_RPE.iterator();
			while (iterator.hasNext()){
				recordMatch = iterator.next();
				if (queryRecord.matches(recordMatch))
					break;
			}
			Iterator<Record> iterator_E = tripleStore_RPE.tailSet(recordMatch, true).iterator();
			while (iterator_E.hasNext()){
				Record record = iterator_E.next();
				if (queryRecord.matches(record))
					queryResult.add(record);
			}
			
		} else if ( entity.equals(getWild()) && relation.equals(getWild()) && !property.equals(getWild()) ) {
			// All wild But Property. Query("", "*","*","Property")
			Record recordMatch = null;
			Iterator<Record> iterator = tripleStore_PER.iterator();
			while (iterator.hasNext()){
				recordMatch = iterator.next();
				if (queryRecord.matches(recordMatch))
					break;
			}
			Iterator<Record> iterator_E = tripleStore_PER.tailSet(recordMatch, true).iterator();
			while (iterator_E.hasNext()){
				Record record = iterator_E.next();
				if (queryRecord.matches(record))
					queryResult.add(record);
			}
			
		} else if ( !entity.equals(getWild()) && !relation.equals(getWild()) && property.equals(getWild()) 
				||  !entity.equals(getWild()) && relation.equals(getWild()) && !property.equals(getWild()) ) {
			// Property is wild. Query("", "Entity","Relation","*")
			// Relation is wild. Query("", "Entity","*","Property")
			Record recordMatch = null;
			Iterator<Record> iterator = tripleStore_ERP.iterator();
			while (iterator.hasNext()){
				recordMatch = iterator.next();
				if (queryRecord.matches(recordMatch))
					break;
			}
			Iterator<Record> iterator_E = tripleStore_ERP.tailSet(recordMatch, true).iterator();
			while (iterator_E.hasNext()){
				Record record = iterator_E.next();
				if (queryRecord.matches(record))
					queryResult.add(record);
			}
		} else if ( !entity.equals(getWild()) && !relation.equals(getWild()) && property.equals(getWild()) 
				||  !entity.equals(getWild()) && relation.equals(getWild()) && !property.equals(getWild()) ) {
			// Property is wild. Query("", "Entity","Relation","*")
			// Relation is wild. Query("", "Entity","*","Property")
			Record recordMatch = null;
			Iterator<Record> iterator = tripleStore_ERP.iterator();
			while (iterator.hasNext()){
				recordMatch = iterator.next();
				if (queryRecord.matches(recordMatch))
					break;
			}
			Iterator<Record> iterator_E = tripleStore_ERP.tailSet(recordMatch, true).iterator();
			while (iterator_E.hasNext()){
				Record record = iterator_E.next();
				if (queryRecord.matches(record))
					queryResult.add(record);
			}
		} else if ( entity.equals(getWild()) && !relation.equals(getWild()) && !property.equals(getWild()) 
				||  !entity.equals(getWild()) && !relation.equals(getWild()) && property.equals(getWild()) ) {
			// Entity is wild. Query("", "*","Relation","Property")
			// Property is wild. Query("", "Entity","Relation","*")
			Record recordMatch = null;
			Iterator<Record> iterator = tripleStore_RPE.iterator();
			while (iterator.hasNext()){
				recordMatch = iterator.next();
				if (queryRecord.matches(recordMatch))
					break;
			}
			Iterator<Record> iterator_E = tripleStore_RPE.tailSet(recordMatch, true).iterator();
			while (iterator_E.hasNext()){
				Record record = iterator_E.next();
				if (queryRecord.matches(record))
					queryResult.add(record);
			}
		} 
		else if ( entity.equals(getWild()) && !relation.equals(getWild()) && !property.equals(getWild()) 
				||  !entity.equals(getWild()) && relation.equals(getWild()) && !property.equals(getWild()) ) {
			// Entity is wild. Query("", "*","Relation","Property")
			// Relation is wild. Query("", "Entity","*","Property")
			Record recordMatch = null;
			Iterator<Record> iterator = tripleStore_PER.iterator();
			while (iterator.hasNext()){
				recordMatch = iterator.next();
				if (queryRecord.matches(recordMatch))
					break;
			}
			Iterator<Record> iterator_E = tripleStore_PER.tailSet(recordMatch, true).iterator();
			while (iterator_E.hasNext()){
				Record record = iterator_E.next();
				if (queryRecord.matches(record))
					queryResult.add(record);
			}
		}
		else {
			Iterator<Record> iterator_E = tripleStore_ERP.iterator();
			while (iterator_E.hasNext()){
				Record record = iterator_E.next();
				if (queryRecord.matches(record))
					queryResult.add(record);
			}
		}

		
		return queryResult;
	}

	// Remove elements from the TripleStore that match the parameter
	// query. If no Records match, no Records are removed. Any of the
	// fields given may be the TripleStore's current wild card which may
	// lead to multiple Records being matched and removed. Return the
	// number of records that are removed from the TripleStore. Throws
	// an IllegalArgumentException if any argument is null.
	//
	// TARGET COMPLEXITY: O(K * log N)
	// K: the number of matching records
	// N: the number of records in the triplestore.
	public int remove(String e, String r, String p) {
		int removeCount = 0;
		if (e == null || r == null || p == null)
			throw new IllegalArgumentException("One or More Arguments are NULL.\n");
		
		TreeSet<Record> copyTree = new TreeSet<>(tripleStore_ERP);
				
		Record queryRecord = Record.makeQuery(getWild(), e, r, p);
		Iterator<Record> iterator_ERP = copyTree.iterator();
		while (iterator_ERP.hasNext()){
			Record rc = iterator_ERP.next();
			if (rc.matches(queryRecord)) {
				tripleStore_ERP.remove(rc);
				tripleStore_RPE.remove(rc);
				tripleStore_PER.remove(rc);
				removeCount++;
			}
		}
		
		return removeCount;
	}

	// Produce a String representation of the TripleStore. Each Record
	// is formatted with its toString() method on its own line. Records
	// must be shown sorted by Entity, Relation, Property in the
	// returned String.
	//
	// TARGET COMPLEXITY: O(N)
	// N: the number of records stored in the TripleStore
	public String toString() {
		StringBuffer strToRet = new StringBuffer();
		Iterator<Record> iterator_ERP = tripleStore_ERP.iterator();
		while (iterator_ERP.hasNext()){
			strToRet.append(iterator_ERP.next() + "\n");
		}
		return strToRet.toString();
	}
}