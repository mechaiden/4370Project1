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
      // -------------------------------------Relation Builders Below--------------------------------------------
      Relation instructor = new RelationBuilder()
              .attributeNames(List.of("ID", "name", "dept_name", "salary"))
              .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.DOUBLE))
              .build();
      instructor.loadData("data/instructor_export.csv");

      Relation course = new RelationBuilder()
              .attributeNames(List.of("course_id", "title", "dept_name", "credits"))
              .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.INTEGER))
              .build();
      course.loadData("data/course_export.csv");

      Relation student = new RelationBuilder()
              .attributeNames(List.of("ID","name", "dept_name", "tot_cred"))
              .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.INTEGER))
              .build();
      student.loadData("data/student_export.csv");

      Relation advisor = new RelationBuilder()
              .attributeNames(List.of("s_ID", "i_ID"))
              .attributeTypes(List.of(Type.STRING, Type.STRING))
              .build();
      advisor.loadData("data/advisor_export.csv");

      Relation section = new RelationBuilder()
              .attributeNames(List.of("course_id","sec_id", "semester", "year", "building", "room_number", "time_slot_id"))
              .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.INTEGER, Type.STRING, Type.STRING, Type.STRING))
              .build();
      section.loadData("data/section_export.csv");

      Relation department = new RelationBuilder()
              .attributeNames(List.of("dept_name","building", "budget"))
              .attributeTypes(List.of(Type.STRING, Type.STRING, Type.DOUBLE))
              .build();
      department.loadData("data/department_export.csv");

      Relation teaches = new RelationBuilder()
              .attributeNames(List.of("ID", "course_id", "sec_id", "semester", "year"))
              .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.STRING, Type.INTEGER))
              .build();
      teaches.loadData("data/teaches_export.csv");


      Relation takes = new RelationBuilder()
	      .attributeNames(List.of("ID", "course_id", "sec_id", "semester", "year", "grade"))
	      .attributeTypes(List.of(Type.STRING, Type.STRING, Type.STRING, Type.STRING, Type.DOUBLE, Type.STRING))
	      .build();
      takes.loadData("data/takes_export.csv");

      // -----------------------------------Assignment Queries Below-------------------------------------------
      /*
       * FORMAT
       * Print the output of the query
       * Then print a description of what it is. Similar to in class: All professors who teach students who .....
       * Then print the relational algebra formulation for it
       */
      RA ra = new RAImpl();
      AIDENSAWESOMEQUERYNUMBER1(student,  instructor,  advisor, ra);
      AIDENSAWESOMEQUERYNUMBER2(instructor, teaches, section, department, ra, course);

      WyattQuery(section, department, teaches, instructor, ra);



      matthewsMarvelousMechanicalQuery(student, takes, course, advisor, instructor, ra);








      /*
      -------------------DEVELOPEMENT TESTING BELOW---------------------------------------------
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
      */
  }

  public static void AIDENSAWESOMEQUERYNUMBER1(Relation student, Relation instructor, Relation advisor, RA ra) {
      Relation studentR = ra.rename(student,
              List.of("ID", "name", "dept_name", "tot_cred"),
              List.of("s_ID", "s_name", "s_dept", "tot_cred")
      );

      Relation instructorR = ra.rename(instructor,
              List.of("ID", "name", "dept_name", "salary"),
              List.of("i_ID", "i_name", "i_dept", "salary")
      );

      int deptCol = instructorR.getAttrIndex("i_dept");
      Relation physicsInstructor = ra.select(instructorR, row -> row.get(deptCol).getAsString().equals("Physics"));

      int credCol = studentR.getAttrIndex("tot_cred");
      Relation seniorStudent = ra.select(studentR, row -> row.get(credCol).getAsInt() >= 100);

      Relation physicsPair = ra.join(advisor, physicsInstructor);  //creates a match based on instuctor ID
      Relation joined = ra.join(physicsPair, seniorStudent); // matches the student ID
      Relation result = ra.project(joined, List.of("s_name", "tot_cred", "i_name"));

      System.out.println("Query: Students with more than 100 credits who are advised by a phyiscs instructor.");
      result.print();
      System.out.println("Rows: " + result.getSize());

  }
  public static void AIDENSAWESOMEQUERYNUMBER2(Relation instructor, Relation teaches, Relation section, Relation department, RA ra, Relation course) {
        int budgetCol = department.getAttrIndex("budget");
        Relation cheapDepts = ra.select(department, row -> row.get(budgetCol).getAsDouble() < 300000);

        Relation instNoDept = ra.project(instructor, List.of("ID", "name", "salary"));
        Relation courseTitles = ra.project(course, List.of("course_id", "title"));

        Relation instTeaches = ra.join(instNoDept, teaches);
        Relation withSection = ra.join(instTeaches, section);
        Relation withDept = ra.join(withSection, cheapDepts);
        Relation joined = ra.join(withDept, courseTitles);

        Relation result = ra.project(joined,
                List.of("name", "title", "dept_name", "building", "budget"));

        System.out.println("Query: Instructors who teach a course in a building belonging to "
                + "a department with a budget under $300,000.");
        result.print();
        System.out.println("Rows: " + result.getSize());
    }

  public static void WyattQuery(Relation section, Relation department, Relation teaches, Relation instructor, RA ra) {
    Relation sectionsInDeptBuilding = ra.join(section, department);
    Predicate fall2010 = row -> {
      int yearIndex = sectionsInDeptBuilding.getAttrIndex("year");
      int semesterIndex = sectionsInDeptBuilding.getAttrIndex("semester");
      Cell yearCell = row.get(yearIndex);
      Cell semesterCell = row.get(semesterIndex);
      int year = yearCell.getAsInt();
      String semester = semesterCell.getAsString();
      return year == 2010 && semester.equals("Fall");
    };
    Relation sectionsInDeptBuildingFall2010 = ra.select(sectionsInDeptBuilding, fall2010);
    sectionsInDeptBuildingFall2010 = ra.project(
            sectionsInDeptBuildingFall2010,
            List.of("course_id", "sec_id", "year", "semester")
    );
    //sectionsInDeptBuildingFall2010.print();
    Relation teachesIds = ra.join(sectionsInDeptBuildingFall2010, teaches);
    Relation result = ra.join(teachesIds, instructor);
    result = ra.project(
            result,
            List.of("ID", "name")
    );
    System.out.println("\nQuery: All teachers who taught a course in a department's building in the Fall Semester of 2010.");
    result.print();
    System.out.println("Rows: " + result.getSize());
  }


  public static void matthewsMarvelousMechanicalQuery(Relation student, Relation takes, Relation course, Relation advisor, Relation instructor, RA ra) {

	Predicate ch = row -> {
		int credCol = student.getAttrIndex("tot_cred");
		return row.get(credCol).getAsInt() == 30;
	};
	Relation studentch = ra.select(student, ch);
	Predicate studentAdvised = row -> {
		int studentID = studentch.getAttrIndex("ID");
		int advisorSID = advisor.getAttrIndex("s_ID");
		return row.get(studentID).equals(row.get(advisorSID));
	};



	Relation advisors = ra.join(advisor, studentch, studentAdvised);
	Relation advisorIDs = ra.project(advisors, List.of("i_ID"));
	advisorIDs = ra.rename(advisorIDs, List.of("i_ID"),List.of("ID"));
	Relation result = ra.join(advisorIDs, instructor);
	result = ra.project(result, List.of("ID", "name"));
	System.out.println("Query: Name and ID of advisors who advise a student that has exactly 30 credit hours.");
	result.print();

    	System.out.println("Rows: " + result.getSize());


  }
}
