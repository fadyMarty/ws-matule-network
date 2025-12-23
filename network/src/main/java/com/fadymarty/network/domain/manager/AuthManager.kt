package com.fadymarty.network.domain.manager

import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс менеджера аутентификации для сохранения настроек пользователя
 */
interface AuthManager {

    /**
     * Сохранить сессию пользователя
     * @param token токен
     * @param userId идентификатор пользователя
     */
    suspend fun saveSession(token: String, userId: String)

    /**
     * Сохранить ПИН-код для входа в приложение
     * @param pin ПИН-код
     */
    suspend fun savePin(pin: String)

    /**
     * Получить токен пользователя
     */
    fun getToken(): Flow<String?>

    /**
     * Получить идентификатор пользователя
     */
    fun getUserId(): Flow<String?>

    /**
     * Получить ПИН-код
     */
    fun getPin(): Flow<String?>

    /**
     * Очистить сессию (выход из аккаунта)
     */
    suspend fun clearSession()
}