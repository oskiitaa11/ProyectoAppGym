package com.example.proyectoappgym.ui.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.ui.util.fastFilteredMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db.db_auth.AuthRepository
import com.example.proyectoappgym.db.db_questions.QuestionsRegistration
import com.example.proyectoappgym.db.db_routines.AllRoutines
import com.example.proyectoappgym.db.db_users.UserRepository
import com.example.proyectoappgym.entity.exceptions.ErrorException
import com.example.proyectoappgym.entity.functions.getParsedBirthdate
import com.example.proyectoappgym.entity.questions.CALISTHENICS_OR_GYM_QUESTION
import com.example.proyectoappgym.entity.questions.GOALS_QUESTION
import com.example.proyectoappgym.entity.questions.GYM_QUESTION
import com.example.proyectoappgym.entity.questions.HOW_MANY_DAYS_FOR_TRAINING
import com.example.proyectoappgym.entity.questions.PLACE_WHERE_WORKOUT_GYM_QUESTION
import com.example.proyectoappgym.entity.trainingroutines.DayOfWeek
import com.example.proyectoappgym.entity.realizationexercises.Exercise
import com.example.proyectoappgym.entity.realizationexercises.ExercisesName
import com.example.proyectoappgym.entity.realizationexercises.GroupMuscles
import com.example.proyectoappgym.entity.realizationexercises.Muscles
import com.example.proyectoappgym.entity.questions.Question
import com.example.proyectoappgym.entity.questions.TRAINING_TIME_QUESTION
import com.example.proyectoappgym.entity.realizationexercises.RealizationExercise
import com.example.proyectoappgym.entity.trainingroutines.TrainingRoutine
import com.example.proyectoappgym.entity.realizationexercises.TypeExercise
import com.example.proyectoappgym.entity.realizationexercises.TypeTensExercise
import com.example.proyectoappgym.entity.users.User
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlin.collections.all
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.forEach
import kotlin.collections.mapOf
import kotlin.collections.mutableMapOf

private const val MAX_DAYS_TO_TRAIN = 4
private const val MIN_DAYS_TO_TRAIN = 2
private const val LAST_ID_DAY_OF_WEEK = 7
private const val MAX_NUMBERS_OF_MUSCLES_FOR_PUSH_AND_PULL = 4
private const val FIRST_ID_DAY_OF_WEEK = 1
private const val MAX_CONSECUTIVE_DAYS_CALISTHENICS = 3
private const val MIN_REPETITIONS_FOR_MUSCLE = 10
private const val MAX_REPETITIONS_FOR_MUSCLE = 15
private const val MAX_REPETITIONS_FOR_STRENGTH = 8
private const val MIN_REPETITIONS_FOR_STRENGTH = 6
private const val N_REPETITIONS_FOR_NO_PULL_UPS = 20
private const val SETS = 3
private const val REST_TIME = 2

@HiltViewModel
class RegistrationQuestionsViewmodel @Inject constructor(@ApplicationContext val context: Context, private val authRepository: AuthRepository): ViewModel() {
    private var _errorToAddUser: MutableSharedFlow<String?> = MutableStateFlow(null)
    val errorToAddUser = _errorToAddUser.asSharedFlow()
    val allQuestions: List<Question> = QuestionsRegistration.allQuestions
    private var _userWithExistedEmail: MutableSharedFlow<User?> = MutableStateFlow(null)
    val userWithExistedEmail = _userWithExistedEmail.asSharedFlow()

    fun addUser(user: User) {
        val trainingRoutines = getTrainingRoutines(user.allQuestionsAnswered)

        user.addTrainingRoutines(trainingRoutines)
        viewModelScope.launch {
            try {
                _errorToAddUser.emit(null)
                authRepository.signUp(user)
                _errorToAddUser.emit("")
            } catch (e: SocketTimeoutException) {
                _errorToAddUser.emit("Error connecting to the server")
            } catch (e: ErrorException) {
                if (e.error == "email_already_exists")
                    _userWithExistedEmail.emit(user)
                else _errorToAddUser.emit(e.message)
            }

        }
    }

    private fun getDaysOfWeek(isTensExercises: Boolean, isBasicExercises: Boolean, isMachines: Boolean, isWeightlifting: Boolean, daysOfWeek: List<String>): List<DayOfWeek> {
        var possibleIdDaysOfWeek = daysOfWeek.map { DayOfWeek.fromString(it).idDay }.sorted().toMutableList()
        var typesExerciseCount = listOf(isTensExercises, isBasicExercises, isWeightlifting, isMachines).count{ it }
        var possibleDaysOfWeek = mutableListOf<DayOfWeek>()
        var nextIdDayOfWeek: Int

        if (isTensExercises && typesExerciseCount in 2..3) typesExerciseCount += 1
        if ((isWeightlifting || isMachines) && !isTensExercises) {
            if (possibleIdDaysOfWeek.size > MAX_DAYS_TO_TRAIN) {
                possibleIdDaysOfWeek = possibleIdDaysOfWeek.take(MAX_DAYS_TO_TRAIN).toMutableList()
                possibleDaysOfWeek = possibleIdDaysOfWeek.map { DayOfWeek.fromId(it) }.toMutableList()
            } else if (possibleIdDaysOfWeek.size < typesExerciseCount) {
                possibleDaysOfWeek = daysOfWeek.map { DayOfWeek.fromString(it) }.toMutableList()
                possibleDaysOfWeek = getConsecutiveDayForGym(possibleDaysOfWeek.toList(), typesExerciseCount).toMutableList()
            } else {
                possibleDaysOfWeek = possibleIdDaysOfWeek.map { DayOfWeek.fromId(it) }.toMutableList()
            }
        } else if (isTensExercises || isBasicExercises) {
            possibleDaysOfWeek = removeConsecutiveDaysOfWeek(possibleIdDaysOfWeek)

            if (typesExerciseCount > possibleDaysOfWeek.size) {
                possibleDaysOfWeek =
                    getConsecutiveDaysOfWeekForCalisthenics(possibleDaysOfWeek, typesExerciseCount)
            }

            if (possibleDaysOfWeek.count() > MAX_DAYS_TO_TRAIN) {
                possibleDaysOfWeek = possibleDaysOfWeek.take(MAX_DAYS_TO_TRAIN).toMutableList()
            }
        }

        if (possibleDaysOfWeek.size == 1) {
            nextIdDayOfWeek = if (possibleIdDaysOfWeek.first() == LAST_ID_DAY_OF_WEEK) LAST_ID_DAY_OF_WEEK - 1 else possibleIdDaysOfWeek.first() + 1
            possibleDaysOfWeek.add(DayOfWeek.fromId(nextIdDayOfWeek))
        }

        return possibleDaysOfWeek.sortedBy { it.idDay }
    }

