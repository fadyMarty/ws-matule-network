package com.fadymarty.network.data.remote

import com.fadymarty.network.data.remote.dto.AuthResponseDto
import com.fadymarty.network.data.remote.dto.CartDto
import com.fadymarty.network.data.remote.dto.NewsDto
import com.fadymarty.network.data.remote.dto.OrderDto
import com.fadymarty.network.data.remote.dto.PocketbaseResponse
import com.fadymarty.network.data.remote.dto.ProductDto
import com.fadymarty.network.data.remote.dto.ProjectDto
import com.fadymarty.network.data.remote.dto.UserDto
import com.fadymarty.network.data.remote.dto.request.AuthRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Сетевой клиент с Запросами к серверу
 */
interface MatuleApi {

    /**
     * Авторизация
     * @param request тело запроса с почтой и паролем
     */
    @POST("collections/users/auth-with-password")
    suspend fun login(
        @Body request: AuthRequest,
    ): AuthResponseDto

    /**
     * Создание пользователя
     * @param user тело запроса с данными пользователя
     */
    @POST("collections/users/records")
    suspend fun register(
        @Body user: UserDto,
    ): UserDto

    /**
     * Изменение профиля
     * @param id идентификатор пользоваля, которого нужно обновить
     * @param user тело запроса с обновленными данными пользователя
     */
    @PATCH("collections/users/records/{id}")
    suspend fun updateUser(
        @Path("id") id: String,
        @Body user: UserDto,
    ): UserDto

    /**
     * Получение акций
     */
    @GET("collections/news/records")
    suspend fun getNews(): PocketbaseResponse<NewsDto>

    /**
     * Получение каталога
     */
    @GET("collections/products/records")
    suspend fun getProducts(): PocketbaseResponse<ProductDto>

    /**
     * Поиск
     * @param filter фильтр каталога
     */
    @GET("collections/products/records")
    suspend fun searchProducts(
        @Query("filter") filter: String,
    ): PocketbaseResponse<ProductDto>

    /**
     * Получение описания товара
     * @param id индентификатор товара
     */
    @GET("collections/products/records/{id}")
    suspend fun getProductById(
        @Path("id") id: String,
    ): ProductDto

    /**
     * Добавление в корзину
     * @param cart тело запроса с данными корзины
     */
    @POST("collections/cart/records")
    suspend fun createCart(
        @Body cart: CartDto,
    ): CartDto

    /**
     * Изменение корзины
     * @param id индентификатор корзины, которую нужно обновить
     * @param cart тело запроса с обновленными данными корзины
     */
    @PATCH("collections/cart/records/{id}")
    suspend fun updateCart(
        @Path("id") id: String,
        @Body cart: CartDto,
    ): CartDto

    /**
     * Оформление заказа
     * @param order тело запроса с данными заказа
     */
    @POST("collections/orders/records")
    suspend fun createOrder(
        @Body order: OrderDto,
    ): OrderDto

    /**
     * Список проектов
     */
    @GET("collections/project/records")
    suspend fun getProjects(): PocketbaseResponse<ProjectDto>

    /**
     * Создание проекта
     * @param project тело запроса с данными проекта
     */
    @POST("collections/project/records")
    suspend fun createProject(
        @Body project: ProjectDto,
    ): ProjectDto

    /**
     * Получение информации о профиле
     * @param id идентификатор пользователя, данные которого нужно получить
     */
    @GET("collections/users/records/{id}")
    suspend fun getUserById(
        @Path("id") id: String,
    ): UserDto

    /**
     * Получение корзины
     */
    @GET("collections/cart/records")
    suspend fun getCarts(): PocketbaseResponse<CartDto>

    /**
     * Удалить корзину
     * @param id идентификатор корзины, которую нужно удалить
     */
    @DELETE("collections/cart/records/{id}")
    suspend fun deleteCart(
        @Path("id") id: String,
    )

    companion object {
        const val BASE_URL = "http://77.239.125.32:8090/api/"
    }
}