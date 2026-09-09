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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'intersect'");
    }

    @Override
    public Relation diff(Relation rel1, Relation rel2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'diff'");
    }

    @Override
    public Relation rename(Relation rel, List<String> origAttr, List<String> renamedAttr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rename'");
    }

    @Override
    public Relation cartesianProduct(Relation rel1, Relation rel2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cartesianProduct'");
    }

    @Override
    public Relation join(Relation rel1, Relation rel2) {
      //Natural Join
      // In case of multiple common columns, compare all matching columns
      // This means we need to use a list for matching columns
      // Then use a for loop based on the size of that list to compare matching columns

      // This gets minimum length, shorts and longest relationships
      // This makes the following for loop more efficient
      // If one table has 100 cols and the other has 1, this will make for loop only run once
      int minLength;
      Relation shortRel;
      Relation longRel;
      if (rel1.getAttrs().size() < rel2.getAttrs().size()) {
        minLength = rel1.getAttrs().size();
        shortRel = rel1;
        longRel = rel2;
      } else {
        minLength = rel2.getAttrs().size();
        shortRel = rel2;
        longRel = rel1;
      }
      // Above could be simplified by making an array w/ 2 elements
      // Make the first element the shorter relation and the second the long relation
      // But this decreases readability and the space saving is negligable b/c dealing with ptrs

      // Each element is a 2 element array [shortIndex, longIndex]
      // The first element of the array is the index of matching col in shortRel
      // The second element is the index of the matching col in longRel
      List<int[]> mapping = new ArrayList<int[]>();
      for (int i = 0; i < minLength; i++) {
        if (longRel.getAttrs().contains(shortRel.getAttrs().get(i))) {
          // Col i name in shortAttr is in longAttr
          int longRelIndex = longRel.getAttrs().indexOf(shortRel.getAttrs().get(i));
          if (longRelIndex >= 0) {
            // Col i type and name in shortRel match col in longRel
            // This works because i is the index of matching col in shortRel
            // And longRelIndex is the index of matching col in longRel
            mapping.add(new int[] {i, longRelIndex});
          }
        }
      }
      System.out.println("Mapping: ");
      for (int i = 0; i < mapping.size(); i++) {
        System.out.print("" + i + ": ");
        System.out.print(mapping.get(i)[0] + ", ");
        System.out.println(mapping.get(i)[1]);
      }


        throw new UnsupportedOperationException("Unimplemented method 'join'");
    }

    @Override
    public Relation join(Relation rel1, Relation rel2, Predicate p) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'join'");
    }

}
