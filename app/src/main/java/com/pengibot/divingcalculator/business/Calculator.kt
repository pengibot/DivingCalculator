package com.pengibot.divingcalculator.business

class Calculator {

    private var direction:String = ""
    private var flying:Boolean = false
    private var somersaults:String = ""
    private var twists:String = ""
    private var position:String = ""
    private var armstand:Boolean = false

    fun diveDescription(diveNumber:String?):String
    {
        if(diveNumber.isNullOrEmpty()) return "Invalid Dive Code"
        if(diveNumber[0] != '6' && diveNumber.length == 4){ // Non twisting dive 1-4
            when(diveNumber[0]) {
                '1' -> direction = "Forward"
                '2' -> direction = "Back"
                '3' -> direction = "Reverse"
                '4' -> direction = "Inward"
            }
            if(diveNumber[1] == '1') {
                flying = true
            }
            // 3rd digit indicates number of half somersaults
            // TODO: D 1.5.4
            somersaults = diveSomersaults(diveNumber[2].toString())
        } else if(diveNumber[0] == '5') {
            // Twisting Dive
            when(diveNumber[1]) {
                '1' -> direction = "Forward"
                '2' -> direction = "Back"
                '3' -> direction = "Reverse"
                '4' -> direction = "Inward"
            }
            somersaults = diveSomersaults(diveNumber[2].toString())
            // 4th digit indicates the number of half twists
            twists = diveTwists(diveNumber[3].toString())
        } else if(diveNumber[0] == '6') {
            // Armstand Dive
            armstand = true
            when(diveNumber[1]) {
                '0' -> direction = "Armstand Dive"
                '1' -> direction = "Armstand Forward"
                '2' -> direction = "Armstand Back"
                '3' -> direction = "Armstand Reverse"
            }
            somersaults = diveSomersaults(diveNumber[2].toString())
            // 4th digit indicates the number of half twists
            twists = diveTwists(diveNumber[3].toString())
        }

        position = divePosition(diveNumber.last())

        return getDiveDescription()
    }

    private fun getDiveDescription():String {
        return direction + " " + (if(flying) "Flying " else "") + somersaults  + (if(twists.isEmpty()) "" else " ") + twists + " in " + position + " Position"
    }

    private fun divePosition(letter:Char):String {
        when(letter.toUpperCase()) {
            'A' -> return "Straight"
            'B' -> return "Pike"
            'C' -> return "Tuck"
            'D' -> return "Free"
        }
        return ""
    }

    private fun diveSomersaults(diveSomersaults:String):String {
        if(armstand){
            when (diveSomersaults) {
                "1" -> return "1/2 Somersault"
                "2" -> return "1 Somersault"
                "3" -> return "1 1/2 Somersaults"
                "4" -> return "2 Somersaults"
                "5" -> return "2 1/2 Somersaults"
                "6" -> return "3 Somersaults"
                "7" -> return "3 1/2 Somersaults"
                "8" -> return "4 Somersaults"
                "9" -> return "4 1/2 Somersaults"
                "10" -> return "5 Somersaults"
                "11" -> return "5 1/2 Somersaults"
            }
        } else {
            when (diveSomersaults) {
                "1" -> return "Dive"
                "2" -> return "Somersault"
                "3" -> return "1 1/2 Somersaults"
                "4" -> return "2 Somersaults"
                "5" -> return "2 1/2 Somersaults"
                "6" -> return "3 Somersaults"
                "7" -> return "3 1/2 Somersaults"
                "8" -> return "4 Somersaults"
                "9" -> return "4 1/2 Somersaults"
                "10" -> return "5 Somersaults"
                "11" -> return "5 1/2 Somersaults"
            }
        }
        return ""
    }

    private fun diveTwists(diveTwists:String):String {
        when(diveTwists) {
            "0" -> return ""
            "1" -> return "1/2 Twist"
            "2" -> return "1 Twist"
            "3" -> return "1 1/2 Twists"
            "4" -> return "2 Twists"
            "5" -> return "2 1/2 Twists"
            "6" -> return "3 Twists"
            "7" -> return "3 1/2 Twists"
            "8" -> return "4 Twists"
            "9" -> return "4 1/2 Twists"
            "10" -> return "5 Twists"
            "11" -> return "5 1/2 Twists"
        }
        return ""
    }
}