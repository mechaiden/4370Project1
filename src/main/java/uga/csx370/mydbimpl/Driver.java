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

      Relation rel2 = new RelationBuilder()
              .attributeNames(List.of("id", "name", "dept_name", "salary"))
              .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.DOUBLE))
              .build();
      rel2.loadData("data/instructor_export.csv");
      // rel2.print();

      Relation rel3 = new RelationBuilder()
              .attributeNames(List.of("course_id", "title", "dept_name", "credits"))
              .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.INTEGER))
              .build();
      rel3.loadData("data/course_export.csv");
      // rel3.print();


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

      // Project -------------------------------------------------------
      List<String> attrs = List.of("course_id", "title", "dept_name");
      Relation projectTest = ra.project(rel3, attrs);
      projectTest.print();

      // Union ----------------------------------------------------------
      Predicate deptCybernetics = row -> {
          int col = rel3.getAttrIndex("dept_name");
          Cell deptCell = row.get(col);
          String dept = deptCell.getAsString();
          return dept.equals("Cybernetics");
      };
      Relation cyberneticsCourses = ra.select(rel3, deptCybernetics);
      // cyberneticsCourses.print();
      Relation unionCourses = ra.union(physicsCourses, cyberneticsCourses);
      //unionCourses.print();
      
      // Intersect --------------------------------------------
      //make a two different selects on a db, then intersect them
      Predicate salary = row -> {
        int col = rel2.getAttrIndex("salary");
        return row.get(col).getAsDouble() > 50000;
      };
      Predicate highSalary = row -> {
        int col = rel2.getAttrIndex("salary");
        return row.get(col).getAsDouble() > 80000;
      };

      Relation sal = ra.select(rel2, salary);
      Relation highSal = ra.select(rel2, highSalary);

      Relation intersection = ra.intersect(sal, highSal);

      System.out.println("salary:");
      sal.print();

      System.out.println("Higher salary:");
      highSal.print();

      System.out.println("Intersection:");
      intersection.print();
      System.out.println(sal.getSize());
      System.out.println(highSal.getSize());
            System.out.println(intersection.equals(highSal));
      // Rename -------------------------------------
      List<String> oldNames = rel2.getAttrs();
      List<String> newNames = List.of("newCourseID", "newTitle", "newDept_name", "new_credits");
      Relation newName = ra.rename(rel2, oldNames, newNames);
      newName.print();	
      
      // Natural Join --------------------------------------------------
      Relation join23 = ra.join(rel2, rel3);
      join23.print();

      // Cartesian Product ---------------------------------------------
      Relation coursesNoDept = ra.project(rel3, List.of("course_id", "title", "credits"));
      Relation cart23 = ra.cartesianProduct(rel2, coursesNoDept);

      cart23.print();


      //------------------------------------------------ set difference test --------------------------------------------
      List<String> deptOnly = List.of("dept_name");

      Relation instructorDept = ra.project(rel2, deptOnly); 
      Relation courseDept = ra.project(rel3, deptOnly); 

      //TEST 1: Regular use cases
      Relation deptNoCourses = ra.diff(instructorDept, courseDept);
      System.out.println("\n\nInstructor's departments NOT offering courses:");
      deptNoCourses.print();
      System.out.println("Size: " + deptNoCourses.getSize());

      Relation deptNoInstructors = ra.diff(courseDept, instructorDept);
      System.out.println("Course's deptartments with NO instructors:");
      deptNoInstructors.print();
      System.out.println("Size: " + deptNoInstructors.getSize());    

      //TEST 2: all matches -> empty relation outputted
      Relation matchDept = ra.diff(instructorDept, instructorDept);
      System.out.println("Diff on itself: " + matchDept.getSize());
      
      //TEST 3: no matches -> everything gets outputted
      Relation emptyDept = new RelationBuilder()
              .attributeNames(List.of("dept_name"))
              .attributeTypes(List.of(Type.STRING))
              .build();
      emptyDept.insert(List.of(Cell.val("Potassium_Pulverizer")));

      Relation noOverlapDiff = ra.diff(instructorDept, emptyDept);
      System.out.println("Diff no overlap: " + noOverlapDiff.getSize() + "\nOriginal: " + instructorDept.getSize());

      //TEST 4: More regular use cases: not physics
      System.out.println("rel3 - deptPhysics:");
      Relation noPhysics = ra.diff(rel3, physicsCourses);
      noPhysics.print();

      //------------------------------------------------ theta join test -----------------------------------------------

      System.out.println("\n");
      //dept_name is in both

      //TEST 1: make sure join works normally
      Predicate testEverything = row -> true;
      Relation instructorAndCourse = ra.join(rel2, rel3, testEverything);
      System.out.println("Instructor size: " + rel2.getSize());
      System.out.println("Course size: " + rel3.getSize());
      System.out.println("Instructor x Course Size: " + instructorAndCourse.getSize());

      //TEST 2: Predicate makes theta join act like natural join
      Predicate sameDept = row -> {
              String instructorDepartment = row.get(2).getAsString(); // dept_name in rel1
              String courseDepartment = row.get(6).getAsString(); // dept_name in rel2
              return instructorDepartment.equals(courseDepartment);
      };

      Relation join23Theta = ra.join(rel2, rel3, sameDept);
      System.out.println("Theta join size: " + join23Theta.getSize());
      System.out.println("Natural join size: " + join23.getSize());
      System.out.println("Are they the same?: " + (join23Theta.getSize() == join23.getSize()));

      //TEST 3: A normal, reasonable predicate
      Predicate rich4Credit = row -> {
              double instSalary = row.get(3).getAsDouble();
              int courseCredits = row.get(7).getAsInt();
              return instSalary > 120000 && courseCredits >= 4;
      };

      Relation joinRich4Credit = ra.join(rel2, rel3, rich4Credit);
      System.out.println("Salary > 100,000 AND credits >= 4, size: " + joinRich4Credit.getSize());
      joinRich4Credit.print();

      //TEST 4: Nothing matches the predicate
      Predicate impossible = row -> false;
      Relation emptyJoin = ra.join(rel2, rel3, impossible);
      System.out.println("Theta join with impossible predicate: " + emptyJoin.getSize());
      emptyJoin.print();
  }
}	
