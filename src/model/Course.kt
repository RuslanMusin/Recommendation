package model

class Course() {

    lateinit var title: String

    var students: MutableList<Student> = ArrayList()

    var likes: Double = 0.0

    var checked = false

    constructor(title: String) : this() {
        this.title = title
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + title.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other == null)
            return false
        if (javaClass != other.javaClass)
            return false
        val student = other as Course
        return if (!title.equals(student.title)) false else true
    }

    override fun toString(): String {
        val rTitle = title
        if(likes > 0.0) {
            return rTitle + " $likes"
        }
        return rTitle
    }
}