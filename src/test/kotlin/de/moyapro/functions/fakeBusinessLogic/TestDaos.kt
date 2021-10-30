data class ApiInput(val value: String)

data class DomainObject(val value: Int) {
    fun double(): DomainObject {
        return this.copy(value = 2 * this.value)
    }
}