    private fun getConsecutiveDaysOfWeekForCalisthenics(
        possibleDaysOfWeek: List<DayOfWeek>,
        typesExerciseCount: Int
    ): MutableList<DayOfWeek> {
        val possibleIdDaysOfWeek: MutableList<Int> = possibleDaysOfWeek.map { it.idDay }.toMutableList()
        var consecutiveNumbersToTrain = 0
        var consecutiveNumbersNotTrain = 0
        var nextNumber: Int
        var lastNumber: Int
        var numberMinusTwo: Int
        val firstIdDayToStart = if (possibleIdDaysOfWeek.contains(LAST_ID_DAY_OF_WEEK)) FIRST_ID_DAY_OF_WEEK + 1 else FIRST_ID_DAY_OF_WEEK

        for (i in firstIdDayToStart..LAST_ID_DAY_OF_WEEK) {
            if (possibleIdDaysOfWeek.size < typesExerciseCount) {
                if (possibleIdDaysOfWeek.contains(i)) {
                    consecutiveNumbersToTrain++
                    consecutiveNumbersNotTrain = 0
                } else {
                    consecutiveNumbersNotTrain++
                    nextNumber = if (i == LAST_ID_DAY_OF_WEEK) 1 else i + 1
                    lastNumber = if (i == FIRST_ID_DAY_OF_WEEK) LAST_ID_DAY_OF_WEEK else i - 1
                    numberMinusTwo = i - 2

                    if (consecutiveNumbersNotTrain == 2) {
                        if (!possibleIdDaysOfWeek.contains(numberMinusTwo)) possibleIdDaysOfWeek.add(lastNumber) else if (!possibleIdDaysOfWeek.contains(nextNumber)){
                            possibleIdDaysOfWeek.add(i)
                            consecutiveNumbersToTrain++
                            consecutiveNumbersNotTrain = 0
                        }
                    } else if (consecutiveNumbersToTrain == 0){
                        if (!possibleIdDaysOfWeek.contains(numberMinusTwo) && !possibleIdDaysOfWeek.contains(nextNumber)){
                            possibleIdDaysOfWeek.add(i)
                            consecutiveNumbersToTrain++
                            consecutiveNumbersNotTrain = 0
                        }
                    } else if (consecutiveNumbersToTrain == 1){
                        if (!possibleIdDaysOfWeek.contains(nextNumber)){
                            possibleIdDaysOfWeek.add(i)
                            consecutiveNumbersToTrain++
                            consecutiveNumbersNotTrain = 0
                        }
                    } else consecutiveNumbersToTrain = 0

                }
            }
        }

        return possibleIdDaysOfWeek.map { DayOfWeek.fromId(it) }.toMutableList()
    }

