package com.pengibot.divingcalculator.business

class Calculator {

    companion object {
        const val STRAIGHT:String = "Straight"
        const val PIKE:String = "Pike"
        const val TUCK:String = "Tuck"
        const val FREE:String = "Free"
        const val FLY:String = "Fly"

        const val FORWARD:String = "Forward"
        const val BACK:String = "Back"
        const val REVERSE:String = "Reverse"
        const val INWARD:String = "Inward"
        const val ARMSTAND:String = "Armstand"

        const val PLATFORM_1M = "1M"
        const val PLATFORM_3M = "3M"
        const val PLATFORM_5M = "5M"
        const val PLATFORM_7_5M = "7.5M"
        const val PLATFORM_10M = "10M"
    }

    private var direction:String = ""
    private var flying:Boolean = false
    private var somersaults:String = ""
    private var twists:String = ""
    private var position:String = ""
    private var armstand:Boolean = false

    fun diveDescription(diveNumber:String?):String
    {
        if(diveNumber == null) {
            return ""
        }
        else if(diveNumber[0] != '6' && diveNumber.length == 4){ // Non twisting dive 1-4
            when(diveNumber[0]) {
                '1' -> direction = FORWARD
                '2' -> direction = BACK
                '3' -> direction = REVERSE
                '4' -> direction = INWARD
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
                '1' -> direction = FORWARD
                '2' -> direction = BACK
                '3' -> direction = REVERSE
                '4' -> direction = INWARD
            }
            somersaults = diveSomersaults(diveNumber[2].toString())
            // 4th digit indicates the number of half twists
            twists = diveTwists(diveNumber[3].toString())
        } else if(diveNumber[0] == '6') {
            // Armstand Dive
            armstand = true
            when(diveNumber[1]) {
                '0' -> direction = "$ARMSTAND Dive"
                '1' -> direction = "$ARMSTAND $FORWARD"
                '2' -> direction = "$ARMSTAND $BACK"
                '3' -> direction = "$ARMSTAND $REVERSE"
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
            'A' -> return STRAIGHT
            'B' -> return PIKE
            'C' -> return TUCK
            'D' -> return FREE
        }
        return ""
    }

    // TODO: Check this out as it is untidy
    private fun getSomersaultString():String {
        when(somersaults) {
            "1/2 Somersault" -> return "0-1"
            "1 Somersault" -> return "0-1"
            "1 1/2 Somersaults" -> return "1.5"
            "2 Somersaults" -> return "etcs"
            "2 1/2 Somersaults" -> return "etcs"
            "3 Somersaults" -> return "etcs"
            "3 1/2 Somersaults" -> return "etcs"
            "4 Somersaults" -> return "etcs"
            "4 1/2 Somersaults" -> return "etcs"
            "5 Somersaults" -> return "etcs"
            "5 1/2 Somersaults" -> return "etcs"
        }
        return "0"
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

    // TODO: Needs more validation. Very basic at the minute.
    fun validate(dive:String):Boolean {
        val pattern = "^[1-4][0-1][1-9][A-D]|5[1-4][1-9][1-9][A-D]|6[0-3][0-9][0-9]?[A-D]$".toRegex(RegexOption.IGNORE_CASE)
        return pattern.matches(dive)

    }

    // Somersaults
    private val componentA = mutableMapOf(
        PLATFORM_1M to mutableMapOf(
            "0" to 0.9,
            "0.5" to 1.1,
            "1" to 1.2,
            "1.5" to 1.6,
            "2" to 2.0,
            "2.5" to 2.4,
            "3" to 2.7,
            "3.5" to 3.0,
            "4" to 3.3,
            "4.5" to 3.8,
            "5.5" to -99.0),
        PLATFORM_3M to mutableMapOf(
            "0" to 1.0,
            "0.5" to 1.3,
            "1" to 1.3,
            "1.5" to 1.5,
            "2" to 1.8,
            "2.5" to 2.2,
            "3" to 2.3,
            "3.5" to 2.8,
            "4" to 2.9,
            "4.5" to 3.5,
            "5.5" to -99.0),
        PLATFORM_5M to mutableMapOf(
            "0" to 0.9,
            "0.5" to 1.1,
            "1" to 1.2,
            "1.5" to 1.6,
            "2" to 2.0,
            "2.5" to 2.4,
            "3" to  2.7,
            "3.5" to 3.0,
            "4" to -99.0,
            "4.5" to -99.0,
            "5.5" to -99.0),
        PLATFORM_7_5M to mutableMapOf(
            "0" to 1.0,
            "0.5" to 1.3,
            "1" to 1.3,
            "1.5" to 1.5,
            "2" to 1.8,
            "2.5" to 2.2,
            "3" to 2.3,
            "3.5" to 2.8,
            "4" to 3.5,
            "4.5" to 3.5,
            "5.5" to -99.0),
        PLATFORM_10M to mutableMapOf(
            "0" to 1.0,
            "0.5" to 1.3,
            "1" to 1.4,
            "1.5" to 1.5,
            "2" to 1.9,
            "2.5" to 2.1,
            "3" to 2.5,
            "3.5" to 2.7,
            "4" to 3.5,
            "4.5"  to 3.5,
            "5.5" to 4.5)
    )

    // Flight Position
    // For flying dives add fly position (E) to either (B) or (C) Position
    private val componentB = mutableMapOf(
        TUCK to mutableMapOf(
            FORWARD to mutableMapOf(
                "0-1" to 0.1,
                "1.5-2" to 0.0,
                "2.5" to 0.0,
                "3-3.5" to 0.0,
                "4-4.5" to 0.0),
            BACK to mutableMapOf(
                "0-1" to 0.1,
                "1.5-2" to 0.0,
                "2.5" to 0.1,
                "3-3.5" to 0.0,
                "4-4.5" to 0.0),
            REVERSE to mutableMapOf(
                "0-1" to 0.1,
                "1.5-2" to 0.0,
                "2.5" to 0.0,
                "3-3.5" to 0.0,
                "4-4.5" to 0.2),
            INWARD to mutableMapOf(
                "0-1" to -0.3,
                "1.5-2" to 0.1,
                "2.5" to 0.2,
                "3-3.5" to 0.3,
                "4-4.5" to 0.4)
        ),
        PIKE to mutableMapOf(
            FORWARD to mutableMapOf(
                "0-1" to 0.2,
                "1.5-2" to 0.1,
                "2.5" to 0.2,
                "3-3.5" to 0.3,
                "4-4.5" to 0.4),
            BACK to mutableMapOf(
                "0-1" to 0.2,
                "1.5-2" to 0.3,
                "2.5" to 0.3,
                "3-3.5" to 0.3,
                "4-4.5" to 0.4),
            REVERSE to mutableMapOf(
                "0-1" to 0.2,
                "1.5-2" to 0.3,
                "2.5" to 0.2,
                "3-3.5" to 0.3,
                "4-4.5" to 0.5),
            INWARD to mutableMapOf(
                "0-1" to -0.2,
                "1.5-2" to 0.3,
                "2.5" to 0.5,
                "3-3.5" to 0.6,
                "4-4.5" to 0.8)
        ),
        STRAIGHT to mutableMapOf(
            FORWARD to mutableMapOf(
                "0-1" to 0.3,
                "1.5-2" to 0.4,
                "2.5" to 0.6,
                "3-3.5" to -99.0,
                "4-4.5" to -99.0),
            REVERSE to mutableMapOf(
                "0-1" to 0.3,
                "1.5-2" to 0.5,
                "2.5" to 0.7,
                "3-3.5" to -99.0,
                "4-4.5" to -99.0),
            BACK to mutableMapOf(
                "0-1" to 0.3,
                "1.5-2" to 0.6,
                "2.5" to 0.6,
                "3-3.5" to -99.0,
                "4-4.5" to -99.0),
            INWARD to mutableMapOf(
                "0-1" to 0.1,
                "1.5-2" to 0.8,
                "2.5" to -99.0,
                "3-3.5" to -99.0,
                "4-4.5" to -99.0)
        ),
        FREE to mutableMapOf(
            FORWARD to mutableMapOf(
                "0-1" to 0.1,
                "1.5-2" to 0.0,
                "2.5" to 0.0,
                "3-3.5" to 0.0,
                "4-4.5" to -99.0),
            BACK to mutableMapOf(
                "0-1" to 0.1,
                "1.5-2" to -0.1,
                "2.5" to -0.1,
                "3-3.5" to 0.0,
                "4-4.5" to -99.0),
            REVERSE to mutableMapOf(
                "0-1" to 0.1,
                "1.5-2" to -0.1,
                "2.5" to -0.2,
                "3-3.5" to 0.0,
                "4-4.5" to -99.0),
            INWARD to mutableMapOf(
                "0-1" to -0.1,
                "1.5-2" to 0.2,
                "2.5" to 0.4,
                "3-3.5" to -99.0,
                "4-4.5" to -99.0)
        ),
        FLY to mutableMapOf(
            FORWARD to mutableMapOf(
                "0-1" to 0.2,
                "1.5-2" to 0.2,
                "2.5" to 0.3,
                "3-3.5" to 0.4,
                "4-4.5" to -99.0),
            BACK to mutableMapOf(
                "0-1" to 0.1,
                "1.5-2" to 0.2,
                "2.5" to 0.3,
                "3-3.5" to -99.0,
                "4-4.5" to -99.0),
            REVERSE to mutableMapOf(
                "0-1" to 0.1,
                "1.5-2" to 0.2,
                "2.5" to 0.3,
                "3-3.5" to -99.0,
                "4-4.5" to -99.0),
            INWARD to mutableMapOf(
                "0-1" to 0.4,
                "1.5-2" to 0.5,
                "2.5" to 0.7,
                "3-3.5" to -99.0,
                "4-4.5" to -99.0)
        )
    )

    // Twists
    // (1) Dives with ½ somersault and twists can only be executed in positions A, B, or C,
    // (2) Dives with 1 or 1 ½ somersaults and twists can only be executed in position D,
    // (3) Dives with 2 or more somersaults and twists can only be executed in positions B or C
    private val componentC = mutableMapOf(
        FORWARD to mutableMapOf(
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
        BACK to mutableMapOf(
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
        REVERSE to mutableMapOf(
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
        INWARD to mutableMapOf(
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
    private val componentD = mutableMapOf(
        PLATFORM_1M to mutableMapOf(
            "$FORWARD 0.5-3.5" to 0.0,
            "$FORWARD 4-4.5" to 0.5,
            "$BACK 0.5-3" to 0.2,
            "$BACK 3.5-4.5" to 0.6,
            "$REVERSE 0.5-3" to 0.3,
            "$REVERSE 3.5-4.5" to 0.5,
            "$INWARD 0.5-1" to 0.6,
            "$INWARD 1.5-4.5" to 0.5),
        PLATFORM_3M to mutableMapOf(
            "$FORWARD 0.5-3.5" to 0.0,
            "$FORWARD 4-4.5" to 0.3,
            "$BACK 0.5-3" to 0.2,
            "$BACK 3.5-4.5" to 0.4,
            "$REVERSE 0.5-3" to 0.3,
            "$REVERSE 3.5-4.5" to 0.3,
            "$INWARD 0.5-1" to 0.3,
            "$INWARD 1.5-4.5" to 0.3),
        // Arm stand Group (Does not apply to arm stand dives with twists)
        PLATFORM_5M to mutableMapOf(
            "$ARMSTAND $FORWARD 0-2" to 0.2,
            "$ARMSTAND $FORWARD 2.5+" to 0.4,
            "$ARMSTAND $BACK 0-0.5" to 0.2,
            "$ARMSTAND $BACK 1.4" to 0.4,
            "$ARMSTAND $REVERSE 0-0.5" to 0.3,
            "$ARMSTAND $REVERSE 1-4" to 0.5),
        PLATFORM_7_5M to mutableMapOf(
            "$ARMSTAND $FORWARD 0-2" to 0.2,
            "$ARMSTAND $FORWARD 2.5+" to 0.4,
            "$ARMSTAND $BACK 0-0.5" to 0.2,
            "$ARMSTAND $BACK 1.4" to 0.4,
            "$ARMSTAND $REVERSE 0-0.5" to 0.3,
            "$ARMSTAND $REVERSE 1-4" to 0.5),
        PLATFORM_10M to mutableMapOf(
            "$ARMSTAND $FORWARD 0-2" to 0.2,
            "$ARMSTAND $FORWARD 2.5+" to 0.4,
            "$ARMSTAND $BACK 0-0.5" to 0.2,
            "$ARMSTAND $BACK 1.4" to 0.4,
            "$ARMSTAND $REVERSE 0-0.5" to 0.3,
            "$ARMSTAND $REVERSE 1-4" to 0.5)
    )

    // Unnatural Entry
    // (does not apply to twisting dives)
    private val componentE = mutableMapOf(
        FORWARD to mutableMapOf(
            "0.5" to 0.0,
            "1" to 0.1,
            "1.5" to 0.0,
            "2" to 0.2,
            "2.5" to 0.0,
            "3" to 0.2,
            "3.5" to 0.0,
            "4" to 0.2,
            "4.5" to 0.0,
            "5.5" to -99.0),
        INWARD to mutableMapOf(
            "0.5" to 0.0,
            "1" to 0.1,
            "1.5" to 0.0,
            "2" to 0.2,
            "2.5" to 0.0,
            "3" to 0.2,
            "3.5" to 0.0,
            "4" to 0.2,
            "4.5" to 0.0,
            "5.5" to -99.0),
        BACK to mutableMapOf(
            "0.5" to 0.1,
            "1" to 0.0,
            "1.5" to 0.2,
            "2" to 0.0,
            "2.5" to 0.3,
            "3" to 0.0,
            "3.5" to 0.4,
            "4" to 0.0,
            "4.5" to 0.4,
            "5.5" to -99.0),
        REVERSE to mutableMapOf(
            "0.5" to 0.1,
            "1" to 0.0,
            "1.5" to 0.2,
            "2" to 0.0,
            "2.5" to 0.3,
            "3" to 0.0,
            "3.5" to 0.4,
            "4" to 0.0,
            "4.5" to 0.4,
            "5.5" to -99.0)
    )

    // Unnatural Entry
    // (does not apply to twisting dives)
    private val componentE_platform = mutableMapOf(
        "$FORWARD" to mutableMapOf(
            "0.5" to 0.0,
            "1" to 0.1,
            "1.5" to 0.0,
            "2" to 0.2,
            "2.5" to 0.0,
            "3" to 0.2,
            "3.5" to 0.0,
            "4" to 0.0,
            "4.5" to 0.0,
            "5.5" to 0.0),
        "$INWARD" to mutableMapOf(
            "0.5" to 0.0,
            "1" to 0.1,
            "1.5" to 0.0,
            "2" to 0.2,
            "2.5" to 0.0,
            "3" to 0.2,
            "3.5" to 0.0,
            "4" to 0.0,
            "4.5" to 0.0,
            "5.5" to 0.0),
        "$BACK" to mutableMapOf(
            "0.5" to 0.1,
            "1" to 0.0,
            "1.5" to 0.2,
            "2" to 0.0,
            "2.5" to 0.3,
            "3" to 0.0,
            "3.5" to 0.4,
            "4" to 0.0,
            "4.5" to 0.4,
            "5.5" to 0.0),
        "$REVERSE" to mutableMapOf(
            "0.5" to 0.1,
            "1" to 0.0,
            "1.5" to 0.2,
            "2" to 0.0,
            "2.5" to 0.3,
            "3" to 0.0,
            "3.5" to 0.4,
            "4" to 0.0,
            "4.5" to 0.4,
            "5.5" to 0.0),
        "$ARMSTAND $BACK" to mutableMapOf(
            "0.5" to 0.0,
            "1" to 0.1,
            "1.5" to 0.0,
            "2" to 0.2,
            "2.5" to 0.0,
            "3" to 0.2,
            "3.5" to 0.0,
            "4" to 0.3,
            "4.5" to 0.0,
            "5.5" to 0.0),
        "$ARMSTAND $REVERSE" to mutableMapOf(
            "0.5" to 0.0,
            "1" to 0.1,
            "1.5" to 0.0,
            "2" to 0.2,
            "2.5" to 0.0,
            "3" to 0.2,
            "3.5" to 0.0,
            "4" to 0.3,
            "4.5" to 0.0,
            "5.5" to 0.0),
        "$ARMSTAND $FORWARD" to mutableMapOf(
            "0.5" to 0.1,
            "1" to 0.0,
            "1.5" to 0.2,
            "2" to 0.0,
            "2.5" to 0.3,
            "3" to 0.0,
            "3.5" to 0.4,
            "4" to 0.0,
            "4.5" to 0.4,
            "5.5" to 0.0)
    )


    fun difficulty(platform:String?):Double {
        val a:Double? = componentA[platform]!![somersaults]
        val b:Double? = componentB[position]!![direction]!![getSomersaultString()]
        val c:Double? = componentA[platform]!![somersaults]
        val d:Double? = componentA[platform]!![somersaults]
        val e:Double? = componentA[platform]!![somersaults]
        println(componentA["5m"]!!["3.5"])
        println(componentB["Pike"]!!["Inw"]!!["3-3.5"])
        println(componentC["Inw"]!!["4 Twists"])
        println(componentD["3m"]!!["Forward 4-4.5 Som"])
        println(componentE["1m"]!!["4"])
        return a!! + b!! + c!! + d!! + e!!
    }

}