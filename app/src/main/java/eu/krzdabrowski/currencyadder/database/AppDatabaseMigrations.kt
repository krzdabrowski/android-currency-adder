package eu.krzdabrowski.currencyadder.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.UUID

internal val MIGRATION_2_3: Migration = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE new_User_Savings (
                id INTEGER PRIMARY KEY,
                uuid TEXT NOT NULL,
                place TEXT NOT NULL,
                amount REAL NOT NULL,
                currency TEXT NOT NULL
            )
        """,
        )

        val cursor = db.query("SELECT id, place, amount, currency FROM User_Savings")
        val idIndex = cursor.getColumnIndex("id")
        val placeIndex = cursor.getColumnIndex("place")
        val amountIndex = cursor.getColumnIndex("amount")
        val currencyIndex = cursor.getColumnIndex("currency")

        // Assure you won't get -1 value that getColumnIndex can return
        if (idIndex == -1 || placeIndex == -1 || amountIndex == -1 || currencyIndex == -1) {
            error("Database column not found")
        }

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idIndex)
            val place = cursor.getString(placeIndex)
            val amount = cursor.getDouble(amountIndex)
            val currency = cursor.getString(currencyIndex)
            val uuid = UUID.randomUUID().toString()

            db.execSQL(
                """
                INSERT INTO new_User_Savings (id, uuid, place, amount, currency)
                VALUES (?, ?, ?, ?, ?)
            """,
                arrayOf(id, uuid, place, amount, currency),
            )
        }
        cursor.close()

        db.execSQL("DROP TABLE User_Savings")

        db.execSQL("ALTER TABLE new_User_Savings RENAME TO User_Savings")
    }
}
