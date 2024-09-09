import java.util.Scanner

class Expenses(
    val price: Double,
    val category: String,
    val date: String
) {
    fun showExpense() {
        println("Расход: $price | Категория: $category | Дата: $date")
    }
}

class ExpensesManager {

    val expensesList = mutableListOf<Expenses>()

    fun addExpense() {
        val scanner = Scanner(System.`in`)
        println("Введите сумму:")
        val price = scanner.nextDouble()
        println("Введите категорию:")
        val category = scanner.next()
        println("Введите дату:")
        val date = scanner.next()
        val expense = Expenses(price, category, date)
        expensesList.add(expense)
        println("Расход добавлен")
    }

    fun showAllExpenses() {
        if (expensesList.isEmpty()) {
            println("Расходы отсуствуют")
        } else {
            println("Все расходы:")
            for (expense in expensesList) {
                expense.showExpense()
            }
        }
    }

    fun sumOfExpenses() {
        val expensesByCategory = expensesList.groupBy { it.category }
        if (expensesByCategory.isEmpty()) {
            println("Расходы отсуствуют")
        } else {
            println("Сумма расходов по категориям:")
            for ((category, expenses) in expensesByCategory) {
                val total = expenses.sumOf { it.price }
                println("$category: $total")
            }
        }
    }
}

fun main() {

    val manager = ExpensesManager()
    val scanner = Scanner(System.`in`)

    while (true) {
        println("Выберите действие:")
        println("1. Добавить расход")
        println("2. Показать все расходы")
        println("3. Показать сумму расходов по категориям")
        println("4. Отмена")

        when (scanner.nextInt()) {
            1 -> manager.addExpense()
            2 -> manager.showAllExpenses()
            3 -> manager.sumOfExpenses()
            4 -> break
            else -> println("Неверный выбор")
        }
    }
}