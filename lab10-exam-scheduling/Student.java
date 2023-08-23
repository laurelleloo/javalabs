import java.util.Scanner;
import structure5.*;

public class Student {
    protected String name; // student name
    protected Vector<String> courses; // vector of student's courses

    public Student (String Name) {
        // initialize the two member variables
        courses = new Vector<String>();
        name = Name;
    }

    /**
    * Adds a course to the student
    * @param course, string representing the course
    * @pre: course is non-null and is not an empty string
    * @post: adds course to courses
    * @return nothing
    */
    public void addCourse(String course) {
        Assert.pre(course != null, "Course cannot be null");
        Assert.pre(course != "", "Course cannot be an empty string");
        courses.add(course);
    }

    /**
    * Accessor Method for member variable "name"
    * @pre: name is non-null
    * @post: returns the "name" variable
    * @return the "name" variable
    */
    public String getName() {
        Assert.pre(this.name != null, "name cannot be null");
        return this.name;
    }

    /**
    * Accessor method for member variable "courses"
    * @pre: courses is non-null
    * @post: returns the vector of courses
    * @return the vector of courses
    */
    public Vector<String> getCourses() {
        Assert.pre(this.courses != null, "courses cannot be null");
        return this.courses;
    }

    /**
    * Making student object an easy-to-read string
    * @pre: courses and name are not null
    * @post: returns a nice string representation of student object
    * @return string representation of student object
    */
    public String toString() {
        String finalString = getName() + "'s Courses:" + "\n";
        for (String course : courses) {
            finalString += course;
            finalString += "\n";
        }
        return finalString;
    }

    /**
    * Main method for testing
    */
    public static void main(String args[]) {
        Student chan = new Student("Chan");
        chan.addCourse("Mathematics");
        chan.addCourse("science");
        System.out.println(chan.toString());
        Student song = new Student("Song");
        song.addCourse("History");
        System.out.println(song.toString());
        System.out.println(chan.getName());
        System.out.println(chan.getCourses());
    }

    public SinglyLinkedList<T> merge(SinglyLinkedList<T> other, Comparator<T> c) {
        SinglyLinkedList<T> list = new SinglyLinkedList<T(); // create L3
        this.sort(Comparator<T> c); // sort L1
        other.sort(Comparator<T> c); // sort L2
        
        // when L1 and L2 are nonempty
        while(!this.isEmpty() && !other.isEmpty()) {
            // if L2 has the smallest element
            if (c.compare(this.get(0), other.get(0)) > 0) {
                T smallest = other.remove(0); // remove from L2
                list.addLast(smallest); // add to L3
            } else {
                T smallest = this.remove(0); // remove from L1
                list.addLast(smallest); // add to L3
            }
        }
        return list;
    }
}
