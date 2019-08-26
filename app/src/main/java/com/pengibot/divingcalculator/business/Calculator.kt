package com.pengibot.divingcalculator.business

class Calculator {

    private var direction:String = ""
    private var flying:Boolean = false
    private var somersaults:String = ""
    private var twists:String = ""
    private var position:String = ""
    private var armstand:Boolean = false

    fun validateDiveNumber(diveNumber:String?):Boolean {

        val pattern = "([1-4][0-1]|[5-6][0-1])[1-9][A-D]$".toRegex(RegexOption.IGNORE_CASE)

        val forwardGroup = listOf("101A", "101B", "101C", "101D",
            "102A", "102B", "102C", "102D",
            "103A", "103B", "103C", "103D",
            "104A", "104B", "104C", "104D",
            "105A", "105B", "105C", "105D",
            "106A", "106B", "106C", "106D",
            "107A", "107B", "107C", "107D",
            "108A", "108B", "108C", "108D",
            "109A", "109B", "109C", "109D",
            "112A", "112B", "112C", "112D",
            "112A", "112B", "112C", "112D",
            "112A", "112B", "112C", "112D")

        forwardGroup.forEach { dive ->
            if(!pattern.matches(dive)) {
                println("$dive is not recognised")
            }
        }

        return true
    }

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

    fun validate(dive:String):Boolean {

        // Possible Dives
        // 101A

        val pattern = "^[1-4][0-1][1-9][A-D]|5[1-4][1-9][1-9][A-D]|6[0-3][1-9][1-9][A-D]$".toRegex(RegexOption.IGNORE_CASE)
        return pattern.matches(dive)

    }

    // Somersaults
    val componentA = mutableMapOf(
        "1m" to mutableMapOf("0" to 0.9, "0.5" to 1.1, "1" to 1.2, "1.5" to 1.6, "2" to 2.0, "2.5" to 2.4, "3" to 2.7, "3.5" to 3.0, "4" to 3.3, "4.5" to 3.8, "5.5" to -99.0),
        "3m" to mutableMapOf("0" to 1.0, "0.5" to 1.3, "1" to 1.3, "1.5" to 1.5, "2" to 1.8, "2.5" to 2.2, "3" to 2.3, "3.5" to 2.8, "4" to 2.9, "4.5" to 3.5, "5.5" to -99.0),
        "5m" to mutableMapOf("0" to 0.9, "0.5" to 1.1, "1" to 1.2, "1.5" to 1.6, "2" to 2.0, "2.5" to 2.4,"3" to  2.7, "3.5" to 3.0, "4" to -99.0, "4.5" to -99.0, "5.5" to -99.0),
        "7.5m" to mutableMapOf("0" to 1.0, "0.5" to 1.3, "1" to 1.3, "1.5" to 1.5, "2" to 1.8, "2.5" to 2.2, "3" to 2.3, "3.5" to 2.8, "4" to 3.5, "4.5" to 3.5, "5.5" to -99.0),
        "10m" to mutableMapOf("0" to 1.0, "0.5" to 1.3, "1" to 1.4, "1.5" to 1.5, "2" to 1.9, "2.5" to 2.1, "3" to 2.5, "3.5" to 2.7, "4" to 3.5, "4.5"  to 3.5, "5.5" to 4.5)
    )

