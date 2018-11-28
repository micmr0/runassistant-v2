package com.micmr0.runassistant.db

import android.provider.BaseColumns


class GPStracking {
    companion object {
        const val DATABASE_NAME = "GPSlog.db"
        /** The version of the database schema  */
        const val DATABASE_VERSION = 1
    }

    class Plans {
        companion object {
            const val TABLE = "plans"

            const val TAB = "tab"
            const val _ID = "_id"

            private const val TAB_TYPE = "INTEGER NOT NULL"
            private const val _ID_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT"

            internal val CREATE_STATEMENT = "CREATE TABLE " + Plans.TABLE +
                    "(" + " " + _ID + " " + _ID_TYPE +
                    "," + " " + TAB + " " + TAB_TYPE +
                    "," + "FOREIGN KEY" + "(" + TAB + ")" + "REFERENCES" + " " + Tables.TABLE + "(" + BaseColumns._ID + ")" + ");"
        }
    }

    class PlanDays {
        companion object {
            const val TABLE = "plandays"

            const val _ID = "_id"
            const val PLAN = "plan"
            const val DAY = "day"
            const val TRAINING = "training"
            const val DATE = "date"

            private const val PLAN_TYPE = "INTEGER NOT NULL"
            private const val DAY_TYPE = "INTEGER NOT NULL"
            private const val TRAINING_TYPE = "INTEGER"
            private const val DATE_TYPE = "TEXT"
            private const val _ID_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT"

            internal val CREATE_STATEMENT = "CREATE TABLE " + PlanDays.TABLE +
                    "(" + " " + _ID + " " + _ID_TYPE +
                    "," + " " + PLAN + " " + PLAN_TYPE +
                    "," + " " + DAY + " " + DAY_TYPE +
                    "," + " " + TRAINING + " " + TRAINING_TYPE +
                    "," + " " + DATE + " " + DATE_TYPE +
                    "," + "FOREIGN KEY" + "(" + PLAN + ")" + "REFERENCES" + " " + Plans.TABLE + "(" + BaseColumns._ID + ")" + "ON DELETE CASCADE" + ");"
        }
    }

    open class Tables {
        companion object {
            internal val TABLE = "tables"

            const val _ID = "_id"
            const val TITLE = "title"
            const val GOAL = "goal"
            const val DESCRIPTION = "description"
            const val DAYS = "days"

            private const val TITLE_TYPE = "TEXT NOT NULL"
            private const val GOAL_TYPE = "TEXT NOT NULL"
            private const val DESCRIPTION_TYPE = "TEXT NOT NULL"
            private const val DAYS_TYPE = "TEXT NOT NULL"
            private const val _ID_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT"

            internal val CREATE_STATEMENT = "CREATE TABLE " + Tables.TABLE +
                    "(" + " " + _ID + " " + _ID_TYPE +
                    "," + " " + TITLE + " " + TITLE_TYPE +
                    "," + " " + GOAL + " " + GOAL_TYPE +
                    "," + " " + DESCRIPTION + " " + DESCRIPTION_TYPE +
                    "," + " " + DAYS + " " + DAYS_TYPE + ");"

            internal val INSERT_STATEMENT = "INSERT INTO tables VALUES (1,'Bieg na 5 km','Ukończyć','Plan ten zakłada, że dotąd nie biegałeś.', 56)"

            internal val INSERT_STATEMENT2 = "INSERT INTO tables VALUES (2,'Bieg na 5 km','Ukończyć w 45 minut','Plan ten zakłada, " + "że przed jego rozpoczęciem biegałeś już przez co najmniej 2 miesiące przynajmniej na poziomie opisanym w pierwszym tygodniu.', 56)"

            internal val INSERT_STATEMENT3 = "INSERT INTO tables VALUES (3,'Bieg na 10 km','Ukończyć','Plan ten zakłada, " + "że przed jego rozpoczęciem biegałeś już przez co najmniej 2 miesiące przynajmniej na poziomie opisanym w pierwszym tygodniu.', 105)"

            internal val INSERT_STATEMENT4 = "INSERT INTO tables VALUES (4,'Półmaraton','Ukończyć','Plan ten zakłada, " + "że dotąd nie biegałeś.', 105)"
        }
    }

