package me.emmano.androidmva.base

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.validateMockitoUsage
import me.emmano.androidmva.CoroutineTest
import me.emmano.androidmva.comics.repo.ComicRepository
import me.emmano.androidmva.rule.CoroutineTestRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest

abstract class BaseTest : AutoCloseKoinTest(), CoroutineTest {

    @get:Rule
    override val coroutineRule: CoroutineTestRule = CoroutineTestRule()

    init { startKoin{} }

    @Before
    fun startKoin() {
        loadKoinModules((module(override = true) { single { mock<ComicRepository>() } } ))
    }

    @After
    fun validate() {
        // Avoid misleading messages from Mockito when mocks are misused.
        validateMockitoUsage()
    }
}