    // Flight Position
    // For flying dives add fly position (E) to either (B) or (C) Position
    val componentB = mutableMapOf(
        "Tuck" to mutableMapOf(
            "Fwd" to mutableMapOf("0-1" to 0.1, "1.5-2" to 0.0, "2.5" to 0.0, "3-3.5" to 0.0, "4-4.5" to 0.0),
            "Back" to mutableMapOf("0-1" to 0.1, "1.5-2" to 0.0, "2.5" to 0.1, "3-3.5" to 0.0, "4-4.5" to 0.0),
            "Rev" to mutableMapOf("0-1" to 0.1, "1.5-2" to 0.0, "2.5" to 0.0, "3-3.5" to 0.0, "4-4.5" to 0.2),
            "Inw" to mutableMapOf("0-1" to -0.3, "1.5-2" to 0.1, "2.5" to 0.2, "3-3.5" to 0.3, "4-4.5" to 0.4)
        ),
        "Pike" to mutableMapOf(
            "Fwd" to mutableMapOf("0-1" to 0.2, "1.5-2" to 0.1, "2.5" to 0.2, "3-3.5" to 0.3, "4-4.5" to 0.4),
            "Back" to mutableMapOf("0-1" to 0.2, "1.5-2" to 0.3, "2.5" to 0.3, "3-3.5" to 0.3, "4-4.5" to 0.4),
            "Rev" to mutableMapOf("0-1" to 0.2, "1.5-2" to 0.3, "2.5" to 0.2, "3-3.5" to 0.3, "4-4.5" to 0.5),
            "Inw" to mutableMapOf("0-1" to -0.2, "1.5-2" to 0.3, "2.5" to 0.5, "3-3.5" to 0.6, "4-4.5" to 0.8)
        ),
        "Str" to mutableMapOf(
            "Fwd" to mutableMapOf("0-1" to 0.3, "1.5-2" to 0.4, "2.5" to 0.6, "3-3.5" to -99.0, "4-4.5" to -99.0),
            "Rev" to mutableMapOf("0-1" to 0.3, "1.5-2" to 0.5, "2.5" to 0.7, "3-3.5" to -99.0, "4-4.5" to -99.0),
            "Back" to mutableMapOf("0-1" to 0.3, "1.5-2" to 0.6, "2.5" to 0.6, "3-3.5" to -99.0, "4-4.5" to -99.0),
            "Inw" to mutableMapOf("0-1" to 0.1, "1.5-2" to 0.8, "2.5" to -99.0, "3-3.5" to -99.0, "4-4.5" to -99.0)
        ),
        "Free" to mutableMapOf(
            "Fwd" to mutableMapOf("0-1" to 0.1, "1.5-2" to 0.0, "2.5" to 0.0, "3-3.5" to 0.0, "4-4.5" to -99.0),
            "Back" to mutableMapOf("0-1" to 0.1, "1.5-2" to -0.1, "2.5" to -0.1, "3-3.5" to 0.0, "4-4.5" to -99.0),
            "Rev" to mutableMapOf("0-1" to 0.1, "1.5-2" to -0.1, "2.5" to -0.2, "3-3.5" to 0.0, "4-4.5" to -99.0),
            "Inw" to mutableMapOf("0-1" to -0.1, "1.5-2" to 0.2, "2.5" to 0.4, "3-3.5" to -99.0, "4-4.5" to -99.0)
        ),
        "Fly" to mutableMapOf(
            "Fwd" to mutableMapOf("0-1" to 0.2, "1.5-2" to 0.2, "2.5" to 0.3, "3-3.5" to 0.4, "4-4.5" to -99.0),
            "Back" to mutableMapOf("0-1" to 0.1, "1.5-2" to 0.2, "2.5" to 0.3, "3-3.5" to -99.0, "4-4.5" to -99.0),
            "Rev" to mutableMapOf("0-1" to 0.1, "1.5-2" to 0.2, "2.5" to 0.3, "3-3.5" to -99.0, "4-4.5" to -99.0),
            "Inw" to mutableMapOf("0-1" to 0.4, "1.5-2" to 0.5, "2.5" to 0.7, "3-3.5" to -99.0, "4-4.5" to -99.0)
        )
    )

