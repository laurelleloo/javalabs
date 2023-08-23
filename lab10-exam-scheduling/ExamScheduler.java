import java.util.Scanner;
import structure5.*;
import java.util.Iterator;

public class ExamScheduler {
    protected Vector<Student> students;
    protected Vector<String> vecOfCourses;
    protected GraphListUndirected<String,Integer> graph;
    protected Vector<Association<String,Vector<String>>> extensionVector;
    protected final static int NUM_OF_COURSES = 4;

    /** Constructor Method */
    public ExamScheduler() {
        students = new Vector<Student>();
        graph = new GraphListUndirected<String,Integer>();
        vecOfCourses = new Vector<String>();
    }

    /**
    * Converts the text file into a string and returns text.
    * @param in scanner object
    * @pre: in is non-null
    * @post: Converts the text file into a string and returns text.
    * @return text as a string
    */
    public String convertFile (Scanner in) {
        StringBuffer textBuffer = new StringBuffer();
        while (in.hasNextLine()) {
            String line = in.nextLine();
            textBuffer.append(line);
            textBuffer.append("\n");
        }
        String text = textBuffer.toString();
        return text;
    }

    /**
    * Given the string text file, create (and returns) a vector of student objects
    * @param text is a string representation of the text file
    * @pre: text is non-null string
    * @post: populates students vector with Student objects
    * @return nothing
    */
    public void makeStudentVector(String text) {
        Assert.pre(text != null, "text cannot be null");
        Scanner scan = new Scanner(text);
        
        while (scan.hasNextLine()) {
            String studentName = scan.nextLine(); // get the student's name on first line
            Student studentObject = new Student(studentName); // create new student object

            // add student's courses
            for (int i =0; i < NUM_OF_COURSES; i++) {
                String courseName = scan.nextLine();
                studentObject.addCourse(courseName);
            }

            // add populated student object to students vector
            students.add(studentObject);
        }
    }

    /**
    * Makes all courses of ONE student exist as vertices in the graph. 
    * I.e., If a course is not already a vertex in the graph, 
    * make it a vertex. Otherwise, do nothing.
    * @param stud a student object
    * @pre: stud is a non-null Student object
    * @post: makes all courses of ONE student exist as vertices in the graph.
    * @return nothing
    */
    public void createVertices(Student stud) {
        Assert.pre(stud != null, "Student object cannot be null");
        Vector<String> courses = stud.getCourses();

        // iterate over student's courses
        for (int i = 0; i < courses.size(); i++) {
            String courseName = courses.get(i); // get course name at index i of courses
            vecOfCourses.add(courseName); // add course to vecOfCourses (global var.)

            // check if the course is not already a vertex
            if (!graph.contains(courseName)) {
                graph.add(courseName); // then add course as vertex
            } // if already a vertex, do nothing.
        }
    }


    /** Between every pair of vertices (the vertices that represent the 
    * classes that the input student is taking), create edges (if the edge
    * does not already exist)
    * @param stud a Student object
    * @pre: stud is non-null
    * @post: create edges between all vertices for stud's classes
    * @return: nothing
    */
    public void makeEdges(Student stud) {
        // precondition is checked in createVerties() method
        createVertices(stud); // make the classes into vertices
        Vector<String> courses = stud.getCourses();

        // make edges between all the vertices
        for (int i = 0; i < courses.size(); i++) {
            for(int j = i+1; j < courses.size(); j++) {
                // if an edge doesn't already exist between two vertices, create edge
                if (!graph.containsEdge(courses.get(i), courses.get(j))) {
                    graph.addEdge(courses.get(i), courses.get(j), 1);
                }
                
            }
        }
    }

    /**
    * Create a graph by calling makeEdges()
    * @pre: students (global var.) is non-null
    * @post: populates graph (global var.) using the students vector (global var.)
    * @return nothing
    */
    public void createGraph () {
        Assert.pre (students != null, "students vector must be non-null");

        // itereate over all the students
        for (Student stud : students) {
            // call makeEdges to make stud's classes into vertices and
            // make edges between said vertices
           makeEdges(stud);
        }
    }

