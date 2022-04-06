package dev.olaore.hero_domain

sealed class HeroAttribute(
    val value: String,
    val abbr: String
) {

    object Agility: HeroAttribute(
        value = "Agility",
        abbr = "agi"
    )

    object Strength: HeroAttribute(
        value = "Strength",
        abbr = "str"
    )

    object Intelligence: HeroAttribute(
        value = "Intelligence",
        abbr = "int"
    )

    object Unknown: HeroAttribute(
        value = "Unknown",
        abbr = "unknown"
    )

}

fun getHeroAttrFromUiValue(uiValue: String): HeroAttribute{
    return when(uiValue){
        HeroAttribute.Agility.value -> {
            HeroAttribute.Agility
        }
        HeroAttribute.Strength.value -> {
            HeroAttribute.Strength
        }
        HeroAttribute.Intelligence.value -> {
            HeroAttribute.Intelligence
        }
        else -> HeroAttribute.Unknown
    }
}

fun getHeroAttrFromAbbreviation(abbreviation: String): HeroAttribute{
    return when(abbreviation){
        HeroAttribute.Agility.abbr -> {
            HeroAttribute.Agility
        }
        HeroAttribute.Strength.abbr -> {
            HeroAttribute.Strength
        }
        HeroAttribute.Intelligence.abbr -> {
            HeroAttribute.Intelligence
        }
        else -> HeroAttribute.Unknown
    }
}

fun Hero.minAttackDmg(): Int {
    return when(primaryAttribute){
        is HeroAttribute.Strength -> {
            baseAttackMin + baseStr
        }
        is HeroAttribute.Agility -> {
            baseAttackMin + baseAgi
        }
        is HeroAttribute.Intelligence -> {
            baseAttackMin + baseInt
        }
        is HeroAttribute.Unknown -> {
            0
        }
    }
}

fun Hero.maxAttackDmg(): Int {
    return when(primaryAttribute){
        is HeroAttribute.Strength -> {
            baseAttackMax + baseStr
        }
        is HeroAttribute.Agility -> {
            baseAttackMax + baseAgi
        }
        is HeroAttribute.Intelligence -> {
            baseAttackMax + baseInt
        }
        is HeroAttribute.Unknown -> {
            0
        }
    }
}