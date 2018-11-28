package com.micmr0.runassistant.util.trainingsAdapter

import com.micmr0.runassistant.db.TrainingDao

class HistoryItem(pTrainingDao: TrainingDao) : ListItem(pTrainingDao.mName) {
    val mTrainingDao = pTrainingDao
}