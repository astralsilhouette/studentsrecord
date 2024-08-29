package Student;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/students")
public class StudentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private List<Student> students = new ArrayList<>();

    public StudentServlet() {
        // Load existing students (for demo purposes)
        students.add(new Student("1", "John Doe", 20));
        students.add(new Student("2", "Jane Smith", 22));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("view".equals(action)) {
            viewStudents(request, response);
        } else {
            response.sendRedirect("index.html");
        }
    }

    private void viewStudents(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Student Records</h1>");
        out.println("<table border='1'><tr><th>ID</th><th>Name</th><th>Age</th></tr>");
        for (Student student : students) {
            out.println("<tr><td>" + student.getId() + "</td><td>" + student.getName() + "</td><td>" + student.getAge() + "</td></tr>");
        }
        out.println("</table>");
        out.println("</body></html>");
    }

    // You would add more methods here to handle adding, editing, and deleting students
}