    class TableDays {
        companion object {
            const val TABLE = "tabledays"

            const val _ID = "_id"
            const val TAB = "tab"
            const val DAY = "day"
            const val TYPE = "type"
            const val TITLE = "title"
            const val DISTANCE = "distance"
            const val TIME = "time"
            const val LAPS = "laps"
            const val TIPS = "tips"

            private const val TAB_TYPE = "INTEGER NOT NULL"
            private const val DAY_TYPE = "INTEGER NOT NULL"
            private const val TYPE_TYPE = "INTEGER NOT NULL"
            private const val TITLE_TYPE = "TEXT NOT NULL"
            private const val DISTANCE_TYPE = "REAL NOT NULL"
            private const val TIME_TYPE = "INTEGER NOT NULL"
            private const val LAPS_TYPE = "INTEGER NOT NULL"
            private const val TIPS_TYPE = "INTEGER NOT NULL"
            private const val _ID_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT"

            internal val CREATE_STATEMENT = "CREATE TABLE " + TableDays.TABLE +
                    "(" + " " + _ID + " " + _ID_TYPE +
                    "," + " " + TAB + " " + TAB_TYPE +
                    "," + " " + DAY + " " + DAY_TYPE +
                    "," + " " + TYPE + " " + TYPE_TYPE +
                    "," + " " + TITLE + " " + TITLE_TYPE +
                    "," + " " + DISTANCE + " " + DISTANCE_TYPE +
                    "," + " " + TIME + " " + TIME_TYPE +
                    "," + " " + LAPS + " " + LAPS_TYPE +
                    "," + " " + TIPS + " " + TIPS_TYPE +
                    "," + "FOREIGN KEY" + "(" + TAB + ")" + "REFERENCES" + " " + Tables.TABLE + "(" + BaseColumns._ID + ")" + "ON DELETE CASCADE" +
                    "," + "FOREIGN KEY" + "(" + TIPS + ")" + "REFERENCES" + " " + Tips.TABLE + "(" + BaseColumns._ID + ")" + "ON DELETE CASCADE" + ");"


            internal val INSERT_STATEMENT = "INSERT INTO `tabledays` VALUES (1,1,1,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (2,1,2,1,'10 min biegu/marszu',0,10,0,2),\n" +
                    " (3,1,3,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (4,1,4,3,'10 min biegu/marszu',0,10,0,2),\n" +
                    " (5,1,5,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (6,1,6,0,'Wolne',0,0,0,3),\n" +
                    " (7,1,7,2,'Bieg/marsz na 1,5 km',1500,0,0,4),\n" +
                    " (8,1,8,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (9,1,9,1,'13 min biegu/marszu',0,13,0,2),\n" +
                    " (10,1,10,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (11,1,11,1,'13 min biegu/marszu',0,13,0,2),\n" +
                    " (12,1,12,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (13,1,13,0,'Wolne',0,0,0,3),\n" +
                    " (14,1,14,2,'Bieg/marsz na 2,5 km',2500,0,0,4),\n" +
                    " (15,1,15,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (16,1,16,1,'15 min biegu/marszu',0,15,0,2),\n" +
                    " (17,1,17,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (18,1,18,1,'15 min biegu/marszu',0,17,0,2),\n" +
                    " (19,1,19,0,'Wolne albo TU',0,0,0,2),\n" +
                    " (20,1,20,0,'Wolne',0,0,0,3),\n" +
                    " (21,1,21,2,'Bieg marsz na 3 km',3000,0,0,4),\n" +
                    " (22,1,22,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (23,1,23,1,'17 min biegu/marszu',0,17,0,2),\n" +
                    " (24,1,24,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (25,1,25,1,'17 min biegu/marszu',0,17,0,2),\n" +
                    " (26,1,26,0,'Wolne albo TU',0,0,0,2),\n" +
                    " (27,1,27,0,'Wolne',0,0,0,3),\n" +
                    " (28,1,28,2,'Bieg/marsz na 4 km',4000,0,0,4),\n" +
                    " (29,1,29,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (30,1,30,1,'19 min biegu/marszu',0,19,0,2),\n" +
                    " (31,1,31,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (32,1,32,1,'19 min biegu/marszu',0,19,0,2),\n" +
                    " (33,1,33,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (34,1,34,0,'Wolne',0,0,0,3),\n" +
                    " (35,1,35,2,'Bieg/marsz na 5 km',5000,0,0,4),\n" +
                    " (36,1,36,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (37,1,37,1,'20 min biegu/marszu',0,20,0,2),\n" +
                    " (38,1,38,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (39,1,39,1,'20 min biegu/marszu',0,20,0,2),\n" +
                    " (40,1,40,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (41,1,41,0,'Wolne',0,0,0,3),\n" +
                    " (42,1,42,2,'Bieg/marsz na 5,5 km',5500,0,0,4),\n" +
                    " (43,1,43,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (44,1,44,1,'20 min biegu/marszu',0,20,0,2),\n" +
                    " (45,1,45,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (46,1,46,1,'20 min biegu/marszu',0,20,0,2),\n" +
                    " (47,1,47,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (48,1,48,0,'Wolne',0,0,0,3),\n" +
                    " (49,1,49,5,'Zawody na 5 km',5000,0,0,10),\n" +
                    " (50,1,50,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (51,1,51,1,'20 min biegu/marszu',0,20,0,2),\n" +
                    " (52,1,52,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (53,1,53,1,'20 min biegu/marszu',0,20,0,2),\n" +
                    " (54,1,54,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (55,1,55,0,'Wolne',0,0,0,3),\n" +
                    " (56,1,56,2,'bieg marsz na 3-5 km',5000,0,0,4);"

            internal val INSERT_STATEMENT2 = "INSERT INTO `tabledays` VALUES (57,2,1,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (58,2,2,1,'10 min biegu/marszu',0,10,0,2),\n" +
                    " (59,2,3,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (60,2,4,3,'3x400 m dzień szybkości',400,0,3,8),\n" +
                    " (61,2,5,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (62,2,6,0,'Wolne',0,0,0,3),\n" +
                    " (63,2,7,2,'Bieg/marsz na 2,5 km',2500,0,0,4),\n" +
                    " (64,2,8,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (65,2,9,1,'13 min biegu/marszu',0,13,0,2),\n" +
                    " (66,2,10,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (67,2,11,3,'4x400 m dzień szybkości',400,0,4,8),\n" +
                    " (68,2,12,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (69,2,13,0,'Wolne',0,0,0,3),\n" +
                    " (70,2,14,2,'Bieg/marsz na 3 km',3000,0,0,4),\n" +
                    " (71,2,15,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (72,2,16,1,'15 min biegu/marszu',0,15,0,2),\n" +
                    " (73,2,17,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (74,2,18,3,'5x400 m dzień szybkości',400,0,5,8),\n" +
                    " (75,2,19,0,'Wolne albo TU',0,0,0,2),\n" +
                    " (76,2,20,0,'Wolne',0,0,0,3),\n" +
                    " (77,2,21,2,'Bieg marsz na 4 km',4000,0,0,4),\n" +
                    " (78,2,22,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (79,2,23,1,'17 min biegu/marszu',0,17,0,2),\n" +
                    " (80,2,24,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (81,2,25,3,'6x400 m dzień szybkości',400,0,6,8),\n" +
                    " (82,2,26,0,'Wolne albo TU',0,0,0,2),\n" +
                    " (83,2,27,0,'Wolne',0,0,0,3),\n" +
                    " (84,2,28,2,'Bieg/marsz na 5 km',5000,0,0,4),\n" +
                    " (85,2,29,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (86,2,30,1,'19 min biegu/marszu',0,19,0,2),\n" +
                    " (87,2,31,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (88,2,32,3,'7x400 m dzień szybkości',400,0,7,8),\n" +
                    " (89,2,33,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (90,2,34,0,'Wolne',0,0,0,3),\n" +
                    " (91,2,35,2,'Bieg/marsz na 6 km',6000,0,0,4),\n" +
                    " (92,2,36,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (93,2,37,1,'20 min biegu/marszu',0,20,0,2),\n" +
                    " (94,2,38,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (95,2,39,3,'8x400 m dzień szybkości',400,0,8,8),\n" +
                    " (96,2,40,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (97,2,41,0,'Wolne',0,0,0,3),\n" +
                    " (98,2,42,2,'Bieg/marsz na 6,5 km',6500,0,0,4),\n" +
                    " (99,2,43,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (100,2,44,1,'20 min biegu/marszu',0,20,0,2),\n" +
                    " (101,2,45,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (102,2,46,3,'4x400 m dzień szybkości',400,0,4,8),\n" +
                    " (103,2,47,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (104,2,48,0,'Wolne',0,0,0,3),\n" +
                    " (105,2,49,5,'Zawody na 5 km',5000,0,0,10),\n" +
                    " (106,2,50,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (107,2,51,1,'20 min biegu/marszu',0,20,0,2),\n" +
                    " (108,2,52,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (109,2,53,1,'20 min biegu/marszu',0,20,0,2),\n" +
                    " (110,2,54,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (111,2,55,0,'Wolne',0,0,0,3),\n" +
                    " (112,2,56,2,'bieg marsz na 3-5 km',5000,0,0,4);"

            internal val INSERT_STATEMENT3 = "INSERT INTO `tabledays` VALUES (113,3,1,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (114,3,2,1,'10 min biegu/marszu',0,10,0,2),\n" +
                    " (115,3,3,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (116,3,4,1,'10 min biegu/marszu',0,10,0,2),\n" +
                    " (117,3,5,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (118,3,6,0,'Wolne',0,0,0,3),\n" +
                    " (119,3,7,2,'Bieg/marsz na 1,5 km',1500,0,0,4),\n" +
                    " (120,3,8,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (121,3,9,1,'13 min biegu/marszu',0,13,0,2),\n" +
                    " (122,3,10,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (123,3,11,1,'13 min biegu/marszu',0,13,0,2),\n" +
                    " (124,3,12,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (125,3,13,0,'Wolne',0,0,0,3),\n" +
                    " (126,3,14,2,'Bieg/marsz na 2,5 km',2500,0,0,4),\n" +
                    " (127,3,15,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (128,3,16,1,'15 min biegu/marszu',0,15,0,2),\n" +
                    " (129,3,17,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (130,3,18,1,'15 min biegu/marszu',0,17,0,2),\n" +
                    " (131,3,19,0,'Wolne albo TU',0,0,0,2),\n" +
                    " (132,3,20,0,'Wolne',0,0,0,3),\n" +
                    " (133,3,21,2,'Bieg marsz na 3 km',3000,0,0,4),\n" +
                    " (134,3,22,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (135,3,23,1,'17 min biegu/marszu',0,17,0,2),\n" +
                    " (136,3,24,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (137,3,25,1,'17 min biegu/marszu',0,17,0,2),\n" +
                    " (138,3,26,0,'Wolne albo TU',0,0,0,2),\n" +
                    " (139,3,27,0,'Wolne',0,0,0,3),\n" +
                    " (140,3,28,2,'Bieg/marsz na 4 km',4000,0,0,4),\n" +
                    " (141,3,29,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (142,3,30,1,'19 min biegu/marszu',0,19,0,2),\n" +
                    " (143,3,31,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (144,3,32,1,'19 min biegu/marszu',0,19,0,2),\n" +
                    " (145,3,33,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (146,3,34,0,'Wolne',0,0,0,3),\n" +
                    " (147,3,35,2,'Bieg/marsz na 5 km',5000,0,0,4),\n" +
                    " (148,3,36,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (149,3,37,1,'20 min biegu/marszu',0,20,0,2),\n" +
                    " (150,3,38,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (151,3,39,1,'20 min biegu/marszu',0,20,0,2),\n" +
                    " (152,3,40,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (153,3,41,0,'Wolne',0,0,0,3),\n" +
                    " (154,3,42,2,'Bieg/marsz na 5,5 km',5500,0,0,4),\n" +
                    " (155,3,43,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (156,3,44,1,'21 min biegu/marszu',0,21,0,2),\n" +
                    " (157,3,45,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (158,3,46,1,'21 min biegu/marszu',0,21,0,2),\n" +
                    " (159,3,47,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (160,3,48,0,'Wolne',0,0,0,3),\n" +
                    " (161,3,49,5,'Bieg/marsz na 6 km',6000,0,0,4),\n" +
                    " (162,3,50,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (163,3,51,1,'22 min biegu/marszu',0,22,0,2),\n" +
                    " (164,3,52,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (165,3,53,1,'22 min biegu/marszu',0,22,0,2),\n" +
                    " (166,3,54,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (167,3,55,0,'Wolne',0,0,0,3),\n" +
                    " (168,3,56,0,'bieg marsz na 7 km',7000,0,0,4),\n" +
                    " (169,3,57,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (170,3,58,0,'23 min biegu/marszu',0,23,0,2),\n" +
                    " (171,3,59,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (172,3,60,0,'23 min biegu/marszu',0,23,0,2),\n" +
                    " (173,3,61,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (174,3,62,0,'Wolne',0,0,0,3),\n" +
                    " (175,3,63,0,'Bieg/marsz na 8 km',8000,0,0,4),\n" +
                    " (176,3,64,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (177,3,65,0,'24 min biegu/marszu',0,24,0,2),\n" +
                    " (178,3,66,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (179,3,67,0,'24 min biegu/marszu',0,24,0,2),\n" +
                    " (180,3,68,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (181,3,69,0,'Wolne',0,0,0,3),\n" +
                    " (182,3,70,0,'Bieg/marsz na 9 km',9000,0,0,4),\n" +
                    " (183,3,71,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (184,3,72,0,'26 min biegu/marszu',0,26,0,2),\n" +
                    " (185,3,73,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (186,3,74,0,'26 min biegu/marszu',0,26,0,2),\n" +
                    " (187,3,75,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (188,3,76,0,'Wolne',0,0,0,3),\n" +
                    " (189,3,77,0,'Bieg/marsz na 9,5 km',9500,0,0,4),\n" +
                    " (190,3,78,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (191,3,79,0,'20 min biegu/marszu',0,20,0,2),\n" +
                    " (192,3,80,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (193,3,81,0,'20 min biegu/marszu',0,20,0,2),\n" +
                    " (194,3,82,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (195,3,83,0,'Wolne',0,0,0,3),\n" +
                    " (196,3,84,4,'Zawody na 5 km',0,0,0,9),\n" +
                    " (197,3,85,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (198,3,86,0,'28 min biegu/marszu',0,28,0,2),\n" +
                    " (199,3,87,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (200,3,88,0,'28 min biegu/marszu',0,28,0,2),\n" +
                    " (201,3,89,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (202,3,90,0,'Wolne',0,0,0,3),\n" +
                    " (203,3,91,0,'Bieg/marsz na 10 km',10000,0,0,4),\n" +
                    " (204,3,92,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (205,3,93,0,'20 min biegu/marszu',0,20,0,2),\n" +
                    " (206,3,94,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (207,3,95,0,'20 min biegu/marszu',0,20,0,2),\n" +
                    " (208,3,96,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (209,3,97,0,'Wolne',0,0,0,3),\n" +
                    " (210,3,98,5,'Zawody na 10 km',10000,0,0,11),\n" +
                    " (211,3,99,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (212,3,100,0,'20 min biegu/marszu',0,20,0,2),\n" +
                    " (213,3,101,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (214,3,102,0,'20 min biegu/marszu',0,20,0,2),\n" +
                    " (215,3,103,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (216,3,104,0,'Wolne',0,0,0,3),\n" +
                    " (217,3,105,2,'Bieg/marsz na 5-8  km',8000,0,0,4);"

            internal val INSERT_STATEMENT4 = "INSERT INTO `tabledays` VALUES (218,4,1,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (219,4,2,1,'10 min biegu/marszu',0,10,0,2),\n" +
                    " (220,4,3,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (221,4,4,1,'10 min biegu/marszu',0,10,0,2),\n" +
                    " (222,4,5,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (223,4,6,0,'Wolne',0,0,0,3),\n" +
                    " (224,4,7,2,'Bieg/marsz na 1,5 km',1500,0,0,4),\n" +
                    " (225,4,8,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (226,4,9,1,'13 min biegu/marszu',0,13,0,2),\n" +
                    " (227,4,10,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (228,4,11,1,'13 min biegu/marszu',0,13,0,2),\n" +
                    " (229,4,12,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (230,4,13,0,'Wolne',0,0,0,3),\n" +
                    " (231,4,14,2,'Bieg/marsz na 3 km',3000,0,0,4),\n" +
                    " (232,4,15,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (233,4,16,1,'15 min biegu/marszu',0,15,0,2),\n" +
                    " (234,4,17,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (235,4,18,1,'15 min biegu/marszu',0,17,0,2),\n" +
                    " (236,4,19,0,'Wolne albo TU',0,0,0,2),\n" +
                    " (237,4,20,0,'Wolne',0,0,0,3),\n" +
                    " (238,4,21,2,'Bieg marsz na 5 km',5000,0,0,4),\n" +
                    " (239,4,22,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (240,4,23,1,'17 min biegu/marszu',0,17,0,2),\n" +
                    " (241,4,24,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (242,4,25,1,'17 min biegu/marszu',0,17,0,2),\n" +
                    " (243,4,26,0,'Wolne albo TU',0,0,0,2),\n" +
                    " (244,4,27,0,'Wolne',0,0,0,3),\n" +
                    " (245,4,28,2,'Bieg/marsz na 6 km',6000,0,0,4),\n" +
                    " (246,4,29,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (247,4,30,1,'20 min biegu/marszu',0,20,0,2),\n" +
                    " (248,4,31,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (249,4,32,1,'20 min biegu/marszu',0,20,0,2),\n" +
                    " (250,4,33,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (251,4,34,0,'Wolne',0,0,0,3),\n" +
                    " (252,4,35,2,'Bieg/marsz na 8 km',8000,0,0,4),\n" +
                    " (253,4,36,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (254,4,37,1,'23 min biegu/marszu',0,23,0,2),\n" +
                    " (255,4,38,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (256,4,39,1,'23 min biegu/marszu',0,23,0,2),\n" +
                    " (257,4,40,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (258,4,41,0,'Wolne',0,0,0,3),\n" +
                    " (259,4,42,2,'Bieg/marsz na 10 km',10000,0,0,4),\n" +
                    " (260,4,43,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (261,4,44,1,'25 min biegu/marszu',0,25,0,2),\n" +
                    " (262,4,45,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (263,4,46,1,'25 min biegu/marszu',0,25,0,2),\n" +
                    " (264,4,47,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (265,4,48,0,'Wolne',0,0,0,3),\n" +
                    " (266,4,49,2,'Bieg/marsz na 11 km',11000,0,0,4),\n" +
                    " (267,4,50,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (268,4,51,1,'27 min biegu/marszu',0,27,0,2),\n" +
                    " (269,4,52,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (270,4,53,1,'27 min biegu/marszu',0,27,0,2),\n" +
                    " (271,4,54,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (272,4,55,0,'Wolne',0,0,0,3),\n" +
                    " (273,4,56,2,'Bieg/marsz na 13 km',13000,0,0,4),\n" +
                    " (274,4,57,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (275,4,58,1,'30 min biegu/marszu',0,30,0,2),\n" +
                    " (276,4,59,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (277,4,60,1,'30 min biegu/marszu',0,30,0,2),\n" +
                    " (278,4,61,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (279,4,62,0,'Wolne',0,0,0,3),\n" +
                    " (280,4,63,2,'Bieg/marsz na 16 km',16000,0,0,4),\n" +
                    " (281,4,64,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (282,4,65,1,'30 min biegu/marszu',0,30,0,2),\n" +
                    " (283,4,66,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (284,4,67,1,'30 min biegu/marszu',0,30,0,2),\n" +
                    " (285,4,68,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (286,4,69,0,'Wolne',0,0,0,3),\n" +
                    " (287,4,70,2,'Bieg/marsz na 8 km',8000,0,0,4),\n" +
                    " (288,4,71,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (289,4,72,1,'30 min biegu/marszu',0,30,0,2),\n" +
                    " (290,4,73,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (291,4,74,1,'30 min biegu/marszu',0,30,0,2),\n" +
                    " (292,4,75,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (293,4,76,0,'Wolne',0,0,0,3),\n" +
                    " (294,4,77,2,'Bieg/marsz na 19 km',19000,0,0,4),\n" +
                    " (295,4,78,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (296,4,79,1,'30 min biegu/marszu',0,30,0,2),\n" +
                    " (297,4,80,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (298,4,81,1,'30 min biegu/marszu',0,30,0,2),\n" +
                    " (299,4,82,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (300,4,83,0,'Wolne',0,0,0,3),\n" +
                    " (301,4,84,2,'Bieg/marsz na 10 km',10000,0,0,4),\n" +
                    " (302,4,85,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (303,4,86,1,'30 min biegu/marszu',0,30,0,2),\n" +
                    " (304,4,87,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (305,4,88,1,'30 min biegu/marszu',0,30,0,2),\n" +
                    " (306,4,89,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (307,4,90,0,'Wolne',0,0,0,3),\n" +
                    " (308,4,91,2,'Bieg/marsz na 22,5 km',22500,0,0,4),\n" +
                    " (309,4,92,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (310,4,93,1,'30 min biegu/marszu',0,30,0,2),\n" +
                    " (311,4,94,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (312,4,95,1,'30 min biegu/marszu',0,30,0,2),\n" +
                    " (313,4,96,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (314,4,97,0,'Wolne',0,0,0,3),\n" +
                    " (315,4,98,2,'Bieg/marsz na 10 km',10000,0,0,4),\n" +
                    " (316,4,99,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (317,4,100,1,'30 min biegu/marszu',0,30,0,2),\n" +
                    " (318,4,101,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (319,4,102,1,'30 min biegu/marszu',0,30,0,2),\n" +
                    " (320,4,103,0,'Wolne albo TU',0,0,0,1),\n" +
                    " (321,4,104,0,'Wolne',0,0,0,3),\n" +
                    " (322,4,105,5,'Półmaraton',21097.5,0,0,12);"
        }
    }