    private fun removeConsecutiveDaysOfWeek(possibleIdDaysOfWeek: MutableList<Int>): MutableList<DayOfWeek> {
        val possibleDaysOfWeek: MutableList<DayOfWeek> =
            possibleIdDaysOfWeek.map { DayOfWeek.fromId(it) }.toMutableList()
        var lastIdDay = 0
        var countConsecutiveDays = 0
        possibleIdDaysOfWeek.sortBy { it }

        possibleIdDaysOfWeek.forEach { idDay ->
            if ((lastIdDay + 1) == idDay) countConsecutiveDays++

            if (countConsecutiveDays == MAX_CONSECUTIVE_DAYS_CALISTHENICS) {
                possibleDaysOfWeek.removeIf { it.idDay == idDay }
                countConsecutiveDays = 0
            }
            lastIdDay = idDay
        }

        return possibleDaysOfWeek
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getConsecutiveDayForGym(possibleDaysOfWeek: List<DayOfWeek>, typesExerciseCount: Int): List<DayOfWeek> {
        val possibleIdDaysOfWeek = possibleDaysOfWeek.map { it.idDay }.toMutableList()
        var isDayForTraining = false

        for (i in FIRST_ID_DAY_OF_WEEK..LAST_ID_DAY_OF_WEEK) {
            if (possibleIdDaysOfWeek.size < typesExerciseCount) {
                if (possibleIdDaysOfWeek.contains(i)) {
                    isDayForTraining = true
                } else {
                    if (isDayForTraining) possibleIdDaysOfWeek.add(i)

                }
            }
        }

        if (possibleIdDaysOfWeek.size < typesExerciseCount)
            isDayForTraining = false

            for (i in LAST_ID_DAY_OF_WEEK downTo FIRST_ID_DAY_OF_WEEK) {
                if (possibleIdDaysOfWeek.size < typesExerciseCount) {
                    if (possibleIdDaysOfWeek.contains(i)) {
                        isDayForTraining = true
                    } else {
                        if (isDayForTraining) possibleIdDaysOfWeek.add(i)
                    }
                }
            }

        return possibleIdDaysOfWeek.map { DayOfWeek.fromId(it) }
    }

    fun getTrainingRoutines(
        allAnsweredQuestions: Map<String, List<String>>
    ): List<TrainingRoutine> {
        val isGainMoreStrength = responseInThisQuestion(GOALS_QUESTION, "Gain more strength", allAnsweredQuestions)
        val isTensExercises = responseInThisQuestion(GOALS_QUESTION, "Improving in tension exercises", allAnsweredQuestions)
        val isBasicExercises = (responseInThisQuestion(CALISTHENICS_OR_GYM_QUESTION, "Calisthenics", allAnsweredQuestions) &&
                allAnsweredQuestions[GOALS_QUESTION]?.contains("Build more muscle") == true) ||
                (responseInThisQuestion(CALISTHENICS_OR_GYM_QUESTION, "Both", allAnsweredQuestions) && responseInThisQuestion(GOALS_QUESTION, "Gain more strength", allAnsweredQuestions))
        val isMachineExercises = (
                responseInThisQuestion(CALISTHENICS_OR_GYM_QUESTION, "Gym workouts", allAnsweredQuestions) ||
                        responseInThisQuestion(CALISTHENICS_OR_GYM_QUESTION, "Both", allAnsweredQuestions)
                ) &&
                responseInThisQuestion(GOALS_QUESTION, "Build more muscle", allAnsweredQuestions) &&
                responseInThisQuestion(GYM_QUESTION, "At the gym", allAnsweredQuestions)
        val isWeightlifting = (responseInThisQuestion(CALISTHENICS_OR_GYM_QUESTION, "Gym workouts", allAnsweredQuestions) ||
                responseInThisQuestion(CALISTHENICS_OR_GYM_QUESTION, "Both", allAnsweredQuestions)) &&
                isGainMoreStrength
        val trainingRoutines = mutableListOf<TrainingRoutine>()
        val workoutAtHomeToBuildMuscle = responseInThisQuestion(PLACE_WHERE_WORKOUT_GYM_QUESTION, "With my equipments at home", allAnsweredQuestions) &&
                responseInThisQuestion(GOALS_QUESTION, "Build more muscle", allAnsweredQuestions)
        val daysOfWeekForTraining = getDaysOfWeek(isTensExercises, isBasicExercises, isMachineExercises || workoutAtHomeToBuildMuscle, isWeightlifting, allAnsweredQuestions[HOW_MANY_DAYS_FOR_TRAINING] ?: emptyList<String>())
        var musclesForDay =
            mutableMapOf<DayOfWeek, MutableMap<TypeExercise, MutableList<Muscles>>>()
        var exercisesForActualTraining: MutableList<Exercise>
        var trainingName: String
        var musclesToTrain: MutableMap<TypeExercise, MutableList<Muscles>>
        val isBeginner = responseInThisQuestion(TRAINING_TIME_QUESTION, "I just started with MyFitnessApp", allAnsweredQuestions)
        var exercisesForCalisthenics = mapOf<DayOfWeek, List<Exercise>>()
        val musclesToWorkoutAtHome = mutableMapOf<DayOfWeek, List<Muscles>>()
        var excludedWeightliftingExercises: List<Exercise>

        if (isTensExercises || isBasicExercises) {
            exercisesForCalisthenics = getExercisesForTypeTens(daysOfWeekForTraining, isWeightlifting, isMachineExercises, isTensExercises, isBasicExercises, isBeginner)
        }

        if (isWeightlifting || isMachineExercises || workoutAtHomeToBuildMuscle) {
            musclesToTrain = getMusclesForDay(isWeightlifting, isMachineExercises, isTensExercises, isBasicExercises, workoutAtHomeToBuildMuscle, daysOfWeekForTraining.size)
            musclesForDay = getTrainingForDays(daysOfWeekForTraining, musclesToTrain, isTensExercises, isMachineExercises || workoutAtHomeToBuildMuscle)
            musclesForDay.forEach {
                musclesToWorkoutAtHome[it.key] =
                    it.value.remove(TypeExercise.WEIGHTLIFTING_AT_HOME)?.toList() ?: emptyList()
            }
            addExercisesToTrainingRoutines(musclesForDay, workoutAtHomeToBuildMuscle, exercisesForCalisthenics) { trainingRoutines.add(it) }

            if (isWeightlifting) addExercisesPullUpAndDip(trainingRoutines, isBeginner)

            excludedWeightliftingExercises = trainingRoutines.flatMap { it.exercises.filter { it.exercise.type == TypeExercise.WEIGHTLIFTING && it.exercise.name != ExercisesName.DUMBBELL_SHRUGS.exercise.name } }.map { it.exercise }

            addTrainingRoutineForWeightliftingHome(musclesToWorkoutAtHome, excludedWeightliftingExercises) { realizationExercises, dayOfWeek ->
                trainingRoutines.find { it.dayOfWeek == dayOfWeek }?.addExercises(realizationExercises) ?: return@addTrainingRoutineForWeightliftingHome
            }
        }

        addCalisthenicsExercisesFromDifferentDaysOfGym(exercisesForCalisthenics, musclesForDay.keys.toList()) { realizationExercises, dayOfWeek ->
            trainingRoutines.add(TrainingRoutine(dayOfWeek, "", realizationExercises))
        }

        addLegsAndCoreExercises(trainingRoutines, daysOfWeekForTraining.size, isWeightlifting, isMachineExercises,
            isBasicExercises, isTensExercises, workoutAtHomeToBuildMuscle)

        trainingRoutines.forEach {
            exercisesForActualTraining = it.exercises.map { it.exercise }.toMutableList()
            trainingName = getTrainingName(exercisesForActualTraining)
            it.changeName(trainingName)
        }

        return trainingRoutines.sortedBy { it.dayOfWeek?.idDay ?: 0 }
    }

    private fun getMusclesForDay(isWeightlifting: Boolean, isMachineExercises: Boolean, isTensExercises: Boolean, isBasicExercises: Boolean, workoutAtHome: Boolean, daysOfWeekForTrainingSize: Int): MutableMap<TypeExercise, MutableList<Muscles>> {
        val musclesToTrain = mutableMapOf<TypeExercise, MutableList<Muscles>>()
        val initialMusclesForGym = mutableListOf(
            Muscles.PECTORALS, Muscles.TRICEPS, Muscles.FRONT_DELTOID,
            Muscles.LATISSIMUS_DORSI, Muscles.BICEPS, Muscles.TRAPEZIUS
        )
        var typeExerciseForMachinesOrAtHome: TypeExercise

        if (isWeightlifting) {
            musclesToTrain[TypeExercise.WEIGHTLIFTING] = initialMusclesForGym
            if (isMachineExercises || workoutAtHome) {
                typeExerciseForMachinesOrAtHome = if (isMachineExercises) TypeExercise.MACHINES else TypeExercise.WEIGHTLIFTING_AT_HOME

                if (isTensExercises && daysOfWeekForTrainingSize == 3) {
                    removeMuscles(
                        mutableListOf(Muscles.TRICEPS, Muscles.BICEPS), musclesToTrain, typeExerciseForMachinesOrAtHome
                    )
                    removeMuscles(mutableListOf(Muscles.LATISSIMUS_DORSI, Muscles.UPPER_PECTORALS), musclesToTrain, TypeExercise.WEIGHTLIFTING)
                } else  {
                    removeMuscles(mutableListOf(Muscles.TRAPEZIUS), musclesToTrain, TypeExercise.WEIGHTLIFTING)

                    addMuscles(
                        listOf(
                            Muscles.UPPER_PECTORALS,
                            Muscles.LATISSIMUS_DORSI,
                            Muscles.MIDDLE_DELTOID,
                            Muscles.TRAPEZIUS,
                            Muscles.BICEPS
                        ), musclesToTrain, typeExerciseForMachinesOrAtHome
                    )

                    if (!isBasicExercises) {
                        addMuscles(
                            listOf(Muscles.TRICEPS), musclesToTrain, typeExerciseForMachinesOrAtHome
                        )
                    }

                }

            } else if (daysOfWeekForTrainingSize == 3) {
                if (isTensExercises)
                    removeMuscles(mutableListOf(Muscles.UPPER_PECTORALS,
                        Muscles.TRICEPS), musclesToTrain,
                        TypeExercise.WEIGHTLIFTING)
                else if (!isBasicExercises)
                    addMuscles(mutableListOf(Muscles.MIDDLE_DELTOID, Muscles.PECTORALS, Muscles.LATISSIMUS_DORSI,
                        Muscles.LATISSIMUS_DORSI), musclesToTrain, TypeExercise.WEIGHTLIFTING)
            } else if (daysOfWeekForTrainingSize == 4) {
                if (!isTensExercises && !isBasicExercises)
                    addMuscles(listOf(Muscles.UPPER_PECTORALS, Muscles.LATISSIMUS_DORSI, Muscles.MIDDLE_DELTOID,
                        Muscles.TRAPEZIUS, Muscles.FRONT_DELTOID), musclesToTrain,
                        TypeExercise.WEIGHTLIFTING)
            }
        } else if ((isMachineExercises || workoutAtHome)) {
            typeExerciseForMachinesOrAtHome = if (isMachineExercises) TypeExercise.MACHINES else TypeExercise.WEIGHTLIFTING_AT_HOME

            musclesToTrain[typeExerciseForMachinesOrAtHome] = initialMusclesForGym
            if (daysOfWeekForTrainingSize == 2)
                addMuscles(listOf(Muscles.TRAPEZIUS, Muscles.LATISSIMUS_DORSI, Muscles.UPPER_PECTORALS), musclesToTrain, typeExerciseForMachinesOrAtHome)
            else if (daysOfWeekForTrainingSize == 3) {
                if (isTensExercises) addMuscles(listOf(Muscles.TRAPEZIUS, Muscles.LATISSIMUS_DORSI, Muscles.UPPER_PECTORALS), musclesToTrain, typeExerciseForMachinesOrAtHome)
                else {
                    addMuscles(listOf(Muscles.TRAPEZIUS, Muscles.LATISSIMUS_DORSI, Muscles.UPPER_PECTORALS), musclesToTrain, typeExerciseForMachinesOrAtHome)

                    if (!isBasicExercises) {
                        addMuscles(listOf(Muscles.MIDDLE_DELTOID, Muscles.BICEPS, Muscles.UPPER_PECTORALS,
                            Muscles.TRICEPS, Muscles.TRAPEZIUS), musclesToTrain,
                            TypeExercise.WEIGHTLIFTING_AT_HOME)
                    }
                }
            } else {
                if (!isTensExercises) {
                    addMuscles(initialMusclesForGym, musclesToTrain,
                        TypeExercise.WEIGHTLIFTING_AT_HOME)
                    removeMuscles(mutableListOf(Muscles.TRAPEZIUS), musclesToTrain, TypeExercise.WEIGHTLIFTING_AT_HOME)
                    addMuscles(listOf(Muscles.LATISSIMUS_DORSI, Muscles.MIDDLE_DELTOID), musclesToTrain, TypeExercise.WEIGHTLIFTING_AT_HOME)
                }
            }
        }

        return musclesToTrain
    }

    private fun addExercisesToTrainingRoutines(musclesForDay: MutableMap<DayOfWeek, MutableMap<TypeExercise, MutableList<Muscles>>>, workoutAtHomeToBuildMuscle: Boolean, exercisesForCalisthenics:Map<DayOfWeek, List<Exercise>>, addTrainingRoutine: (TrainingRoutine) -> Unit){
        val exercisesForActualTraining = mutableListOf<Exercise>()
        var realizationExercisesInLoop: List<RealizationExercise>
        var allExercisesToWorkOut: MutableList<Exercise>
        var exercisesToAddActualTraining: MutableList<Exercise>

        musclesForDay.forEach { (dayOfWeek, musclesForType) ->
            musclesForType.forEach { (typeExercise, muscles) ->
                allExercisesToWorkOut = ExercisesName.entries.fastFilteredMap({ it.exercise.type == typeExercise }, { it.exercise }).toMutableList()
                allExercisesToWorkOut.removeIf{ it.name == ExercisesName.WEIGHTED_DIP.exercise.name || it.name == ExercisesName.PULL_UPS_WEIGHTED.exercise.name }
                if (workoutAtHomeToBuildMuscle) allExercisesToWorkOut.remove(ExercisesName.DUMBBELL_SHRUGS.exercise)
                if (isDayForPullAndPush(muscles)) allExercisesToWorkOut.reverse()
                exercisesToAddActualTraining = getExercisesForMuscles(
                    allExercisesToWorkOut,
                    muscles
                )
                exercisesForActualTraining.addAll(
                    exercisesToAddActualTraining
                )
            }
            exercisesForActualTraining += exercisesForCalisthenics[dayOfWeek] ?: emptyList()
            realizationExercisesInLoop = getRealizationExercises(exercisesForActualTraining, false)
            exercisesForActualTraining.clear()
            addTrainingRoutine(TrainingRoutine(dayOfWeek, "", realizationExercisesInLoop))
        }
    }

    private fun addTrainingRoutineForWeightliftingHome(musclesAtHomeForDay: MutableMap<DayOfWeek, List<Muscles>>, exercisesInRoutine: List<Exercise>, addExercises: (List<RealizationExercise>, DayOfWeek) -> Unit) {
        var allExercises = ExercisesName.entries.fastFilteredMap({ it.exercise.type == TypeExercise.WEIGHTLIFTING && it.exercise !in exercisesInRoutine }, { it.exercise })
        var exercisesToAdd: List<Exercise>
        var realizationExercises: List<RealizationExercise>

        if (musclesAtHomeForDay.isEmpty()) return
        musclesAtHomeForDay.forEach { (dayOfWeek, muscles) ->
            exercisesToAdd = getExercisesForMuscles(allExercises, muscles)
            realizationExercises = getRealizationExercises(exercisesToAdd, true)
            addExercises(realizationExercises, dayOfWeek)
            if (isDayForPullAndPush(muscles)) allExercises = allExercises.reversed()
        }
    }

    private fun addCalisthenicsExercisesFromDifferentDaysOfGym(
        exercisesForCalisthenics: Map<DayOfWeek, List<Exercise>>,
        daysOfWeekForGym: List<DayOfWeek>,
        addExercises: (List<RealizationExercise>, DayOfWeek) -> Unit
    ) {
        var realizationsExercisesToAdd: List<RealizationExercise>
        val calisthenicsExercisesOfDifferentDays = exercisesForCalisthenics.filter { it.key !in daysOfWeekForGym }

        calisthenicsExercisesOfDifferentDays.forEach { (dayOfWeek, exercises) ->
            realizationsExercisesToAdd = getRealizationExercises(exercises, false)
            addExercises(realizationsExercisesToAdd, dayOfWeek)
        }
    }

    private fun addLegsAndCoreExercises(
        trainingRoutines: List<TrainingRoutine>,
        dayOfWeekSize: Int,
        isWeightlifting: Boolean,
        isMachines: Boolean,
        isBasicCalisthenics: Boolean,
        isTensCalisthenics: Boolean,
        workoutAtHome: Boolean
    ) {
        var trainingRoutineToAddExercisesLegs: TrainingRoutine? = null
        var trainingRoutineToAddExercisesCore: TrainingRoutine? = null
        var trainingRoutineToAddExercisesCoreAndLegs: TrainingRoutine? = null

        when (dayOfWeekSize) {
            2 -> {

                if (!isTensCalisthenics) {
                    trainingRoutineToAddExercisesCore = findTrainingRoutineFromGroupMuscles(GroupMuscles.PULL_UP, trainingRoutines)

                    addRealizationExercisesForCore(
                        trainingRoutineToAddExercisesCore, isMachines, isWeightlifting, isBasicCalisthenics, workoutAtHome
                    )
                }

                trainingRoutineToAddExercisesLegs = findTrainingRoutineFromGroupMuscles(GroupMuscles.PUSH_UP, trainingRoutines)

                addRealizationExercisesForLegs(
                    trainingRoutineToAddExercisesLegs, isMachines, isWeightlifting, isBasicCalisthenics, workoutAtHome, dayOfWeekSize
                )

            }

            3 -> {
                if (isTensCalisthenics) {
                    trainingRoutineToAddExercisesLegs =
                        trainingRoutines.minByOrNull { it.exercises.size }
                } else {
                    trainingRoutineToAddExercisesCoreAndLegs =
                        trainingRoutines.find { it.exercises.isEmpty() } ?: TrainingRoutine()
                }

                addRealizationExercisesForLegs(
                    trainingRoutineToAddExercisesCoreAndLegs ?: (trainingRoutineToAddExercisesLegs ?: TrainingRoutine()), isMachines, isWeightlifting, isBasicCalisthenics, workoutAtHome, dayOfWeekSize
                )
                if (trainingRoutineToAddExercisesCoreAndLegs != null)
                    addRealizationExercisesForCore(
                        trainingRoutineToAddExercisesCoreAndLegs, isMachines, isWeightlifting, isBasicCalisthenics, workoutAtHome
                    )
            }

            4 -> {
                if (isTensCalisthenics) {
                    trainingRoutineToAddExercisesLegs =
                        trainingRoutines.find { it.exercises.isEmpty() }
                            ?: trainingRoutines.minBy { it.exercises.size }
                } else {
                    trainingRoutineToAddExercisesCore =
                        trainingRoutines.find { it.exercises.isEmpty() } ?: TrainingRoutine()
                    trainingRoutineToAddExercisesLegs =
                        trainingRoutines.find { it.exercises.isEmpty() }
                            ?: TrainingRoutine()
                }

                addRealizationExercisesForLegs(
                    trainingRoutineToAddExercisesLegs, isMachines, isWeightlifting, isBasicCalisthenics, workoutAtHome, dayOfWeekSize
                )
                if (trainingRoutineToAddExercisesCore != null)
                    addRealizationExercisesForCore(
                        trainingRoutineToAddExercisesCore, isMachines, isWeightlifting, isBasicCalisthenics, workoutAtHome
                    )
            }
        }
    }

    private fun findTrainingRoutineFromGroupMuscles(groupMuscles: GroupMuscles, trainingRoutines: List<TrainingRoutine>): TrainingRoutine {
        var predominantMuscleGroup: GroupMuscles?
        var exercisesInRoutine: List<Exercise>
        val typeTensExercise = if (groupMuscles == GroupMuscles.PUSH_UP) TypeTensExercise.PUSH_UP else TypeTensExercise.PULL_UP

        return trainingRoutines.first {
            exercisesInRoutine = it.exercises.map { it.exercise }
            predominantMuscleGroup = getGroupFromLargestNumberOfPushOrPull(exercisesInRoutine)
            if(predominantMuscleGroup != null) predominantMuscleGroup == groupMuscles
            else it.exercises.any { it.exercise.typeTensExercise == typeTensExercise }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun addRealizationExercisesForLegs(
        legTrainingRoutine: TrainingRoutine,
        isMachines: Boolean,
        isWeightlifting: Boolean,
        isCalisthenics: Boolean,
        workoutAtHome: Boolean,
        daysOfWeekSize: Int
    ) {
        val newRealizationExercises = mutableListOf<RealizationExercise>()
        var numbersOfExercises: Int
        var exercisesFromWeightliftingAndMachines: List<ExercisesName>
            (if (isWeightlifting && isMachines) {
                numbersOfExercises = if (daysOfWeekSize == 2) 2 else 3
                exercisesFromWeightliftingAndMachines =  AllRoutines.weightliftingLegExercises.takeLast(numbersOfExercises) +
                        AllRoutines.machinesLegsExercises.takeLast(numbersOfExercises)

            }
            //Se cogen los ultimos 3 o 2 ejercicios porque son los que mejor se adaptan a la rutina
            else if (isWeightlifting) exercisesFromWeightliftingAndMachines = AllRoutines.weightliftingLegExercises
            else if (isCalisthenics && !isMachines) exercisesFromWeightliftingAndMachines = AllRoutines.calisthenicsLegsExercises
            else exercisesFromWeightliftingAndMachines = AllRoutines.machinesLegsExercises)

        newRealizationExercises.addAll(
            getRealizationExercises(exercisesFromWeightliftingAndMachines.map {
                it.exercise
            }, workoutAtHome)
        )
        if (!(isWeightlifting && isMachines)) newRealizationExercises.forEach { it.changeSets(it.series + 1) }
        legTrainingRoutine.addExercises(newRealizationExercises)
    }

    private fun addRealizationExercisesForCore(
        coreTrainingRoutine: TrainingRoutine,
        isMachines: Boolean,
        isWeightlifting: Boolean,
        isCalisthenics: Boolean,
        workoutAtHome: Boolean
    ) {
        val newRealizationExercises = mutableListOf<RealizationExercise>()
        val exercisesFromWeightliftingAndMachines =
            (if (isWeightlifting && !isMachines) AllRoutines.weightliftingCoreExercises
            else if (isCalisthenics && !isWeightlifting) AllRoutines.calisthenicsCoreExercises else AllRoutines.weightliftingCoreExercises.take(3) +
                    AllRoutines.machinesCoreExercises.takeLast(1)).map { it.exercise }

        newRealizationExercises.addAll(
            getRealizationExercises(exercisesFromWeightliftingAndMachines, workoutAtHome)
        )
        if (!(isWeightlifting && isMachines)) newRealizationExercises.forEach { it.changeSets(it.series + 1) }
        coreTrainingRoutine.addExercises(newRealizationExercises)
    }

    private fun addMuscles(
        musclesToAdd: List<Muscles>,
        musclesListToAdd: MutableMap<TypeExercise, MutableList<Muscles>>,
        typeExercise: TypeExercise
    ) {
        musclesListToAdd[typeExercise]?.addAll(musclesToAdd) ?: musclesListToAdd.getOrPut(typeExercise) { musclesToAdd.toMutableList() }
    }

    private fun removeMuscles(
        musclesToRemove: MutableList<Muscles>,
        musclesListToRemoveMuscles: MutableMap<TypeExercise, MutableList<Muscles>>,
        typeExercise: TypeExercise
    ) {
        musclesToRemove.forEach { muscleToRemove ->
            musclesListToRemoveMuscles.getValue(typeExercise).remove(muscleToRemove)
        }
    }

    private fun addExercisesPullUpAndDip(trainingRoutines: List<TrainingRoutine>, isBeginner: Boolean) {
        val trainingRoutineForPushUp = trainingRoutines.find { trainingRoutine -> trainingRoutine.exercises.any { realizationExercise -> realizationExercise.exercise.type == TypeExercise.WEIGHTLIFTING && realizationExercise.exercise.trainedPrimaryMuscles.all { it.groupMuscles == GroupMuscles.PUSH_UP } } } ?: return
        val trainingRoutineForPullUp = trainingRoutines.find { trainingRoutine -> trainingRoutine.exercises.any { realizationExercise -> realizationExercise.exercise.type == TypeExercise.WEIGHTLIFTING && realizationExercise.exercise.trainedPrimaryMuscles.all { it.groupMuscles == GroupMuscles.PULL_UP } } } ?: return
        val realizationExerciseExample = trainingRoutineForPullUp.exercises.first { it.exercise.type == TypeExercise.WEIGHTLIFTING }
        val realizationExerciseDip = RealizationExercise(if(isBeginner) ExercisesName.DIPS.exercise else ExercisesName.WEIGHTED_DIP.exercise, realizationExerciseExample.series, realizationExerciseExample.maxRepetitions, realizationExerciseExample.minRepetitions, realizationExerciseExample.restBetweenSeries)
        val realizationExercisePullUp = RealizationExercise(if(isBeginner) ExercisesName.PULL_UPS.exercise else ExercisesName.PULL_UPS_WEIGHTED.exercise, realizationExerciseExample.series, realizationExerciseExample.maxRepetitions, realizationExerciseExample.minRepetitions, realizationExerciseExample.restBetweenSeries)

        trainingRoutineForPushUp.addExercises(listOf(realizationExerciseDip))
        trainingRoutineForPullUp.addExercises(listOf(realizationExercisePullUp))
    }

    private fun getTrainingForDays(daysOfWeekForTraining: List<DayOfWeek>, muscles: MutableMap<TypeExercise, MutableList<Muscles>>, isTensionResponse: Boolean, isMachines: Boolean): MutableMap<DayOfWeek, MutableMap<TypeExercise, MutableList<Muscles>>> {
        val daysOfWeekSize = daysOfWeekForTraining.size
        var musclesByDayOfWeek: MutableMap<DayOfWeek, MutableMap<TypeExercise, MutableList<Muscles>>>
        var numbersOfExercises: Int

        musclesByDayOfWeek = mutableMapOf(
            daysOfWeekForTraining[0] to muscles.mapValues { (_, musclesForType) ->
                musclesForType.filter { it.groupMuscles == GroupMuscles.PUSH_UP }.toMutableList()
            }.toMutableMap(),
            daysOfWeekForTraining[1] to muscles.mapValues { (_, musclesForType) ->
                musclesForType.filter { it.groupMuscles == GroupMuscles.PULL_UP }.toMutableList()
            }.toMutableMap()
        )

        if (daysOfWeekSize == 2) {
            return musclesByDayOfWeek
        } else if (daysOfWeekSize == 3) {
            if (isTensionResponse) {
                musclesByDayOfWeek = mutableMapOf(
                    daysOfWeekForTraining[0] to mutableMapOf(),
                    daysOfWeekForTraining[1] to mutableMapOf(),
                    daysOfWeekForTraining[2] to muscles
                )
            } else {
                musclesByDayOfWeek[daysOfWeekForTraining[2]] = mutableMapOf()
            }
        } else {
            if (isTensionResponse) {
                musclesByDayOfWeek = mutableMapOf(
                    daysOfWeekForTraining[2] to muscles.mapValues { (_, musclesForType) ->
                        musclesForType.filter { it.groupMuscles == GroupMuscles.PUSH_UP }.toMutableList()
                    }.toMutableMap(),
                    daysOfWeekForTraining[3] to muscles.mapValues { (_, musclesForType) ->
                        musclesForType.filter { it.groupMuscles == GroupMuscles.PULL_UP }.toMutableList()
                    }.toMutableMap()
                )
            } else {
                numbersOfExercises = if (isMachines) 2 else 3

                musclesByDayOfWeek[daysOfWeekForTraining[2]] = muscles.mapValues { (_, musclesForType) ->
                    (musclesForType.filter { it.groupMuscles == GroupMuscles.PULL_UP }.take(numbersOfExercises) +
                            musclesForType.filter { it.groupMuscles == GroupMuscles.PUSH_UP }.take(numbersOfExercises)).toMutableList()
                }.toMutableMap()
                musclesByDayOfWeek[daysOfWeekForTraining[3]] = mutableMapOf()
            }
        }

        return musclesByDayOfWeek
    }

    private fun getExercisesForTypeTens(
        daysOfWeek: List<DayOfWeek>,
        isWeightlifting: Boolean,
        isMachines: Boolean,
        isTensExercise: Boolean,
        isBasicCalisthenics: Boolean,
        isBeginner: Boolean
    ): Map<DayOfWeek, List<Exercise>> {
        val daysOfWeekSize = daysOfWeek.size
        var allTensExercises = if (isBeginner) AllRoutines.tensExercises.filter { it != ExercisesName.MALTESE_PLANK_PUSH_UP && it != ExercisesName.MALTESE_FRONT_LEVER_PRESS } else AllRoutines.tensExercises
        val exercisesNameForDay = mutableMapOf<DayOfWeek, List<ExercisesName>>()
        var basicExercisesForPushUp = AllRoutines.calisthenicsExerciseBasic.filter { it.exercise.typeTensExercise == TypeTensExercise.PUSH_UP && it != ExercisesName.DIPS }
        var basicExercisesForPullUp = AllRoutines.calisthenicsExerciseBasic.filter { it.exercise.typeTensExercise == TypeTensExercise.PULL_UP && it != ExercisesName.PULL_UPS }
        var mutableExercisesNameInLoop: MutableList<ExercisesName>
        var exercisesForPushAndPull: List<ExercisesName>

        if (daysOfWeekSize <= MIN_DAYS_TO_TRAIN) {
            if (isTensExercise) {
                exercisesNameForDay[daysOfWeek[0]] = allTensExercises.filter { it.exercise.typeTensExercise == TypeTensExercise.PUSH_UP }.take(2)
                exercisesNameForDay[daysOfWeek[1]] = allTensExercises.filter { it.exercise.typeTensExercise == TypeTensExercise.PULL_UP }
            } else {
                basicExercisesForPushUp = AllRoutines.calisthenicsExerciseBasic.filter { it.exercise.typeTensExercise == TypeTensExercise.PUSH_UP }
                basicExercisesForPullUp = AllRoutines.calisthenicsExerciseBasic.filter { it.exercise.typeTensExercise == TypeTensExercise.PULL_UP }

                if (!isWeightlifting && !isMachines) {
                    exercisesNameForDay[daysOfWeek[0]] = basicExercisesForPushUp
                    exercisesNameForDay[daysOfWeek[1]] = basicExercisesForPullUp
                } else {
                    exercisesNameForDay[daysOfWeek[0]] = basicExercisesForPushUp.filter { it != ExercisesName.DIPS }.take(2)
                    exercisesNameForDay[daysOfWeek[1]] = basicExercisesForPullUp.filter { it != ExercisesName.PULL_UPS }.take(2)
                }
            }

        } else {
            if (daysOfWeekSize == 3) {
                if (isWeightlifting || isMachines) {
                    if (isTensExercise) {
                        exercisesNameForDay[daysOfWeek[0]] = allTensExercises.filter { it.exercise.typeTensExercise == TypeTensExercise.PUSH_UP }
                        exercisesNameForDay[daysOfWeek[1]] = allTensExercises.filter { it.exercise.typeTensExercise == TypeTensExercise.PULL_UP }
                    } else if (isBasicCalisthenics) {
                        exercisesNameForDay[daysOfWeek[0]] = basicExercisesForPushUp.take(2)
                        exercisesNameForDay[daysOfWeek[1]] = basicExercisesForPullUp.take(2)
                    }
                } else {
                    if (isTensExercise) {
                        exercisesNameForDay[daysOfWeek[0]] = allTensExercises.filter { it.exercise.typeTensExercise == TypeTensExercise.PUSH_UP }
                        exercisesNameForDay[daysOfWeek[1]] = allTensExercises.filter { it.exercise.typeTensExercise == TypeTensExercise.PULL_UP }

                        if (isBasicCalisthenics) {
                            exercisesNameForDay[daysOfWeek[0]] = exercisesNameForDay[daysOfWeek[0]]?.toMutableList().apply { this?.addAll(basicExercisesForPushUp.take(2)) } ?: basicExercisesForPushUp.take(2)
                            exercisesNameForDay[daysOfWeek[1]] = exercisesNameForDay[daysOfWeek[1]]?.toMutableList().apply { this?.addAll(basicExercisesForPullUp.take(2)) } ?: basicExercisesForPullUp.take(2)
                        }

                    } else {
                        exercisesNameForDay[daysOfWeek[0]] = basicExercisesForPushUp
                        exercisesNameForDay[daysOfWeek[1]] = basicExercisesForPullUp
                    }

                    exercisesNameForDay[daysOfWeek[2]] = emptyList<ExercisesName>()
                }
            } else if (daysOfWeekSize == 4) {
                exercisesForPushAndPull = if (isBasicCalisthenics) basicExercisesForPullUp + basicExercisesForPushUp
                else allTensExercises.filter { it.exercise.typeTensExercise == TypeTensExercise.PUSH_UP }.take(2) +
                        allTensExercises.filter { it.exercise.typeTensExercise == TypeTensExercise.PULL_UP }.take(2)

                if (allTensExercises.size == AllRoutines.tensExercises.size){
                    allTensExercises = listOf(ExercisesName.PUSH_UP_PLANK, ExercisesName.PULL_UP_FRONT_LEVER,
                        ExercisesName.PRESS_PLANK, ExercisesName.PRESS_FRONT_LEVER)
                }

                if (isWeightlifting || isMachines) {
                    if (isTensExercise) {
                        exercisesNameForDay[daysOfWeek[0]] = allTensExercises.filter { it.exercise.typeTensExercise == TypeTensExercise.PUSH_UP }
                        exercisesNameForDay[daysOfWeek[1]] = allTensExercises.filter { it.exercise.typeTensExercise == TypeTensExercise.PULL_UP }
                    }
                    if (isBasicCalisthenics) {
                        exercisesNameForDay[daysOfWeek[0]] = exercisesNameForDay[daysOfWeek[0]]?.toMutableList().apply { this?.addAll(basicExercisesForPushUp.take(2)) } ?: basicExercisesForPushUp.take(2)
                        exercisesNameForDay[daysOfWeek[1]] = exercisesNameForDay[daysOfWeek[1]]?.toMutableList().apply { this?.addAll(basicExercisesForPullUp.take(2)) } ?: basicExercisesForPullUp.take(2)
                    }

                } else if (isTensExercise) {
                    exercisesNameForDay[daysOfWeek[0]] = allTensExercises.filter { it.exercise.typeTensExercise == TypeTensExercise.PUSH_UP }
                    exercisesNameForDay[daysOfWeek[1]] = allTensExercises.filter { it.exercise.typeTensExercise == TypeTensExercise.PULL_UP }
                    exercisesNameForDay[daysOfWeek[2]] = exercisesForPushAndPull
                } else {
                    exercisesNameForDay[daysOfWeek[0]] = basicExercisesForPushUp
                    exercisesNameForDay[daysOfWeek[1]] = basicExercisesForPullUp
                    exercisesNameForDay[daysOfWeek[2]] = exercisesForPushAndPull
                }

                if (!isWeightlifting && !isMachines) exercisesNameForDay[daysOfWeek[3]] = emptyList<ExercisesName>()
            }
        }

        return exercisesNameForDay.mapValues { (_, exercisesName) ->
            mutableExercisesNameInLoop = exercisesName.toMutableList()

            if (isTensExercise &&
                ExercisesName.COMBOS_TENSION !in exercisesName &&
                exercisesName.any { it.exercise.type == TypeExercise.TENS }) mutableExercisesNameInLoop.add(
                0,ExercisesName.COMBOS_TENSION)

            mutableExercisesNameInLoop.map { it.exercise }
        }
    }

    private fun getTrainingName(exercises: List<Exercise>): String {
        val namePushOrPull = getGroupFromLargestNumberOfPushOrPull(exercises)?.nameGroup
        val distinctGroupMusclesForGym = exercises.filter { it.typeTensExercise == null }.flatMap { it.trainedPrimaryMuscles }.map { it.groupMuscles }.filter { if (namePushOrPull != null) it != GroupMuscles.PUSH_UP && it != GroupMuscles.PULL_UP else true }.distinct()
        val distinctGroupMusclesForCalisthenics = exercises.fastFilteredMap({ it.typeTensExercise != TypeTensExercise.COMBOS &&
                !it.typeTensExercise?.name.equals(namePushOrPull + "_up", true) }, { it.typeTensExercise }, ).distinct().map {
                when(it) {
                    TypeTensExercise.PUSH_UP -> GroupMuscles.PUSH_UP
                    TypeTensExercise.PULL_UP -> GroupMuscles.PULL_UP
                    else -> null
                }
        }
        val finalDistinctGroupMuscles = (distinctGroupMusclesForCalisthenics.filter { it != null } + distinctGroupMusclesForGym).distinct()
        val firstGroupMuscle = namePushOrPull ?: (finalDistinctGroupMuscles[0]?.nameGroup ?: "")
        val secondGroupMuscle = finalDistinctGroupMuscles.getOrNull(if (namePushOrPull != null) 0 else 1)?.nameGroup ?: ""

        return "$firstGroupMuscle ${ if(secondGroupMuscle.isNotEmpty() && secondGroupMuscle != firstGroupMuscle) "and $secondGroupMuscle " else "" }day"
    }

    private fun getExercisesForMuscles(allExercises: List<Exercise>, muscles: List<Muscles>): MutableList<Exercise> {
        val exercises = mutableListOf<Exercise>()

        muscles.forEach { muscle ->
            exercises.add(allExercises.first { muscle in it.trainedPrimaryMuscles && it !in exercises })
        }
        return exercises
    }

    private fun getGroupFromLargestNumberOfPushOrPull(exercises: List<Exercise>): GroupMuscles? {
        val numberOfExercisesForPush = exercises.filter { it.typeTensExercise == null }.flatMap { it.trainedPrimaryMuscles }.count { it.groupMuscles == GroupMuscles.PUSH_UP }
        val numberOfExercisesForPull = exercises.filter { it.typeTensExercise == null }.flatMap { it.trainedPrimaryMuscles }.count { it.groupMuscles == GroupMuscles.PULL_UP }
        val isDayForPushAndPull = numberOfExercisesForPull >= MAX_NUMBERS_OF_MUSCLES_FOR_PUSH_AND_PULL && numberOfExercisesForPush >= MAX_NUMBERS_OF_MUSCLES_FOR_PUSH_AND_PULL

        if ((numberOfExercisesForPull == 0 && numberOfExercisesForPush == 0) ||
            exercises.all { it.typeTensExercise != null } ||
            isDayForPushAndPull) return null

        return if (numberOfExercisesForPush > numberOfExercisesForPull) GroupMuscles.PUSH_UP else GroupMuscles.PULL_UP
    }

    private fun isDayForPullAndPush(musclesToWorkout: List<Muscles>): Boolean {
        val numberOfMusclesForPush = musclesToWorkout.count { it.groupMuscles == GroupMuscles.PUSH_UP }
        val numberOfMusclesForPull = musclesToWorkout.count { it.groupMuscles == GroupMuscles.PULL_UP }

        return numberOfMusclesForPull > 0 && numberOfMusclesForPush > 0
    }

    private fun getRealizationExercises(exercises: List<Exercise>, workoutAtHome: Boolean): List<RealizationExercise> {
        var maxRepetitions: Int
        var minRepetitions: Int

        return exercises.map {
            if ((it.type == TypeExercise.WEIGHTLIFTING && !workoutAtHome) || it.type == TypeExercise.TENS) {
                minRepetitions = MIN_REPETITIONS_FOR_STRENGTH
                maxRepetitions = MAX_REPETITIONS_FOR_STRENGTH
            } else if (it.type == TypeExercise.MACHINES || (workoutAtHome && it.type == TypeExercise.WEIGHTLIFTING)) {
                minRepetitions = MIN_REPETITIONS_FOR_MUSCLE
                maxRepetitions = MAX_REPETITIONS_FOR_MUSCLE
            } else {
                if (it.typeTensExercise != TypeTensExercise.PULL_UP){
                    minRepetitions = 1
                    maxRepetitions = N_REPETITIONS_FOR_NO_PULL_UPS
                } else {
                    minRepetitions = MIN_REPETITIONS_FOR_MUSCLE
                    maxRepetitions = MAX_REPETITIONS_FOR_MUSCLE
                }
            }

            RealizationExercise(it, SETS, maxRepetitions, minRepetitions, REST_TIME)
        }.toList()
    }

    private fun responseInThisQuestion(question: String, response: String, allQuestionAnswered: Map<String, List<String>>): Boolean {
        return allQuestionAnswered[question]?.contains(response) == true
    }

}
