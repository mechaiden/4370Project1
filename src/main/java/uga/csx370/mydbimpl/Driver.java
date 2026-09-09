/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 *
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package uga.csx370.mydbimpl;

import java.util.List;

import uga.csx370.mydb.Relation;
import uga.csx370.mydb.RelationBuilder;
import uga.csx370.mydb.Predicate;
import uga.csx370.mydb.Type;
import uga.csx370.mydb.Cell;
import uga.csx370.mydb.RA;

public class Driver {

    public static void main(String[] args) {
        // Following is an example of how to use the relation class.
        // This creates a table with three columns with below mentioned
        // column names and data types.
        // After creating the table, data is loaded from a CSV file.
        // Path should be replaced with a correct file path for a compatible
        // CSV file.
        /*
        Relation rel1 = new RelationBuilder()
                .attributeNames(List.of("Col01_Name", "Col02_Name", "Col03_Name"))
                .attributeTypes(List.of(Type.INTEGER, Type.STRING, Type.DOUBLE))
                .build();
        */

        /*
        Relation rel2 = new RelationBuilder()
                .attributeNames(List.of("ID", "Name", "Dept_Name", "Salary"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        rel2.loadData("data/instructor_export.csv");
        rel2.print();
        */

        Relation rel3 = new RelationBuilder()
                .attributeNames(List.of("course_id", "title", "dept_name", "credits"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.INTEGER))
                .build();
        rel3.loadData("data/course_export.csv");
        rel3.print();


        //Select -------------------------------------------------------
        Predicate deptPhysics = row -> {
          // Row is a list of cells, so getting an index gets the specific cell value
          int col = rel3.getAttrIndex("dept_name");
          Cell deptCell = row.get(col);
          String dept = deptCell.getAsString();
          // Return equality value
          return dept.equals("Physics");
        };

        // Needed instance of RA
        RA ra = new RAImpl();
        Relation physicsCourses = ra.select(rel3, deptPhysics);
        physicsCourses.print();

        //Project -------------------------------------------------------
        List<String> attrs = List.of("course_id", "title", "dept_name");
        Relation projectTest = ra.project(rel3, attrs);
        projectTest.print();

        // UNION TEST
        Predicate deptCybernetics = row -> {
            int col = rel3.getAttrIndex("dept_name");
            Cell deptCell = row.get(col);
            String dept = deptCell.getAsString();
            return dept.equals("Cybernetics");
        };
        Relation cyberneticsCourses = ra.select(rel3, deptCybernetics);
        cyberneticsCourses.print();
        Relation unionCourses = ra.union(physicsCourses, cyberneticsCourses);
        unionCourses.print();
    }

}
