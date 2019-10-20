package com.example.mobilelab.model

class DataList(
    val dataList: ArrayList<Data>
) {

    fun addData(
        data: Data
    ) {
        if (data.isTask) {
            addTask(data)
        } else {
            addCategory(data)
        }
    }

    fun deleteData(
        position: Int
    ) {
        if (dataList[position].isTask) {
            deleteTask(position)
        } else {
            deleteCategory(position)
        }
    }

    private fun addCategory(
        data: Data
    ) {
        if (dataList.indexOfFirst { it.category == data.category } != -1) {
            dataList.add(data)
        }
    }

    private fun addTask(
        data: Data
    ) {
        val lastInd = dataList.indexOfLast { it.category == data.category }

        if (lastInd < 0) {
            dataList.add(
                Data(
                    data.category,
                    null,
                    null,
                    null,
                    null,
                    false
                )
            )
            dataList.add(data)
        } else {
            dataList.add(lastInd, data)
        }
    }

    private fun deleteCategory(
        position: Int
    ) {
        val category = dataList[position].category

        for (d in dataList) {
            if (d.category == category) {
                dataList.remove(d)
            }
        }
    }

    private fun deleteTask(
        position: Int
    ) {
        val data = dataList[position]

        dataList.removeAt(position)

        val firstInd = dataList.indexOfFirst { it.category == data.category }
        val lastInd = dataList.indexOfLast { it.category == data.category }

        if (lastInd - firstInd == 0) {
            dataList.removeAt(firstInd)
        }
    }

}