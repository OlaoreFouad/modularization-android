package dev.olaore.hero_datasource_test.cache

import dev.olaore.hero_domain.Hero

class FakeHeroDatabase {
    val heros: MutableList<Hero> = mutableListOf()
}
