package com.projects.sydnyrestaurant.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.projects.sydnyrestaurant.models.TableEntity

@Dao
interface TableDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTable(table: TableEntity)

    @Query("SELECT * FROM tables")
    suspend fun getAllTables(): List<TableEntity>

    @Update
    suspend fun updateTable(table: TableEntity)

    @Query("SELECT * FROM tables WHERE id = :tableId")
    suspend fun getTableById(tableId: Long): TableEntity

    @Query("SELECT * FROM tables WHERE isAvailable = 1")
    suspend fun getAvailableTables(): List<TableEntity>
}