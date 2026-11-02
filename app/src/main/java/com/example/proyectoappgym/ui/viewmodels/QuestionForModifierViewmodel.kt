package com.example.proyectoappgym.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.R
import com.example.proyectoappgym.db.db_questions.QuestionsRegistration
import com.example.proyectoappgym.db.db_routines.RoutineRepository
import com.example.proyectoappgym.db.db_users.UserRepository
import com.example.proyectoappgym.entity.data.request.DataUserRequest
import com.example.proyectoappgym.entity.exceptions.ErrorException
import com.example.proyectoappgym.entity.exceptions.ErrorForShowing
import com.example.proyectoappgym.entity.exceptions.ErrorType
import com.example.proyectoappgym.entity.functions.getErrorForShowingScreen
import com.example.proyectoappgym.entity.functions.getParsedBirthdate
import com.example.proyectoappgym.entity.trainingroutines.TrainingRoutine
import com.example.proyectoappgym.entity.users.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class QuestionForModifierViewmodel @Inject constructor(private val userRepository: UserRepository, private val routineRepository: RoutineRepository): ViewModel() {
    private var _currentUser = MutableStateFlow(User())
    val currentUser = _currentUser.asStateFlow()
    private var _isChangeRoutine: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isChangeRoutine = _isChangeRoutine.asStateFlow()
    val allQuestions = QuestionsRegistration.allQuestions
    private var _error: MutableSharedFlow<ErrorForShowing?> = MutableStateFlow(null)
    val error = _error.asSharedFlow()
    private var _validationDone: MutableSharedFlow<Boolean> = MutableStateFlow(false)
    val validationDone = _validationDone.asSharedFlow()

    init {
        viewModelScope.launch {
            val user: User

            try {
                user = userRepository.getCurrentUser()
                _currentUser.update { user }
            } catch (e: SocketTimeoutException) {
                _error.emit(ErrorForShowing("Unable to connect to the server", R.drawable.ic_not_connecting,
                    ErrorType.ERROR_IN_ERROR_SCREEN))
            } catch (e: ErrorException) {
                _error.emit(getErrorForShowingScreen(e.error, e.message))
            }
        }
    }

    fun changeAllAnsweredQuestions(
        newAnsweredQuestion: Map<String, List<String>>,
        newTrainingRoutines: List<TrainingRoutine>?
    ) {
        val currentUserToUpdate = currentUser.value
        val dataUserRequest = DataUserRequest(currentUserToUpdate.username, currentUserToUpdate.name,
            getParsedBirthdate(currentUserToUpdate.birthdate), currentUserToUpdate.gender, newAnsweredQuestion,
            currentUserToUpdate.idProfileAvatar)
        var user: User

        viewModelScope.launch {
            try {
                user = userRepository.updateCurrentUser(dataUserRequest)
                if (newTrainingRoutines != null) routineRepository.updateRoutine(newTrainingRoutines)
                _currentUser.update { user }
                _validationDone.emit(true)
            } catch (e: SocketTimeoutException) {
                _error.emit(ErrorForShowing("Unable to connect to the server", R.drawable.ic_not_connecting, ErrorType.ERROR_IN_SAME_SCREEN))
            } catch (e: ErrorException) {
                 _error.emit(getErrorForShowingScreen(e.error, e.message))
            }
        }
    }
}