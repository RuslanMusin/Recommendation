package utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import model.Course
import model.Student
import java.io.*
import java.util.*


object SimpleStorage {

    var HIGH_NUM = 14
    var LOW_NUM = 0
    var FORMAT = ".dat"
    var rand = Random()

    var fieldStudents = "students"
    var fieldCourses = "myCourses"

    var studentListType = object : TypeToken<List<Student>>() {}.type
    var coursesListType = object : TypeToken<List<Course>>() { }.type

    var gson = Gson()

    var students: MutableList<Student> = ArrayList()
    var courses: MutableList<Course> = ArrayList()

    fun loadCourses() {
        /*1 */    courses.add(Course("Программирование на Java"))
        /*2 */    courses.add(Course("Английский с нуля"))
        /*3 */    courses.add(Course("Основы статистики"))
        /*4 */    courses.add(Course("Основы интернет-маркетинга"))
        /*5 */    courses.add(Course("Философия"))
        /*6 */    courses.add(Course("Экономика для неэкономистов"))
        /*7 */    courses.add(Course("Сравнителная политика"))
        /*8 */    courses.add(Course("Введение в машинное обучение"))
        /*9 */    courses.add(Course("Геймификация"))
        /*10*/    courses.add(Course("Введение в молекулярную биологию"))
        /*11*/    courses.add(Course("Политические процессы в современной России"))
        /*12*/    courses.add(Course("Журналистика и медиаграмотность"))
        /*13*/    courses.add(Course("Введение в физику"))
        /*14*/    courses.add(Course("Финансовая грамотность"))


    }

    fun loadStudents() {
        /*1 */     students.add(Student("Константин"))
        /*2 */     students.add(Student("Александр"))
        /*3 */     students.add(Student("Азат"))
        /*4 */     students.add(Student("Роман"))
        /*5 */     students.add(Student("Зарина"))
        /*6 */     students.add(Student("Руслан"))
        /*7 */     students.add(Student("Антон"))
    }


    @Throws(IOException::class)
    private fun saveField(fieldName: String, fieldValue: Any) {
        val fos = FileOutputStream(File(fieldName + FORMAT))
        val oos = ObjectOutputStream(fos)
        oos.writeObject(fieldValue)
        oos.close()
    }


    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readField(fieldName: String): Any {
        val fis = FileInputStream(File(fieldName + FORMAT))
        val ois = ObjectInputStream(fis)
        val value = ois.readObject()
        ois.close()

        return value
    }

    @Throws(IOException::class)
    fun save() {
        removeOldFiles()
        saveField(fieldStudents, gson.toJson(students))
        saveField(fieldCourses, gson.toJson(courses))
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    fun load() {
        if(File(fieldStudents + FORMAT).exists()) {
            students = gson.fromJson(readField(fieldStudents) as String, studentListType)
            courses = gson.fromJson(readField(fieldCourses) as String, coursesListType)
        } else {
            loadCourses()
        }
    }

    @Throws(IOException::class)
    fun removeOldFiles() {
        File(fieldStudents + FORMAT).delete()
        File(fieldCourses + FORMAT).delete()
    }

    fun generateNum(): Int {
        return rand.nextInt(HIGH_NUM - LOW_NUM) + LOW_NUM
    }
}
