package com.example.proyectoappgym.entity

enum class Equipment(val nameEquipment: String) {
    // Calisthenics
    PULL_UP_BAR("pull_up_bar"),
    PARALLETTES("parallettes"),
    MAT("mat"),
    RINGS("rings"),
    WEIGHT_VEST("weight_vest"),
    RESISTANCE_BAND("resistance_band"),
    CHAIRS("chairs"),

    // Cardio / Endurance
    JUMP_ROPE("jump_rope"),
    TRAINING_SHOES("training_shoes"),
    STATIONARY_BIKE("stationary_bike"),
    ROWING_MACHINE("rowing_machine"),
    STAIR_CLIMBER("stair_climber"),
    TREADMILL("treadmill"),
    TIMER("timer"),

    // Machines
    CHEST_PRESS_MACHINE("chest_press_machine"),
    ROW_MACHINE("row_machine"),
    LAT_PULLDOWN_MACHINE("lat_pulldown_machine"),
    SHOULDER_PRESS_MACHINE("shoulder_press_machine"),
    BICEP_CURL_MACHINE("bicep_curl_machine"),
    TRICEP_PUSHDOWN_MACHINE("tricep_pushdown_machine"),
    LEG_PRESS_MACHINE("leg_press_machine"),
    LEG_EXTENSION_MACHINE("leg_extension_machine"),
    LEG_CURL_MACHINE("leg_curl_machine"),
    CALF_RAISE_MACHINE("calf_raise_machine"),
    AB_CRUNCH_MACHINE("ab_crunch_machine"),
    CABLE_MACHINE("cable_machine"),

    // Strength
    BARBELL("barbell"),
    WEIGHT_PLATES("weight_plates"),
    DUMBBELLS("dumbbells"),
    BENCH("bench"),
    POWER_RACK("power_rack"),
    BARBELL_RACK("barbell_rack"),
    PLYO_BOX("plyo_box"),
    MEDICINE_BALL("medicine_ball"),
    KETTLEBELL("kettlebell"),

    // Other
    FLAT_SURFACE("flat_surface"),
    INTERVAL_TIMER_APP("interval_timer_app"),
    BACKPACK("backpack");

    override fun toString(): String = nameEquipment
}