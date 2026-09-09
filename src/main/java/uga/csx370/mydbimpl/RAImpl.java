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
      // Need to get type of each column.
      // So need to find index of each attr in original relation
      // Then copy the types of those columns to the new types list
      List<Type> newTypes = new ArrayList<Type>();
      List<Type> oldTypes = rel.getTypes();
      List<String> oldAttrs = rel.getAttrs();
      int[] neededCols = new int[attrs.size()];
      // This loop looks at each attribute in attrs and adds the corresponding type to newTypes
      for (int i = 0; i < attrs.size(); i++) {
        int index = oldAttrs.indexOf(attrs.get(i));
        if (index > 0) {
          newTypes.add(oldTypes.get(index));
        } 
      }
      Relation result = new RelationBuilder()
              .attributeNames(attrs)
              .attributeTypes(newTypes)
              .build();
      // Now need to copy over each row, removing the un-needed columns
      // Easier to remove un-needed or add needed?
      // Probably remove b/c then I don't need to construct whole new rows
      // Probably want a list of indeces I need, can create in previous for loop
      // What about .removeAll(collection) I don't think this will work because each element is a Cell
      for (int i = 0; i < rel.getSize(); i++) {
        List<Cell> row = rel.getRow(i);
        //for(int k = 0; k < )
        //Testing for push
      }



        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'project'");
    }

    @Override
    public Relation union(Relation rel1, Relation rel2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'union'");
    }

    @Override
    public Relation intersect(Relation rel1, Relation rel2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'intersect'");
    }

    @Override
    public Relation diff(Relation rel1, Relation rel2) {
        // TODO Auto-generated method stub
        // Boris will do this method.
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
        throw new UnsupportedOperationException("Unimplemented method 'join'");
    }

    @Override
    public Relation join(Relation rel1, Relation rel2, Predicate p) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'join'");
    }

}
