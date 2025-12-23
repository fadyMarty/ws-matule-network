package com.fadymarty.network.data.repository

import com.fadymarty.network.data.remote.MatuleApi
import com.fadymarty.network.data.remote.dto.CartDto
import com.fadymarty.network.data.remote.dto.OrderDto
import com.fadymarty.network.data.remote.dto.request.AuthRequest
import com.fadymarty.network.data.remote.mappers.toAuthResponse
import com.fadymarty.network.data.remote.mappers.toCart
import com.fadymarty.network.data.remote.mappers.toCartDto
import com.fadymarty.network.data.remote.mappers.toNews
import com.fadymarty.network.data.remote.mappers.toProduct
import com.fadymarty.network.data.remote.mappers.toProject
import com.fadymarty.network.data.remote.mappers.toProjectDto
import com.fadymarty.network.data.remote.mappers.toUser
import com.fadymarty.network.data.remote.mappers.toUserDto
import com.fadymarty.network.data.remote.safeCall
import com.fadymarty.network.domain.manager.AuthManager
import com.fadymarty.network.domain.model.AuthResponse
import com.fadymarty.network.domain.model.Cart
import com.fadymarty.network.domain.model.News
import com.fadymarty.network.domain.model.Product
import com.fadymarty.network.domain.model.Project
import com.fadymarty.network.domain.model.User
import com.fadymarty.network.domain.repository.MatuleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update

/**
 * Реализация основного репозитория
 */
class MatuleRepositoryImpl(
    private val matuleApi: MatuleApi,
    private val authManager: AuthManager,
) : MatuleRepository {

    private val carts = MutableStateFlow<List<Cart>>(emptyList())
    private val projects = MutableStateFlow<List<Project>>(emptyList())

    override fun observeCarts(): Flow<List<Cart>> = carts

    override fun observeProjects(): Flow<List<Project>> = projects

    override suspend fun login(
        email: String,
        password: String,
    ): Result<AuthResponse> {
        return safeCall {
            val authResponse = matuleApi.login(AuthRequest(email, password)).toAuthResponse()
            authManager.saveSession(authResponse.token, authResponse.record.id!!)
            authResponse
        }
    }

    override suspend fun register(user: User): Result<AuthResponse> {
        return safeCall {
            matuleApi.register(user.toUserDto())
            val authResponse = matuleApi.login(
                AuthRequest(
                    identity = user.email!!,
                    password = user.password!!
                )
            ).toAuthResponse()
            authManager.saveSession(authResponse.token, authResponse.record.id!!)
            authResponse
        }
    }

    override suspend fun updateUser(user: User): Result<User> {
        return safeCall {
            matuleApi.updateUser(user.id!!, user.toUserDto()).toUser()
        }
    }

    override suspend fun getNews(): Result<List<News>> {
        return safeCall {
            matuleApi.getNews().items.map { it.toNews() }
        }
    }

    override suspend fun getProducts(): Result<List<Product>> {
        return safeCall {
            matuleApi.getProducts().items.map { it.toProduct() }
        }
    }

    override suspend fun searchProducts(
        query: String?,
        typeCloses: String?,
    ): Result<List<Product>> {
        return safeCall {
            matuleApi.searchProducts(
                filter = "(title ?~ '$query' && typeCloses = '$typeCloses')"
            ).items.map { it.toProduct() }
        }
    }

    override suspend fun getProductById(id: String): Result<Product> {
        return safeCall {
            matuleApi.getProductById(id).toProduct()
        }
    }

    override suspend fun addProductToCart(product: Product): Result<Cart> {
        return safeCall {
            val userId = authManager.getUserId().first()!!
            val cart = CartDto(
                userId = userId,
                productId = product.id,
                count = 1
            )
            matuleApi.createCart(cart).toCart().also { getCarts() }
        }
    }

    override suspend fun updateCart(cart: Cart): Result<Cart> {
        return safeCall {
            matuleApi.updateCart(cart.id!!, cart.toCartDto()).toCart().also { getCarts() }
        }
    }

    override suspend fun createOrder(carts: List<Cart>): Result<Unit> {
        return safeCall {
            val userId = authManager.getUserId().first()!!
            carts.forEach { cart ->
                val order = OrderDto(
                    userId = userId,
                    productId = cart.productId,
                    count = cart.count
                )
                matuleApi.createOrder(order)
                matuleApi.deleteCart(cart.id!!)
            }
        }
    }

    override suspend fun getProjects(): Result<List<Project>> {
        return safeCall {
            matuleApi.getProjects().items
                .map { it.toProject() }
                .also { newProjects ->
                    projects.update { newProjects }
                }
        }
    }

    override suspend fun createProject(project: Project): Result<Project> {
        return safeCall {
            matuleApi.createProject(project.toProjectDto()).toProject().also { getProjects() }
        }
    }

    override suspend fun getUserById(id: String): Result<User> {
        return safeCall {
            matuleApi.getUserById(id).toUser()
        }
    }

    override suspend fun getCarts(): Result<List<Cart>> {
        return safeCall {
            matuleApi.getCarts().items
                .map { it.toCart() }
                .also { newCarts ->
                    carts.update { newCarts }
                }
        }
    }

    override suspend fun deleteCart(id: String): Result<Unit> {
        return safeCall {
            matuleApi.deleteCart(id)
            getCarts()
        }
    }
}