package com.example.proyectoappgym.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.R
import com.example.proyectoappgym.db.db_questions.QuestionsRegistration
import com.example.proyectoappgym.db.db_users.UserRepository
import com.example.proyectoappgym.entity.data.request.DataUserRequest
import com.example.proyectoappgym.entity.exceptions.ErrorException
import com.example.proyectoappgym.entity.exceptions.ErrorForShowing
import com.example.proyectoappgym.entity.exceptions.ErrorType
import com.example.proyectoappgym.entity.functions.getErrorForShowingScreen
import com.example.proyectoappgym.entity.users.Avatars
import com.example.proyectoappgym.entity.users.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class EditProfileViewmodel @Inject constructor(private val userRepository: UserRepository): ViewModel() {
    private var _currentUser = MutableStateFlow(User())
    val currentUser = _currentUser.asStateFlow()
    val allQuestions = QuestionsRegistration.allQuestions
    private var _showDialog = MutableStateFlow(true)
    val showDialog = _showDialog.asStateFlow()
    private var _error: MutableSharedFlow<ErrorForShowing?> = MutableStateFlow(null)
    val error = _error.asSharedFlow()

    init {
        viewModelScope.launch {
            val user: User

            try {
                user = userRepository.getCurrentUser()
                _currentUser.update { user }
            } catch (e: SocketTimeoutException) {
                _error.emit(ErrorForShowing("Unable to connect to the server", R.drawable.ic_not_connecting, ErrorType.ERROR_IN_ERROR_SCREEN))
            } catch (e: ErrorException) {
                _error.emit(getErrorForShowingScreen(e.error, e.message))
            }

        }
    }
    
    fun updateCurrentUser(name: String, newAvatar: Avatars) {
        var user: User
        val dataUserRequest = DataUserRequest(_currentUser.value.username, name, _currentUser.value.birthdate, _currentUser.value.gender, _currentUser.value.allQuestionsAnswered, newAvatar.idAvatar)

        viewModelScope.launch {

            try {
                user = userRepository.updateCurrentUser(dataUserRequest)
                _currentUser.update { user }
            } catch (e: SocketTimeoutException) {
                _error.emit(ErrorForShowing("Unable to connect to the server", R.drawable.ic_not_connecting, ErrorType.ERROR_IN_ERROR_SCREEN))
            } catch (e : ErrorException) {
                _error.emit(getErrorForShowingScreen(e.error, e.message))
            }
        }
    }

    fun updateShowDialog() {
        _showDialog.update { !_showDialog.value }
    }
}