    // Twists
    // (1) Dives with ½ somersault and twists can only be executed in positions A, B, or C,
    // (2) Dives with 1 or 1 ½ somersaults and twists can only be executed in position D,
    // (3) Dives with 2 or more somersaults and twists can only be executed in positions B or C
    val componentC = mutableMapOf(
        "Fwd" to mutableMapOf(
            "0.5 Twist 0.5-1 Som" to 0.4,
            "0.5 Twist 1.5-2 Som" to 0.4,
            "0.5 Twist 2.5 Som" to 0.4,
            "0.5 Twist 3-3.5 Som" to 0.4,
            "1 Twists" to 0.6,
            "1.5 Twist 0.5-2 Som" to 0.8,
            "1.5 Twist 2.5-3.5 Som" to 0.8,
            "2 Twists" to 1.0,
            "2.5 Twist 0.5-2 Som" to 1.2,
            "2.5 Twist 2.5-3.5 Som" to 1.2,
            "3 Twists" to 1.5,
            "3.5 Twists" to 1.6,
            "4 Twists" to 1.9,
            "4.5 Twists" to 2.0),
        "Back" to mutableMapOf(
            "0.5 Twist 0.5-1 Som" to 0.2,
            "0.5 Twist 1.5-2 Som" to 0.4,
            "0.5 Twist 2.5 Som" to 0.0,
            "0.5 Twist 3-3.5 Som" to 0.0,
            "1 Twists" to 0.4,
            "1.5 Twist 0.5-2 Som" to 0.8,
            "1.5 Twist 2.5-3.5 Som" to 0.7,
            "2 Twists" to 0.8,
            "2.5 Twist 0.5-2 Som" to 1.2,
            "2.5 Twist 2.5-3.5 Som" to 1.1,
            "3 Twists" to 1.4,
            "3.5 Twists" to 1.7,
            "4 Twists" to 1.8,
            "4.5 Twists" to 2.1),
        "Rev" to mutableMapOf(
            "0.5 Twist 0.5-1 Som" to 0.2,
            "0.5 Twist 1.5-2 Som" to 0.4,
            "0.5 Twist 2.5 Som" to 0.0,
            "0.5 Twist 3-3.5 Som" to 0.0,
            "1 Twists" to 0.4,
            "1.5 Twist 0.5-2 Som" to 0.8,
            "1.5 Twist 2.5-3.5 Som" to 0.6,
            "2 Twists" to 0.8,
            "2.5 Twist 0.5-2 Som" to 1.2,
            "2.5 Twist 2.5-3.5 Som" to 1.0,
            "3 Twists" to 1.4,
            "3.5 Twists" to 1.8,
            "4 Twists" to 1.8,
            "4.5 Twists" to 2.1),
        "Inw" to mutableMapOf(
            "0.5 Twist 0.5-1 Som" to 0.2,
            "0.5 Twist 1.5-2 Som" to 0.4,
            "0.5 Twist 2.5 Som" to 0.2,
            "0.5 Twist 3-3.5 Som" to 0.4,
            "1 Twists" to 0.4,
            "1.5 Twist 0.5-2 Som" to 0.8,
            "1.5 Twist 2.5-3.5 Som" to 0.8,
            "2 Twists" to 0.8,
            "2.5 Twist 0.5-2 Som" to 1.2,
            "2.5 Twist 2.5-3.5 Som" to 1.2,
            "3 Twists" to 1.5,
            "3.5 Twists" to 1.6,
            "4 Twists" to 1.9,
            "4.5 Twists" to 2.0)
    )

    // Approach
    val componentD = mutableMapOf(
        "1m" to mutableMapOf(
            "Forward 0.5-3.5 Som" to 0.0,
            "Forward 4-4.5 Som" to 0.5,
            "Back 0.5-3 Som" to 0.2,
            "Back 3.5-4.5 Som" to 0.6,
            "Reverse 0.5-3 Som" to 0.3,
            "Reverse 3.5-4.5 Som" to 0.5,
            "Inward 0.5-1 Som" to 0.6,
            "Inward 1.5-4.5 Som" to 0.5),
        "3m" to mutableMapOf(
            "Forward 0.5-3.5 Som" to 0.0,
            "Forward 4-4.5 Som" to 0.3,
            "Back 0.5-3 Som" to 0.2,
            "Back 3.5-4.5 Som" to 0.4,
            "Reverse 0.5-3 Som" to 0.3,
            "Reverse 3.5-4.5 Som" to 0.3,
            "Inward 0.5-1 Som" to 0.3,
            "Inward 1.5-4.5 Som" to 0.3)
    )

    // Unnatural Entry
    // (does not apply to twisting dives)
    val componentE = mutableMapOf(
        "1m" to mutableMapOf(
            "0.5" to 0.0,
            "1" to 0.1,
            "1.5" to 0.0,
            "2" to 0.2,
            "2.5" to 0.0,
            "3" to 0.2,
            "3.5" to 0.0,
            "4" to 0.2,
            "4.5" to 0.0),
        "3m" to mutableMapOf(
            "0.5" to 0.1,
            "1" to 0.0,
            "1.5" to 0.2,
            "2" to 0.0,
            "2.5" to 0.3,
            "3" to 0.0,
            "3.5" to 0.4,
            "4" to 0.0,
            "4.5" to 0.4)
    )


    fun difficulty(dive:String, platform:String):Double {
        //println(componentA["5m"]!!["3.5"])
        //println(componentB["Pike"]!!["Inw"]!!["3-3.5"])
        //println(componentC["Inw"]!!["4 Twists"])
        //println(componentD["3m"]!!["Forward 4-4.5 Som"])
        //println(componentE["1m"]!!["4"])
        if(platform == "5m") {

        }
        return 0.0
    }
}