    class Tips {
        companion object {
            const val TABLE = "tips"

            const val _ID = "_id"
            const val DESCRIPTION = "description"

            private val DESCRIPTION_TYPE = "TEXT NOT NULL"
            private val _ID_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT"

            internal val CREATE_STATEMENT = "CREATE TABLE " + Tips.TABLE +
                    "(" + " " + _ID + " " + _ID_TYPE +
                    "," + " " + DESCRIPTION + " " + DESCRIPTION_TYPE + ");"

            // 4 tabele treningowe:
            // Bieg na 5 km Cel: Ukonczyc
            // Bieg na 5 km Cel: Ukończyć w 45 minut
            // Bieg na 10 km Cel: Ukonczyc
            // Półmaraton Cel: Ukonczyc
            internal val INSERT_STATEMENT = "INSERT INTO `tips` VALUES (1,'TU to trening uzupełniający. Może to być krótki marsz lub inne nieobciążające ćwiczenia jak pływanie czy jazda na rowerze. Jeśli dopiero zaczynasz to ćwiczenie lub wracasz po przerwie, zacznij od 5 min i zwiększaj o 2-3 min na każdej sesji.'),\n" +
                    " (2,'Trening interwałowy typu bieg-marsz. Podchodź swobodnie do przerw na marsz. '),\n" +
                    " (3,'Dzień wolny.'),\n" +
                    // bieg-marsz niedziela
                    " (4,'Podchodź swobodnie do przerw na marsz. Jeśli we wtorek twój bieg to: 3 min biegu, 1 minuta marszu, dzisiaj postaraj się wyrównać proporcję 1:1.'),\n" +
                    " (5,'Podchodź swobodnie do przerw na marsz. Jeśli we wtorek twój bieg to: 3 min biegu, 1 minuta marszu, dzisiaj postaraj się wyrównać proporcję 2:1 (gorsze dni: 2min marszu, 2min biegu). Tempo biegu powinno być o 40sek/km wolniejsze niż Twoje zamierzone tempo wyścigowe (ponad 10 min/km).'),\n" +
                    " (6,'Podchodź swobodnie do przerw na marsz. Jeśli we wtorek twój bieg to: 2 min biegu, 1 minuta marszu, dzisiaj postaraj się wyrównać proporcję 2:2 albo 1:1 (gorsze dni: 2min marszu, 1min biegu).Wielu początkujących biegaczy będzie przeważnie trenowało przykładowo: 4 minuty marszu 1 minuta biegu.'),\n" +
                    " (7,'Podchodź swobodnie do przerw na marsz. Jeśli we wtorek twój bieg to: 2 min biegu, 1 minuta marszu, dzisiaj postaraj się wyrównać proporcję 2:2 albo 1:1 (gorsze dni: 2min marszu, 1min biegu).Wielu początkujących biegaczy będzie przeważnie trenowało przykładowo: 3 minuty marszu 1 minuta biegu.'),\n" +
                    // dni szybkosci
                    " (8,'Dzień szybkości. Udaj się na bieżnię i po 5 min rozgrzewce zrób wyznaczoną liczbę okrążeń po 400 m w czasie 3:28 - 3:30. Przemaszeruj wolno pół okrążenia i zrób powtórkę. Na zakończenie zrób 5 min ćwiczeń rozluźnijących.'),\n" +
                    //zawody 2 tyg przed
                    " (9,'Zawody na 5 km 2 tygodnie przed zamierzonymi zawodami na 10 km to \"przymiarka\" przed wielkim dniem. Pamiętaj aby robić takie przerwy na marsz jak w trakcie krótszych biegów w ciągu tygodnia. Jeżeli na 1.5 km przed metą bedziesz czuł się na siłach, możesz zmiejszyć przrwy na marsz.'),\n" +
                    //zawody
                    " (10,'Podczas zawodów proponowana przerwa na marsz co 1.5 km. Większość biegaczy powinna pokonać pierwsze 1.5 - 3 km przeplatając bieg przerwami na marsz. Na ostatnie 1.5 km możesz skrócić te przerwy lub z nich zrezygnować, jeśli czujesz sie na siłach. '),\n" +
                    " (11,' W trakcie samego wyścigu na 10 km większość osób powinna robić przerwy na marsz mniej więcej co 1.5km jeśli tak im wygodnie. Większość biegazy powinna pokonać pierwsze 2-3 km biegając w taki sam sposób, jak podczas biegów długodystansowych. Na ostatnim kilometrze możesz zrezygnować z pewnej ilości przerw, jeśli czujesz się na siłach.'),\n" +
                    " (12,' W trakcie półmaratonu większość osób powinna robić sobie przerwy na marsz co 1,5 km, jeśli tak im wygodnie. Większość biegaczy powinna pokonać pierwsze 18km, robiąc takie same przerwy na marsz jak podczas biegów długodystansowych. Na ostatnie 4 km możesz zrezygnować z części przerw, jeśli czujesz się na siłach.');"
        }
    }

