package com.example.toolsdisplay.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.toolsdisplay.database.entities.*

/**
 * Database class that invokes the creation of this app's database
 * This is initialized on the ToolsInfoApplication class as a dependency
 */
@Database(entities = [AuthInfoData::class, ToolsInfoData::class, MediaGalleryData::class,
                      StockItemData::class, CustomAttributeData::class], version = 1, exportSchema = false)
abstract class ToolsRoomDatabase: RoomDatabase() {

        abstract fun toolsDao(): ToolsInfoDao

        companion object {

            @Volatile
            private var INSTANCE: ToolsRoomDatabase? = null

            @JvmField
            val MIGRATION_1_2: Migration = MigrationVersion(1,2)

            operator fun invoke(context: Context) = INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE= it }
            }

            private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                    ToolsRoomDatabase::class.java, "toolsinfo_database")
                    .addMigrations(MIGRATION_1_2)
                    .build()

        }

    class MigrationVersion(var previousVersion: Int,
                           var NextVersion: Int) : Migration(previousVersion, NextVersion) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Since we didn't alter the table, there's nothing else to do here.
            //Action must be done here if schemas are edited
        }

    }

}