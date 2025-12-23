package com.fadymarty.network.data.remote

import com.fadymarty.network.data.remote.dto.CartDto
import com.fadymarty.network.data.remote.dto.OrderDto
import com.fadymarty.network.data.remote.dto.ProjectDto
import com.fadymarty.network.data.remote.dto.UserDto
import com.fadymarty.network.data.remote.dto.request.AuthRequest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.MediaType.Companion.toMediaType
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class MatuleApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var matuleApi: MatuleApi

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val json = Json { ignoreUnknownKeys = true }
        matuleApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(MatuleApi::class.java)
    }

    @Test
    fun `login() returns auth response with token`() = runTest {
        val mockResponse = MockResponse(
            body = """
                {
                    "record": {
                        "collectionId": "_pb_users_auth_",
                        "collectionName": "users",
                        "created": "2025-12-23 14:03:08.644Z",
                        "dateBirthday": "",
                        "email": "asdf@asdf.ru",
                        "emailVisibility": false,
                        "firstname": "asdf",
                        "gender": "",
                        "id": "ipi7p814rpzqmku",
                        "lastname": "asdf",
                        "secondname": "asdf",
                        "updated": "2025-12-23 14:03:08.644Z",
                        "verified": false
                    },
                    "token": "token"
                }
            """.trimIndent()
        )
        mockWebServer.enqueue(mockResponse)
        val authResponse = matuleApi.login(AuthRequest("asdf@adsf.ru", "qwQW12!@ "))
        assertThat(authResponse.token).isEqualTo("token")
    }

    @Test
    fun `register() creates new user and returns it`() = runTest {
        val mockResponse = MockResponse(
            body = """
                {
                    "collectionId": "_pb_users_auth_",
                    "collectionName": "users",
                    "created": "2025-12-23 14:03:08.644Z",
                    "dateBirthday": "",
                    "email": "asdf@asdf.ru",
                    "emailVisibility": false,
                    "firstname": "asdf",
                    "gender": "",
                    "id": "ipi7p814rpzqmku",
                    "lastname": "asdf",
                    "secondname": "asdf",
                    "updated": "2025-12-23 14:03:08.644Z",
                    "verified": false
                }
            """.trimIndent()
        )
        mockWebServer.enqueue(mockResponse)
        val user = matuleApi.register(
            UserDto(
                email = "asdf@asdf.ru",
                firstName = "asdf",
                gender = "",
                lastName = "asdf",
                secondName = "asdf",
                dateBirthday = "",
                password = "qwQW12!@ ",
                passwordConfirm = "qwQW12!@ "
            )
        )
        assertThat(user.id).isEqualTo("ipi7p814rpzqmku")
    }

    @Test
    fun `getNews() returns list of news`() = runTest {
        val mockResponse = MockResponse(
            body = """
                {
                    "items": [
                        {
                            "collectionId": "pbc_987692768",
                            "collectionName": "news",
                            "created": "2025-11-17 14:09:03.289Z",
                            "id": "9voom067oz6lcau",
                            "newsImage": "https://i.postimg.cc/sDFKZP8j/Mask-Group.png",
                            "updated": "2025-11-17 14:34:46.840Z"
                        },
                        {
                            "collectionId": "pbc_987692768",
                            "collectionName": "news",
                            "created": "2025-11-17 14:11:56.048Z",
                            "id": "zwo0bezreicng42",
                            "newsImage": "https://i.postimg.cc/MKktj04J/Mask-Group-2.png",
                            "updated": "2025-11-17 14:34:55.806Z"
                        }
                    ],
                    "page": 1,
                    "perPage": 30,
                    "totalItems": 2,
                    "totalPages": 1
                }
            """.trimIndent()
        )
        mockWebServer.enqueue(mockResponse)
        val news = matuleApi.getNews()
        assertThat(news.items).isNotEmpty()
    }

    @Test
    fun `getProducts() returns list of products`() = runTest {
        val mockResponse = MockResponse(
            body = """
                {
                    "items": [
                        {
                            "approximate_cost": "80-90",
                            "collectionId": "pbc_4092854851",
                            "collectionName": "products",
                            "created": "2025-11-17 14:21:59.541Z",
                            "description": "Мой выбор для этих шапок – кардные составы, которые раскрываются деликатным пушком. Кашемиры, мериносы, смесовки с ними отлично подойдут на шапку.\r\nКардные составы берите в большое количество сложений, вязать будем резинку 1х1, плотненько.\r\nПряжу 1400-1500м в 100г в 4 сложения, пряжу 700м в 2 сложения. Ориентир для конечной толщины – 300-350м в 100г.\r\nАртикулы, из которых мы вязали эту модель: Zermatt Zegna Baruffa, Cashfive, Baby Cashmere Loro Piana, Soft Donegal и другие.\r\nПримерный расход на шапку с подгибом 70-90г.",
                            "id": "knwd7jd5ei8guou",
                            "price": 690,
                            "title": "Рубашка воскресенье для машинного вязания",
                            "type": "Мужская одежда",
                            "typeCloses": "Мужчинам",
                            "updated": "2025-11-17 14:21:59.541Z"
                        },
                        {
                            "approximate_cost": "80-90",
                            "collectionId": "pbc_4092854851",
                            "collectionName": "products",
                            "created": "2025-11-17 14:22:57.761Z",
                            "description": "Мой выбор для этих шапок – кардные составы, которые раскрываются деликатным пушком. Кашемиры, мериносы, смесовки с ними отлично подойдут на шапку.\r\nКардные составы берите в большое количество сложений, вязать будем резинку 1х1, плотненько.\r\nПряжу 1400-1500м в 100г в 4 сложения, пряжу 700м в 2 сложения. Ориентир для конечной толщины – 300-350м в 100г.\r\nАртикулы, из которых мы вязали эту модель: Zermatt Zegna Baruffa, Cashfive, Baby Cashmere Loro Piana, Soft Donegal и другие.\r\nПримерный расход на шапку с подгибом 70-90г.",
                            "id": "mw59ugxqqiit56d",
                            "price": 300,
                            "title": "Шорты вторник для машинного вязания",
                            "type": "Мужская одежда",
                            "typeCloses": "Мужчинам",
                            "updated": "2025-11-17 14:22:57.761Z"
                        },
                        {
                            "approximate_cost": "80-90",
                            "collectionId": "pbc_4092854851",
                            "collectionName": "products",
                            "created": "2025-11-17 14:23:22.990Z",
                            "description": "Мой выбор для этих шапок – кардные составы, которые раскрываются деликатным пушком. Кашемиры, мериносы, смесовки с ними отлично подойдут на шапку.\r\nКардные составы берите в большое количество сложений, вязать будем резинку 1х1, плотненько.\r\nПряжу 1400-1500м в 100г в 4 сложения, пряжу 700м в 2 сложения. Ориентир для конечной толщины – 300-350м в 100г.\r\nАртикулы, из которых мы вязали эту модель: Zermatt Zegna Baruffa, Cashfive, Baby Cashmere Loro Piana, Soft Donegal и другие.\r\nПримерный расход на шапку с подгибом 70-90г.",
                            "id": "h1v5njxjnq1nxrx",
                            "price": 690,
                            "title": "Рубашка воскресенье для машинного вязания",
                            "type": "Женская одежда",
                            "typeCloses": "Женщинам",
                            "updated": "2025-11-17 14:23:22.990Z"
                        }
                    ],
                    "page": 1,
                    "perPage": 30,
                    "totalItems": 3,
                    "totalPages": 1
                }
            """.trimIndent()
        )
        mockWebServer.enqueue(mockResponse)
        val products = matuleApi.getProducts()
        assertThat(products.items).isNotEmpty()
    }

    @Test
    fun `searchProducts() returns list of products with titles that contain query`() = runTest {
        val mockResponse = MockResponse(
            body = """
                {
                    "items": [
                        {
                            "approximate_cost": "80-90",
                            "collectionId": "pbc_4092854851",
                            "collectionName": "products",
                            "created": "2025-11-17 14:21:59.541Z",
                            "description": "Мой выбор для этих шапок – кардные составы, которые раскрываются деликатным пушком. Кашемиры, мериносы, смесовки с ними отлично подойдут на шапку.\r\nКардные составы берите в большое количество сложений, вязать будем резинку 1х1, плотненько.\r\nПряжу 1400-1500м в 100г в 4 сложения, пряжу 700м в 2 сложения. Ориентир для конечной толщины – 300-350м в 100г.\r\nАртикулы, из которых мы вязали эту модель: Zermatt Zegna Baruffa, Cashfive, Baby Cashmere Loro Piana, Soft Donegal и другие.\r\nПримерный расход на шапку с подгибом 70-90г.",
                            "id": "knwd7jd5ei8guou",
                            "price": 690,
                            "title": "Рубашка воскресенье для машинного вязания",
                            "type": "Мужская одежда",
                            "typeCloses": "Мужчинам",
                            "updated": "2025-11-17 14:21:59.541Z"
                        },
                        {
                            "approximate_cost": "80-90",
                            "collectionId": "pbc_4092854851",
                            "collectionName": "products",
                            "created": "2025-11-17 14:23:22.990Z",
                            "description": "Мой выбор для этих шапок – кардные составы, которые раскрываются деликатным пушком. Кашемиры, мериносы, смесовки с ними отлично подойдут на шапку.\r\nКардные составы берите в большое количество сложений, вязать будем резинку 1х1, плотненько.\r\nПряжу 1400-1500м в 100г в 4 сложения, пряжу 700м в 2 сложения. Ориентир для конечной толщины – 300-350м в 100г.\r\nАртикулы, из которых мы вязали эту модель: Zermatt Zegna Baruffa, Cashfive, Baby Cashmere Loro Piana, Soft Donegal и другие.\r\nПримерный расход на шапку с подгибом 70-90г.",
                            "id": "h1v5njxjnq1nxrx",
                            "price": 690,
                            "title": "Рубашка воскресенье для машинного вязания",
                            "type": "Женская одежда",
                            "typeCloses": "Женщинам",
                            "updated": "2025-11-17 14:23:22.990Z"
                        }
                    ],
                    "page": 1,
                    "perPage": 30,
                    "totalItems": 2,
                    "totalPages": 1
                }
            """.trimIndent()
        )
        mockWebServer.enqueue(mockResponse)
        val products = matuleApi.searchProducts(
            filter = "(title ?~ 'Рубашка')"
        )
        assertThat(products.items.any { it.title.contains("Рубашка") }).isTrue()
    }

    @Test
    fun `getProductById() returns product with description`() = runTest {
        val mockResponse = MockResponse(
            body = """
                {
                    "approximate_cost": "80-90",
                    "collectionId": "pbc_4092854851",
                    "collectionName": "products",
                    "created": "2025-11-17 14:21:59.541Z",
                    "description": "Мой выбор для этих шапок – кардные составы, которые раскрываются деликатным пушком. Кашемиры, мериносы, смесовки с ними отлично подойдут на шапку.\r\nКардные составы берите в большое количество сложений, вязать будем резинку 1х1, плотненько.\r\nПряжу 1400-1500м в 100г в 4 сложения, пряжу 700м в 2 сложения. Ориентир для конечной толщины – 300-350м в 100г.\r\nАртикулы, из которых мы вязали эту модель: Zermatt Zegna Baruffa, Cashfive, Baby Cashmere Loro Piana, Soft Donegal и другие.\r\nПримерный расход на шапку с подгибом 70-90г.",
                    "id": "knwd7jd5ei8guou",
                    "price": 690,
                    "title": "Рубашка воскресенье для машинного вязания",
                    "type": "Мужская одежда",
                    "typeCloses": "Мужчинам",
                    "updated": "2025-11-17 14:21:59.541Z"
                }
            """.trimIndent()
        )
        mockWebServer.enqueue(mockResponse)
        val product = matuleApi.getProductById("knwd7jd5ei8guou")
        assertThat(product.id).isEqualTo("knwd7jd5ei8guou")
    }

    @Test
    fun `createCart() creates new cart and returns it`() = runTest {
        val mockResponse = MockResponse(
            body = """
                {
                    "collectionId": "pbc_749661959",
                    "collectionName": "cart",
                    "count": 1,
                    "created": "2025-12-23 14:38:50.796Z",
                    "id": "x8lgc2h9zoujlfn",
                    "porduct_id": "ipi7p814rpzqmku",
                    "updated": "2025-12-23 14:38:50.796Z",
                    "user_id": "ipi7p814rpzqmku"
                }
            """.trimIndent()
        )
        mockWebServer.enqueue(mockResponse)
        val cart = matuleApi.createCart(
            CartDto(
                userId = "ipi7p814rpzqmku",
                productId = "ipi7p814rpzqmku",
                count = 1
            )
        )
        assertThat(cart.id).isEqualTo("x8lgc2h9zoujlfn")
    }

    @Test
    fun `updateCart() updates cart and returns it`() = runTest {
        val mockResponse = MockResponse(
            body = """
                {
                    "collectionId": "pbc_749661959",
                    "collectionName": "cart",
                    "count": 2,
                    "created": "2025-12-23 14:38:50.796Z",
                    "id": "x8lgc2h9zoujlfn",
                    "porduct_id": "ipi7p814rpzqmku",
                    "updated": "2025-12-23 14:38:50.796Z",
                    "user_id": "ipi7p814rpzqmku"
                }
            """.trimIndent()
        )
        mockWebServer.enqueue(mockResponse)
        val cart = matuleApi.updateCart(
            id = "x8lgc2h9zoujlfn",
            cart = CartDto(
                userId = "ipi7p814rpzqmku",
                productId = "ipi7p814rpzqmku",
                count = 2
            )
        )
        assertThat(cart.count).isEqualTo(2)
    }

    @Test
    fun `createOrder() creates order and returns it`() = runTest {
        val mockResponse = MockResponse(
            body = """
                {
                    "collectionId": "pbc_3527180448",
                    "collectionName": "orders",
                    "count": 2,
                    "created": "2025-12-23 14:44:54.131Z",
                    "id": "v1oaetlsedem3r2",
                    "product_id": "knwd7jd5ei8guou",
                    "updated": "2025-12-23 14:44:54.131Z",
                    "user_id": "ipi7p814rpzqmku"
                }
            """.trimIndent()
        )
        mockWebServer.enqueue(mockResponse)
        val order = matuleApi.createOrder(
            OrderDto(
                userId = "ipi7p814rpzqmku",
                productId = "ipi7p814rpzqmku",
                count = 2
            )
        )
        assertThat(order.id).isEqualTo("v1oaetlsedem3r2")
    }

    @Test
    fun `getProjects() returns list of projects`() = runTest {
        val mockResponse = MockResponse(
            body = """
                {
                    "items": [
                        {
                            "category": "asdf",
                            "collectionId": "pbc_3202395908",
                            "collectionName": "project",
                            "created": "2025-12-23 14:48:15.812Z",
                            "dateEnd": "",
                            "dateStart": "",
                            "description_source": "asdf",
                            "gender": "",
                            "id": "vfij2hnqvclvhyb",
                            "image": "asdf",
                            "title": "asdf",
                            "typeProject": "",
                            "updated": "2025-12-23 14:48:15.812Z",
                            "user_id": "ipi7p814rpzqmku"
                        }
                    ],
                    "page": 1,
                    "perPage": 30,
                    "totalItems": 1,
                    "totalPages": 1
                }
            """.trimIndent()
        )
        mockWebServer.enqueue(mockResponse)
        val projects = matuleApi.getProjects()
        assertThat(projects.items).isNotEmpty()
    }

    @Test
    fun `createProject() creates project and returns it`() = runTest {
        val mockResponse = MockResponse(
            body = """
                {
                    "category": "asdf",
                    "collectionId": "pbc_3202395908",
                    "collectionName": "project",
                    "created": "2025-12-23 14:48:15.812Z",
                    "dateEnd": "",
                    "dateStart": "",
                    "description_source": "asdf",
                    "gender": "",
                    "id": "vfij2hnqvclvhyb",
                    "image": "asdf",
                    "title": "asdf",
                    "typeProject": "",
                    "updated": "2025-12-23 14:48:15.812Z",
                    "user_id": "ipi7p814rpzqmku"
                }
            """.trimIndent()
        )
        mockWebServer.enqueue(mockResponse)
        val project = matuleApi.createProject(
            ProjectDto(
                category = "asdf",
                dateEnd = "",
                dateStart = "",
                descriptionSource = "asdf",
                gender = "",
                image = "asdf",
                title = "asdf",
                typeProject = "",
                userId = "ipi7p814rpzqmku"
            )
        )
        assertThat(project.id).isEqualTo("vfij2hnqvclvhyb")
    }

    @Test
    fun `getUserById() returns user`() = runTest {
        val mockResponse = MockResponse(
            body = """
                {
                    "collectionId": "_pb_users_auth_",
                    "collectionName": "users",
                    "created": "2025-12-23 14:03:08.644Z",
                    "dateBirthday": "",
                    "email": "asdf@asdf.ru",
                    "emailVisibility": false,
                    "firstname": "asdf",
                    "gender": "",
                    "id": "ipi7p814rpzqmku",
                    "lastname": "asdf",
                    "secondname": "asdf",
                    "updated": "2025-12-23 14:03:08.644Z",
                    "verified": false
                }
            """.trimIndent()
        )
        mockWebServer.enqueue(mockResponse)
        val user = matuleApi.getUserById("ipi7p814rpzqmku")
        assertThat(user.id).isEqualTo("ipi7p814rpzqmku")
    }
}