    class Trainings {
        companion object {
            const val TABLE = "trainings"

            const val _ID = "_id"
            const val NAME = "name"
            const val CREATION_DATE = "creation_date"
            const val TRAINING_TYPE = "training_type"
            const val TIME = "time"
            const val YEAR = "year"
            const val MONTH = "month"
            const val WEEK_OF_YEAR = "week_of_year"
            const val DISTANCE = "distance"
            const val AVERAGE_SPEED = "average_speed"
            const val MAX_SPEED = "max_speed"
            const val MIN_ALTITUDE = "min_altitude"
            const val MAX_ALTITUDE = "max_altitude"
            const val CALORIES = "calories"
            const val LOCATION = "location"
            const val WEATHER_TEMPERATURE = "weather_temperature"
            const val WEATHER_WIND_SPEED = "weather_wind"
            const val WEATHER_HUMIDITY = "weather_humidity"
            const val WEATHER_PRESSURE = "weather_pressure"
            const val WEATHER_ICON = "weather_icon"
            const val WEATHER_DESCRIPTION = "weather_description"

            private const val NAME_TYPE = "TEXT"
            private const val CREATION_DATE_TYPE = "TEXT"
            private const val YEAR_TYPE = "INTEGER NOT NULL"
            private const val MONTH_TYPE = "INTEGER NOT NULL"
            private const val WEEK_OF_YEAR_TYPE = "INTEGER NOT NULL"

            private const val TRAINING_TYPE_TYPE = "TEXT NOT NULL"
            private const val TIME_TYPE = "INTEGER NOT NULL"
            private const val DISTANCE_TYPE = "REAL NOT NULL"
            private const val MAX_SPEED_TYPE = "REAL NOT NULL"
            private const val AVERAGE_SPEED_TYPE = "REAL NOT NULL"
            private const val MIN_ALTITUDE_TYPE = "REAL NOT NULL"
            private const val MAX_ALTITUDE_TYPE = "REAL NOT NULL"
            private const val CALORIES_TYPE = "INTEGER NOT NULL"
            private const val LOCATION_TYPE = "TEXT NOT NULL"
            private const val WEATHER_TEMPERATURE_TYPE = "REAL"
            private const val WEATHER_WIND_SPEED_TYPE = "REAL"
            private const val WEATHER_HUMIDITY_TYPE = "INTEGER"
            private const val WEATHER_PRESSURE_TYPE = "INTEGER"
            private const val WEATHER_ICON_TYPE = "TEXT"
            private const val WEATHER_DESCRIPTION_TYPE = "TEXT"
            private const val _ID_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT"

            const val CREATE_STATEMENT = (
                    "CREATE TABLE " + Trainings.TABLE +
                            "(" + " " + _ID + " " + _ID_TYPE +
                            "," + " " + NAME + " " + NAME_TYPE +
                            "," + " " + CREATION_DATE + " " + CREATION_DATE_TYPE +
                            "," + " " + YEAR + " " + YEAR_TYPE +
                            "," + " " + MONTH + " " + MONTH_TYPE +
                            "," + " " + WEEK_OF_YEAR + " " + WEEK_OF_YEAR_TYPE +
                            "," + " " + TRAINING_TYPE + " " + TRAINING_TYPE_TYPE +
                            "," + " " + TIME + " " + TIME_TYPE +
                            "," + " " + DISTANCE + " " + DISTANCE_TYPE +
                            "," + " " + AVERAGE_SPEED + " " + AVERAGE_SPEED_TYPE +
                            "," + " " + MAX_SPEED + " " + MAX_SPEED_TYPE +
                            "," + " " + MIN_ALTITUDE + " " + MIN_ALTITUDE_TYPE +
                            "," + " " + MAX_ALTITUDE + " " + MAX_ALTITUDE_TYPE +
                            "," + " " + CALORIES + " " + CALORIES_TYPE +
                            "," + " " + LOCATION + " " + LOCATION_TYPE +
                            "," + " " + WEATHER_TEMPERATURE + " " + WEATHER_TEMPERATURE_TYPE +
                            "," + " " + WEATHER_WIND_SPEED + " " + WEATHER_WIND_SPEED_TYPE +
                            "," + " " + WEATHER_HUMIDITY + " " + WEATHER_HUMIDITY_TYPE +
                            "," + " " + WEATHER_PRESSURE + " " + WEATHER_PRESSURE_TYPE +
                            "," + " " + WEATHER_ICON + " " + WEATHER_ICON_TYPE +
                            "," + " " + WEATHER_DESCRIPTION + " " + WEATHER_DESCRIPTION_TYPE + ");")
        }
    }

