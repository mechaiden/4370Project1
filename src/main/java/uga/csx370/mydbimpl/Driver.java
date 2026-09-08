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
import uga.csx370.mydb.Type;

public class Driver {

    public static void main(String[] args) {
        // Following is an example of how to use the relation class.
        // This creates a table with three columns with below mentioned
        // column names and data types.
        // After creating the table, data is loaded from a CSV file.
        // Path should be replaced with a correct file path for a compatible
        // CSV file.
        Relation rel1 = new RelationBuilder()
                .attributeNames(List.of("Col01_Name", "Col02_Name", "Col03_Name"))
                .attributeTypes(List.of(Type.INTEGER, Type.STRING, Type.DOUBLE))
                .build();

        Relation rel2 = new RelationBuilder()
                .attributeNames(List.of("ID", "Name", "Dept_Name", "Salary"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        Relation rel3 = new RelationBuilder()
                .attributeNames(List.of("course_id", "title", "dept_name", "credits"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.INTEGER))
                .build();
        rel2.loadData("/Users/aidengoff23/sql_output/mysql-files/instructor_export.csv");
        rel3.loadData("/Users/aidengoff23/sql_output/mysql-files/course_export.csv");
        System.out.println("awg43376");
        rel2.print();
        System.out.println("awg43376");
        rel3.print();
    }

}
