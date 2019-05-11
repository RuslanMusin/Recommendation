import model.Course
import model.Student
import utils.RecUtil.Companion.bindCourseAndStudent
import utils.RecUtil.Companion.findRecommended
import utils.SimpleStorage.courses
import utils.SimpleStorage.generateNum
import utils.SimpleStorage.loadCourses
import utils.SimpleStorage.loadStudents
import utils.SimpleStorage.save
import utils.SimpleStorage.students
import java.util.*
import kotlin.collections.ArrayList
import org.apache.commons.io.FileUtils
import utils.RecUtil.Companion.findSimilarity
import java.io.File


object Main {

    private lateinit var sc: Scanner

    private lateinit var student: Student

    val htmlTemplateFile = File("src/template.html")
    var htmlString = FileUtils.readFileToString(htmlTemplateFile)

    @JvmStatic
    fun main(args: Array<String>) {
        sc = Scanner(System.`in`)
        startExample()
        while(true) {
            println("Выберите дейстивие\n" +
                    "1) Создать юзера и выбрать курсы\n" +
                    "2) Показать рекомендации")
            if (sc.hasNextInt()) {
                val number = sc.nextInt()
                sc.nextLine()
                when (number) {

                    1 -> {
                        addStudent()
                        chooseCourses(7)
                        prepareMatrix()
                        printToHtml()
                        save()
                    }

                    2 -> {
                        chooseStudent()
                        showRecommendations()
                    }

                    else -> System.exit(-1);
                }
            }
        }
    }

    private fun addStudent() {
        var name: String? = null
        println("Введите имя студента")
        if (sc.hasNextLine()) {
            name = sc.nextLine()
        }
        if (name != null) {
            student = Student(name)
            students.add(student)
            println("Студент успешно добавлен!")
        }
    }

    private fun chooseCourses(size: Int) {
        println("Выберите 4-5 предметов из предложенных (пример: 1,2,4,6")
        val chooseCourses = courses.subList(0, size)
        printCourses(chooseCourses)
        if (sc.hasNextLine()) {
            val line = sc.nextLine()
            val nums: List<Int> = line.split(",".toRegex()).map( { it.trim().toInt() - 1 })
            val userCourses = ArrayList<Course>()
            for (num in nums) {
                userCourses.add(chooseCourses[num])
            }
            bindCourseAndStudent(student, userCourses)
            println("Связь между студентом и курсами успешно создана!")
        }
    }

    private fun chooseStudent() {
        println("Выбор студента")
        println("Выберите студента")
        printStudents()
        var studNum: Int? = null
        if (sc.hasNextLine()) {
            studNum = sc.nextInt()
            sc.nextLine()
            if(studNum != 0) {
                student = students[studNum - 1]
                println("Студент успешно выбран!")
            }
        }

    }

    private fun showRecommendations() {
        var list = findRecommended(student)
        println("Рекоммендация для студента ${student.name}")
        println(getOrderedCourse(list))
        prepareRecommendations(getOrderedCourse(list))
        printToHtml()
    }

    private fun printCourses(chooseCourses: List<Course>) {
        var i = 1
        for (sub in chooseCourses) {
            println("$i) ${sub.title}")
            i++
        }
    }

    private fun getOrderedCourse(list: List<Course>): String {
        var i = 1
        var listStr = ""
        for (sub in list) {
            listStr += "$i) ${sub.title} with recommendation prob = ${"%.2f".format(sub.likes)} <br>\n"
            i++
        }
        return listStr
    }


    private fun printStudents() {
        var i = 1
        for (sub in students) {
            println("$i) ${sub.name}")
            i++
        }
    }

    private fun startExample() {
        loadStudents()
        loadCourses()
        var course = Course()
        var i = 0
        for(stud in students) {
            while (i < 5) {
                course = courses[generateNum()]
                if(!course.students.contains(stud)) {
                    course.students.add(stud)
                    stud.coursesIds.add(course.title)
                    i++
                }
            }
            i = 0
            println("Student ${stud.name} and courses names = " + Arrays.toString(stud.myCourses.toTypedArray()))

        }
       /* prepareMatrix()
        printToHtml()*/
        save()
    }

    private fun prepareMatrix() {
        var th = "<th>User Name</th>\n"
        var tr = ""
        var intersect: Set<Course>
        for(stud in students) {
            th += "<th>${stud.name}</th>\n"
            tr += "<tr><td>${stud.name}</td>\n"
            for(st in students) {
                if(!stud.equals(st)) {
                    intersect = stud.myCourses.intersect(st.myCourses)
                    tr += "<td>${"%.2f".format(findSimilarity(stud, st))} \n${Arrays.toString(intersect.toTypedArray())}</td>\n"
                } else {
                    tr += "<th>-</th>\n"
                }
            }
            tr += "</tr>\n\n"
        }
        htmlString = htmlString.replace("\$ths",th)
        htmlString = htmlString.replace("\$tr",tr)

    }

    private fun printToHtml() {
        val newHtmlFile = File("src/new.html")
        FileUtils.writeStringToFile(newHtmlFile, htmlString)
    }

    private fun prepareRecommendations(list: String) {
        var rec = "<h2>Рекоммендации для ${student.name}</h2>\n"
        rec += "<p>${list}</p>"
        htmlString = htmlString.replace("\$rec",rec)
    }

}
