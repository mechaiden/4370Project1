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
        // TODO Auto-generated method stub
        // Testing branch status
        throw new UnsupportedOperationException("Unimplemented method 'join'");
    }

    @Override
    public Relation join(Relation rel1, Relation rel2, Predicate p) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'join'");
    }

}
