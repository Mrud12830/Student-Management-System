import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentManagementSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ArrayList<Student> students = new ArrayList<>();

        while (true) {

            System.out.println();
			System.out.println("==========================================");
			System.out.println("       STUDENT MANAGEMENT SYSTEM");
			System.out.println("==========================================");
			System.out.println("          StudentSphere");
			System.out.println("------------------------------------------");
			System.out.println("  1.  Add Student");
			System.out.println("  2.  View All Students");
			System.out.println("  3.  Search Student");
			System.out.println("  4.  Update Student");
			System.out.println("  5.  Delete Student");
			System.out.println("  6.  Exit");
			System.out.println("------------------------------------------");
System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {


                    case 1:
					    System.out.print("Enter Student ID: ");
						int id = sc.nextInt();

						System.out.print("Enter Name: ");
						String name = sc.next();

						System.out.print("Enter Age: ");
						int age = sc.nextInt();

						System.out.print("Enter Course: ");
						String course = sc.next();

						try {
						    Connection con = DBConnection.getConnection();

						    String sql = "INSERT INTO students (id, name, age, course) VALUES (?, ?, ?, ?)";

						    PreparedStatement pst = con.prepareStatement(sql);

						    pst.setInt(1, id);
						    pst.setString(2, name);
						    pst.setInt(3, age);
						    pst.setString(4, course);

						    pst.executeUpdate();

						    System.out.println("Student added successfully!");

						    pst.close();
						    con.close();

						} catch (Exception e) {
						    e.printStackTrace();
}

    break;



					        case 2:

							    try {
							        Connection con = DBConnection.getConnection();

							        String sql = "SELECT * FROM students";

							        PreparedStatement pst = con.prepareStatement(sql);

							        ResultSet rs = pst.executeQuery();

							        System.out.println("\n===== Student List =====");

							        while (rs.next()) {
							            System.out.println("ID: " + rs.getInt("id"));
							            System.out.println("Name: " + rs.getString("name"));
							            System.out.println("Age: " + rs.getInt("age"));
							            System.out.println("Course: " + rs.getString("course"));
							            System.out.println("----------------------");
							        }

							        rs.close();
							        pst.close();
							        con.close();

							    } catch (Exception e) {
							        e.printStackTrace();
							    }

    break;

               case 3:

			       System.out.print("Enter Student ID to search: ");
			       int searchId = sc.nextInt();

			       try {
			           Connection con = DBConnection.getConnection();

			           String sql = "SELECT * FROM students WHERE id = ?";

			           PreparedStatement pst = con.prepareStatement(sql);

			           pst.setInt(1, searchId);

			           ResultSet rs = pst.executeQuery();

			           if (rs.next()) {

			               System.out.println("\n===== Student Found =====");
			               System.out.println("ID: " + rs.getInt("id"));
			               System.out.println("Name: " + rs.getString("name"));
			               System.out.println("Age: " + rs.getInt("age"));
			               System.out.println("Course: " + rs.getString("course"));

			           } else {

			               System.out.println("Student not found.");

			           }

			           rs.close();
			           pst.close();
			           con.close();

			       } catch (Exception e) {
			           System.out.println("Error while searching student.");
			           e.printStackTrace();
			       }

    break;
                case 4:

				    System.out.print("Enter Student ID to update: ");
				    int updateId = sc.nextInt();

				    System.out.print("Enter New Name: ");
				    String newName = sc.next();

				    System.out.print("Enter New Age: ");
				    int newAge = sc.nextInt();

				    System.out.print("Enter New Course: ");
				    String newCourse = sc.next();

				    try {
				        Connection con = DBConnection.getConnection();

				        String sql = "UPDATE students SET name = ?, age = ?, course = ? WHERE id = ?";

				        PreparedStatement pst = con.prepareStatement(sql);

				        pst.setString(1, newName);
				        pst.setInt(2, newAge);
				        pst.setString(3, newCourse);
				        pst.setInt(4, updateId);

				        int rows = pst.executeUpdate();

				        if (rows > 0) {
				            System.out.println("Student updated successfully!");
				        } else {
				            System.out.println("Student not found.");
				        }

				        pst.close();
				        con.close();

				    } catch (Exception e) {
				        System.out.println("Error while updating student.");
				        e.printStackTrace();
				    }

    break;

                case 5:

				    System.out.print("Enter Student ID to delete: ");
				    int deleteId = sc.nextInt();

				    try {
				        Connection con = DBConnection.getConnection();

				        String sql = "DELETE FROM students WHERE id = ?";

				        PreparedStatement pst = con.prepareStatement(sql);

				        pst.setInt(1, deleteId);

				        int rows = pst.executeUpdate();

				        if (rows > 0) {
				            System.out.println("Student deleted successfully!");
				        } else {
				            System.out.println("Student not found.");
				        }

				        pst.close();
				        con.close();

				    } catch (Exception e) {
				        System.out.println("Error while deleting student.");
				        e.printStackTrace();
				    }

    break;
                case 6:
                    System.out.println("Thank you for using the system!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}