    class Laps {
        companion object {
            val TABLE = "laps"

            val _ID = "_id"
            val TRAINING = "training"
            val TIME = "time"
            val AVERAGE_SPEED = "average_speed"

            private val TRAINING_TYPE = "INTEGER NOT NULL"
            private val TIME_TYPE = "TEXT"
            private val AVERAGE_SPEED_TYPE = "REAL NOT NULL"
            private val _ID_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT"

            internal val CREATE_STATEMENT = (
                    "CREATE TABLE " + Laps.TABLE + "(" + " " + _ID + " " + _ID_TYPE +
                            "," + " " + TRAINING + " " + TRAINING_TYPE +
                            "," + " " + TIME + " " + TIME_TYPE +
                            "," + " " + AVERAGE_SPEED + " " + AVERAGE_SPEED_TYPE +
                            "," + "FOREIGN KEY" + "(" + TRAINING + ")" + "REFERENCES" + " " + Trainings.TABLE + "(" + BaseColumns._ID + ")" + "ON DELETE CASCADE" + ");")
        }
    }

    class Runpoints {
        companion object {
            val TABLE = "runpoints"

            val _ID = "_id"
            /** id lap odpowiadajace id  w tabeli LAPS  */
            val LAP = "lap"
            val TIME = "time"
            val LATITUDE = "latitude"
            val LONGTITUDE = "longtitude"
            /** predkosc w metrach na sekunde  */
            val SPEED = "speed"
            val ACCURACY = "accuracy"
            /** wysokosc  */
            val ALTITUDE = "altitude"
            val BEARING = "bearing"

            private val LAP_TYPE = "INTEGER NOT NULL"
            private val TIME_TYPE = "INTEGER NOT NULL"
            private val LATITUDE_TYPE = "REAL NOT NULL"
            private val LONGTITUDE_TYPE = "REAL NOT NULL"
            private val SPEED_TYPE = "REAL NOT NULL"
            private val ACCURACY_TYPE = "REAL NOT NULL"
            private val ALTITUDE_TYPE = "REAL NOT NULL"
            private val BEARING_TYPE = "REAL NOT NULL"
            private val _ID_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT"

            internal val CREATE_STATEMENT = (
                    "CREATE TABLE " + Runpoints.TABLE + "(" + " " + _ID + " " + _ID_TYPE +
                            "," + " " + LAP + " " + LAP_TYPE +
                            "," + " " + TIME + " " + TIME_TYPE +
                            "," + " " + LATITUDE + " " + LATITUDE_TYPE +
                            "," + " " + LONGTITUDE + " " + LONGTITUDE_TYPE +
                            "," + " " + SPEED + " " + SPEED_TYPE +
                            "," + " " + ACCURACY + " " + ACCURACY_TYPE +
                            "," + " " + ALTITUDE + " " + ALTITUDE_TYPE +
                            "," + " " + BEARING + " " + BEARING_TYPE +
                            "," + "FOREIGN KEY" + "(" + LAP + ")" + "REFERENCES" + " " + Laps.TABLE + "(" + BaseColumns._ID + ")" + "ON DELETE CASCADE" + ");")
        }
    }
}
