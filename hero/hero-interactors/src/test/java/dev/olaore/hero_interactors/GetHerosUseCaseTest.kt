package dev.olaore.hero_interactors

import dev.olaore.core.domain.DataState
import dev.olaore.core.domain.ProgressBarState
import dev.olaore.core.domain.UIComponent
import dev.olaore.core.util.Logger
import dev.olaore.hero_datasource_test.cache.FakeHeroCache
import dev.olaore.hero_datasource_test.cache.FakeHeroDatabase
import dev.olaore.hero_datasource_test.network.FakeHeroService
import dev.olaore.hero_datasource_test.network.HeroServiceResponseType
import dev.olaore.hero_datasource_test.network.data.HeroDataValid
import dev.olaore.hero_datasource_test.network.data.HeroDataValid.NUM_HEROS
import dev.olaore.hero_datasource_test.network.serializeHeroData
import dev.olaore.hero_domain.Hero
import dev.olaore.hero_interactors.usecases.GetHerosUseCase
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetHerosUseCaseTest {

    private lateinit var systemUnderTest: GetHerosUseCase

    @Test
    fun `given_GetHerosConditionsAreMet_should_ReturnSuccessData`() = runBlocking {

        val db = FakeHeroDatabase()
        val cache = FakeHeroCache(db)
        val service = FakeHeroService.build(HeroServiceResponseType.GoodData)
        val logger = Logger("GetHerosUseCaseTest")

        systemUnderTest = GetHerosUseCase(service, cache, logger)

        val cachedHeros = cache.selectAll()
        assert(cachedHeros.isEmpty())

        val emissions = systemUnderTest.execute().toList()

        assert(
            emissions[0] == DataState.Loading(ProgressBarState.Loading)
        )
        assert(
            emissions[1] is DataState.Data<List<Hero>>
        )
        assert(
            ((emissions[1] as DataState.Data<List<Hero>>).data?.size ?: 0) == NUM_HEROS
        )
        assert(
            emissions[2] == DataState.Loading(ProgressBarState.Idle)
        )

    }

    @Test
    fun getHeros_malformedData_successFromCache() =  runBlocking {
        // setup
        val heroDatabase = FakeHeroDatabase()
        val heroCache = FakeHeroCache(heroDatabase)
        val heroService = FakeHeroService.build(
            type = HeroServiceResponseType.MalformedData // Malformed Data
        )
        val logger = Logger("GetHerosUseCaseTest")

        systemUnderTest = GetHerosUseCase(
            cache = heroCache,
            service = heroService,
            logger = logger
        )

        // Confirm the cache is empty before any use-cases have been executed
        var cachedHeros = heroCache.selectAll()
        assert(cachedHeros.isEmpty())

        // Add some data to the cache by executing a successful request
        val heroData = serializeHeroData(HeroDataValid.data)
        heroCache.insert(heroData)

        // Confirm the cache is not empty anymore
        cachedHeros = heroCache.selectAll()
        assert(cachedHeros.size == NUM_HEROS)

        // Execute the use-case
        val emissions = systemUnderTest.execute().toList()

        // First emission should be loading
        assert(emissions[0] == DataState.Loading(ProgressBarState.Loading))

        // Confirm second emission is error response
        assert(emissions[1] is DataState.Response)
        assert(((emissions[1] as DataState.Response).uiComponent as UIComponent.Dialog).title == "Error Occurred")
        assert(((emissions[1] as DataState.Response).uiComponent as UIComponent.Dialog).description.contains("Unexpected JSON token at offset"))

        // Confirm third emission is data from the cache
        assert(emissions[2] is DataState.Data)
        assert((emissions[2] as DataState.Data).data?.size == NUM_HEROS)

        // Confirm the cache is still not empty
        cachedHeros = heroCache.selectAll()
        assert(cachedHeros.size == NUM_HEROS)

        // Confirm loading state is IDLE
        assert(emissions[3] == DataState.Loading(ProgressBarState.Idle))
    }

    @Test
    fun getHeros_emptyList() =  runBlocking {
        // setup
        val heroDatabase = FakeHeroDatabase()
        val heroCache = FakeHeroCache(heroDatabase)
        val heroService = FakeHeroService.build(
            type = HeroServiceResponseType.EmptyList // Malformed Data
        )
        val logger = Logger("GetHerosUseCaseTest")

        systemUnderTest = GetHerosUseCase(
            cache = heroCache,
            service = heroService,
            logger = logger
        )

        // Confirm the cache is empty before any use-cases have been executed
        var cachedHeros = heroCache.selectAll()
        assert(cachedHeros.isEmpty())

        // Execute the use-case
        val emissions = systemUnderTest.execute().toList()

        // First emission should be loading
        assert(emissions[0] == DataState.Loading(ProgressBarState.Loading))

        // Confirm second emission is data (empty list)
        assert(emissions[1] is DataState.Data)
        assert((emissions[1] as DataState.Data).data?.size?: 0 == 0)

        // Confirm the cache is STILL EMPTY
        cachedHeros = heroCache.selectAll()
        assert(cachedHeros.isEmpty())

        // Confirm loading state is IDLE
        assert(emissions[2] == DataState.Loading(ProgressBarState.Idle))
    }

}
