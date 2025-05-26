package com.example.proyectoappgym.entity

enum class Muscles(val nameMuscle: String) {
    UPPER_PECTORALS("Upper pectorals"),
    LOWER_PECTORAL("Lower pectoral"),
    MIDDLE_PECTORAL("Middle pectoral"),
    PECTORALS("Pectorals"),
    DELTOIDS("Deltoids"),
    MIDDLE_DELTOID("Middle deltoid"),
    FRONT_DELTOID("Front deltoid"),
    POSTERIOR_DELTOID("Posterior deltoid"),
    BRACHIALIS("Brachialis"),
    BRACHIORADIALIS("Brachioradialis"),
    INFRASPINATUS("Infraspinatus"),
    TERES_MAJOR("Teres major"),
    TERES_MINOR("Teres minor"),
    TRAPEZIUS("Trapezius"),
    LATISSIMUS_DORSI("Latissimus dorsi"),
    TRICEPS("Triceps"),
    QUADRICEPS("Quadriceps"),
    GLUTEUS("Gluteus"),
    HAMSTRINGS("Hamstrings"),
    CALVES("Calves"),
    GASTROCNEMIUS("Gastrocnemius"),
    ADDUCTORS("Adductors"),
    LOWER_BACK("Lower back"),
    BICEPS("Biceps"),
    ABS("Abs"),
    LOWER_ABS("Lower abs"),
    MIDDLE_ABS("Middle abs"),
    UPPER_ABS("Upper abs"),
    OBLIQUES("Obliques"),
    SARTORIUS("Sartorius"),
    SOLEUS("Soleus"),
    RHOMBOIDS("Rhomboids"),
    ILIOPSOAS("Iliopsoas"),
    TENSOR_FASCIAE_FEMORIS("Tensor fasciae femoris");

    override fun toString(): String {
        return super.toString()
    }}