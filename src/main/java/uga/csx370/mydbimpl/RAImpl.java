package uga.csx370.mydbimpl;

import java.util.List;
import java.util.ArrayList;

import uga.csx370.mydb.Predicate;
import uga.csx370.mydb.Type;
import uga.csx370.mydb.RA;
import uga.csx370.mydb.Relation;
import uga.csx370.mydb.RelationBuilder;
import uga.csx370.mydb.Cell;

public class RAImpl implements RA {

    @Override
    public Relation select(Relation rel, Predicate p) {
        Relation result = new RelationBuilder()
                .attributeNames(rel.getAttrs())
                .attributeTypes(rel.getTypes())
                .build();
        for (int i = 0; i < rel.getSize(); i++) {
          if (p.check(rel.getRow(i))) {
            result.insert(rel.getRow(i));
          }
        }
        return result;
        //throw new UnsupportedOperationException("Unimplemented method 'project'");
    }

    @Override
    public Relation project(Relation rel, List<String> attrs) {
      List<Type> newTypes = new ArrayList<Type>();
      List<Type> oldTypes = rel.getTypes();
      List<String> oldAttrs = rel.getAttrs();
      int[] neededCols = new int[attrs.size()];
      // This loop looks at each attribute in attrs and adds the corresponding type to newTypes
      System.out.println(oldAttrs);
      System.out.println(attrs);
      for (int i = 0; i < attrs.size(); i++) {
        int index = oldAttrs.indexOf(attrs.get(i));
        System.out.println("Curr attr: " + attrs.get(i));
        if (index >= 0) {
          System.out.println("Index: " + index);
          newTypes.add(oldTypes.get(index));
          neededCols[i] = index;
        } 
      }
      Relation result = new RelationBuilder()
              .attributeNames(attrs)
              .attributeTypes(newTypes)
              .build();

      for (int i = 0; i < rel.getSize(); i++) {
        List<Cell> row = rel.getRow(i);
        List<Cell> newRow = new ArrayList<Cell>();
        for(int k = 0; k < attrs.size(); k++) {
          newRow.add(row.get(neededCols[k]));
        }
        result.insert(newRow);
      }
      return result;
    }

    @Override
    public Relation union(Relation rel1, Relation rel2) {
        // TODO Auto-generated method stub
        rel1.getTypes().equals(rel2.getTypes());

        if (!rel1.getTypes().equals(rel2.getTypes())) {
            throw new IllegalArgumentException("Relations cannot be unioned");
        }

        Relation result = new RelationBuilder()
            .attributeNames(rel1.getAttrs())
            .attributeTypes(rel1.getTypes())
            .build();

        List<List<Cell>> seen = new ArrayList<>();

        for (int i = 0; i < rel1.getSize(); i++) {
            List<Cell> row = rel1.getRow(i);
            if (!seen.contains(row)) {
                seen.add(row);
                result.insert(row);
            }
        }

        for (int i = 0; i < rel2.getSize(); i++) {
            List<Cell> row = rel2.getRow(i);
            if (!seen.contains(row)) {
                seen.add(row);
                result.insert(row);
            }
        }

        return result;



    }

    @Override
    public Relation intersect(Relation rel1, Relation rel2) {
	// Error handling:
	// 	Make sure same number of attributes
	// 	Make sure attributes have compatible types.
	//	
//	if (rel1.getSize() != rel2.getSize()) {
//		throw new IllegalArgumentException("Relations cannot be intersected; Different number of rows");
//	}
	if (!rel1.getTypes().equals(rel2.getTypes())) {
		throw new IllegalArgumentException("Relations cannot be intersected; Attribute Types do not match.");
	}


	//Implementation:
	//result = new empty relation,
	//
	//loop through each row in R1
	//	compare each row R1  to each row in R2,
	//	if R1 and R2 are equal, add it to result
	//return result 
	 Relation result = new RelationBuilder()
            .attributeNames(rel1.getAttrs())
            .attributeTypes(rel1.getTypes())
            .build();	
	
	 for (int i = 0; i < rel1.getSize(); i++) {
		List<Cell> rowR1 = rel1.getRow(i);
	
		for (int j = 0; j < rel2.getSize(); j++) {
			List<Cell> rowR2 = rel2.getRow(j);	
			if (rowR1.equals(rowR2)) {
				result.insert(rowR1);
			}
		}
	}

    	return result;
    }

    @Override
    public Relation diff(Relation rel1, Relation rel2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'diff'");
    }

