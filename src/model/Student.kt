package model
import utils.SimpleStorage.courses

class Student(): Cloneable {

    lateinit var name: String
    var coursesIds: MutableList<String> = ArrayList()

    @Transient
    var myCourses: MutableList<Course> = ArrayList()
        get() {
            for(course in courses) {
                if(coursesIds.contains(course.title) && !field.contains(course)) {
                    field.add(course)
                }
            }
            return field
        }

    constructor(name: String) : this() {
        this.name = name
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + name.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other == null)
            return false
        if (javaClass != other.javaClass)
            return false
        val student = other as Student
        return if (!name.equals(student.name)) false else true
    }

    override public fun clone(): Any {
        return super.clone()
    }
}