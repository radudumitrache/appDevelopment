import android.content.Context
import androidx.room.Room
import com.example.appdev.database.GoalSaverDatabase
import com.example.appdev.database.daos.TransactionsDao
import java.text.SimpleDateFormat
import java.util.Locale

object AverageSavingsCalculator {

    private lateinit var transactionsDao: TransactionsDao
    var averageSavingsPerMonth: Float = 0f
        private set

    fun initialize(context: Context) {
        val db = Room.databaseBuilder(
            context,
            GoalSaverDatabase::class.java, "goal_saver_database"
        ).allowMainThreadQueries().build()

        transactionsDao = db.transactionDao()
        calculateAverageSavingsPerMonth()
    }

    fun calculateAverageSavingsPerMonth() {
        val transactions = transactionsDao.getAllTransactions()
        val monthlySavings = mutableMapOf<String, Float>()

        val dateFormat = SimpleDateFormat("yyyy-MM", Locale.US)

        transactions.forEach { transaction ->
            val month = dateFormat.format(transaction.date)
            val currentSavings = monthlySavings[month] ?: 0f
            monthlySavings[month] = currentSavings + transaction.amount
        }

        val totalSavings = monthlySavings.values.sum()
        val monthsCount = monthlySavings.size
        averageSavingsPerMonth = if (monthsCount > 0) totalSavings / monthsCount else 0f
    }
}
