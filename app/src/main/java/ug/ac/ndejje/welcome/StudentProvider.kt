package ug.ac.ndejje.welcome

import ug.ac.ndejje.welcome.Student

class StudentProvider {
    companion object {
        val studentList = listOf(
            Student(1, "Akello Stellamaris", "24/2/314/01", "BIT", R.drawable.female_1,   true),
            Student(2, "Kirya James",       "24/2/314/02", "BCS", R.drawable.male_1,     false),
            Student(3, "Mbabazi Joan",      "24/2/314/03", "BIT", R.drawable.female_2, true),
            Student(4, "Kato Johnmary",     "24/2/314/04", "BSE", R.drawable.male_2,     true),
            Student(5,"Akello Peace",       "24/2/306/05", "BCS",R.drawable.female_3,true),
            Student(6,"Ssemugga Alex",      "24/2/314/06", "BIT",R.drawable.male_4,false),
            Student(7,"Wafuli James",       "24/2/306/07", "BSE",R.drawable.male_5,true),
            Student(8,"Namu Joy",           "24/2/314/08", "BIT",R.drawable.female_4,false)
        )
    }

}
