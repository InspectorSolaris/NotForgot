package com.example.mobilelab.model.data

fun addDataToList(
    taskData: TaskData,
    taskDataList: ArrayList<TaskData>
) {
    if (taskData.isTask) {
        addTask(taskData, taskDataList)
    } else {
        addCategory(taskData, taskDataList)
    }
}

fun deleteDataToList(
    position: Int,
    taskDataList: ArrayList<TaskData>
) {
    if (taskDataList[position].isTask) {
        deleteTask(position, taskDataList)
    } else {
        deleteCategory(position, taskDataList)
    }
}

private fun addCategory(
    taskData: TaskData,
    taskDataList: ArrayList<TaskData>
) {
    if (taskDataList.indexOfFirst { it.category == taskData.category } != -1) {
        taskDataList.add(taskData)
    }
}

private fun addTask(
    taskData: TaskData,
    taskDataList: ArrayList<TaskData>
) {
    val lastInd = taskDataList.indexOfLast { it.category == taskData.category }

    if (lastInd < 0) {
        taskDataList.add(
            TaskData(
                taskData.category,
                null,
                null,
                null,
                null,
                null,
                null,
                false
            )
        )
        taskDataList.add(taskData)
    } else {
        taskDataList.add(lastInd, taskData)
    }
}

private fun deleteCategory(
    position: Int,
    taskDataList: ArrayList<TaskData>
) {
    val category = taskDataList[position].category

    for (d in taskDataList) {
        if (d.category == category) {
            taskDataList.remove(d)
        }
    }
}

private fun deleteTask(
    position: Int,
    taskDataList: ArrayList<TaskData>
) {
    val data = taskDataList[position]

    taskDataList.removeAt(position)

    val firstInd = taskDataList.indexOfFirst { it.category == data.category }
    val lastInd = taskDataList.indexOfLast { it.category == data.category }

    if (lastInd - firstInd == 0) {
        taskDataList.removeAt(firstInd)
    }
}