    @Override
    public Relation rename(Relation rel, List<String> origAttr, List<String> renamedAttr) {
	
	    //Throw IllegalArgument if any attr in orgAttr is not pressent in re1.
	    for (int i = 0; i < origAttr.size(); i++) {
		    if (!rel.getAttrs().contains(origAttr.get(i))){
			    throw new IllegalArgumentException("Attribute in origAttr not pressent in relation.");
		    }
	    }

 			
	    //Create a new relation with new name, and new attr names
	   //copy every row from the original relation.
	   Relation newRelation = new RelationBuilder()
		   .attributeNames(renamedAttr)
		   .attributeTypes(rel.getTypes())
		   .build();
	
	   for (int i = 0; i < rel.getSize(); i++) {
		   newRelation.insert(rel.getRow(i));
	   }
	   return newRelation;	    
    }
    @Override
    public Relation cartesianProduct(Relation rel1, Relation rel2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cartesianProduct'");
    }

    @Override
    public Relation join(Relation rel1, Relation rel2) {
      // This gets minimum length, thinest and widest relationships
      // This makes the following for loop more efficient
      // If one table has 100 cols and the other has 1, this will make for loop only run once
      int minWidth;
      Relation thinRel;
      Relation wideRel;
      if (rel1.getAttrs().size() < rel2.getAttrs().size()) {
        minWidth = rel1.getAttrs().size();
        thinRel = rel1;
        wideRel = rel2;
      } else {
        minWidth = rel2.getAttrs().size();
        thinRel = rel2;
        wideRel = rel1;
      }

      // Each element is a 2 element array [shortIndex, longIndex]
      // The first element of the array is the index of matching col in thinRel
      // The second element is the index of the matching col in wideRel
      // Create Mappings
      List<int[]> mappings = new ArrayList<int[]>();
      List<String> matchingAttrs = new ArrayList<String>();
      List<Type> matchingTypes = new ArrayList<Type>();
      for (int i = 0; i < minWidth; i++) {
        String attr = thinRel.getAttrs().get(i);
        if (wideRel.getAttrs().contains(attr)) {
          int wideRelIndex = wideRel.getAttrs().indexOf(attr);
          Type type = thinRel.getTypes().get(i);
          if (wideRel.getTypes().get(wideRelIndex).equals(type)) {
            mappings.add(new int[] {i, wideRelIndex});
            matchingAttrs.add(attr);
            matchingTypes.add(type);
          }
        }
      }

      List<String> attrs = new ArrayList<>(matchingAttrs);
      List<Type> types = new ArrayList<>(matchingTypes);
      // Get attrs and types for result relation
      for (int i = 0; i < minWidth; i++) {
        String attr = thinRel.getAttrs().get(i);
        Type type = thinRel.getTypes().get(i);
        if (!attrs.contains(attr)) {
          attrs.add(attr);
          types.add(type);
        }
      }
      for (int i = 0; i < wideRel.getAttrs().size(); i++) {
        String attr = wideRel.getAttrs().get(i);
        Type type = wideRel.getTypes().get(i);
        if (!attrs.contains(attr)) {
          attrs.add(attr);
          types.add(type);
        }
      }

      // Order of attrs here is:
      // Matching Cols, thinRel Cols, wideRelCols
      Relation result = new RelationBuilder()
              .attributeNames(attrs)
              .attributeTypes(types)
              .build();

      // Could use some sort of hash function but I don't know how to do that
      // This has complexity o(nmk) 
      // where n and m are the # of rows in each relation and k is # of matching columns
      boolean matchingRow = false;
      for (int i = 0; i < thinRel.getSize(); i++) {
        for (int k = 0; k < wideRel.getSize(); k++) {
          matchingRow = true;
          for (int[] map : mappings) {
            if (!thinRel.getRow(i).get(map[0]).equals(wideRel.getRow(k).get(map[1]))) {
              // Cannot build row here because all mappings must match to join
              matchingRow = false;
              break;
            }
          }
          if (matchingRow) {
            List<Cell> newRow = new ArrayList<Cell>();
            for (int[] map : mappings) {
              newRow.add(thinRel.getRow(i).get(map[0]));
            }
            for (int j = 0; j < thinRel.getAttrs().size(); j++) {
              if (!matchingAttrs.contains(thinRel.getAttrs().get(j))) {
                newRow.add(thinRel.getRow(i).get(j));
              }
            }
            for (int j = 0; j < wideRel.getAttrs().size(); j++) {
              if (!matchingAttrs.contains(wideRel.getAttrs().get(j))) {
                newRow.add(wideRel.getRow(k).get(j));
              }
            }
            result.insert(newRow);
          }
        }
      }
      return result;
    }

    @Override
    public Relation join(Relation rel1, Relation rel2, Predicate p) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'join'");
    }

}
