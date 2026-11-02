package com.example.proyectoappgym.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.R
import com.example.proyectoappgym.db.db_routines.RoutineRepository
import com.example.proyectoappgym.db.db_users.UserApiService
import com.example.proyectoappgym.db.db_users.UserRepository
import com.example.proyectoappgym.entity.exceptions.ErrorException
import com.example.proyectoappgym.entity.exceptions.ErrorForShowing
import com.example.proyectoappgym.entity.exceptions.ErrorType
import com.example.proyectoappgym.entity.functions.getErrorForShowingScreen
import com.example.proyectoappgym.entity.trainingroutines.TrainingRoutine
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.proyectoappgym.entity.users.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(private val userRepository: UserRepository, private val routineRepository: RoutineRepository): ViewModel() {
    private var _currentUser = MutableStateFlow(User())
    val currentUser = _currentUser.asStateFlow()
    private var _error: MutableSharedFlow<ErrorForShowing?> = MutableStateFlow(null)
    val error = _error.asSharedFlow()

    init {
        viewModelScope.launch {
            val user: User
            val trainingRoutines: List<TrainingRoutine>
            val errorForShowing: ErrorForShowing

            try {
                user = userRepository.getCurrentUser()
                trainingRoutines = routineRepository.getAllRoutines()
                user.addTrainingRoutines(trainingRoutines)
                _currentUser.update { user }
            } catch (e: SocketTimeoutException) {
                _error.emit(ErrorForShowing("Unable to connect to the server", R.drawable.ic_not_connecting, ErrorType.ERROR_IN_ERROR_SCREEN))
            } catch (e: ErrorException) {
                errorForShowing = getErrorForShowingScreen(e.error, e.message)
                _error.emit(errorForShowing)
            }
        }
    }

}