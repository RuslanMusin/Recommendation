package utils

import model.Course
import model.Student
import utils.SimpleStorage.courses
import utils.SimpleStorage.students
import java.util.*

class RecUtil {

    companion object {

        fun wireStudentToCourses(student: Student, courses: List<Course>) {
            val stud: Student = students[students.indexOf(student)]
            stud.myCourses = courses.toMutableList()
            for(course in courses) {
                if(stud.myCourses.contains(course)) {
                    val st = stud.clone() as Student
                    st.myCourses = ArrayList()
                    course.students.add(st)
                }
            }
        }

        fun bindCourseAndStudent(student: Student, courses: List<Course>) {
            for(course in courses) {
                course.students.add(student)
                student.coursesIds.add(course.title)
            }
        }

        fun findRecommended(stud: Student): List<Course> {
            val recCourses = courses.minus(stud.myCourses)
            for(course in recCourses) {
                course.likes = findLikes(stud, course)
            }
            System.out.println(Arrays.toString(recCourses.sortedByDescending { it.likes }.toTypedArray()));
            return recCourses.sortedByDescending { it.likes }
        }

        fun findLikes(student: Student, course: Course): Double {
            var sum: Double = 0.0
            var count = 0
            for(stud in course.students) {
                val sim = findSimilarity(student, stud)
                System.out.println("sim = ${sim}");
                if(sim > 0.2) {
                    sum += sim
                    count++
                }
            }
            var result = 0.0
            if(count > 0) {
                result = sum / count
            }
            return result
        }

        fun findSimilarity(student: Student, stud: Student): Double {
            var sim: Double = 0.0
            val coursesOne = student.myCourses
            val coursesTwo = stud.myCourses

            sim = (coursesOne.intersect(coursesTwo)).size.toDouble() / coursesOne.union(coursesTwo).size
            return sim
        }
    }


}