    /**
    * Returns true if any of the the strings in nSlot shares an edge with string m
    * @param nSlot a vector of strings
    * @param m a string
    * @pre: nSlot and m are non-null
    * @post: returns true if any of the the strings in nSlot shares an edge with string m.
    * otherwise, returns false.
    * @return true if any of the the strings in nSlot shares an edge with string m. else false.
    */
    public boolean isConnected(Vector<String> nSlot, String m) {
        Assert.pre(nSlot != null, "Vector input cannot be null");
        Assert.pre(m != null, "String input cannot be null");
        
        for (String s : nSlot) {
            // if any string s in nSlot shares an edge with m
            if (graph.containsEdge(s, m)) {
                return true; // then return true
            }
        }
        // if every string in nSlot doesn't share an edge with m, return false
        return false;
    }

    /** Make an Exam Schedule using the graph
    * @pre: vecOfCourses is non-null
    * @post: returns a vector of associations (each representing a slot)
    * @return a vector of associations (key: slots num, value: vector of classes in slot num)
    */
    public Vector<Association<Integer,Vector<String>>> makeSchedule() {
        // initialize a vector of slots (which are vectors of strings)
        Vector<Association<Integer,Vector<String>>> slots = new Vector<Association<Integer,Vector<String>>>();
        // a vector of courses from which we will "remove" courses and add them to slots
        Vector<String> depletingVec = vecOfCourses;
        int k = 1;

        // each while loop creates one slot
        while (depletingVec.size() > 0) {

            // create vector for the nth slot
            Vector<String> nSlot = new Vector<String>();

            // remove first element of depletingVec and add it to nSlot
            String defaultCourse = depletingVec.remove(0);
            nSlot.add(defaultCourse);

            // create iterator for all the vertices
            Iterator<String> iterator = graph.iterator();

            // iterate over every vertex in the graph
            while(iterator.hasNext()) {
                String m = iterator.next(); // m is the vertex
                // check if m is not connected to any element in nSlot
                if(isConnected(nSlot,m) == false) {
                    nSlot.add(m); // add m to nSlot
                    depletingVec.remove(m); // remove m from the depletingVec
                }
            }

            slots.add(new Association(k, nslot)); // add nSlot to an association, and add it to slots
            k++; // increment slot number
        }

        return slots;
    }

    /** Converts a Vector<Association<Integer,Vector<String>>> into a string
    * @param vec a vector of associations
    * @pre: vec is non-null
    * @post: return a string representation of the input
    * @return a string representation of the input vector
    */
    public String toString(Vector<Association<Integer,Vector<String>>> vec) {
        Assert.pre(vec != null, "vec is non-null");
        String finalString = "";
        for (Association<Integer,Vector<String>> assoc : vec) {
            String slotName = "Slot " + Integer.toString(assoc.getKey()) + ":";
            finalString += slotName;
            finalString += "\n";
        }
    }


    /** Extension 1 */
    public Vector<String> orderVector() {
        // order vecOfCourses vector
        // return the ordered vector
    }

    public void findStudentsforClass () {
        Vector<String> orderedVecOfCourses = orderVector();

        // find students in each course in orderedVecOfCourses
        for (Student stud : students) {
            Vector<String> courses = stud.getCourses();
            
        }

    }

    /** Main method */
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in); // scans the text through System.in

        ExamScheduler scheduler = new ExamScheduler(); // create instance of ExamScheduler
        String text = scheduler.convertFile(in);
        scheduler.makeStudentVector(text); //make vector of students
        scheduler.createGraph(); // finalize graph
        Vector<Association<Integer,Vector<String>>> sampleSchedule = scheduler.makeSchedule(); // make schedule
        System.out.println(scheduler.toString(sampleSchedule));
    }
}